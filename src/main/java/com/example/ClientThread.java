package com.example;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientThread extends Thread {

    private BufferedReader inDalServer;
    private String nickname;
    private boolean running;
    private String messaggeReceived;

    public ClientThread(BufferedReader inDalServer, String nickname) {
        this.inDalServer = inDalServer;
        this.nickname = nickname;
        this.running = true;
        this.messaggeReceived = "";
    }

    @Override
    public void run() {
        try {
            while (this.running == true) {
                messaggeReceived = inDalServer.readLine();
                if (!messaggeReceived.equals(null)) {
                    formattaMessaggio(messaggeReceived);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERRORE LATO THREAD CLIENT");
        }
        return;
    }

    public void terminaEsecuzione() {
        this.setRunning(false);
        System.out.println("> Termine esecuzione thread client");
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void formattaMessaggio(String messaggeReceived) {
        String[] arrayString = messaggeReceived.split(":", 4);
        if (!this.getNickname().equals("")) {
            if (arrayString[0].equals("/nick")) {
                System.out.println("> " + arrayString[1] + " si e' unito/a al gruppo!");
                //System.out.println("> Premere invio");
                this.setNickname(arrayString[1]);
            }
            else if(arrayString[0].equals("/all")){
                System.out.println("> " + arrayString[1] + " ha scritto " + arrayString[2]);
                //System.out.println("> Premere invio");
            }
            else if(arrayString[0].equals("/lista")){
                System.out.println("---LISTA PARTECIPANTI---");
                System.out.println(arrayString[1].replaceAll(";", "\n"));
                //System.out.println("> Premere invio");
            }
            else if(arrayString[0].equals("@alone")){
                System.out.println("> Attenzione, sei da solo/a");
            }
            // else if(arrayString[0].equals("@only")){
            //     System.out.println("> " + arrayString[1] + " ti ha scritto " + arrayString[2]);
            // }
            else if(arrayString[0].equals("@wrong")){
                System.out.println("> Attenzione, il partecipante inserito non fa parte del gruppo");
            }
            else if(arrayString[0].equals("/exit")){
                System.out.println("> " + arrayString[1] + " ha abbandonato il gruppo!");
                //System.out.println("> Premere Invio");
            }
        }
    }

}