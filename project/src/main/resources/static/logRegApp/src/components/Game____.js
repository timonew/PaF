import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import { initializeMap, calculateDistance } from "./leaflet/leafletfunctions";

const loadExternalCSS = (url) => {
  const link = document.createElement("link");
  link.rel = "stylesheet";
  link.href = url;
  document.head.appendChild(link);
};

const Game = () => {
  const { gameId } = useParams();
  const jwtToken = localStorage.getItem("jwtToken");
  const [spielzuege, setSpielzuege] = useState([]);
  const [currentSpielzugIndex, setCurrentSpielzugIndex] = useState(0);
  const [timer, setTimer] = useState(30);
  const [isPreparing, setIsPreparing] = useState(true);
  const [score, setScore] = useState("");
  const [selectedCoordinates, setSelectedCoordinates] = useState(null);
  const [mapInitialized, setMapInitialized] = useState(false);
  let stompClient = null;

  // Laden der initialen Spielinformationen und Setzen des WebSocket
  useEffect(() => {
    loadExternalCSS("https://unpkg.com/leaflet@1.9.4/dist/leaflet.css");
    fetchInitialData();
    setupWebSocket();
    return () => {
      disconnectWebSocket();
    };
  }, []);

  // Initialisierung der Karte, wenn Spielzüge geladen wurden
  useEffect(() => {
    if (spielzuege.length > 0 && !mapInitialized) {
      initializeMap("map", spielzuege, currentSpielzugIndex, handleMapClick);
      setMapInitialized(true);
    }
  }, [spielzuege, currentSpielzugIndex, mapInitialized]);

  // Timer-Logik: Vorbereitungs- und Spielzeit wechseln
  useEffect(() => {
    if (timer > 0) {
      const interval = setInterval(() => {
        setTimer((prev) => prev - 1);
      }, 1000);
      return () => clearInterval(interval);
    } else {
      if (isPreparing) {
        setIsPreparing(false);
        setTimer(30); // Start der Spielzeit
      } else {
        submitGuess();
        setIsPreparing(true);
        setTimer(5); // Vorbereitung für nächsten Spielzug
        setCurrentSpielzugIndex((prevIndex) => prevIndex + 1); // Wechsle zum nächsten Spielzug
      }
    }
  }, [timer, isPreparing]);

  // Funktion zum Abrufen der initialen Daten
  const fetchInitialData = async () => {
    try {
      const gameResponse = await axios.get(`http://localhost:8080/rest/game/init?gameId=${gameId}`, {
        headers: { Authorization: `Bearer ${jwtToken}` },
      });
      setSpielzuege(gameResponse.data.spielzuege || []);
    } catch (error) {
      console.error("Fehler beim Abrufen der Spielinformationen:", error);
    }
  };

  // WebSocket-Setup zur Kommunikation mit dem Server
  const setupWebSocket = () => {
    const socket = new SockJS("http://localhost:8080/websocket");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      stompClient.subscribe(`/topic/game/${gameId}/guess`, (message) => {
        const newGuess = JSON.parse(message.body);
        updateSpielzug(newGuess);
      });
    });

    stompClient.onStompError = (frame) => {
      console.error("WebSocket-Fehler:", frame.headers["message"]);
    };
  };

  // Trennung der WebSocket-Verbindung
  const disconnectWebSocket = () => {
    if (stompClient) {
      stompClient.disconnect(() => {
        console.log("WebSocket-Verbindung geschlossen.");
      });
    }
  };

  // Funktion, die beim Klick auf die Karte aufgerufen wird
  const handleMapClick = (lat, lng) => {
    setSelectedCoordinates({ lat, lng });
    console.log(`Ausgewählte Koordinaten: ${lat}, ${lng}`);
  };

  // Funktion zum Absenden der Schätzung (Guess)
  const submitGuess = async () => {
    if (!spielzuege[currentSpielzugIndex] || !selectedCoordinates) return;

    const spielZugId = spielzuege[currentSpielzugIndex].spielZugId;
    const { koordinaten } = spielzuege[currentSpielzugIndex];
    const [lat, lng] = koordinaten.split(",").map((coord) => parseFloat(coord.trim()));

      const payload = {
      spielId: gameId,
      spielZugId: spielZugId,
      spielZugScore: score,
    };
    try {
      await axios.post(`http://localhost:8080/rest/game/submitGuess`, payload, {
        headers: { Authorization: `Bearer ${jwtToken}` },
      });
    } catch (error) {
      console.error("Fehler beim Senden des Guesses:", error);
    }
  };

  // Aktualisieren des Spielzugs mit der neuen Schätzung
  const updateSpielzug = (newGuess) => {
    setSpielzuege((prevSpielzuege) =>
      prevSpielzuege.map((spielzug) =>
        spielzug.spielZugId === newGuess.spielZugId
          ? { ...spielzug, ...newGuess } // Die Daten des Spielzugs mit der neuen Schätzung kombinieren
          : spielzug // Andernfalls den Spielzug unverändert lassen
      )
    );
  };

  // Wenn keine Spielzüge vorhanden sind, lade die Seite
  if (!spielzuege.length) {
    return <div>Lade...</div>;
  }

  return (
    <div>
      <h1>Spiel Details</h1>
      <h2>Timer: {isPreparing ? "Vorbereitung" : "Spielzug"} {timer} Sekunden</h2>

      {spielzuege[currentSpielzugIndex] && !isPreparing && (
        <div>
          <h3>Aktueller Spielzug</h3>
          <p>Gesuchte Stadt: {spielzuege[currentSpielzugIndex]?.stadtName}</p>
          <p>Ausgewählte Koordinaten: {selectedCoordinates ? `${selectedCoordinates.lat}, ${selectedCoordinates.lng}` : "Keine"}</p>
          <p>Punkte für diesen Zug: {score || "-"}</p>
        </div>
      )}

      <div
        id="map"
        style={{
          width: "800px",
          height: "800px",
          margin: "20px auto",
          borderRadius: "50%",
          overflow: "hidden",
          border: "2px solid #ccc",
        }}
      ></div>

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
    </div>
  );
};

export default Game;
