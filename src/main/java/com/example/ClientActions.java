package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class ClientActions {
    // Declaration of the variables used
    private Socket dataSocket;
    private BufferedReader inDalServer;
    private DataOutputStream outVersoIlServer;
    private BufferedReader inputTastiera;
    private ClientThread clientThread;
    private String nickname;
    private boolean nuovo;
    private boolean exit = false;
    private String tastiera;
    private DateTimeFormatter dtf;
    private LocalDateTime now;
    private ClientColors color;

    // Constructor
    public ClientActions(Socket dataSocket, BufferedReader inDalServer, DataOutputStream outVersoIlServer) {
        this.dataSocket = dataSocket;
        this.inDalServer = inDalServer;
        this.outVersoIlServer = outVersoIlServer;
        this.inputTastiera = new BufferedReader(new InputStreamReader(System.in));
        this.nickname = "";
        this.nuovo = false;
        this.exit = false;
        this.tastiera = "";
        this.dtf = DateTimeFormatter.ofPattern("HH:mm");
        this.now = LocalDateTime.now();
        this.color = new ClientColors();
    }

    public void init() {
        try {
            //Initial nickname request to the client
            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.BLUE_BOLD_BRIGHT
                    + "Benvenuto/a, inserisci il tuo nickname" + color.RESET);
            
            // Cycle to request the nickname from the client until it enters one that is not present among those in the chat
            do {
                nickname = inputTastiera.readLine().toUpperCase();
                outVersoIlServer.writeBytes("@nick:" + nickname + ":*" + "\n");
                String messageServer = inDalServer.readLine();
                checkNome(messageServer);
            } while (!isNuovo());

            // Declaration and start of the thread associated with the client to read input messages from the server
            clientThread = new ClientThread(inDalServer);
            clientThread.start();

            // Calling the method to display the menu of choices
            richiamaMenu();
            
            // Client welcome print
            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.BLUE_BOLD_BRIGHT
                    + "Ti sei unito/a alla chat! " + color.RESET + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now)
                    + color.RESET + "\n");
            
            // Infinite loop, until the client wants to exit
            do {
                // Reading the choice entered by the client from the keyboard
                tastiera = inputTastiera.readLine();
                if (tastiera instanceof String && !tastiera.equals(null)) {
                    // Controls over client choice
                    switch (tastiera) {
                        // Choice to send a broadcast message
                        case "/all":
                            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.GREEN_BOLD_BRIGHT
                                    + "Inserisci il messaggio in broadcast" + color.RESET);
                            tastiera = inputTastiera.readLine();
                            outVersoIlServer.writeBytes("@all:" + this.getNickname() + ":" + tastiera + "\n");
                            break;
                        // Choice to send a message privately
                        case "/only":
                            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.WHITE_BOLD_BRIGHT
                                    + "Inserisci il nome del partecipante seguito da " + color.RESET
                                    + color.YELLOW_BOLD_BRIGHT + "'#'" + color.RESET + color.WHITE_BOLD_BRIGHT
                                    + " e dal messaggio da inviare" + color.RESET);
                            String messaggio = inputTastiera.readLine();
                            outVersoIlServer.writeBytes("@only:" + this.getNickname() + ":" + messaggio + "\n");
                            break;
                        // Choice to request the list of names of clients currently present
                        case "/lista":
                            outVersoIlServer.writeBytes("@lista:" + this.getNickname() + "\n");
                            break;
                        // Choice to exit the chat and stop communication
                        case "/exit":
                            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.BLUE_BOLD_BRIGHT
                                    + "Hai abbandonato la chat! " + color.RESET + color.BLACK_BACKGROUND_BRIGHT
                                    + dtf.format(now) + color.RESET + "\n");
                            outVersoIlServer.writeBytes("@exit:" + this.getNickname() + ":-" + "\n");
                            exit = true;
                            break;
                        // Case in which the client has entered a choice not covered among the previous ones
                        default:
                            richiamaMenu();
                            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.RED_BOLD_BRIGHT
                                    + "Scelta non valida, riprovare " + color.RESET + color.BLACK_BACKGROUND_BRIGHT
                                    + dtf.format(now) + color.RESET + "\n");
                            break;
                    }
                }
            } while (!exit);
            // Interrupting the thread associated with the client to read incoming messages from the server
            clientThread.setRunning(false);
            // Closing the socket to communicate with the server
            this.dataSocket.close();
            System.exit(1);
        } catch (Exception e) {
            System.out.println(color.RED_BOLD_BRIGHT + "ERRORE DI COMUNICAZIONE CLIENT SIDE " + color.RESET
                    + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now) + color.RESET + "\n");
        }
    }

    // Method for printing the menu with the various possible choices
    public void richiamaMenu() {
        System.out.println("\n" + color.YELLOW_BACKGROUND_BRIGHT + "-----MENU-----" + color.RESET + "\n\n"
                + color.GREEN_BACKGROUND_BRIGHT + "-Invia un messaggio a tutti i partecipanti:" + color.RESET
                + color.GREEN_BOLD_BRIGHT + " || /all ||" + color.RESET + "\n\n"
                + color.WHITE_BACKGROUND_BRIGHT + "-Invia un messaggio ad un partecipante specifico:" + color.RESET
                + color.WHITE_BOLD_BRIGHT + " || /only ||" + color.RESET + "\n\n"
                + color.CYAN_BACKGROUND_BRIGHT + "-Visualizza lista partecipanti:" + color.RESET
                + color.CYAN_BOLD_BRIGHT + " || /lista ||" + color.RESET + "\n\n"
                + color.BLUE_BACKGROUND_BRIGHT + "-Abbandona il gruppo:" + color.RESET + color.BLUE_BOLD_BRIGHT
                + " || /exit ||" + color.RESET + "\n");
    }

    // Method to set the client name
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // Method to return the client name
    public String getNickname() {
        return this.nickname;
    }

    // Method to set the new client variable
    public void setNuovo(boolean nuovo) {
        this.nuovo = nuovo;
    }

    // Method to return the new client variable
    public boolean isNuovo() {
        return nuovo;
    }

    // Method to check what the server sent relative to the entered nickname
    public void checkNome(String messageServer) {
        if (messageServer != null) {
            if (messageServer.split(":")[0].equals("@new")) {
                setNickname(nickname);
                setNuovo(true);
            } else if (messageServer.split(":")[0].equals("@old")) {
                System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.RED_BOLD_BRIGHT
                        + "Attenzione inserire un nickname diverso" + color.RESET + "\n");
            }
        }
    }
}