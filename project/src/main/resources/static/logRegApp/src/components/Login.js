import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Login() {
    const [formData, setFormData] = useState({
        userName: "",
        password: "",
    });
    const [error, setError] = useState(null);  // Fehlerzustand für Login-Fehler
    const navigate = useNavigate();  // React-Router Hook zur Navigation

    // Ändert den Zustand des Formulars, wenn der Benutzer eingibt
    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

   const handleSubmit = async (e) => {
    e.preventDefault();
    try {
        const response = await axios.post("http://localhost:8080/rest/user/login", formData);
        console.log("Login response:", response.data);  // Der Token als String
        if (response.data) {  // Wenn ein Token im Antwort-Body vorhanden ist
            localStorage.setItem("jwtToken", response.data);  // Speichert den Token direkt
            navigate("/lobby");  // Weiterleitung zur Lobby-Seite
        } else {
            setError("Login failed! No token received.");  // Fehleranzeige bei fehlendem Token
        }
    } catch (error) {
        console.error("Error during login request:", error);
        setError("Login failed!");  // Fehleranzeige bei Login-Fehler
    }

};



    return (
        <div>
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    Username:
                    <input
                        type="text"
                        name="userName"
                        value={formData.userName}
                        onChange={handleChange}
                        required
                    />
                </label>
                <br />
                <label>
                    Password:
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </label>
                <br />
                {error && <p style={{ color: "red" }}>{error}</p>}  {/* Fehleranzeige, wenn vorhanden */}
                <button type="submit">Login</button>
            </form>
        </div>
    );
}

export default Login;
