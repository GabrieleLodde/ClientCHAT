package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class ClientThread extends Thread {
    // Declaration of the variables used
    private BufferedReader inDalServer;
    private boolean running;
    private String messageReceived;
    private DateTimeFormatter dtf;
    private LocalDateTime now;
    private ClientColors color;

    // Constructor
    public ClientThread(BufferedReader inDalServer) {
        this.inDalServer = inDalServer;
        this.running = true;
        this.messageReceived = "";
        this.dtf = DateTimeFormatter.ofPattern("HH:mm");
        this.now = LocalDateTime.now();
        this.color = new ClientColors();
    }

    @Override
    public void run() {
        try {
            // Infinite loop until the "running" variable is set to false, then the client
            // wants to end the communication
            while (this.running == true) {
                // Reading the message sent by the server
                messageReceived = inDalServer.readLine();
                // Check if the received message is not null, whether the server is sending
                // something or not
                if (messageReceived != null) {
                    // Invocation of the method to format the received message and print it
                    formatMessage(messageReceived);
                }
            }
        } catch (IOException e) {
            System.out.println(color.RED_BOLD_BRIGHT + "ERRORE THREAD CLIENT (IOEXCEPTION) " + color.RESET
                    + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now) + color.RESET + "\n");
        } catch (NullPointerException ex) {
            System.out.println(color.RED_BOLD_BRIGHT + "ERRORE THREAD CLIENT (LETTURA MESSAGGIO NULL) "
                    + color.RESET + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now) + color.RESET + "\n");
        }
        return;
    }

    // Method for setting the "running" variable
    public void setRunning(boolean running) {
        this.running = running;
    }

    // Method for formatting and printing messages received from the server based on
    // their syntactic nature
    public void formatMessage(String messageReceived) {
        // Creation of the string array coming from the string received from the server
        String[] arrayString = messageReceived.split(":", 3);
        // Check if the server sent the nickname of a new client that initiated the
        // communication
        if (arrayString[0].equals("@nick")) {
            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.BLUE_BOLD_BRIGHT + arrayString[1]
                    + color.RESET + color.CYAN_BOLD_BRIGHT + " si e' unito/a al gruppo! " + color.RESET
                    + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now) + color.RESET + "\n");
        }
        // Check if the server has forwarded a message previously broadcast by a client
        else if (arrayString[0].equals("@all")) {
            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.BLUE_BOLD_BRIGHT + arrayString[1]
                    + color.RESET + color.WHITE_BOLD_BRIGHT + " ha scritto > " + color.RESET + color.YELLOW_BOLD_BRIGHT
                    + "'" + arrayString[2] + "' " + color.RESET + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now)
                    + color.RESET + "\n");
        }
        // Check if the server has forwarded the list of nicknames of connected clients,
        // previously requested by a client
        else if (arrayString[0].equals("@lista")) {
            System.out.println(color.CYAN_BACKGROUND_BRIGHT + "---LISTA PARTECIPANTI---" + color.RESET);
            System.out.println(arrayString[1].replaceAll(";", "\n") + color.RESET);
        }
        // Check if the server forwarded the nickname of the client that interrupted the
        // communication
        else if (arrayString[0].equals("@exit")) {
            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.BLUE_BOLD_BRIGHT + arrayString[1]
                    + color.RESET + color.CYAN_BOLD_BRIGHT + " ha abbandonato il gruppo! " + color.RESET
                    + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now) + color.RESET + "\n");
        }
        // Check if the server forwarded an error sending a broadcast message because
        // only one client is connected
        else if (arrayString[0].equals("@alone1")) {
            System.out.println(
                    color.RED_BOLD_BRIGHT + "Impossibile inviare il messaggio in broadcast, sei presente solo tu! "
                            + color.RESET + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now) + color.RESET + "\n");
        }
        // Check if the server forwarded an error sending a private message, because
        // only one client is connected
        else if (arrayString[0].equals("@alone2")) {
            System.out
                    .println(color.RED_BOLD_BRIGHT + "Impossibile inviare il messaggio privato, sei presente solo tu! "
                            + color.RESET + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now) + color.RESET + "\n");
        }
        // Check if the server forwarded an error in sending a private message, because
        // the nickname of the searched client is not present at the moment
        else if (arrayString[0].equals("@wrong")) {
            System.out.println(color.RED_BOLD_BRIGHT + "Impossibile inviare il messaggio privato: " + color.RESET
                    + color.BLUE_BOLD_BRIGHT + arrayString[1] + color.RESET + color.RED_BOLD_BRIGHT
                    + " non fa parte del gruppo! " + color.RESET + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now)
                    + color.RESET + "\n");
        }
        // Check if the server forwarded an error in sending a private message,
        // because the nickname of the client being searched for is the same as the one
        // who requested to send the same message
        else if (arrayString[0].equals("@you")) {
            System.out.println(color.RED_BOLD_BRIGHT + "Impossibile inviare il messaggio privato: " + color.RESET
                    + color.BLUE_BOLD_BRIGHT + arrayString[1] + color.RESET + color.RED_BOLD_BRIGHT
                    + " sei tu stesso/a!" + color.RESET + "\n");
        }
        // Check if the server has forwarded a private message to a specific client
        else if (arrayString[0].equals("@only")) {
            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.BLUE_BOLD_BRIGHT + arrayString[1]
                    + color.RESET + color.WHITE_BOLD_BRIGHT + " ti ha scritto > " + color.RESET
                    + color.YELLOW_BOLD_BRIGHT + "'" + arrayString[2] + "' " + color.RESET
                    + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now) + color.RESET + "\n");
        }
        // Check if the server has forwarded a confirmation message to a client
        // regarding the previous sending of a broadcast message
        else if (arrayString[0].equals("@ok1")) {
            printConfirmationBroadcastMessage(color.GREEN_BOLD_BRIGHT, "Messaggio inviato in broadcast!");
        }
        // Check if the server has forwarded a confirmation message to a client
        // regarding the previous sending of a message privately
        else if (arrayString[0].equals("@ok2")) {
            printConfirmationPrivateMessage(color.GREEN_BOLD_BRIGHT, "Messaggio inviato in privato a ",
                    color.BLUE_BOLD_BRIGHT, (arrayString[1] + "! "));
        }
    }

    public void printConfirmationBroadcastMessage(String firstColor, String firstString) {
        System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + firstColor + firstString + color.RESET
                + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now)
                + color.RESET + "\n");
    }

    public void printConfirmationPrivateMessage(String firstColor, String firstString, String secondColor,
            String secondString) {
        System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + firstColor + firstString + color.RESET
                + secondColor + secondString + color.RESET + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now)
                + color.RESET + "\n");
    }

}