import React, { useEffect, useState, useRef } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import L from "leaflet"; // Leaflet importieren

const loadExternalCSS = (url) => {
  const link = document.createElement("link");
  link.rel = "stylesheet";import React, { useEffect, useState, useRef } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import L from "leaflet"; // Leaflet importieren

const loadExternalCSS = (url) => {
  const link = document.createElement("link");
  link.rel = "stylesheet";
  link.href = url;
  document.head.appendChild(link);
};

const Game = () => {
  const { gameId } = useParams();
  const jwtToken = localStorage.getItem("jwtToken");
  const [gameDetails, setGameDetails] = useState(null);
  const [userDetails, setUserDetails] = useState(null);
  const [currentSpielzugIndex, setCurrentSpielzugIndex] = useState(0);
  const [timer, setTimer] = useState(5); // Beginne mit 5 Sekunden Vorbereitung
  const [isPreparing, setIsPreparing] = useState(true);
  const [spieler1Bool, setSpieler1Bool] = useState(true);
  const [score, setScore] = useState("");
  const [spielzuege, setSpielzuege] = useState([]); // Spielzüge mit Städten
  const [connected, setConnected] = useState(false); // Verbindungstatus für WebSocket
  const [map, setMap] = useState(null); // Leaflet map state
  const markerRef = useRef(null);
  const currentMarker = useRef(null);
  const selectedCoordinates = useRef(null);
  let stompClient = null;

  const mapRef = useRef(null); // Referenz für das Map-Container-Div

  // Effekt: Initialisiere Daten und WebSocket-Verbindung
  useEffect(() => {
    loadExternalCSS("https://unpkg.com/leaflet@1.9.4/dist/leaflet.css");
    fetchInitialData();
    setupWebSocket();
    return () => {
      disconnectWebSocket();
    };
  }, []);

  // Effekt: Timer für Vorbereitung und Spielzüge
  useEffect(() => {
    if (timer > 0) {
      const interval = setInterval(() => {
        setTimer((prev) => prev - 1);
      }, 1000);
      return () => clearInterval(interval);
    } else if (timer === 0) {
      if (isPreparing) {
        setIsPreparing(false);
        setTimer(30); // 30 Sekunden für Spielzug
      } else {
        submitGuess();
        setIsPreparing(true);
        setTimer(8); // 5 Sekunden Vorbereitung
        setCurrentSpielzugIndex((prevIndex) => prevIndex + 1);
      }
    }
  }, [timer, isPreparing]);

  // Initiale Daten abrufen
  const fetchInitialData = async () => {
    try {
      const userResponse = await axios.get("http://localhost:8080/rest/user/details", {
        headers: { Authorization: `Bearer ${jwtToken}` },
      });
      setUserDetails(userResponse.data);

      const gameResponse = await axios.get(`http://localhost:8080/rest/game/init?gameId=${gameId}`, {
        headers: { Authorization: `Bearer ${jwtToken}` },
      });
      setGameDetails(gameResponse.data);
      setSpieler1Bool(gameResponse.data.spieler1Id === userResponse.data.userID);

      // Initiale Spielzüge aus den GameDetails laden
      setSpielzuege(gameResponse.data.spielzuege || []); // Setze initiale Spielzüge
    } catch (error) {
      console.error("Fehler beim Abrufen der Daten:", error);
      alert("Fehler beim Abrufen der Spielinformationen.");
    }
  };

  // WebSocket-Verbindung einrichten
  const setupWebSocket = () => {
    const socket = new SockJS("http://localhost:8080/websocket");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
      console.log("Connected to WebSocket:", frame);
      setConnected(true); // Markiere die Verbindung als erfolgreich

      stompClient.subscribe(`/topic/game/${gameId}/guess`, (message) => {
        const newGuess = JSON.parse(message.body);
        updateSpielzug(newGuess);
      });

    }, (error) => {
      console.error("WebSocket connection error:", error);
    });

    stompClient.onStompError = (frame) => {
      console.error("WebSocket Error: ", frame.headers["message"]);
    };
  };

  const disconnectWebSocket = () => {
    if (stompClient) {
      stompClient.disconnect(() => {
        console.log("WebSocket disconnected");
        setConnected(false); // Setze Verbindung auf false, wenn sie getrennt wird
      });
    }
  };

  // Spielzug aktualisieren
  const updateSpielzug = (newGuess) => {
    setSpielzuege((prevSpielzuege) =>
      prevSpielzuege.map((spielzug) =>
        spielzug.spielZugId === newGuess.spielZugId
          ? {
              ...spielzug,
              guessSpieler1: newGuess.guessSpieler1,
              guessSpieler2: newGuess.guessSpieler2,
              scoreSpieler1: spielzug.scoreSpieler1 || newGuess.scoreSpieler1, // Nur setzen, wenn nicht vorhanden
              scoreSpieler2: spielzug.scoreSpieler2 || newGuess.scoreSpieler2, // Nur setzen, wenn nicht vorhanden
            }
          : spielzug
      )
    );
  };

  // Guess an REST-Endpoint senden
  const submitGuess = async () => {
    if (!spielzuege[currentSpielzugIndex]) return;

    const spielZugId = spielzuege[currentSpielzugIndex].spielZugId;

    const payload = {
      spielId: gameId,
      spielZugId: spielZugId,
      spielZugScore: score,
      spieler1Bool: spieler1Bool,
    };

    try {
      await axios.post(`http://localhost:8080/rest/game/submitGuess`, payload, {
        headers: { Authorization: `Bearer ${jwtToken}` },
      });
    } catch (error) {
      console.error("Fehler beim Senden des Guesses:", error);
    }
  };

  // Leaflet Map initialisieren
  const initMap = () => {
    if (mapRef.current && !map) {
      console.log("Initialisiere Karte...");

      const mapInstance = L.map(mapRef.current).setView([51.505, -0.09], 5); // Standardansicht

      // TileLayer hinzufügen
      L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager_nolabels/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
      }).addTo(mapInstance);

      // Karte im State speichern
      setMap(mapInstance);

      // Klickereignis für Marker hinzufügen
      mapInstance.on('click', (e) => {
        if (!isPreparing) {
          const { lat, lng } = e.latlng;

          // Falls ein Marker existiert, entferne ihn
          if (currentMarker.current) {
            mapInstance.removeLayer(currentMarker.current);
          }

          // Neuen Marker setzen
          currentMarker.current = L.marker([lat, lng])
            .addTo(mapInstance)
            .bindPopup(`Du hast hier geklickt: <br>Breite: ${lat.toFixed(5)}, Länge: ${lng.toFixed(5)}`)
            .openPopup();

          // Koordinaten speichern
          selectedCoordinates.current = { lat, lng };
          console.log("Ausgewählte Koordinaten:", selectedCoordinates.current);
        }
      });
    }
  };

  // Effekt: Karte initialisieren, wenn die Komponente gemountet wird
  useEffect(() => {
    initMap();
  }, [map]);

  useEffect(() => {
    if (isPreparing) {
      // Hole den letzten abgeschlossenen Spielzug
      const vergangenerSpielzug = spielzuege[currentSpielzugIndex - 1];
      if (vergangenerSpielzug?.koordinaten) {
        const [lat, lng] = vergangenerSpielzug.koordinaten.split(",");

        // Zeige vorbereitenden Marker
        const vorbereitenderMarker = L.marker([parseFloat(lat), parseFloat(lng)])
          .addTo(map)
          .bindPopup(`Letzter Spielzug: ${vergangenerSpielzug.stadtName}`)
          .openPopup();

        // Entferne vorbereitenden Marker nach 5 Sekunden, falls gewünscht
        setTimeout(() => {
          if (map.hasLayer(vorbereitenderMarker)) {
            map.removeLayer(vorbereitenderMarker);
          }
        }, 5000);
      }
    }
  }, [map, isPreparing, currentSpielzugIndex, spielzuege]);

  useEffect(() => {
    return () => {
      if (currentMarker.current && map) {
        map.removeLayer(currentMarker.current);
      }
    };
  }, [map]);

  if (!gameDetails || !userDetails) {
    return <div>Lade...</div>;
  }

  return (
    <div>
      <h1>Spiel Details</h1>
      <p><strong>spieler1Id:</strong> {gameDetails.spieler1Id}</p>
      <p><strong>spieler2Id:</strong> {gameDetails.spieler2Id}</p>
      <p><strong>Spiel-ID:</strong> {gameDetails.gameId}</p>
      <p><strong>Status:</strong> {gameDetails.status}</p>
      <p><strong>Kontinent:</strong> {gameDetails.continent}</p>
      <p><strong>Aktueller Spieler:</strong> {userDetails.username}</p>

      <h2>Timer: {isPreparing ? "Vorbereitung" : "Spielzug"} {timer} Sekunden</h2>

      {spielzuege[currentSpielzugIndex] && !isPreparing && (
        <div>
          <h3>Aktueller Spielzug</h3>
          <p><strong>Stadt:</strong> {spielzuege[currentSpielzugIndex]?.stadtName}</p>
          <input
            type="text"
            value={score}
            onChange={(e) => setScore(e.target.value)}
            placeholder="Dein Guess"
          />
        </div>
      )}

      <h2>Spielzüge</h2>
      <table border="1">
        <thead>
          <tr>
            <th>Spielzug-ID</th>
            <th>Stadt</th>
            <th>Koordinaten</th>
            <th>Guess Spieler 1</th>
            <th>Guess Spieler 2</th>
            <th>Score Spieler 1</th>
            <th>Score Spieler 2</th>
          </tr>
        </thead>
        <tbody>
          {spielzuege
            .filter((_, index) => index < currentSpielzugIndex) // Nur vergangene Züge anzeigen
            .map((spielzug) => (
              <tr key={spielzug.spielZugId}>
                <td>{spielzug.spielZugId}</td>
                <td>{spielzug.stadtName}</td>
                <td>{spielzug.koordinaten}</td>
                <td>{spielzug.guessSpieler1 || "-"}</td>
                <td>{spielzug.guessSpieler2 || "-"}</td>
                <td>{spielzug.scoreSpieler1 || "-"}</td>
                <td>{spielzug.scoreSpieler2 || "-"}</td>
              </tr>
            ))}
        </tbody>
      </table>

      <div ref={mapRef} style={{
          width: "800px",
          height: "800px",
          margin: "20px auto",
          borderRadius: "50%",
          overflow: "hidden",
          border: "2px solid #ccc",
        }}></div> {/* Map Container */}
    </div>
  );
};

export default Game;

  link.href = url;
  document.head.appendChild(link);
};

const Game = () => {
  const { gameId } = useParams();
  const jwtToken = localStorage.getItem("jwtToken");
  const [gameDetails, setGameDetails] = useState(null);
  const [userDetails, setUserDetails] = useState(null);
  const [currentSpielzugIndex, setCurrentSpielzugIndex] = useState(0);
  const [timer, setTimer] = useState(5); // Beginne mit 5 Sekunden Vorbereitung
  const [isPreparing, setIsPreparing] = useState(true);
  const [spieler1Bool, setSpieler1Bool] = useState(true);
  const [score, setScore] = useState("");
  const [spielzuege, setSpielzuege] = useState([]); // Spielzüge mit Städten
  const [connected, setConnected] = useState(false); // Verbindungstatus für WebSocket
  const [map, setMap] = useState(null); // Leaflet map state
  const markerRef = useRef(null);
  const currentMarker = useRef(null);
  const selectedCoordinates = useRef(null);
  let stompClient = null;

  const mapRef = useRef(null); // Referenz für das Map-Container-Div

  // Effekt: Initialisiere Daten und WebSocket-Verbindung
  useEffect(() => {
    loadExternalCSS("https://unpkg.com/leaflet@1.9.4/dist/leaflet.css");
    fetchInitialData();
    setupWebSocket();
    return () => {
      disconnectWebSocket();
    };
  }, []);

  // Effekt: Timer für Vorbereitung und Spielzüge
  useEffect(() => {
    if (timer > 0) {
      const interval = setInterval(() => {
        setTimer((prev) => prev - 1);
      }, 1000);
      return () => clearInterval(interval);
    } else if (timer === 0) {
      if (isPreparing) {
        setIsPreparing(false);
        setTimer(30); // 30 Sekunden für Spielzug
      } else {
        submitGuess();
        setIsPreparing(true);
        setTimer(8); // 5 Sekunden Vorbereitung
        setCurrentSpielzugIndex((prevIndex) => prevIndex + 1);
      }
    }
  }, [timer, isPreparing]);

  // Initiale Daten abrufen
  const fetchInitialData = async () => {
    try {
      const userResponse = await axios.get("http://localhost:8080/rest/user/details", {
        headers: { Authorization: `Bearer ${jwtToken}` },
      });
      setUserDetails(userResponse.data);

      const gameResponse = await axios.get(`http://localhost:8080/rest/game/init?gameId=${gameId}`, {
        headers: { Authorization: `Bearer ${jwtToken}` },
      });
      setGameDetails(gameResponse.data);
      setSpieler1Bool(gameResponse.data.spieler1Id === userResponse.data.userID);

      // Initiale Spielzüge aus den GameDetails laden
      setSpielzuege(gameResponse.data.spielzuege || []); // Setze initiale Spielzüge
    } catch (error) {
      console.error("Fehler beim Abrufen der Daten:", error);
      alert("Fehler beim Abrufen der Spielinformationen.");
    }
  };

  // WebSocket-Verbindung einrichten
  const setupWebSocket = () => {
    const socket = new SockJS("http://localhost:8080/websocket");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
      console.log("Connected to WebSocket:", frame);
      setConnected(true); // Markiere die Verbindung als erfolgreich

      stompClient.subscribe(`/topic/game/${gameId}/guess`, (message) => {
        const newGuess = JSON.parse(message.body);
        updateSpielzug(newGuess);
      });

    }, (error) => {
      console.error("WebSocket connection error:", error);
    });

    stompClient.onStompError = (frame) => {
      console.error("WebSocket Error: ", frame.headers["message"]);
    };
  };

  const disconnectWebSocket = () => {
    if (stompClient) {
      stompClient.disconnect(() => {
        console.log("WebSocket disconnected");
        setConnected(false); // Setze Verbindung auf false, wenn sie getrennt wird
      });
    }
  };

  // Spielzug aktualisieren
  const updateSpielzug = (newGuess) => {
    setSpielzuege((prevSpielzuege) =>
      prevSpielzuege.map((spielzug) =>
        spielzug.spielZugId === newGuess.spielZugId
          ? {
              ...spielzug,
              guessSpieler1: newGuess.guessSpieler1,
              guessSpieler2: newGuess.guessSpieler2,
              scoreSpieler1: spielzug.scoreSpieler1 || newGuess.scoreSpieler1, // Nur setzen, wenn nicht vorhanden
              scoreSpieler2: spielzug.scoreSpieler2 || newGuess.scoreSpieler2, // Nur setzen, wenn nicht vorhanden
            }
          : spielzug
      )
    );
  };

  // Guess an REST-Endpoint senden
  const submitGuess = async () => {
    if (!spielzuege[currentSpielzugIndex]) return;

    const spielZugId = spielzuege[currentSpielzugIndex].spielZugId;

    const payload = {
      spielId: gameId,
      spielZugId: spielZugId,
      spielZugScore: score,
      spieler1Bool: spieler1Bool,
    };

    try {
      await axios.post(`http://localhost:8080/rest/game/submitGuess`, payload, {
        headers: { Authorization: `Bearer ${jwtToken}` },
      });
    } catch (error) {
      console.error("Fehler beim Senden des Guesses:", error);
    }
  };

  // Leaflet Map initialisieren
  const initMap = () => {
    if (mapRef.current && !map) {
      console.log("Initialisiere Karte...");

      const mapInstance = L.map(mapRef.current).setView([51.505, -0.09], 5); // Standardansicht

      // TileLayer hinzufügen
      L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager_nolabels/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
      }).addTo(mapInstance);

      // Karte im State speichern
      setMap(mapInstance);

      // Klickereignis für Marker hinzufügen
      mapInstance.on('click', (e) => {
        if (!isPreparing) {
          const { lat, lng } = e.latlng;

          // Falls ein Marker existiert, entferne ihn
          if (currentMarker.current) {
            mapInstance.removeLayer(currentMarker.current);
          }

          // Neuen Marker setzen
          currentMarker.current = L.marker([lat, lng])
            .addTo(mapInstance)
            .bindPopup(`Du hast hier geklickt: <br>Breite: ${lat.toFixed(5)}, Länge: ${lng.toFixed(5)}`)
            .openPopup();

          // Koordinaten speichern
          selectedCoordinates.current = { lat, lng };
          console.log("Ausgewählte Koordinaten:", selectedCoordinates.current);
        }
      });

      // Wenn Zielkoordinaten gesetzt sind, berechne die Distanz
          if (cityCoordinates) {
            calculateDistance(lat, lng, cityCoordinates.lat, cityCoordinates.lng);
          }
    }
  };

 // Distanzberechnung mit Haversine-Formel
  function toRadians(degrees) {
    return degrees * (Math.PI / 180);
  }

  // Haversine-Formel für Entfernung
  function calculateHaversineDistance(lat1, lng1, lat2, lng2) {
    const R = 6371; // Erdradius in km
    const dLat = toRadians(lat2 - lat1);
    const dLng = toRadians(lng2 - lng1);
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) *
        Math.sin(dLng / 2) * Math.sin(dLng / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  }

  // Entfernung berechnen und anzeigen
  const calculateDistance = (playerLat, playerLng, cityLat, cityLng) => {
    const distance = calculateHaversineDistance(playerLat, playerLng, cityLat, cityLng);

    const resultElement = document.getElementById('result');
    if (resultElement) {
      resultElement.innerHTML = `Entfernung zur gesuchten Stadt: ${distance.toFixed(2)} km`;
    }
  };

  // Effekt: Karte initialisieren, wenn die Komponente gemountet wird
  useEffect(() => {
    initMap();
  }, [map]);

  useEffect(() => {
    if (isPreparing) {
      // Hole den letzten abgeschlossenen Spielzug
      const vergangenerSpielzug = spielzuege[currentSpielzugIndex - 1];
      if (vergangenerSpielzug?.koordinaten) {
        const [lat, lng] = vergangenerSpielzug.koordinaten.split(",");

        // Zeige vorbereitenden Marker
        const vorbereitenderMarker = L.marker([parseFloat(lat), parseFloat(lng)])
          .addTo(map)
          .bindPopup(`Letzter Spielzug: ${vergangenerSpielzug.stadtName}`)
          .openPopup();

        // Entferne vorbereitenden Marker nach 5 Sekunden, falls gewünscht
        setTimeout(() => {
          if (map.hasLayer(vorbereitenderMarker)) {
            map.removeLayer(vorbereitenderMarker);
          }
        }, 5000);
      }
    }
  }, [map, isPreparing, currentSpielzugIndex, spielzuege]);

  useEffect(() => {
    return () => {
      if (currentMarker.current && map) {
        map.removeLayer(currentMarker.current);
      }
    };
  }, [map]);

  if (!gameDetails || !userDetails) {
    return <div>Lade...</div>;
  }

  return (
    <div>
      <h1>Spiel Details</h1>
      <p><strong>spieler1Id:</strong> {gameDetails.spieler1Id}</p>
      <p><strong>spieler2Id:</strong> {gameDetails.spieler2Id}</p>
      <p><strong>Spiel-ID:</strong> {gameDetails.gameId}</p>
      <p><strong>Status:</strong> {gameDetails.status}</p>
      <p><strong>Kontinent:</strong> {gameDetails.continent}</p>
      <p><strong>Aktueller Spieler:</strong> {userDetails.username}</p>

      <h2>Timer: {isPreparing ? "Vorbereitung" : "Spielzug"} {timer} Sekunden</h2>

      {spielzuege[currentSpielzugIndex] && !isPreparing && (
        <div>
          <h3>Aktueller Spielzug</h3>
          <p><strong>Stadt:</strong> {spielzuege[currentSpielzugIndex]?.stadtName}</p>
          <input
            type="text"
            value={score}
            onChange={(e) => setScore(e.target.value)}
            placeholder="Dein Guess"
          />
        </div>
      )}

      <h2>Spielzüge</h2>
      <table border="1">
        <thead>
          <tr>
            <th>Spielzug-ID</th>
            <th>Stadt</th>
            <th>Koordinaten</th>
            <th>Guess Spieler 1</th>
            <th>Guess Spieler 2</th>
            <th>Score Spieler 1</th>
            <th>Score Spieler 2</th>
          </tr>
        </thead>
        <tbody>
          {spielzuege
            .filter((_, index) => index < currentSpielzugIndex) // Nur vergangene Züge anzeigen
            .map((spielzug) => (
              <tr key={spielzug.spielZugId}>
                <td>{spielzug.spielZugId}</td>
                <td>{spielzug.stadtName}</td>
                <td>{spielzug.koordinaten}</td>
                <td>{spielzug.guessSpieler1 || "-"}</td>
                <td>{spielzug.guessSpieler2 || "-"}</td>
                <td>{spielzug.scoreSpieler1 || "-"}</td>
                <td>{spielzug.scoreSpieler2 || "-"}</td>
              </tr>
            ))}
        </tbody>
      </table>

      <div ref={mapRef} style={{
          width: "800px",
          height: "800px",
          margin: "20px auto",
          borderRadius: "50%",
          overflow: "hidden",
          border: "2px solid #ccc",
        }}></div> {/* Map Container */}
    </div>
  );
};

export default Game;
