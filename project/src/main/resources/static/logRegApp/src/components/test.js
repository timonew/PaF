import React, { useState, useEffect } from "react";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

const WebSocketTest = () => {
  const [messages, setMessages] = useState([]);
  const [isConnected, setIsConnected] = useState(false);
  let stompClient = null;

  const addMessage = (message) => {
    setMessages((prev) => [...prev, message]);
  };

  const connect = () => {
    const socket = new SockJS("http://localhost:8080/websocket"); // Der WebSocket-Endpunkt
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      setIsConnected(true);
      addMessage("Connected to WebSocket!");

      // Abonniere das Topic '/topic/waitingGames', um die Wartespiele zu erhalten
      stompClient.subscribe("/topic/waitingGames", (message) => {
        const waitingGames = JSON.parse(message.body); // Die Nachricht als JSON parsen
        waitingGames.forEach((game) => {
          addMessage(`Spiel ID: ${game.id}`);
          addMessage(`Spieler 1: ${game.spieler1Name}`);
          addMessage(`Kontinent: ${game.continent}`);
          addMessage(`Schwierigkeitsgrad: ${game.difficultyLevel}`);
        });
      });
    });

    stompClient.onStompError = (frame) => {
      addMessage(`Broker error: ${frame.headers["message"]}`);
    };

    stompClient.onWebSocketError = (event) => {
      addMessage("WebSocket error: " + event.message || "Unknown error");
    };
  };

  const disconnect = () => {
    if (stompClient) {
      stompClient.disconnect(() => {
        setIsConnected(false);
        addMessage("Disconnected from WebSocket.");
      });
    }
  };

  return (
    <div>
      <h1>WebSocket Test</h1>
      <button onClick={connect} disabled={isConnected}>
        Connect
      </button>
      <button onClick={disconnect} disabled={!isConnected}>
        Disconnect
      </button>
      <div>
        <h2>Messages</h2>
        <ul>
          {messages.map((msg, index) => (
            <li key={index}>{msg}</li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default WebSocketTest;
