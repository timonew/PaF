import React, { useEffect, useState, useRef } from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import L from "leaflet"; // Leaflet importieren
import "./custom.css";

// Hilfsfunktionen
// Haversine-Formel für Entfernung
function calculateHaversineDistance(lat1, lng1, lat2, lng2) {
  const R = 6371; // Erdradius in km
  const dLat = toRadians(lat2 - lat1);
  const dLng = toRadians(lng2 - lng1);
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) *
      Math.sin(dLng / 2) * Math.sin(dLng / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c; // Entfernung in Kilometern
}

// Funktion, um Grad in Bogenmaß umzuwandeln
function toRadians(degrees) {
  return degrees * (Math.PI / 180);
}

// CSS-Loader
const loadExternalCSS = (url) => {
  const link = document.createElement("link");
  link.rel = "stylesheet";
  link.href = url;
  document.head.appendChild(link);
};

const Game = () => {
  // =========================================
  // ** State- und Ref-Initialisierungen **
  // =========================================

  const { gameId } = useParams(); // Parameter aus der URL
  const jwtToken = localStorage.getItem("jwtToken"); // Authentifizierungs-Token
  const [gameDetails, setGameDetails] = useState(null); // Spielinformationen
  const [userDetails, setUserDetails] = useState(null); // Benutzerdetails
  const [currentSpielzugIndex, setCurrentSpielzugIndex] = useState(0); // Aktueller Spielzug
  const [timer, setTimer] = useState(5); // Timer für Züge (Vorbereitung/Spielzeit)
  const [gameModus, setGameModus] = useState(0); // Status des Vorbereitungsmodus
  const [spieler1Bool, setSpieler1Bool] = useState(true); // Spielerzuordnung
  const [score, setScore] = useState(0); // Punktzahl für den aktuellen Zug
  const [distance, setDistance] = useState(20000); // Distanz für den aktuellen Zug
  const [spielzuege, setSpielzuege] = useState([]); // Liste der Spielzüge
  const [connected, setConnected] = useState(false); // Verbindungstatus für WebSocket
  const [map, setMap] = useState(null); // Leaflet Map-Instanz
  const markerRef = useRef(null); // Referenz für den Marker
  const currentMarker= useRef(null); // Aktueller gesetzter Marker
  const selectedCoordinates = useRef(null); // Benutzer-Auswahlkoordinaten
  const mapRef = useRef(null); // Referenz für das Map-Container-Div
  const [isPlayer1,setIsPlayer1] = useState(false);
  const navigate = useNavigate();
  const [newScore,setNewScore] = useState(null);
  const [endGameMessage,setEndGameMessage]=useState(null);
  const [countdown, setCountdown] = useState(null);
  let stompClient = null; // WebSocket-Client
  const totalScorePl1 = useRef(0);
  const totalScorePl2 = useRef(0);



    // =========================================
  // ** useEffect Hooks **
  // =========================================

  // Effekt: Initialisiere Daten und WebSocket-Verbindung
  useEffect(() => {
    loadExternalCSS("https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"); // CSS für Leaflet laden
    fetchInitialData(); // Daten abrufen (Spiel & Benutzer)
    setupWebSocket(); // WebSocket-Verbindung einrichten

    return () => {
      disconnectWebSocket(); // Verbindung bei Cleanup trennen
    };
  }, []); // Läuft nur einmal bei Komponent-Mount


  // Effekt: Initialisiere Leaflet-Karte
  useEffect(() => {
    if (!map) initMap(); // Karte initialisieren, falls noch nicht vorhanden

    if (map && gameModus===2) {
      const vergangenerSpielzug = spielzuege[currentSpielzugIndex];
      if (vergangenerSpielzug?.koordinaten) {
          const [lat, lng] = vergangenerSpielzug.koordinaten.split(",");
          let calculatedDistance = 20000;
              const lat2 = selectedCoordinates.current.lat
              const lng2 = selectedCoordinates.current.lng
              calculatedDistance = parseInt(calculateHaversineDistance(
                parseFloat(lat),
                parseFloat(lng),
                parseFloat(lat2),
                parseFloat(lng2)
              ));
          console.log("distanz:",calculatedDistance);
          setDistance(calculatedDistance);
          setScore(20000 - calculatedDistance);// Berechnung des Scores aus ermittelter Distanz und halben Erdumfang

           // Marker und Linie für den vorherigen Spielzug anzeigen
        let currentMoveMarker = L.marker([parseFloat(lat), parseFloat(lng)])
          .addTo(map)
          .bindPopup(
            `Gesuchte Stadt: ${spielzuege[currentSpielzugIndex].stadtName} | Distanz: ${calculatedDistance}`
          )
          .openPopup();


           const line = L.polyline(
          [
            [lat2, lng2], // Spieler-Tipp
            [parseFloat(lat), parseFloat(lng)], // Ziel
          ],
          {
            color: "blue",
            weight: 3,
            dashArray: "5, 10",
          }
        ).addTo(map);

        // Entferne Marker nach 5 Sekunden löschen
        setTimeout(() => {
          if (map.hasLayer(currentMoveMarker)) {
            map.removeLayer(currentMoveMarker);
          }
          if (map.hasLayer(line)) {
            map.removeLayer(line);
          }
        }, 5000);

     }
    }
  }, [map, gameModus]);

useEffect(() => {
  const timeout = setTimeout(() => {
    submitGuess();
  }, 3000);
  return () => clearTimeout(timeout);
},[distance]);

  // Effekt: Klickereignis für Kartenauswahl hinzufügen
  useEffect(() => {
    if (map) {
      map.on("click", handleClick); // Klick-Handler hinzufügen
      return () => {
        map.off("click", handleClick); // Cleanup: Event-Handler entfernen
      };
    }
  }, [map]);

  // Cleanup-Effekt: Entferne Marker beim Komponenten-Unmount
  useEffect(() => {
    return () => {
      if (currentMarker.current && map) {
        map.removeLayer(currentMarker.current);
      }
    };
  }, [map]);

  useEffect(() => {
  if (gameDetails && userDetails) {
    if (gameDetails.spieler1Name === userDetails.username) {
      setIsPlayer1(true);
    } else {
      setIsPlayer1(false);
    }
  }
}, [gameDetails, userDetails]);

  // Countdown-Logik bei Spielende
  useEffect(() => {
    if (countdown !== null && countdown > 0) {
      const interval = setInterval(() => {
        setCountdown((prevCountdown) => {
          if (prevCountdown <= 1) {
            clearInterval(interval);
            navigate("/lobby"); // Weiterleitung zur Lobby
            return 0;
          }
          return prevCountdown - 1;
        });
      }, 1000);
    }
  }, [countdown]);

  useEffect(() => {
  if (spielzuege.length > 0) {
    starteSpielzuege(); // Startet erst, wenn spielzuege gefüllt ist
  }
}, [spielzuege]);


    // =========================================
  // ** Funktionen **
  // =========================================

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
      setSpielzuege(gameResponse.data.spielzuege || []);
    } catch (error) {
      console.error("Fehler beim Abrufen der Daten:", error);
    }
  };

  // WebSocket-Verbindung einrichten
  const setupWebSocket = () => {
    const socket = new SockJS("http://localhost:8080/websocket");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, (frame) => {
      console.log("Connected to WebSocket:", frame);
      setConnected(true);

      //Websocket für Spielzugwerte
      stompClient.subscribe(`/topic/game/${gameId}/guess`, (message) => {
        const newGuess = JSON.parse(message.body);
        updateSpielzug(newGuess);
      });

    });
  };

  // WebSocket-Verbindung trennen
  const disconnectWebSocket = () => {
    if (stompClient) {
      stompClient.disconnect(() => {
        console.log("WebSocket disconnected");
        setConnected(false);
      });
    }
  };

  // Spielzug-Daten aktualisieren
    const updateSpielzug = (newGuess) => {
      setSpielzuege((prevSpielzuege) =>
        prevSpielzuege.map((spielzug) =>
          spielzug.spielZugId === newGuess.spielZugId
            ? {
                ...spielzug,
                ...newGuess, // Neue Daten einfügen
                guessSpieler1: newGuess.guessSpieler1 || spielzug.guessSpieler1, // Vorhandene Daten erhalten
                guessSpieler2: newGuess.guessSpieler2 || spielzug.guessSpieler2,
                scoreSpieler1: newGuess.scoreSpieler1 || spielzug.scoreSpieler1,
                scoreSpieler2: newGuess.scoreSpieler2 || spielzug.scoreSpieler2,
              }
            : spielzug
        )
      );
      totalScorePl1.current +=  newGuess.scoreSpieler1;
      totalScorePl2.current +=  newGuess.scoreSpieler2;
    };


  //Map-Klick Aktionen
   const handleClick = (e) => {
        const { lat, lng } = e.latlng;
        // Entferne bestehenden Marker
        if (currentMarker.current) {
          map.removeLayer(currentMarker.current);
        }

        // Neuen Marker setzen
        currentMarker.current = L.marker([lat, lng])
          .addTo(map)
          .bindPopup(`Du hast hier geklickt: <br>Breite: ${lat.toFixed(5)}, Länge: ${lng.toFixed(5)}`)
          .openPopup();

        selectedCoordinates.current = { lat, lng }; // Koordinaten speichern
        console.log("Ausgewählte Koordinaten:", selectedCoordinates.current);
};

// Funktion für den Timer
const startTimer = (duration, callback) => {
  let timer = duration;
  setTimer(timer); // Initialen Timer-Wert setzen

  const interval = setInterval(() => {
    timer -= 1; // Timer Wert verringern
    setTimer(timer); // Zustand mit reduziertem Wert aktualisieren

    if (timer <= 0) {
      clearInterval(interval); // Stoppe den Timer, wenn er abgelaufen ist
      callback(); // Callback auslösen, um zu signalisieren, dass der Timer abgelaufen ist
    }
  }, 1000);
};

// Funktion zum Starten der Spielzüge
  const starteSpielzuege = () => {
    const vorbereitungsZeit = 5; // 5 Sekunden initiale Vorbereitung
    const pausenZeit = 8; // 8 Sekunden Vorbereitung zwischen Spielzügen
    const spielZeit = 20; // 20 Sekunden Spielzeit pro Spielzug


    const initialPreparation = () => {startTimer(vorbereitungsZeit,() =>
    startDurchlauf(0))};// Starte den Hauptablauf der Spielzüge nach der Vorbereitung


    const startDurchlauf = (index) => {
         setGameModus(1); // Spielmodus aktivieren
         setCurrentSpielzugIndex(index);
         startTimer(spielZeit, () => {
                   // Pause zwischen den Spielzügen
                  setGameModus(2);
                  startTimer(pausenZeit, () => {
                  if (index === spielzuege.length - 1) { // Letzter Spielzug abgeschlossen
                  endGame();
                } else {
                  startDurchlauf(index + 1);
                   }
               });
           });
    };

  // Starte mit der Initialen Vorbereitung
  if (gameModus===0){
    initialPreparation();
    }
};


// Endsequenz
const endGame = () => {
  const currentScorePl1 = totalScorePl1.current;
  const currentScorePl2 = totalScorePl2.current;

  console.log(currentScorePl1, currentScorePl2);

  if (currentScorePl1 > currentScorePl2) {
    setEndGameMessage(
      `Spielende erreicht! ${gameDetails.spieler1Name} hat gewonnen mit ${currentScorePl1} Punkten. Zurück zur Lobby in 5 Sekunden.`
    );
  } else if (currentScorePl1 < currentScorePl2) {
    setEndGameMessage(
      `Spielende erreicht! ${gameDetails.spieler2Name} hat gewonnen mit ${currentScorePl2} Punkten. Zurück zur Lobby in 5 Sekunden.`
    );
  } else {
    setEndGameMessage(
      `Spielende erreicht! Unentschieden mit ${currentScorePl1} Punkten. Zurück zur Lobby in 7 Sekunden.`
    );
  }

  setCountdown(7);
};



  // Benutzerantwort senden
  const submitGuess = async () => {
    if (!spielzuege[currentSpielzugIndex]) return;

    const spielZugId = spielzuege[currentSpielzugIndex].spielZugId;

    const payload = {
      spielId: gameId,
      spielZugId,
      spielZugScore: score,
      spieler1Bool,
      spielZugGuess: `${selectedCoordinates.current.lat.toFixed(4)},${selectedCoordinates.current.lng.toFixed(4)}`
    };

    try {
      await axios.post("http://localhost:8080/rest/game/submitGuess", payload, {
        headers: { Authorization: `Bearer ${jwtToken}` },
      });
    } catch (error) {
      console.error("Fehler beim Senden des Guesses:", error);
    }
  };

  // Karte initialisieren
  const initMap = () => {
    if (!map && mapRef.current) {
      const mapInstance = L.map(mapRef.current).setView([51.505, -0.09], 5);
      L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager_nolabels/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
      }).addTo(mapInstance);
      setMap(mapInstance);

       // initialen Marker setzen
       const lat = 52.51497377167247
       const lng = 9.265730853751185
        currentMarker.current = L.marker([lat, lng])
          .addTo(mapInstance)
          .bindPopup(`Klicke die Karte!`)
          .openPopup();

        selectedCoordinates.current = { lat, lng }; // Koordinaten speichern
        console.log("Ausgewählte Koordinaten:", selectedCoordinates.current);
    }
  };




  // =========================================
  // ** Rendering **
  // =========================================

  if (!gameDetails || !userDetails) {
    return <div>Lade...</div>;
  }

  return (
    <div>
      <p><strong>{gameDetails.spieler1Name} Punkte: {totalScorePl1.current} </strong> | <strong>{gameDetails.spieler2Name} Punkte: {totalScorePl2.current} </strong></p>

       <table>
        <thead>
          <tr>
            <th>Spielzug</th>
            <th>Stadt</th>
            <th>Koordinaten</th>
            <th>Guess {gameDetails.spieler1Name}</th>
            <th>Guess {gameDetails.spieler2Name}</th>
            <th>Score {gameDetails.spieler1Name}</th>
            <th>Score {gameDetails.spieler2Name}</th>
          </tr>
        </thead>
        <tbody>
          {spielzuege
           .filter((_, index) => index < currentSpielzugIndex)
           .map((spielzug, index) => (
            <tr key={spielzug.spielZugId}>
              <td>{index + 1}</td> {/* Nummer basierend auf dem Index der Map */}
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

      {endGameMessage && (
        <div style={{ marginTop: "20px", padding: "10px", border: "1px solid #ccc", backgroundColor: "#f0f0f0" }}>
          <p>{endGameMessage}</p>
          {countdown !== null && countdown > 0 && <p>Countdown: {countdown} Sekunden</p>}
        </div>
      )}

      <h4>Timer: {gameModus===2 || gameModus===0 ? "Vorbereitung" : "Spielzug"} {timer} Sekunden</h4>
      {gameModus===1 && (
        <div>
          <p><strong>gesuchte Stadt:{spielzuege[currentSpielzugIndex]?.stadtName}</strong></p>
        </div>
      )}

      <div ref={mapRef} style={{
        width: "800px",
        height: "800px",
        margin: "20px auto",
        borderRadius: "50%",
        overflow: "hidden",
        border: "2px solid #ccc",
      }}></div>

    </div>
  );
};

export default Game;
