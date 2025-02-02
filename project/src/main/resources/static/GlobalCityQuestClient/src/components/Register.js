import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Register() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
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
            navigate("/");
        } catch (error) {
            console.error(error);
            alert("Registration failed!");
        }
    };

    return (
        <div className="form">
            <h1 className="control">
                Register
            </h1>
            <div className="control block-cube block-input">
                <input type="text" name="userName" value={formData.userName} onChange={handleChange} required
                       placeholder="User Name"/>
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
                <input type="password" name="password" value={formData.password} onChange={handleChange} required
                       placeholder="Password"/>
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
            <button className="btn block-cube block-cube-hover"
                onClick={handleSubmit}>
                <div className="bg-top">
                    <div className="bg-inner"></div>
                </div>
                <div className="bg-right">
                    <div className="bg-inner"></div>
                </div>
                <div className="bg">
                    <div className="bg-inner"></div>
                </div>
                <div className="text">Register</div>
            </button>
        </div>
    );
}

export default Register;
