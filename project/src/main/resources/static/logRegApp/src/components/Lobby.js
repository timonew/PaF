import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Lobby = () => {
  const [userDetails, setUserDetails] = useState(null); // Benutzer-Details
  const [loading, setLoading] = useState(true); // Ladezustand
  const navigate = useNavigate();

  const jwtToken = localStorage.getItem("jwtToken");

  // Überprüfen des JWT-Tokens und Abrufen der Benutzer-Details
  useEffect(() => {
    if (!jwtToken) {
      navigate("/login"); // Falls kein JWT vorhanden ist, zur Login-Seite
      return;
    }

    const fetchUserDetails = async () => {
   try {
     const response = await axios.get("http://localhost:8080/api/user/details", {
       headers: {
         Authorization: `Bearer ${jwtToken}`,
       },
     });
     setUserDetails(response.data); // Benutzerdetails setzen
   } catch (error) {
     if (error.response && error.response.status === 403) {
       alert("Zugriff verweigert. Bitte sicherstellen, dass du eingeloggt bist.");
       //navigate("/login"); // Benutzer zur Login-Seite umleiten
     } else {
       console.error("Error fetching user details:", error);
       //navigate("/login"); // Bei anderen Fehlern zur Login-Seite
     }
   } finally {
     setLoading(false);
   }
};

    fetchUserDetails();
  }, [jwtToken, navigate]);

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

          <h2>Deine Highscores:</h2>
          {userDetails.highscores && userDetails.highscores.length > 0 ? (
            <ul>
              {userDetails.highscores.map((highscore, index) => (
                <li key={index}>
                  <strong>Schwierigkeitsgrad:</strong> {highscore.difficultyLevel} <br />
                  <strong>Kontinent:</strong> {highscore.continent} <br />
                  <strong>Punkte:</strong> {highscore.score}
                </li>
              ))}
            </ul>
          ) : (
            <p>Du hast noch keine Highscores.</p>
          )}
        </div>
      ) : (
        <p>Benutzerinformationen konnten nicht geladen werden.</p>
      )}
    </div>
  );
};

export default Lobby;
