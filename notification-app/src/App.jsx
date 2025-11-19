import React, { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

export default function Notifications() {
  const [messages, setMessages] = useState([]);
  const [connected, setConnected] = useState(false);

  const userId = "U2001";
  const authToken = "demo-token-123";

  useEffect(() => {
    const stompClient = new Client({
      brokerURL: `ws://localhost:8087/ws`,
      reconnectDelay: 5000,

      connectHeaders: {
        'userId': userId,
        'Authorization': `Bearer ${authToken}`
      },

      onConnect: () => {
        console.log("✅ Connected as:", userId);
        setConnected(true);

        // ✅ ONLY THIS SUBSCRIPTION IS NEEDED
        stompClient.subscribe("/user/queue/notifications", (msg) => {
          console.log("📬 Received user notification:", msg.body);
          const body = JSON.parse(msg.body);
          setMessages((prev) => [...prev, body]);
        });
      },

      onStompError: (frame) => {
        console.error("❌ Broker error:", frame.headers["message"]);
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
  }, [userId, authToken]);

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