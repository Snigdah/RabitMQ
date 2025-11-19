import React, { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

export default function Notifications() {
  const [messages, setMessages] = useState([]);
  const [connected, setConnected] = useState(false);

  const userId = "U2001"; // TODO: get from auth later

  useEffect(() => {
    const stompClient = new Client({
      brokerURL: `ws://localhost:8087/ws`,
      reconnectDelay: 5000,

      // Use webSocketFactory to add custom headers
      webSocketFactory: () => {
        // Create WebSocket with user in URL
        return new WebSocket(`ws://localhost:8087/ws?userId=${userId}`);
      },

      
      onConnect: () => {
        console.log("✅ Connected as:", userId);
        setConnected(true);

        // Subscribe to user-specific queue
        stompClient.subscribe("/user/queue/notifications", (msg) => {
          console.log("📬 Received notification:", msg.body);
          const body = JSON.parse(msg.body);
          setMessages((prev) => [...prev, body]);
        });
      },

      onStompError: (frame) => {
        console.error("❌ Broker error:", frame.headers["message"]);
        console.error("Details:", frame.body);
        setConnected(false);
      },

      onDisconnect: () => {
        console.log("🔌 Disconnected");
        setConnected(false);
      }
    });

    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, [userId]);

  return (
    <div style={{ padding: "20px" }}>
      <h2>Notifications for {userId}</h2>
      <p>Status: {connected ? "🟢 Connected" : "🔴 Disconnected"}</p>
      
      {messages.length === 0 ? (
        <p>No notifications yet...</p>
      ) : (
        messages.map((m, i) => (
          <div
            key={i}
            style={{
              padding: "12px",
              margin: "8px 0",
              border: "1px solid #ddd",
              borderRadius: "4px",
              backgroundColor: "#f9f9f9"
            }}
          >
            <b>Order ID:</b> {m.orderId}<br />
            <b>User:</b> {m.userId}<br />
            <b>Product:</b> {m.product}
          </div>
        ))
      )}
    </div>
  );
}