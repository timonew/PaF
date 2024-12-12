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
        const response = await axios.get(`http://localhost:8080/rest/game/init?gameId=${gameId}`, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        setGameDetails(response.data);
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
    return <div>Lade Spielinformationen...</div>;
  }

  if (!gameDetails) {
    return <div>Keine Daten verfügbar.</div>;
  }

  return (
    <div>
      <h1>Spiel Details</h1>
      <p><strong>Spiel-ID:</strong> {gameDetails.gameId}</p>
      <p><strong>Status:</strong> {gameDetails.status}</p>
      <h2>Spielzüge</h2>
      <table border="1">
        <thead>
          <tr>
            <th>Spielzug-ID</th>
            <th>Stadt-ID</th>
            <th>Stadt-Name</th>
            <th>Koordinaten</th>
          </tr>
        </thead>
        <tbody>
          {gameDetails.spielzuege.map((spielzug, index) => (
            <tr key={index}>
               <td>{spielzug.SpielZugId}</td>
              <td>{spielzug.stadtId}</td>
              <td>{spielzug.stadtName}</td>
              <td>{spielzug.koordinaten}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Game;
