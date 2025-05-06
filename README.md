# 🖍️ Shared Whiteboard Application

A Java RMI-based collaborative whiteboard system that allows multiple users to draw together in real-time. The application supports both **Manager** (admin) and **Guest** (participant) roles.

## 📁 Project Structure

```
Whiteboard/
├── src/
│   ├── Guest/                # Guest-side logic
│   │   ├── ClientServiceImp.java
│   │   └── JoinWhiteBoard.java
│   ├── Manager/              # Manager-side logic
│   │   ├── CreateWhiteBoard.java
│   │   └── ManagerServiceImp.java
│   ├── RMI/                  # RMI interfaces
│   │   ├── ClientService.java
│   │   └── ManagerService.java
│   ├── ShapeLibrary/         # Shape classes for drawing
│   │   ├── Shape.java
│   │   ├── ShapeCircle.java
│   │   ├── ShapeEraser.java
│   │   ├── ShapeLine.java
│   │   ├── ShapeOval.java
│   │   ├── ShapeRect.java
│   │   └── ShapeString.java
│   └── SharedWhiteBoard/     # Shared utilities or classes (if any)
│
├── META-INF/
│   └── MANIFEST.MF           # Manifest file for JAR execution
├── out/                      # Compiled output directory
```

## ✅ Features

- Manager can create and manage a shared whiteboard session.
- Guests can join existing sessions and collaborate.
- Real-time drawing updates for all connected users.
- Support for multiple shape types: Line, Oval, Rectangle, Circle, Text, Eraser.
- Java RMI used for remote method invocation and communication.

## 🚀 How to Run

### 1. Compile the Project

Using terminal or your IDE, make sure all `.java` files are compiled properly.

### 2. Start RMI Registry

In your project root directory:
```bash
rmiregistry
```

Make sure the RMI registry is running in the background.

### 3. Launch the Manager

```bash
java Manager.CreateWhiteBoard
```

### 4. Launch a Guest

In a new terminal window:
```bash
java Guest.JoinWhiteBoard
```

> 📝 You can run multiple guests to simulate collaborative sessions.

## 🛠️ Requirements

- Java 8 or above
- No external libraries required

## 📌 Notes

- This project was developed as part of the Distributed Systems course at the University of Melbourne.
- No authentication or persistence layer is included — designed for demo purposes.

## 📄 License

MIT License (or specify your preferred license)
