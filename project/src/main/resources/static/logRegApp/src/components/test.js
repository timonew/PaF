import React, { useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";

const WebSocketTest = () => {
    const [message, setMessage] = useState("");
    const [response, setResponse] = useState("");
    const [connected, setConnected] = useState(false);
    const [client, setClient] = useState(null);

    // Initialisiere STOMP-Client und stelle Verbindung her
    useEffect(() => {
        const stompClient = new Client({
            brokerURL: "ws://localhost:8080/websocket", // WebSocket-Endpoint des Servers
            connectHeaders: {
                // Optionale Header, wenn benötigt
            },
            debug: (str) => {
                console.log(str);
            },
            onConnect: () => {
                setConnected(true);
                console.log("Verbindung zu WebSocket hergestellt");
                
                // Abonniere den Topic "/topic/greetings" auf Nachrichten
                stompClient.subscribe("/topic/greetings", (message) => {
                    setResponse(message.body);
                });
            },
            onDisconnect: () => {
                setConnected(false);
                console.log("Verbindung zu WebSocket getrennt");
            },
            onStompError: (frame) => {
                console.error(frame);
            },
        });

        stompClient.activate();
        setClient(stompClient);

        // Cleanup: Trenne die WebSocket-Verbindung bei Verlassen der Seite
        return () => {
            if (stompClient.connected) {
                stompClient.deactivate();
            }
        };
    }, []);

    // Funktion zum Senden einer Nachricht an den Server
    const sendMessage = () => {
        if (client && connected) {
            const helloMessage = { name: "User" }; // Beispielnachricht mit einem Namen
            client.publish({
                destination: "/app/hello", // Endpunkt, der in WebSocketConfig definiert wurde
                body: JSON.stringify(helloMessage),
            });
        }
    };

    return (
        <div>
            <h1>WebSocket Kommunikation mit STOMP</h1>
            <button onClick={sendMessage} disabled={!connected}>
                Nachricht an Server senden
            </button>
            <div>
                <h3>Antwort vom Server:</h3>
                <p>{response}</p>
            </div>
            <div>
                <p>Status: {connected ? "Verbunden" : "Nicht verbunden"}</p>
            </div>
        </div>
    );
};

export default WebSocketTest;
