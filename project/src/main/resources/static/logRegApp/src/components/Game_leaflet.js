import React, { useEffect, useRef, useState } from "react";
import L from "leaflet";
import "leaflet/dist/leaflet.css";

const LeafletGame = ({ currentSpielzug, onGuessSubmit }) => {
  const mapRef = useRef(null);
  const [playerMarker, setPlayerMarker] = useState(null);

  useEffect(() => {
    if (!mapRef.current) {
      const map = L.map("map").setView([51.505, -0.09], 5);
      mapRef.current = map;

      L.tileLayer(
        "https://{s}.basemaps.cartocdn.com/rastertiles/voyager_nolabels/{z}/{x}/{y}{r}.png",
        {
          attribution:
            "&copy; <a href='https://www.openstreetmap.org/copyright'>OpenStreetMap</a> contributors &copy; <a href='https://carto.com/attributions'>CARTO</a>",
        }
      ).addTo(map);

      map.on("click", (e) => {
        const lat = e.latlng.lat;
        const lng = e.latlng.lng;

        if (playerMarker) {
          map.removeLayer(playerMarker);
        }

        const marker = L.marker([lat, lng]).addTo(map).bindPopup("Dein Tipp").openPopup();
        setPlayerMarker(marker);

        // Callback mit der Spielerposition
        onGuessSubmit({ lat, lng });
      });
    }

    // Bereite eine neue Runde vor, wenn sich der Spielzug ändert
    if (currentSpielzug) {
      if (playerMarker) {
        mapRef.current.removeLayer(playerMarker);
        setPlayerMarker(null);
      }
      alert(`Rate die Position von: ${currentSpielzug.stadtName}`);
    }
  }, [currentSpielzug, onGuessSubmit]);

  return <div id="map" style={{ width: "100%", height: "500px" }}></div>;
};

export default LeafletGame;
