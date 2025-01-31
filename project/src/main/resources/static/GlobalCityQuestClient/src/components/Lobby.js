import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import axios from "axios";
import "./custom.css"

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

 const handleLogout = () => {
    localStorage.removeItem("jwt");
    localStorage.removeItem("userDetails");
    navigate("/login");
  };

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
      await axios.post("http://localhost:8080/rest/game/requestAnswer",{ gameId, player2Name, decision },
        {headers: {
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
    <form className="lobbyinterface">
        <div>
            <button
                onClick={handleLogout} className="btn block-cube block-cube-hover" style={{position: 'absolute', top: 0, right: 0, margin: '25px'}} >
            <div className="bg-top">
                <div className="bg-inner"></div>
            </div>
            <div className="bg-right">
                <div className="bg-inner"></div>
            </div>
            <div className="bg">
                <div className="bg-inner"></div>
            </div>
            <div className="text">Log Out</div>
        </button>
    </div>
    <div>
        <h3>Willkommen beim Global City Guess</h3>
        {userDetails ? (
            <div>
                <p>Angemeldeter Benutzer: {userDetails.username} </p>
                <p> Spiele gespielt / Spiele gewonnen: {userDetails.gamesPlayed} / {userDetails.gamesWon} </p>
                <h4>Highscores</h4>
                {userDetails.highscores && userDetails.highscores.length > 0 ? (
                    <table>
                        <thead>
                        <tr>
                            <th>Schwierigkeitsgrad</th>
                            <th>Kontinent</th>
                            <th>Höchster Punktestand</th>
                        </tr>
                        </thead>
                        <tbody>
                        {userDetails.highscores.map((highscore, index) => (
                            <tr key={index}>
                                <td>
                                    {highscore.difficultyLevel === 1
                                        ? "Leicht"
                                        : highscore.difficultyLevel === 2
                                            ? "Mittel"
                                            : "Schwer"}
                                </td>
                                <td>{highscore.continent}</td>
                                <td>{highscore.score}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>Keine Highscores verfügbar.</p>
                )}


                <h5>Schlage ein Spiel vor oder wähle ein vorhandenes aus!</h5>
                <label>Schwierigkeitsgrad:</label>
                <div className="control block-cube block-input">
                    <div className="">
                        <select className="dropdown" value={difficulty}
                                onChange={(e) => setDifficulty(parseInt(e.target.value, 10))}>
                            <option value={1}>1 - Einfach</option>
                            <option value={2}>2 - Mittel</option>
                            <option value={3}>3 - Schwer</option>
                        </select>
                        <div className="bg-top">
                            <div className="bg-inner"></div>
                        </div>
                        <div className="bg-right">
                            <div className="bg-inner"></div>
                        </div>
                        <div className="bg">
                            <div className="bg-inner"></div>
                        </div>
                    </div>
                </div>

                <div>
                    <label>Kontinent:</label>
                    <div className="control block-cube block-input">
                        <select className="dropdown" value={continent} onChange={(e) => setContinent(e.target.value)}>
                            <option value="Europe">Europa</option>
                            <option value="Asia">Asien</option>
                        </select>
                        <div className="bg-top">
                            <div className="bg-inner"></div>
                        </div>
                        <div className="bg-right">
                            <div className="bg-inner"></div>
                        </div>
                        <div className="bg">
                            <div className="bg-inner"></div>
                        </div>
                    </div>
                </div>
                <button onClick={startGame} type="submit" className="btn block-cube block-cube-hover"  style={{position: 'relative', width: '100%', height: '100%'}} >
                    <div className="bg-top">
                        <div className="bg-inner"></div>
                    </div>
                    <div className="bg-right">
                        <div className="bg-inner"></div>
                    </div>
                    <div className="bg">
                        <div className="bg-inner"></div>
                    </div>
                    <div className="text">Spiel vorschlagen</div>

                </button>

                <h2>Wartende Spiele</h2>

                {waitingGames.length > 0 ? (
                    <table>
                        <thead style={{width: "100px"}}>
                        <tr>
                            <th>Spieler 1</th>
                            <th>Kontinent</th>
                            <th>Schwierigkeitsgrad</th>
                            <th>Aktion</th>
                        </tr>
                        </thead>
                        <tbody style={{width: "100px"}}>
                        {waitingGames.map((game) => (
                            <tr key={game.id}>
                                <td>{game.spieler1Name}</td>
                                <td>{game.continent}</td>
                                <td>{game.difficultyLevel}</td>
                                <td>
                                    {game.spieler1Name === userDetails.username ? (
                                        <button onClick={() => stopGame(game.id)}>Spiel abbrechen</button>
                                    ) : (
                                        <button onClick={() => joinRequest(game.id, game.spieler1ID)}>Spiel
                                            beitreten</button>
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
              <button type="submit" className="btn block-cube block-cube-hover" style={{marginRight: 25}}
                      onClick={() => requestAnswer(gameRequest.gameId, gameRequest.requestingPlayer, "requestAccepted")}>
                  <div className="bg-top">
                      <div className="bg-inner"></div>
                  </div>
                  <div className="bg-right">
                      <div className="bg-inner"></div>
                  </div>
                  <div className="bg">
                      <div className="bg-inner"></div>
                  </div>
                  <div className="text">Anfrage annehmen</div>
              </button>
              <button type="submit" className="btn block-cube-cancel block-cube-hover"
                      onClick={() => requestAnswer(gameRequest.gameId, gameRequest.requestingPlayer, "requestDeclined")}>
                  <div className="bg-top">
                      <div className="bg-inner"></div>
                  </div>
                  <div className="bg-right">
                      <div className="bg-inner"></div>
                  </div>
                  <div className="bg">
                      <div className="bg-inner"></div>
                  </div>
                  <div className="text">Anfrage ablehnen</div>
              </button>
          </div>
        ) : null}

        {message && (
            <div className="control block-cube block-input"
                 style={{marginTop: "20px", padding: "10px", border: "1px solid #ccc"}}>
                <div className="bg-top">
                    <div className="bg-inner"></div>
                </div>
                <div className="bg-right">
                    <div className="bg-inner"></div>
                </div>
                <div className="bg">
                    <div className="bg-inner"></div>
                </div>
                <div className="text"><p>{message}</p>

                    {countdown !== null && countdown > 0 && <p>Countdown: {countdown} Sekunden</p>}
                </div>

            </div>
        )}
    </div>
    </form>
  );
};

export default Lobby;
