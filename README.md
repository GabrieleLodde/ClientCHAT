# Colored Chat Client-Server
---
## Technologies:

The program was developed with Visual Studio Code software and was written exploiting the potential of the Java language.

## Basic operation of the program:

The program represents a client-server application that simulates a chat, by sending commands from client to server and vice versa, with the related printouts based on the type of command received/sent.

### Server Side (classes):

1. AppServer.java
The AppServer.java file represents the entry point of the server. Upon startup, the server creates a socket listening on port 4500 and accepts connections from clients. Each client that connects generates a new thread (ClientConnected) to manage communication with that particular client, in order to manage multi-threads operation.

2. ClientConnected.java
The ClientConnected class extends the Thread class and handles communication with a single client. It interprets commands sent by the client and handles various operations, including broadcast messages, private messages, participant list requests and disconnect management. For each functionality there are checks relating to the feasibility of these operations, since the client could have sent any type of message and the server must be able to handle any situation.

### Client Side (classes):

1. AppClient.java
The App.java file is the client entry point. When started, the client connects to the server on port 4500 and initializes an instance of ClientActions to handle user interactions.

2. ClientActions.java
The ClientActions class manages user actions and interactions. Allows the user to enter a nickname, send broadcast messages, request a list of participants, send private messages and exit the chat. This implementation choice was dictated by the need to have a single class to manage each operation granted to the client, rather than having all the functioning within the App.java file (relating to the client part). A separate thread (ClientThread) handles the continuous receipt of messages from the server.

3. ClientThread.java
The ClientThread class extends the Thread class and handles the continuous receipt of messages from the server. The messages received are formatted and displayed clearly for the user, based on the commands sent by the server itself at the beginning of each individual message.

## Instructions for Execution:

### Server side: 

- Run the AppServer.java file on the server side. The server will be listening on port 4500.
- For each command received, the server has specific methods that allow linear communication with the client.

### Client side: 

- Run the AppClient.java file on the client side. The client will connect to the server on the port specified in the socket declaration (by programmer choice on port 4500).
- Enter a nickname when requested and use the menu commands, displayed only initially, to interact with the chat.
- If the choice entered is not contemplated, the client is notified and is forced to enter a further one, until it is consistent with those provided in the menu.
- The program allows you to communicate in real time with other connected clients.

### Choices Available Client Side (from the main menu):

/all: Send a broadcast message to all clients.

/only: Send a private message to a specific client.

/lista: Request and display the list of current clients.

/exit: Leave the chat and exit the communication. Once the communication is expired, the socket that allows the exchange of messages is closed on both the client and server sides.

### Commands sent Server Side:

@new: ..............................................................................

@old: ..............................................................................

@nick: ..............................................................................

@alone1: ..............................................................................

@ok1: ..............................................................................

@all: ..............................................................................

@alone2: ..............................................................................

@you: ..............................................................................

@wrong: ..............................................................................

@ok2: ..............................................................................

@only: ..............................................................................

@lista: ..............................................................................

@exit: ..............................................................................

## Programming notes:

- The commands are preceded by an "@" symbol to be recognized by both the server and the client for printing purposes.
- The program dynamically manages the addition and removal of clients.
- Messages are clearly formatted in the ClientThread class for better understanding.
- Clients cannot have the same nickname, otherwise the server sends the client a further request to change it.

## Programmers who created the program

The program was developed by:

Gabriele Lodde, 
Lorenzo Scarpulla, 
Tommaso Fallani
