// JavaScript für das Spiel



// Städte-Daten
const cities = [
    { name: "Berlin", lat: 52.5200, lng: 13.4050 },
    { name: "Paris", lat: 48.8566, lng: 2.3522 },
    { name: "New York", lat: 40.7128, lng: -74.0060 }
];

const city = cities[Math.floor(Math.random() * cities.length)];



let timer, selectedCity, playerMarker;

// Initialisiere die Karte
var map = L.map('map').setView([51.505, -0.09], 5); // Koordinaten: Lat/Lng und Zoom-Level

// Füge eine Kartenebene hinzu (OpenStreetMap)
L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager_nolabels/{z}/{x}/{y}{r}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',

}).addTo(map);



// Haversine-Formel für Entfernung
function calculateHaversineDistance(lat1, lng1, lat2, lng2) {
    const R = 6371; // Erdradius in km
    const dLat = toRadians(lat2 - lat1);
    const dLng = toRadians(lng2 - lng1);
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) *
        Math.sin(dLng / 2) * Math.sin(dLng / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}

function updateCityDisplay(cityName) {
    const cityDisplay = document.getElementById('current-city');
    cityDisplay.textContent = cityName; // Aktualisiert den Text im Textfeld
}

function toRadians(degrees) {
    return degrees * (Math.PI / 180);
}

// Timer starten
function startTimerWithCountdown() {
    clearTimeout(timer);
    let timeLeft = 30;
    const timerElement = document.getElementById('timer');
    const countdown = setInterval(() => {
        if (timeLeft <= 0) {
            clearInterval(countdown);
            calculateDistance();
        } else {
            timeLeft--;
            timerElement.textContent = timeLeft;
        }
    }, 1000);
    timer = setTimeout(() => clearInterval(countdown), 30000);
}

// Entfernung berechnen
function calculateDistance() {
    if (!playerMarker) {
        alert("Kein Marker gesetzt!");
        return;
    }
    const playerLat = playerMarker.getLatLng().lat;
    const playerLng = playerMarker.getLatLng().lng;
    const cityLat = selectedCity.lat;
    const cityLng = selectedCity.lng;
    const distance = calculateHaversineDistance(playerLat, playerLng, cityLat, cityLng);
    alert(`Die Entfernung zur Stadt "${selectedCity.name}" beträgt: ${distance.toFixed(2)} km`);
}

// Neue Runde starten
function startNewRound() {
    selectedCity = cities[Math.floor(Math.random() * cities.length)];
    document.getElementById('current-city').textContent = selectedCity.name;
    startTimerWithCountdown();
    if (playerMarker) {
        map.removeLayer(playerMarker);
        playerMarker = null;
    }
}
var currentMarker= null;

alert(`Rate die Position von: ${city.name}`);

updateCityDisplay(city.name);
startTimerWithCountdown();

// Klickevent: Setze einen Marker und gebe somit deinen Tip ab
map.on('click', function(e) {
    var lat = e.latlng.lat;
    var lng = e.latlng.lng;

    if (currentMarker){
        map.removeLayer(currentMarker);
    }

    currentMarker = L.marker([lat, lng]).addTo(map)
        .bindPopup("dein Tip")
        .openPopup();



});
