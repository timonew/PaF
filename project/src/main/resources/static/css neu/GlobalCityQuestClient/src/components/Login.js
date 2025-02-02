import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Login.css"

function Login() {
    const [formData, setFormData] = useState({
        userName: "",
        password: "",
    });
    const [error, setError] = useState(null);  // Fehlerzustand für Login-Fehler
    const navigate = useNavigate();  // React-Router Hook zur Navigation

    // Ändert den Zustand des Formulars, wenn der Benutzer eingibt
    const handleChange = (e) => {
        setFormData({...formData, [e.target.name]: e.target.value});
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
        <form className="form" onSubmit={handleSubmit} autoComplete="off">
            <div className="control">
                <h1>Login</h1>
            </div>
            <div className="control block-cube block-input">
                <input
                    type="text"
                    name="userName"
                    value={formData.userName}
                    onChange={handleChange}
                    placeholder="Username"
                    required
                />
                <div className="bg-top">
                    <div className="bg-inner"></div>
                </div>
                <div className="bg-right">
                    <div className="bg-inner"></div>
                </div>
                <div className="bg">
                    <div className="bg-inner"></div>
                </div>
            </div>
            <div className="control block-cube block-input">
                <input
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="Password"
                    required
                />
                <div className="bg-top">
                    <div className="bg-inner"></div>
                </div>
                <div className="bg-right">
                    <div className="bg-inner"></div>
                </div>
                <div className="bg">
                    <div className="bg-inner"></div>
                </div>
            </div>
            <button type="submit" className="btn block-cube block-cube-hover">
                <div className="bg-top">
                    <div className="bg-inner"></div>
                </div>
                <div className="bg-right">
                    <div className="bg-inner"></div>
                </div>
                <div className="bg">
                    <div className="bg-inner"></div>
                </div>
                <div className="text">Log In</div>
            </button>
            {error && <p style={{color: "red", marginTop: "15px"}}>{error}</p>}
        </form>
    );
}

export default Login;
