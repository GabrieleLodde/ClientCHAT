package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class App {
    // Dichiarazione variabili
    static Socket dataSocket;
    static Socket broadcastSocket;
    static BufferedReader inDalServer;
    static DataOutputStream outVersoIlServer;
    static BufferedReader inputTastiera;
    static ClientThread canaleBroadcast;
    static String stringaTastiera;
    static String stringaRicevuta;
    static int scelta;
    static boolean esci;
    static boolean firstTime;
    static String nicknameRicerca;
    static String nickname;
    static boolean entrato;

    public static void main(String[] args) {
        // HO SOLO DA GESTIRE LA STAMPA DEL MESSAGGIO PRIVATO, NON SO DOVE STAMPARE IL
        // MESSAGGIO PRIVATO INOLTRATO DAL SERVER AD UNO SPECIFICO CLIENT

        istanziaSocket();
        instauraCanaliServer();
        instauraInputTastiera();
        inizializzaThreadBroadcast();
        inizializzaVariabili();

        avviaThread();
        richiediNomeClient();

        while (!esci) {
            if (firstTime) {
                richiamaMenu();
            }
            firstTime = false;
            switch (restituisciSceltaClient()) {
                case 1:
                    inviaMessaggioBroadcast();
                    break;

                case 2:
                    richiediLista();
                    stampaLista();
                    inviaNomeClient();
                    controllaNomeClient();
                    break;

                case 3:
                    esciDalGruppo();
                    break;

                case -1:
                    richiamaMenu();
                    break;
            }
        }
        chiudiSocket();
    }

    public static void richiamaMenu() {
        System.out.println("-----MENU-----" +
                "\n> Invia un messaggio a tutti i partecipanti --> (1)" +
                "\n> Invia un messaggio ad un partecipante specifico --> (2)" +
                "\n> Abbandona il gruppo --> (3)" +
                "\n--------------");
    }

    public static void getListaNicknamesCorretta(String listaPartecipanti) {
        listaPartecipanti = listaPartecipanti.replaceAll(";", "\n");
        System.out.println("---LISTA PARTECIPANTI---\n" + listaPartecipanti);
    }

    public static void istanziaSocket() {
        try {
            dataSocket = new Socket("localhost", 4500);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("> Errore durante la dichiarazione del socket per server");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante la dichiarazione del socket per server (1)");
        }
        try {
            broadcastSocket = new Socket("localhost", 4501);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("> Errore durante la dichiarazione del socket broadcast");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante la dichiarazione del socket broadcast (1)");
        }
    }

    public static void instauraCanaliServer() {
        try {
            inDalServer = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante l'istaurazione del canale input dal server");
        }
        try {
            outVersoIlServer = new DataOutputStream(dataSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante l'istaurazione del canale verso il server");
        }
    }

    public static void instauraInputTastiera() {
        inputTastiera = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void inizializzaThreadBroadcast() {
        canaleBroadcast = new ClientThread(broadcastSocket, "", entrato);
    }

    public static void inizializzaVariabili() {
        stringaTastiera = "";
        stringaRicevuta = "";
        esci = false;
        firstTime = true;
        nicknameRicerca = "";
        nickname = "";
        entrato = false;
    }

    public static void avviaThread() {
        canaleBroadcast.start();
    }

    public static void richiediNomeClient() {
        System.out.println("> Benvenuto/a, inserisci il tuo nickname");
        try {
            stringaTastiera = inputTastiera.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante la richiesta del nome al client");
        }
        try {
            outVersoIlServer.writeBytes(stringaTastiera + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante l'invio del nome al server");
        }
        nickname = stringaTastiera;
        canaleBroadcast.setName(nickname);
        canaleBroadcast.setEntrato();
    }

    public static int restituisciSceltaClient() {
        try {
            stringaTastiera = inputTastiera.readLine();
            if (!stringaTastiera.equals(null) && !stringaTastiera.equals("")) {
                scelta = Integer.parseInt(stringaTastiera);
                return scelta;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante la ricezione della scelta del client");
        }
        return -1;
    }

    public static void inviaMessaggioBroadcast() {
        System.out.println("> Inserisci il testo del messaggio");
        try {
            stringaTastiera = inputTastiera.readLine();
            outVersoIlServer.writeBytes("/all:" + stringaTastiera + "\n");
            System.out.println("> Messaggio inviato!\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("> Errore nella comunicazione del messaggio in broadcast");
        }
    }

    public static void richiediLista() {
        try {
            outVersoIlServer.writeBytes("/lista:" + nickname + "\n");
        } catch (IOException e) {
            System.out.println("> Errore durante la richiesta della lista dei client al server");
            e.printStackTrace();
        }
    }

    public static void stampaLista() {
        try {
            stringaRicevuta = inDalServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante la ricezione della lista dal server");
        }
        getListaNicknamesCorretta(stringaRicevuta);
    }

    public static void inviaNomeClient() {
        System.out.println("> Inserisci il nome del partecipante");
        try {
            stringaTastiera = inputTastiera.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante la ricezione del nome del client da tastiera");
        }
        try {
            outVersoIlServer.writeBytes("/verify:" + stringaTastiera + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante l'invio del nome del client al server");
        }
        nicknameRicerca = stringaTastiera;
    }

    public static void controllaNomeClient() {
        try {
            stringaRicevuta = inDalServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante la ricezione della risposta dal server");
        }
        if (stringaRicevuta.equals("/ok")) {
            System.out.println("> Inserisci il messaggio da inviare a " + nicknameRicerca);
            try {
                stringaTastiera = inputTastiera.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("> Errore durante la ricezione del messaggio da inviare al client");
            }
            try {
                outVersoIlServer.writeBytes("/only:" + nicknameRicerca + ":" + stringaTastiera + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("> Errore durante l'invio del messaggio specifico al client");
            }
            System.out.println("> Messaggio inviato a " + nicknameRicerca + "\n");
        } else if (stringaRicevuta.equals("/not")) {
            System.out.println("> " + nicknameRicerca + " non appartiene al gruppo!");
        } else if (stringaRicevuta.equals("/you")) {
            System.out.println("> Non puoi inviare un messaggio a te stesso/a");
        }
    }

    public static void esciDalGruppo() {
        esci = true;
        try {
            outVersoIlServer.writeBytes("/esci" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore durante l'invio della richiesta di uscita al server");
        }
        System.out.println("Peccato, hai abbandonato il gruppo!\n");
        canaleBroadcast.terminaEsecuzione();
    }

    public static void chiudiSocket() {
        try {
            dataSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}