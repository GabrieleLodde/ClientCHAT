package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread{
    
    private Socket broadcastSocket;
    private BufferedReader inBroadcastDalServer;
    private boolean running;
    private String nome;
    private boolean entrato;

    public ClientThread(Socket broadcastSocket, String nome, boolean entrato) {
        this.broadcastSocket = broadcastSocket;
        this.running = true;
        this.nome = nome;
        try {
            this.inBroadcastDalServer = new BufferedReader(new InputStreamReader(this.broadcastSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("> Errore nella creazione della comunicazione broadcast" + e.getMessage());
        }
        this.entrato = entrato;
    }

    @Override
    public void run(){
        String messaggioRicevutoBroadcast = "";
        while (this.running) {
            try {
                messaggioRicevutoBroadcast = inBroadcastDalServer.readLine();
                if(!messaggioRicevutoBroadcast.equals(null) && this.entrato == true ){
                    if(checkNome(messaggioRicevutoBroadcast)){
                        System.out.println(formattaMessaggio(messaggioRicevutoBroadcast) + "\n> Premere Invio");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("> Errore nella comunicazione broadcast");
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

    public void setEntrato(){
        this.entrato = true;
    }

    public boolean checkNome(String messaggio){
        String[] array = messaggio.split(":");
        if(array[1].equals(this.getNome())){
            return false;
        }
        return true;
    }

    public String formattaMessaggio(String messaggio){
        String[] array = messaggio.split(":");
        return array[0] + array[1] + array[2] + array[3];
    }
}