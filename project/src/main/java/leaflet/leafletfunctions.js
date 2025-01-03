// JavaScript für das Spiel



// Städte-Daten
const cities = [
    { name: "Berlin", lat: 52.5200, lng: 13.4050 },
    { name: "Paris", lat: 48.8566, lng: 2.3522 },
    { name: "New York", lat: 40.7128, lng: -74.0060 }
];


let timer, selectedCity, playerMarker;
let lastCityMarker = null;
let lastDistanceLine = null;
let roundCounter = 0; // Zählvariable für die Anzahl der Runden
const maxRounds = 2; // Maximale Anzahl der Runden
let totalDistance = 0;

// Initialisiere die Karte
var map = L.map('map').setView([51.505, -0.09], 5); // Koordinaten: Lat/Lng und Zoom-Level

// Füge eine Kartenebene hinzu (OpenStreetMap)
L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager_nolabels/{z}/{x}/{y}{r}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',

}).addTo(map);

// Funktion, um die Tabelle zu aktualisieren
function updateTable(cityName, distance, message = null) {
    const tableBody = document.querySelector("#game-log tbody");
    const newRow = document.createElement("tr");

    const cityCell = document.createElement("td");
    cityCell.textContent = cityName;

    const distanceCell = document.createElement("td");
    if (message) {
        distanceCell.textContent = message;
    } else {
        distanceCell.textContent = distance;

        totalDistance += parseFloat(distance);
    }

    newRow.appendChild(cityCell);
    newRow.appendChild(distanceCell);
    tableBody.appendChild(newRow);
}


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
    let timeLeft = 5;
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
        // kein Marker gesetzt, maximale Distanz verwenden
        const maxDistance = 20000;
        alert("kein Marker wurde gesetzt! Eine Maximale Distanz von 20.000km wird angenommen");
        totalDistance += maxDistance;

        // Tabelle aktualisieren
        updateTable(selectedCity.name, maxDistance.toFixed(2) + " km", "kein Marker gesetzt (max Distance 20.000km)");


        setTimeout(() => {
            startNewRound();
        }, 1000);
        return; // Beende die Funktion hier, um weiteren Code nicht auszuführen
    }

    //Wenn ein Marker gesetzt wurde, berechne die Distanz
    const playerLat = playerMarker.getLatLng().lat;
    const playerLng = playerMarker.getLatLng().lng;
    const cityLat = selectedCity.lat;
    const cityLng = selectedCity.lng;
    const distance = calculateHaversineDistance(playerLat, playerLng, cityLat, cityLng);

    lastCityMarker = L.marker([cityLat, cityLng]).addTo(map)
        .bindPopup(`${selectedCity.name}`)
        .openPopup();

    lastDistanceLine = L.polyline(
        [
            [playerLat, playerLng], // Startpunkt: Spieler-Tipp
            [cityLat, cityLng]      // Endpunkt: Gesuchte Stadt
        ],
        {
            color: 'blue',
            weight: 3,
            dashArray: '5, 10' // Definiert das gestrichelte Muster
        }
    ).addTo(map);

    const resultElement = document.getElementById('result');
    if (resultElement) {
        resultElement.innerHTML = `Entfernung zur gesuchten Stadt "${selectedCity.name}": ${distance.toFixed(2)} km`;
    }

    // Tabelle aktualisieren
    updateTable(selectedCity.name, distance.toFixed(2)+" km");

    // Startet die nächste Runde nach 3 Sekunden
    setTimeout(() => {
        startNewRound();
    }, 3000);
}

// Neue Runde starten
function startNewRound() {

    // Überprüfe, ob die maximale Rundenzahl erreicht ist
    roundCounter++;
    if (roundCounter > maxRounds) {
        alert(`Spiel beendet, Danke fürs spielen! Deine Gesamtdisanz beträgt: ${totalDistance.toFixed(2)} km`);

        //Gesamtdistanz zur Tabelle hinzufügen
        const tableBody =document.querySelector("#game-log tbody");
        const summaryRow = document.createElement("tr");
        summaryRow.innerHTML = `
            <td><strong>Gesamtdistanz</strong></td>
            <td><strong>${totalDistance.toFixed(2)} km</strong></td>
        `;
        tableBody.appendChild(summaryRow);

        return;
    }

    // Entferne alten Stadt-Marker
    if (lastCityMarker){
        map.removeLayer(lastCityMarker);
        lastCityMarker = null;
    }

    //Entferne alte Entfernungslinie
    if (lastDistanceLine) {
        map.removeLayer(lastDistanceLine);
        lastDistanceLine = null;
    }
    selectedCity = cities[Math.floor(Math.random() * cities.length)];
    document.getElementById('current-city').textContent = selectedCity.name;

    if (playerMarker) {
        map.removeLayer(playerMarker);
        playerMarker = null;
    }
    startTimerWithCountdown();
}



// Starte das Spiel
selectedCity = cities[Math.floor(Math.random() * cities.length)];
alert(`Rate die Position von: ${selectedCity.name}`);
updateCityDisplay(selectedCity.name);
startTimerWithCountdown();

// Klickevent: Setze einen Marker und gebe somit deinen Tip ab
map.on('click', function(e) {
    var lat = e.latlng.lat;
    var lng = e.latlng.lng;

    if (playerMarker){
        map.removeLayer(playerMarker);
    }

    playerMarker = L.marker([lat, lng]).addTo(map)
        .bindPopup("dein Tip")
        .openPopup();



});
