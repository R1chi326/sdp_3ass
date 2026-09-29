# Design Rationale Document: Smart Home Notification System

## 1. Chosen Problem Domain
The domain is a **Smart Home Notification Dispatch System**. The system disconnects high-level notification types (such as Security Alerts or Device Status updates) from the underlying transport channels (Push notifications, Email, or Legacy GSM SMS).

---

## 2. Selected Complexity Module
**Dynamic Implementor Selection (Runtime Selection)**
The implementation channel is dynamically selected at runtime by `DynamicChannelSelector` based on real-time environmental factors (e.g., internet connectivity and alert severity). The client never hardcodes which concrete channel to use.

---

## 3. Architectural Justification

### Why Bridge Alone is Not Enough
The Bridge pattern decouples the notification abstraction (`SmartHomeNotification`) from the transport channel interface (`NotificationChannel`). However, our system requires integrating an external physical driver (`LegacyGsmModem`). This driver has an incompatible method signature (`transmitSms`), primitive parameter types (`int countryCode, long phoneNumber, byte[] payloadPdu`), and uses a custom status object (`GsmSendResult`) instead of throwing exceptions. Bridge alone cannot integrate this class without modifying either its code (violating third-party driver constraints) or changing our clean `NotificationChannel` interface.

### Why Adapter Alone is Not Enough
The Adapter pattern allows `LegacyGsmModem` to be used through the `NotificationChannel` interface. However, Adapter alone does not decouple the notification logic (e.g., message header/body formatting for Security Alerts vs. Device Statuses) from the channel implementations. Without Bridge, adding a new notification type or channel would cause class explosion (e.g., `SecurityPushNotification`, `SecuritySmsNotification`, `StatusPushNotification`, etc.).

---

## 4. Incompatibility Analysis of the Adapted Class
The `LegacyGsmModem` class meets all criteria for genuine incompatibility:
1. **Method & Signature Differences:** Natively uses `transmitSms(int, long, byte[])` instead of `send(String, String, String)`.
2. **Data Type Mismatch:** Requires parsing a unified phone string (e.g., `"+77011234567"`) into distinct integer country codes and long phone numbers, and converting text to UTF-8 byte arrays (`byte[]`).
3. **Failure Mechanism:** Natively returns a `GsmSendResult` status object with numeric HTTP-like status codes (`0`, `400`, `500`). The adapter (`GsmSmsChannelAdapter`) catches non-zero status codes and translates them into a domain-specific `ChannelDeliveryException`, preventing vendor-specific leakages.

---

## 5. Limitation of the Final Design
* **Synchronous & Single-Channel Delivery:** The current design sends a notification through a single selected channel synchronously. If the selected channel fails mid-transmission, the system throws a `ChannelDeliveryException` rather than automatically cascading/retrying through a backup channel within the same execution path.