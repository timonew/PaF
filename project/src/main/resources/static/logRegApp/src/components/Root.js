// src/components/Root.js
import React from 'react';
import { Link } from 'react-router-dom';

const Root = () => {
    return (
        <div style={{ textAlign: 'center', marginTop: '50px' }}>
            <h1>Willkommen auf unserer Plattform</h1>
            <p>Bitte wählen Sie eine der folgenden Optionen:</p>
            <div>
                <Link to="/login">
                    <button style={{ margin: '10px', padding: '10px 20px' }}>Login</button>
                </Link>
                <Link to="/register">
                    <button style={{ margin: '10px', padding: '10px 20px' }}>Register</button>
                </Link>
            </div>
        </div>
    );
};

export default Root;
