import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

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
  let stompClient = null;

  // Effekt: Initialisiere Daten und WebSocket-Verbindung
  useEffect(() => {
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
        setTimer(5); // 5 Sekunden Vorbereitung
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

    stompClient.connect({}, () => {
      stompClient.subscribe(`/topic/game/${gameId}/guess`, (message) => {
        const newGuess = JSON.parse(message.body);
        updateSpielzug(newGuess);
      });
    });

    stompClient.onStompError = (frame) => {
      console.error("WebSocket Error: ", frame.headers["message"]);
    };
  };

  const disconnectWebSocket = () => {
    if (stompClient) {
      stompClient.disconnect(() => {
        console.log("WebSocket disconnected");
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
              .filter((_, index) => index < currentSpielzugIndex) // Nur  vergangene Züge anzeigen
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
