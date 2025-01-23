import L from "leaflet"; // Importiere Leaflet

let map;
let markers = []; // Array, um die Marker zu verwalten und bei Bedarf zu entfernen

// Definiere ein benutzerdefiniertes Icon für den ausgewählten Marker
const selectedIcon = new L.Icon({
  iconUrl: '/assets/marker-icon-2x.png', // Standard Icon URL
  iconSize: [25, 41], // Größe des Markers
  iconAnchor: [12, 41], // Verankerungspunkt des Markers
  popupAnchor: [1, -34], // Position des Popups relativ zum Marker
  shadowUrl: '/assets/marker-shadow.png', // Schatten
  shadowSize: [41, 41], // Schattengröße
  shadowAnchor: [12, 41], // Schattenverankerung
});

// Funktion zur Initialisierung der Karte
export const initializeMap = (mapId, spielzuege, currentSpielzugIndex, selectedCoordinates) => {

  if (!map) {
    map = L.map(mapId).setView([51.505, -0.09], 2); // Setze einen Standard-Startpunkt

    L.tileLayer(
        "https://{s}.basemaps.cartocdn.com/rastertiles/voyager_nolabels/{z}/{x}/{y}{r}.png",
        {
          attribution:
            "&copy; <a href='https://www.openstreetmap.org/copyright'>OpenStreetMap</a> contributors &copy; <a href='https://carto.com/attributions'>CARTO</a>",
        }
      ).addTo(map);
  }

  // Marker für die aktuellen Spielzüge hinzufügen
  const spielzug = spielzuege[currentSpielzugIndex];
  if (spielzug && spielzug.koordinaten) {
    const [lat, lng] = spielzug.koordinaten.split(",").map((coord) => parseFloat(coord.trim()));
    L.marker([lat, lng]).addTo(map);
  }

  // Wenn Koordinaten ausgewählt wurden, auch den Marker anzeigen
  if (selectedCoordinates) {
    const { lat, lng } = selectedCoordinates;
    const selectedMarker = L.marker([lat, lng]).addTo(map);
    selectedMarker.bindPopup(`<b>Ausgewählte Koordinaten:</b><br>Lat: ${lat}, Lng: ${lng}`).openPopup();
  }
};

// Haversine-Formel für Entfernung
export function calculateHaversineDistance(lat1, lng1, lat2, lng2) {
  const R = 6371; // Erdradius in km
  const dLat = toRadians(lat2 - lat1);
  const dLng = toRadians(lng2 - lng1);
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) *
    Math.sin(dLng / 2) * Math.sin(dLng / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c;
}

// Hilfsfunktion: Grad in Radiant umrechnen
export function toRadians(degrees) {
  return degrees * (Math.PI / 180);
}

// Entfernung berechnen und Linie zeichnen
export function calculateDistance(playerLat, playerLng, cityLat, cityLng, selectedCity, mapInstance) {
  const distance = calculateHaversineDistance(playerLat, playerLng, cityLat, cityLng);

  L.marker([cityLat, cityLng]).addTo(mapInstance)
    .bindPopup(`${selectedCity.name}`)
    .openPopup();

  const distanceLine = L.polyline(
    [
      [playerLat, playerLng], // Startpunkt: Spieler-Tipp
      [cityLat, cityLng]      // Endpunkt: Gesuchte Stadt
    ],
    {
      color: 'blue',
      weight: 3,
      dashArray: '5, 10'
    }
  ).addTo(mapInstance);

  const resultElement = document.getElementById('result');
  if (resultElement) {
    resultElement.innerHTML = `Entfernung zur gesuchten Stadt "${selectedCity.name}": ${distance.toFixed(2)} km`;
  }
  return distance;
}

// Funktion zum Hinzufügen eines Markers bei Klick auf die Karte
export const handleMapClick = (event, setSelectedCoordinates) => {
  const { lat, lng } = event.latlng; // Extrahiere lat/lng aus dem Event

  // Überprüfe, ob die Koordinaten gültig sind
  if (lat === undefined || lng === undefined) {
    console.error("Ungültige Koordinaten:", lat, lng);
    return; // Falls ungültige Koordinaten, beenden
  }

  // Koordinaten speichern
  setSelectedCoordinates({ lat, lng });

  // Marker hinzufügen
  const marker = L.marker([lat, lng], { icon: selectedIcon }).addTo(map);
  marker.bindPopup(`<b>Ausgewählte Koordinaten:</b><br>Lat: ${lat}, Lng: ${lng}`).openPopup();

  // Optional: Vorherige Marker löschen (falls Marker ausgetauscht werden sollen)
  markers.forEach((marker) => {
    map.removeLayer(marker);
  });
  markers = [marker]; // Den neuen Marker in die Marker-Liste setzen
};

