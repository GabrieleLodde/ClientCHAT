package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  

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
                if (messaggeReceived != null) {
                    formattaMessaggio(messaggeReceived);
                }
            }
        } catch (IOException e) {
            System.out.println("ERRORE THREAD CLIENT (IOEXCEPTION)");
        } catch (NullPointerException ex){
            System.out.println("ERRORE THREAD CLIENT (LETTURA MESSAGGIO NULL)");
        }
        return;
    }

    public void terminaEsecuzione() {
        this.setRunning(false);
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        
        String[] arrayString = messaggeReceived.split(":", 4);
        if (!this.getNickname().equals("")) {
            if (arrayString[0].equals("@nick")) {
                System.out.println("> | " + arrayString[1] + " | si e' unito/a al gruppo! ||" + dtf.format(now));
                this.setNickname(arrayString[1]);
            }
            else if(arrayString[0].equals("@all")){
                System.out.println("> | " + arrayString[1] + " | ha scritto --> " + arrayString[2] + " ||" + dtf.format(now));
            }
            else if(arrayString[0].equals("@lista")){
                System.out.println("---LISTA PARTECIPANTI--- ||" + dtf.format(now));
                System.out.println(arrayString[1].replaceAll(";", "\n"));
            }
            else if(arrayString[0].equals("@exit")){
                System.out.println("> | " + arrayString[1] + " | ha abbandonato il gruppo! ||" + dtf.format(now));
            }
            else if(arrayString[0].equals("@alone1")){
                System.out.println("> Impossibile inviare il messaggio in broadcast, sei presente solo tu! ||" + dtf.format(now));
            }
            else if(arrayString[0].equals("@alone2")){
                System.out.println("> Impossibile inviare il messaggio privato, sei presente solo tu! ||" + dtf.format(now));
            }
            else if(arrayString[0].equals("@wrong")){
                System.out.println("> Impossibile inviare il messaggio privato,  | " + arrayString[1] + " | non fa parte del gruppo ||" + dtf.format(now));
            }
            else if(arrayString[0].equals("@only")){
                System.out.println("> | " + arrayString[1] + " | ti ha scritto --> " + arrayString[2] + " ||" + dtf.format(now));
            }
            else if(arrayString[0].equals("@ok1")){
                System.out.println("> Messaggio inviato in broadcast! ||" + dtf.format(now));
            }
            else if(arrayString[0].equals("@ok2")){
                System.out.println("> | " + arrayString[1] + " | ha ricevuto il messaggio! ||" + dtf.format(now));
            }
        }
    }

}