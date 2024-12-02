import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Lobby = () => {
  const [userDetails, setUserDetails] = useState(null); // Benutzer-Details
  const [loading, setLoading] = useState(true); // Ladezustand
  const [waitingGames, setWaitingGames] = useState([]); // Wartende Spiele
  const [difficulty, setDifficulty] = useState(1); // Schwierigkeitsgrad
  const [continent, setContinent] = useState("Europe"); // Kontinent
  const navigate = useNavigate();

  const jwtToken = localStorage.getItem("jwtToken");

  // Überprüfen des JWT-Tokens und Abrufen der Benutzer-Details
  useEffect(() => {
    if (!jwtToken) {
      navigate("/login");
      return;
    }

    const fetchUserDetails = async () => {
      try {
        const response = await axios.get("http://localhost:8080/rest/user/details", {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        setUserDetails(response.data);
      } catch (error) {
        if (error.response && error.response.status === 403) {
          alert("Zugriff verweigert. Bitte sicherstellen, dass du eingeloggt bist.");
        } else {
          console.error("Fehler beim Abrufen der Benutzerdetails:", error);
        }
        navigate("/login");
      } finally {
        setLoading(false);
      }
    };

    fetchUserDetails();
    fetchWaitingGames();
  }, [jwtToken, navigate]);

  // Wartende Spiele abrufen
  const fetchWaitingGames = async () => {
    try {
      const response = await axios.get("http://localhost:8080/rest/game/waiting", {
        headers: {
          Authorization: `Bearer ${jwtToken}`,
        },
      });
      setWaitingGames(response.data);
    } catch (error) {
      console.error("Fehler beim Abrufen der wartenden Spiele:", error);
    }
  };

  // Spiel starten
  const startGame = async () => {
    try {
      const gameStartDTO = {
        difficulty,
        continent,
      };

      const response = await axios.post("http://localhost:8080/rest/game/start", gameStartDTO, {
        headers: {
          Authorization: `Bearer ${jwtToken}`,
          "Content-Type": "application/json",
        },
      });

      alert("Spiel erfolgreich gestartet!");
      fetchWaitingGames();
    } catch (error) {
      console.error("Fehler beim Starten des Spiels:", error);
      alert("Spiel konnte nicht gestartet werden.");
    }
  };

  // Spiel stoppen
const stopGame = async (gameId) => {
  try {
    const updateGameStatusDTO = {
      gameId: gameId,
      newStatus: "Stopped",
    };

    // Den ausgehenden DTO als JSON in der Konsole anzeigen
    console.log("Ausgehender DTO:", JSON.stringify(updateGameStatusDTO, null, 2));

    // API-Aufruf zum Stoppen des Spiels
    const response = await axios.post(
      "http://localhost:8080/rest/game/updateStatus",
      updateGameStatusDTO,
      {
        headers: {
          Authorization: `Bearer ${jwtToken}`,
          "Content-Type": "application/json",
        },
      }
    );

    alert(`Spiel mit ID ${gameId} wurde gestoppt.`);
    console.log("Antwort vom Server:", response.data);

    // Wartende Spieleliste aktualisieren
    fetchWaitingGames();
  } catch (error) {
    console.error("Fehler beim Stoppen des Spiels:", error);
    alert("Fehler beim Stoppen des Spiels.");
  }
};

// Spiel beitreten
const joinGame = async (gameId) => {
  try {
    // JSON mit der gameId erstellen
    const payload = {
      gameId: gameId,
    };

    // POST-Anfrage an den Server senden
    await axios.post("http://localhost:8080/rest/game/join", payload, {
      headers: {
        Authorization: `Bearer ${jwtToken}`,
        "Content-Type": "application/json",  // Setzen des Content-Types auf JSON
      },
    });

    alert(`Erfolgreich dem Spiel mit ID ${gameId} beigetreten!`);
    fetchWaitingGames(); // Wartende Spiele aktualisieren

    // Nach dem erfolgreichen Beitritt zur Game-Seite weiterleiten
    navigate(`/game/${gameId}`);
  } catch (error) {
    console.error("Fehler beim Beitreten des Spiels:", error);
    alert("Fehler beim Beitreten des Spiels.");
  }
};

  // Überprüfung, ob der Benutzer bereits in einem wartenden Spiel ist
  const isUserInWaitingGames = waitingGames.some(
    (game) => game.spieler1Name === userDetails?.username
  );

  if (loading) {
    return <p>Lade...</p>;
  }

  return (
    <div>
      <h1>Willkommen in der Lobby</h1>
      {userDetails ? (
        <div>
          <p>Angemeldeter Benutzer: {userDetails.username}</p>
          <p>Gesamtzahl der gespielten Spiele: {userDetails.gamesPlayed}</p>

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
          <button onClick={startGame} disabled={isUserInWaitingGames}>
            Spiel starten
          </button>
          {isUserInWaitingGames && (
            <p style={{ color: "red" }}>
              Du kannst kein neues Spiel starten, da du bereits in einem wartenden Spiel bist.
            </p>
          )}

          <h2>Wartende Spiele</h2>
          {waitingGames.length > 0 ? (
            <table>
              <thead>
                <tr>
                  <th>Spieler 1</th>
                  <th>Kontinent</th>
                  <th>Schwierigkeitsgrad</th>
                  <th>Aktion</th>
                </tr>
              </thead>
              <tbody>
                {waitingGames.map((game) => (
                  <tr key={game.id}>
                    <td>{game.spieler1Name}</td>
                    <td>{game.continent}</td>
                    <td>{game.difficultyLevel}</td>
                    <td>
                      {game.spieler1Name === userDetails.username ? (
                        <button onClick={() => stopGame(game.id)}>Spiel stoppen</button>
                      ) : (
                        <button onClick={() => joinGame(game.id)}>Spiel beitreten</button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>Derzeit keine wartenden Spiele verfügbar.</p>
          )}
        </div>
      ) : (
        <p>Benutzerinformationen konnten nicht geladen werden.</p>
      )}
    </div>
  );
};

export default Lobby;
