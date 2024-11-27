// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Root from './components/Root';
import Login from './components/Login';
import Register from './components/Register';
import Lobby from './components/Lobby'; // Stellen Sie sicher, dass Sie die Lobby-Komponente importieren

// ProtectedRoute-Komponente: Umleitung, falls der Benutzer nicht authentifiziert ist
const ProtectedRoute = ({ element, redirectTo }) => {
    const token = localStorage.getItem("jwtToken"); // Überprüfen, ob ein Token im localStorage vorhanden ist
    return token ? element : <Navigate to={redirectTo} />; // Wenn Token vorhanden, zeigt die Seite an, andernfalls Umleitung zu Login
};

const App = () => {
    return (
        <Router>
            <Routes>
                {/* Root-Route, könnte die Startseite sein */}
                <Route path="/" element={<Root />} />

                {/* Login-Route */}
                <Route path="/login" element={<Login />} />

                {/* Registrierungs-Route */}
                <Route path="/register" element={<Register />} />

                {/* Protected Lobby-Route: Nur zugänglich, wenn ein JWT-Token im localStorage vorhanden ist */}
                <Route 
                    path="/lobby" 
                    element={
                        <ProtectedRoute 
                            element={<Lobby />} 
                            redirectTo="/login" 
                        />
                    } 
                />
            </Routes>
        </Router>
    );
};

export default App;
