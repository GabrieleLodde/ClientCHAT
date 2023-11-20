package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread{
    
    Socket broadcastSocket;
    BufferedReader inBroadcastDalServer;
    boolean running;
    String nome;

    public ClientThread(Socket broadcastSocket, String nome) {
        this.broadcastSocket = broadcastSocket;
        this.running = true;
        this.nome = nome;
        try {
            this.inBroadcastDalServer = new BufferedReader(new InputStreamReader(broadcastSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore nella creazione della comunicazione broadcast" + e.getMessage());
        }
    }

    @Override
    public void run(){
        String messaggioRicevutoBroadcast = "";
        while (this.running) {
            try {
                messaggioRicevutoBroadcast = inBroadcastDalServer.readLine();
                if(!messaggioRicevutoBroadcast.equals(null)){
                    System.out.println(messaggioRicevutoBroadcast);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("> Errore nella comunicazione broadcast " + e.getMessage());
            }
        }
        System.out.println("> Termine esecuzione thread client");
    }

    public void terminaEsecuzione(){
        this.running = false;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }
}