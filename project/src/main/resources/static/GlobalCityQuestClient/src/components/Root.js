// src/components/Root.js
import React from 'react';
import { Link } from 'react-router-dom';
import "./Login.css"


const Root = () => {
    return (
        <form className="form">
            <div style={{ textAlign: 'center', marginTop: '50px' }}>
                <h1>Willkommen beim Global City Quest</h1>
                <p>Bitte wählen Sie eine der folgenden Optionen:</p>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '20px', alignItems: 'center' }}>
                    <Link to="/login">
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
                    </Link>
                    <Link to="/register">
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
                            <div className="text">Register</div>
                        </button>
                    </Link>
                </div>
            </div>
        </form>
    );
};

export default Root;
