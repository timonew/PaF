import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import axios from "axios";

const Lobby = () => {
  const [userDetails, setUserDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const [waitingGames, setWaitingGames] = useState([]);
  const [difficulty, setDifficulty] = useState(1);
  const [continent, setContinent] = useState("Europe");
  const [gameRequest, setGameRequest] = useState(null);
  const [gameRequestsAnswer, setGameRequestsAnswer] = useState(null);
  const [countdown, setCountdown] = useState(null); // Neues State für den Countdown
  const [message, setMessage] = useState(""); // Neues State für die Nachricht
  const navigate = useNavigate();
  let stompClient = null;

  const jwtToken = localStorage.getItem("jwtToken");

  useEffect(() => {
    if (!jwtToken) {
      navigate("/login");
      return;
    }

    const fetchInitialData = async () => {
      try {
        const userResponse = await axios.get("http://localhost:8080/rest/user/details", {
          headers: { Authorization: `Bearer ${jwtToken}` },
        });
        setUserDetails(userResponse.data);

        const gameResponse = await axios.get("http://localhost:8080/rest/game/waiting", {
          headers: { Authorization: `Bearer ${jwtToken}` },
        });
        setWaitingGames(gameResponse.data);

        setupWebSocket(userResponse.data.userID);
      } catch (error) {
        console.error("Fehler beim Abrufen der Daten:", error);
        navigate("/login");
      } finally {
        setLoading(false);
      }
    };

    fetchInitialData();

    return () => disconnectWebSocket();
  }, [jwtToken, navigate]);

  const setupWebSocket = (userID) => {
    const socket = new SockJS("http://localhost:8080/websocket");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      console.log("WebSocket-Verbindung hergestellt");

      if (stompClient.connected) {
        stompClient.subscribe("/topic/waitingGames", (message) => {
          const games = JSON.parse(message.body);
          setWaitingGames(games);
        });

        stompClient.subscribe(`/topic/user/${userID}/gameRequests`, (message) => {
          const request = JSON.parse(message.body);
          setGameRequest(request);
        });

        stompClient.subscribe(`/topic/user/${userID}/gameRequestsAnswer`, (message) => {
          const gameRequestsAnswer = JSON.parse(message.body);
          setGameRequestsAnswer(gameRequestsAnswer);

          if (gameRequestsAnswer.gameAccepted === false) {
            setMessage(`Spielanfrage für Spiel ${gameRequestsAnswer.gameId} wurde abgelehnt.`);
            setCountdown(null); // Countdown zurücksetzen
          } else {
            setMessage(`Spielanfrage akzeptiert! Das Spiel startet in 5 Sekunden.`);
            setCountdown(5); // Countdown auf 5 setzen
          }
        });
      } else {
        console.error("StompClient ist nicht verbunden!");
      }
    });

    stompClient.onStompError = (frame) => {
      console.error(`WebSocket Broker Error: ${frame.headers["message"]}`);
    };

    stompClient.onWebSocketError = (event) => {
      console.error(`WebSocket Error: ${event.message || "Unbekannter Fehler"}`);
    };
  };

  const disconnectWebSocket = () => {
    if (stompClient) {
      stompClient.disconnect(() => {
        console.log("WebSocket-Verbindung geschlossen");
      });
    }
  };

  // Countdown-Logik
  useEffect(() => {
    if (countdown !== null && countdown > 0) {
      const interval = setInterval(() => {
        setCountdown((prevCountdown) => {
          if (prevCountdown <= 1) {
            clearInterval(interval);
            window.location.href = `/game/${gameRequestsAnswer.gameId}`; // Weiterleitung zum Spiel
            return 0;
          }
          return prevCountdown - 1;
        });
      }, 1000);
    }
  }, [countdown, gameRequestsAnswer]);

  const startGame = async () => {
    try {
      const gameStartDTO = { difficulty, continent };
      await axios.post("http://localhost:8080/rest/game/start", gameStartDTO, {
        headers: {
          Authorization: `Bearer ${jwtToken}`,
          "Content-Type": "application/json",
        },
      });
    } catch (error) {
      console.error("Fehler beim Starten des Spiels:", error);
      alert("Spiel konnte nicht gestartet werden.");
    }
  };

  const stopGame = async (gameId) => {
    try {
      await axios.post("http://localhost:8080/rest/game/updateStatus", { gameId, newStatus: "Stopped" }, {
        headers: {
          Authorization: `Bearer ${jwtToken}`,
          "Content-Type": "application/json",
        },
      });

    } catch (error) {
      console.error("Fehler beim Stoppen des Spiels:", error);
      alert("Fehler beim Stoppen des Spiels.");
    }
  };

  const joinRequest = async (gameId, player1Id) => {
    try {
      await axios.post(
        `http://localhost:8080/rest/game/joinrequest`,
        { gameId, player1Id },
        {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
            "Content-Type": "application/json",
          },
        }
      );

    } catch (error) {
      console.error("Fehler beim Senden der Spielanfrage:", error);
      alert("Spielanfrage konnte nicht gesendet werden.");
    }
  };

  const requestAnswer = async (gameId, player2Name, decision) => {
    try {
      await axios.post(
        `http://localhost:8080/rest/game/requestAnswer`,
        { gameId, player2Name, decision },
        {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
            "Content-Type": "application/json",
          },
        }
      );
    } catch (error) {
      console.error("Fehler beim Senden der Antwort auf Spielanfrage:", error);
      alert("Antwort auf Spielanfrage konnte nicht gesendet werden.");
    }
  };

  if (loading) {
    return <p>Lade...</p>;
  }

  return (
    <div>
      <h1>Willkommen in der Lobby</h1>
      {userDetails ? (
        <div>
          <p>Angemeldeter Benutzer: {userDetails.username}</p>
          <p>mit der ID: {userDetails.userID}</p>

          <h2>Spiel starten</h2>
          <div>
            <label>Schwierigkeitsgrad:</label>
            <select value={difficulty} onChange={(e) => setDifficulty(parseInt(e.target.value, 10))}>
              <option value={1}>1 - Einfach</option>
              <option value={2}>2 - Mittel</option>
              <option value={3}>3 - Schwer</option>
            </select>
          </div>
          <div>
            <label>Kontinent:</label>
            <select value={continent} onChange={(e) => setContinent(e.target.value)}>
              <option value="Europe">Europa</option>
              <option value="Asia">Asien</option>
              <option value="Africa">Afrika</option>
              <option value="North America">Nordamerika</option>
              <option value="South America">Südamerika</option>
              <option value="Australia">Australien</option>
            </select>
          </div>
          <button onClick={startGame}>Spiel starten</button>

          <h2>Wartende Spiele</h2>
          {waitingGames.length > 0 ? (
            <table>
              <thead>
                <tr>
                  <th>Spieler 1</th>
                  <th>Kontinent</th>
                  <th>Schwierigkeitsgrad</th>
                  <th>Spieler1ID</th>
                  <th>Aktion</th>
                </tr>
              </thead>
              <tbody>
                {waitingGames.map((game) => (
                  <tr key={game.id}>
                    <td>{game.spieler1Name}</td>
                    <td>{game.continent}</td>
                    <td>{game.difficultyLevel}</td>
                    <td>{game.spieler1ID}</td>
                    <td>
                       {game.spieler1Name === userDetails.username ? (
                        <button onClick={() => stopGame(game.id)}>Spiel stoppen</button>
                      ) : (
                        <button onClick={() => joinRequest(game.id, game.spieler1ID)}>Spiel beitreten</button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>Keine wartenden Spiele verfügbar.</p>
          )}
        </div>
      ) : (
        <p>Benutzerinformationen konnten nicht geladen werden.</p>
      )}
      {gameRequest ? (
        <div>
          <p>{gameRequest.requestingPlayer} möchte mit dir spielen!</p>
          <button onClick={() => requestAnswer(gameRequest.gameId, gameRequest.requestingPlayer, "requestAccepted")}>Anfrage annehmen</button>
          <button onClick={() => requestAnswer(gameRequest.gameId, gameRequest.requestingPlayer, "requestDeclined")}>Anfrage ablehnen</button>
        </div>
      ) : null}

      {message && (
        <div style={{ marginTop: "20px", padding: "10px", border: "1px solid #ccc", backgroundColor: "#f0f0f0" }}>
          <p>{message}</p>
          {countdown !== null && countdown > 0 && <p>Countdown: {countdown} Sekunden</p>}
        </div>
      )}
    </div>
  );
};

export default Lobby;
