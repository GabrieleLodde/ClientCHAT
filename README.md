# Colored Chat Client-Server
---
## Technologies:
---

The program was developed with Visual Studio Code software and was written exploiting the potential of the Java language.

---
### Basic operation of the program:
---

The program represents a client-server application that simulates a chat, by sending commands from client to server and vice versa, with the related printouts based on the type of command received/sent.

---

### Parte Server:
---

1. App.java
Il file App.java rappresenta il punto di ingresso del server. All'avvio, il server crea un socket in ascolto sulla porta 4500 e accetta connessioni dai client. Ogni client che si connette genera un nuovo thread (ClientCollegato) per gestire la comunicazione con quel particolare client.

2. ClientCollegato.java
La classe ClientCollegato estende la classe Thread e gestisce la comunicazione con un singolo client. Essa interpreta i comandi inviati dal client e gestisce varie operazioni, inclusi messaggi broadcast, messaggi privati, richieste di lista partecipanti e la gestione delle disconnessioni.

---
### Parte Client:
---

1. App.java
Il file App.java è il punto di ingresso del client. Quando avviato, il client si connette al server sulla porta 4500 e inizializza un'istanza di ClientAzioni per gestire le interazioni con l'utente.

2. ClientAzioni.java
La classe ClientAzioni gestisce le azioni e le interazioni dell'utente. Consente all'utente di inserire un nickname, inviare messaggi broadcast, richiedere la lista dei partecipanti, inviare messaggi privati e uscire dalla chat. Un thread separato (ClientThread) gestisce la ricezione continua di messaggi dal server.

3. ClientThread.java
La classe ClientThread estende la classe Thread e gestisce la ricezione continua di messaggi dal server. I messaggi ricevuti vengono formattati e visualizzati in modo chiaro per l'utente.

---

## Istruzioni per l'Esecuzione:
---

### Server: 

- Eseguire il file App.java nella parte del server. Il server sarà in ascolto sulla porta 4500.
- Per ogni comando ricevuto, il server ha dei metodi specifici che consentono la comunicazione lineare con il client.

### Client: 

- Eseguire il file App.java nella parte del client. Il client si connetterà al server sulla porta specificata nella dichiarazione del socket.
- Inserire un nickname quando richiesto e utilizzare i comandi del menu, visualizzato solo inizialmente, per interagire con la chat.
- Qualora la scelta inserita non dovesse essere contemplata, il client viene avvisato e questi è costretto ad inserirne una ulteriore, fino a quando non è coerente con il menu.
- La chat permette di comunicare in tempo reale con altri partecipanti.

---

### Scelte Disponibili Client Side:
---

/all: Invia un messaggio in broadcast a tutti i partecipanti.

/lista: Richiede e visualizza la lista dei partecipanti attuali.

/only: Invia un messaggio privato a un partecipante specifico.

/exit: Abbandona la chat.

---

### Comandi inviati Server Side:
---

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

---
## Note:
---

- I comandi sono preceduti da un simbolo "@" per essere riconosciuti sia dal server che dal client per motivi di stampa.
- Il programma gestisce dinamicamente l'aggiunta e la rimozione di partecipanti alla chat.
- I messaggi sono formattati in modo chiaro per una migliore comprensione.
- I client non possono avere lo stesso nickname, altrimenti il server invia al client una richiesta ulteriore di diversificare.

---

## Autori
---

Il progetto è stato sviluppato da:

Gabriele Lodde, Lorenzo Scarpulla, Tommaso Fallani

---
