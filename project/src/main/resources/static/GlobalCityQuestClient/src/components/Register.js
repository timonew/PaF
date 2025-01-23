import React, { useState } from "react";
import axios from "axios";

function Register() {
    const [formData, setFormData] = useState({
        name: "",
        userName: "",
        password: "",
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/rest/user/register", formData);
            alert("Registration successful!");
        } catch (error) {
            console.error(error);
            alert("Registration failed!");
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Name:
                <input type="text" name="name" value={formData.name} onChange={handleChange} required />
            </label>
            <label>
                Username:
                <input type="text" name="userName" value={formData.userName} onChange={handleChange} required />
            </label>
            <label>
                Password:
                <input type="password" name="password" value={formData.password} onChange={handleChange} required />
            </label>
            <button type="submit">Register</button>
        </form>
    );
}

export default Register;
