# ModularPubSub

**ModularPubSub** is a dynamic, modular Publish/Subscribe system implemented in Java.  
It allows the creation of agents and topics with dependency relationships, simulating real-world event-driven communication.

---

## 🚀 Features

- 🧩 Modular topic graph construction with dependency handling
- 🧠 Dynamic agent instantiation using reflection
- 🔄 Event publishing and update propagation between topics and agents
- ⚠️ Loop detection to avoid circular dependencies
- 🌐 Simple HTTP Controller layer for triggering module logic (simulates REST-like behavior)

---

## 📂 Project Structure

- `src/` — Contains all source code for module logic and HTTP controller
- `input.txt` — Configuration file to define topics and agents
- `*.java` — Each agent is implemented as a separate class

---

## 🖥️ How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/Amit-Eizen/ModularPubSub.git
   cd ModularPubSub
   ```

2. Compile and run the main file (e.g., `Main.java` in `src/`):
   ```bash
   javac src/Main.java
   java -cp src Main
   ```

3. Example command via HTTP (if using the controller layer):
   ```
   http://localhost:8000/publish?topic=Weather&value=Rain
   ```

---

## 🧠 Example Use Case

> "Add a new agent class, reference it in `input.txt`, and the system will load and connect it to the relevant topics dynamically—no need to change core logic."

---

## 📌 Author

**Amit Eizenberg**  
Computer Science Student & NOC Engineer  
📍 Israel | [LinkedIn](https://www.linkedin.com/in/amit-eizenberg)
