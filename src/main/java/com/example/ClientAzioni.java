package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientAzioni {

    private Socket dataSocket;
    private BufferedReader inDalServer;
    private DataOutputStream outVersoIlServer;
    private BufferedReader inputTastiera;
    private ClientThread clientThread;
    private String nickname;
    private boolean esci = false;
    private String tastiera;

    public ClientAzioni(Socket dataSocket, BufferedReader inDalServer, DataOutputStream outVersoIlServer) {
        this.dataSocket = dataSocket;
        this.inDalServer = inDalServer;
        this.outVersoIlServer = outVersoIlServer;
        this.inputTastiera = new BufferedReader(new InputStreamReader(System.in));
        this.nickname = "";
        this.clientThread = new ClientThread(this.inDalServer, nickname);
        this.esci = false;
        this.tastiera = "";
    }

    public void init() {
        try {
            System.out.println("> Benvenuto/a, inserisci il tuo nickname");
            nickname = inputTastiera.readLine();
            outVersoIlServer.writeBytes("/nick:" + nickname + ":" + "\n");
            System.out.println("> Ti sei unito/a al gruppo!");
            clientThread.start();
            clientThread.setNickname(nickname);

            richiamaMenu();
            do {
                tastiera = inputTastiera.readLine();
                if (tastiera instanceof String && !tastiera.equals(null)) {
                    switch (tastiera) {
                        case "/all":
                            System.out.println("> Inserisci il messaggio in broadcast");
                            tastiera = inputTastiera.readLine();
                            outVersoIlServer.writeBytes("/all:" + getNickname() + ":" + tastiera + "\n");
                            break;
                        case "/lista":
                            outVersoIlServer.writeBytes("/lista:" + this.getNickname() + "\n");
                            System.out.println("> Inserisci il nome del partecipante");
                            String nickRicerca = inputTastiera.readLine();
                            outVersoIlServer.writeBytes("@only:" + this.getNickname() + ":" + nickRicerca + ":" + "\n");
                            //..........
                            break;
                        case "/exit":
                            outVersoIlServer.writeBytes(tastiera + ":" + getNickname() + "\n");
                            esci = true;
                            break;
                    }
                } else {
                    System.out.println("> Attenzione scelta non valida, riprovare" + "\n");
                    richiamaMenu();
                }
            } while (!esci);
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
                "\n> Invia un messaggio ad un partecipante specifico --> (/lista)" +
                "\n> Abbandona il gruppo --> (/exit)" +
                "\n--------------");
    }

    public String getNickname() {
        return this.nickname;
    }
}