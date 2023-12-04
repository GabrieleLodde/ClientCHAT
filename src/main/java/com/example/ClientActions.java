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
    private boolean newC;
    private boolean exit = false;
    private String keyBoardString;
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
        this.newC = false;
        this.exit = false;
        this.keyBoardString = "";
        this.dtf = DateTimeFormatter.ofPattern("HH:mm");
        this.now = LocalDateTime.now();
        this.color = new ClientColors();
    }

    public void init() {
        try {
            // Initial nickname request to the client
            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.BLUE_BOLD_BRIGHT
                    + "Benvenuto/a, inserisci il tuo nickname" + color.RESET);

            // Cycle to request the nickname from the client until it enters one that is not
            // present among those in the chat
            do {
                nickname = inputTastiera.readLine().toUpperCase();
                outVersoIlServer.writeBytes("@nick:" + nickname + ":*" + "\n");
                String messageServer = inDalServer.readLine();
                checkNome(messageServer);
            } while (!isNewC());

            // Declaration and start of the thread associated with the client to read input
            // messages from the server
            clientThread = new ClientThread(inDalServer);
            clientThread.start();

            // Calling the method to display the menu of choices
            printMenu();

            // Client welcome print
            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.BLUE_BOLD_BRIGHT
                    + "Ti sei unito/a alla chat! " + color.RESET + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now)
                    + color.RESET + "\n");

            // Infinite loop, until the client wants to exit
            do {
                String messageKeyboard;
                // Reading the choice entered by the client from the keyboard
                keyBoardString = inputTastiera.readLine();
                if (keyBoardString instanceof String && !keyBoardString.equals(null)) {
                    // Controls over client choice
                    switch (keyBoardString) {
                        // Choice to send a broadcast message
                        case "/all":
                            boolean correctB = false;
                            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.GREEN_BOLD_BRIGHT
                                    + "Inserisci il messaggio in broadcast" + color.RESET);
                            do {
                                messageKeyboard = inputTastiera.readLine();
                                correctB = checkInput(messageKeyboard);
                            } while (!correctB);
                            outVersoIlServer.writeBytes("@all:" + this.getNickname() + ":" + messageKeyboard + "\n");
                            break;
                        // Choice to send a message privately
                        case "/only":
                            boolean correctP = false;
                            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET
                                    + color.WHITE_BOLD_BRIGHT
                                    + "Inserisci il nome del client seguito da " + color.RESET
                                    + color.YELLOW_BOLD_BRIGHT + "'#'" + color.RESET + color.WHITE_BOLD_BRIGHT
                                    + " e dal messaggio da inviare" + color.RESET);
                            do {
                                messageKeyboard = inputTastiera.readLine();
                                correctP = checkPrivateInput(messageKeyboard);
                            } while (!correctP);
                            outVersoIlServer.writeBytes("@only:" + this.getNickname() + ":" + messageKeyboard + "\n");
                            break;
                        // Choice to request the list of names of clients currently present
                        case "/list":
                            outVersoIlServer.writeBytes("@list:" + this.getNickname() + "\n");
                            break;
                        // Choice to exit the chat and stop communication
                        case "/exit":
                            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.BLUE_BOLD_BRIGHT
                                    + "Hai abbandonato la chat! " + color.RESET + color.BLACK_BACKGROUND_BRIGHT
                                    + dtf.format(now) + color.RESET + "\n");
                            outVersoIlServer.writeBytes("@exit:" + this.getNickname() + ":-" + "\n");
                            exit = true;
                            break;
                        // Case in which the client has entered a choice not covered among the previous
                        // ones
                        default:
                            System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.RED_BOLD_BRIGHT
                                    + "Scelta non valida, riprovare " + color.RESET + color.BLACK_BACKGROUND_BRIGHT
                                    + dtf.format(now) + color.RESET + "\n");
                            break;
                    }
                }
            } while (!exit);
            // Interrupting the thread associated with the client to read incoming messages
            // from the server
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
    public void printMenu() {
        System.out.println("\n" + color.YELLOW_BACKGROUND_BRIGHT + "-----MENU-----" + color.RESET + "\n\n"
                + color.GREEN_BACKGROUND_BRIGHT + "-Invia un messaggio a tutti i clients:" + color.RESET
                + color.GREEN_BOLD_BRIGHT + " || /all ||" + color.RESET + "\n\n"
                + color.WHITE_BACKGROUND_BRIGHT + "-Invia un messaggio ad un client specifico:" + color.RESET
                + color.WHITE_BOLD_BRIGHT + " || /only ||" + color.RESET + "\n\n"
                + color.CYAN_BACKGROUND_BRIGHT + "-Visualizza lista clients:" + color.RESET
                + color.CYAN_BOLD_BRIGHT + " || /list ||" + color.RESET + "\n\n"
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
    public void setNewC(boolean newC) {
        this.newC = newC;
    }

    // Method to return the new client variable
    public boolean isNewC() {
        return newC;
    }

    // Method to check what the server sent relative to the entered nickname
    public void checkNome(String messageServer) {
        if (messageServer != null) {
            if (messageServer.split(":")[0].equals("@new")) {
                setNickname(nickname);
                setNewC(true);
            } else if (messageServer.split(":")[0].equals("@old")) {
                System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET + color.RED_BOLD_BRIGHT
                        + "Attenzione inserire un nickname diverso" + color.RESET + "\n");
            }
        }
    }

    // Method for printing an error message when entering the message to send
    // privately
    public void printPrivateError() {
        System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET
                + color.RED_BOLD_BRIGHT
                + "Inserisci correttamente sia il nome del client seguito da " + color.RESET
                + color.YELLOW_BOLD_BRIGHT + "'#'" + color.RESET
                + color.RED_BOLD_BRIGHT
                + " che il testo del messaggio da inviare" + color.RESET + "\n");
    }

    // Method for printing an error message when entering the message to send
    // in broadcast
    public void printBroadcastError() {
        System.out.println(color.PURPLE_BOLD_BRIGHT + "> " + color.RESET
                + color.RED_BOLD_BRIGHT
                + "Inserisci correttamente il messaggio da inviare! " + color.RESET);
    }

    // Method for checking whether the client has entered both the nickname to
    // search for and the text of the message to send
    public boolean checkPrivateInput(String messageKeyboard) {
        if (messageKeyboard.contains("#")) {
            try {
                String nick = messageKeyboard.split("#", 2)[0];
                String message = messageKeyboard.split("#", 2)[1];
                if (nick.length() > 0 && message.length() > 0) {
                    return true;
                } else {
                    printPrivateError();
                    return false;
                }
            } catch (IndexOutOfBoundsException ex) {
                printPrivateError();
                return false;
            }
        } else {
            printPrivateError();
            return false;
        }
    }

    // Method for checking whether the client has entered a message without text
    public boolean checkInput(String messageKeyboard) {
        if (messageKeyboard.equals(" ") || messageKeyboard.equals("")) {
            printBroadcastError();
            return false;
        }
        return true;
    }
}