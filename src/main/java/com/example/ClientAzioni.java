package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  


public class ClientAzioni {

    private Socket dataSocket;
    private BufferedReader inDalServer;
    private DataOutputStream outVersoIlServer;
    private BufferedReader inputTastiera;
    private ClientThread clientThread;
    private String nickname;
    private boolean exit = false;
    private String tastiera;

    public ClientAzioni(Socket dataSocket, BufferedReader inDalServer, DataOutputStream outVersoIlServer) {
        this.dataSocket = dataSocket;
        this.inDalServer = inDalServer;
        this.outVersoIlServer = outVersoIlServer;
        this.inputTastiera = new BufferedReader(new InputStreamReader(System.in));
        this.nickname = "";
        this.clientThread = new ClientThread(this.inDalServer, nickname);
        this.exit = false;
        this.tastiera = "";
    }

    public void init() {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();
            System.out.println("> Benvenuto/a, inserisci il tuo nickname");
            nickname = inputTastiera.readLine().toUpperCase();
            outVersoIlServer.writeBytes("@nick:" + nickname + ":*" + "\n");
            clientThread.start();
            clientThread.setNickname(nickname);

            richiamaMenu();
            System.out.println("> Ti sei unito/a alla chat! ||" + dtf.format(now));
            do {
                tastiera = inputTastiera.readLine();
                if (tastiera instanceof String && !tastiera.equals(null)) {
                    switch (tastiera) {
                        case "/all":
                            System.out.println("> Inserisci il messaggio in broadcast");
                            tastiera = inputTastiera.readLine();
                            outVersoIlServer.writeBytes("@all:" + getNickname() + ":" + tastiera + "\n");
                            break;
                        case "/lista":
                            outVersoIlServer.writeBytes("@lista:" + this.getNickname() + "\n");
                            break;
                        case "/only":
                            System.out.println("> Inserisci il nome del partecipante, seguito da '#' e dal messaggio da inviare");
                            String messaggio = inputTastiera.readLine();
                            outVersoIlServer.writeBytes("@only:" + this.getNickname() + ":" + messaggio + ":" + "\n");
                            break;
                        case "/exit":
                            System.out.println("> Hai abbandonato la chat! ||" + dtf.format(now));
                            outVersoIlServer.writeBytes("@exit" + ":" + getNickname() + ":-" + "\n");
                            exit = true;
                            break;
                        default:
                            System.out.println("Scelta non valida, riprovare ||" + dtf.format(now));
                            richiamaMenu();
                            break;
                    }
                }
            } while (!exit);
            clientThread.terminaEsecuzione();
            this.dataSocket.close();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRORE LATO CLIENT");
        }
    }

    public void richiamaMenu() {
        System.out.println("-----MENU-----" +
                "\n> Invia un messaggio a tutti i partecipanti --> (/all)" +
                "\n> Invia un messaggio ad un partecipante specifico --> (/only)" +
                "\n> Visualizza lista partecipanti --> (/lista)" +
                "\n> Abbandona il gruppo --> (/exit)" +
                "\n--------------");
    }

    public String getNickname() {
        return this.nickname;
    }
}