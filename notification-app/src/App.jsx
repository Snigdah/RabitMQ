import React, { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

export default function Notifications() {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    const stompClient = new Client({
      brokerURL: "ws://localhost:8087/ws", // direct WebSocket
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("Connected to WebSocket");

        stompClient.subscribe("/topic/notifications", (msg) => {
          const body = JSON.parse(msg.body);
          setMessages((prev) => [...prev, body]);
        });
      },
      onStompError: (frame) => {
        console.error("Broker reported error:", frame.headers["message"]);
        console.error("Details:", frame.body);
      },
    });

    stompClient.activate();
  }, []);

  return (
    <div>
      <h2>Notifications</h2>
      {messages.map((m, i) => (
        <div key={i}
          style={{ padding: "8px", margin: "5px", border: "1px solid #ddd" }}>
          <b>Order ID:</b> {m.orderId}<br/>
          <b>User:</b> {m.userId}<br/>
          <b>Product:</b> {m.product}
        </div>
      ))}
    </div>
  );
}
