package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread{
    
    Socket s;
    BufferedReader inBroadcastDalServer;
    boolean running;
    public ClientThread(Socket s) {
        this.s = s;
        this.running = true;
        try {
            this.inBroadcastDalServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore nella comunicazione " + e.getMessage());
        }
    }

    @Override
    public void run(){
        String messaggioRicevuto = "";
        while (this.running) {
            try {
                messaggioRicevuto = inBroadcastDalServer.readLine();
                if(messaggioRicevuto != null)
                    System.out.println(messaggioRicevuto);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("> Errore nella comunicazione " + e.getMessage());
            }
        }
        System.out.println("Sto terminando");
    }

    public void terminate(){
        this.running = false;
    }
}