import React, { useEffect } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
 
const WebSocketTest = () => {
  useEffect(() => {
    // WebSocket connection
    const socket = new SockJS("http://localhost:8080/ws");
    const stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      onConnect: () => {
        console.log("Connected to WebSocket");
 
        stompClient.subscribe("/topic/waitingGames", () => {
           console.log("Triggered");
        });
      },
      onStompError: () => {
        console.log("error");
      },
    });
 
    stompClient.activate();
 
  }, []);
 
  return <h1>Listening for WebSocket updates...</h1>;
};
 
export default WebSocketTest;