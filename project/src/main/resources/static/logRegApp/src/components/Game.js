import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

const Game = () => {
  const { gameId } = useParams();
  const [gameDetails, setGameDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const jwtToken = localStorage.getItem("jwtToken");

  // Spielinformationen abrufen
  useEffect(() => {
    const fetchGameDetails = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/rest/game/${gameId}`, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });

        // Wenn die Bilddaten vorhanden sind, in eine Base64-URL umwandeln
        if (response.data.mapImage) {
          const base64Image = btoa(
            new Uint8Array(response.data.mapImage).reduce((data, byte) => data + String.fromCharCode(byte), "")
          );
          setGameDetails({ ...response.data, mapImage: `data:image/png;base64,${base64Image}` });
        } else {
          setGameDetails(response.data);
        }
      } catch (error) {
        console.error("Fehler beim Abrufen der Spiel-Details:", error);
        alert("Fehler beim Abrufen der Spiel-Details.");
      } finally {
        setLoading(false);
      }
    };

    fetchGameDetails();
  }, [gameId, jwtToken]);

  if (loading) {
    return <p>Lade...</p>;
  }

  if (!gameDetails) {
    return <p>Spiel nicht gefunden!</p>;
  }

  return (
    <div>
      <h1>Spiel: {gameDetails.spielId}</h1>
      <p>Spieler 1: {gameDetails.spieler1Name}</p>
      <p>Spieler 2: {gameDetails.spieler2Name}</p>
      <p>Status: {gameDetails.status}</p>
      
      {/* Wenn eine Karte vorhanden ist, diese anzeigen */}
      {gameDetails.mapImage && (
        <div>
          <h2>Karte</h2>
          <img src={gameDetails.mapImage} alt="Spielkarte" style={{ maxWidth: "100%" }} />
        </div>
      )}
    </div>
  );
};

export default Game;
