package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class App {
    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();
        try{
            Socket dataSocket = new Socket("localhost", 4500);
            BufferedReader inDalServer = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
            DataOutputStream outVersoIlServer = new DataOutputStream(dataSocket.getOutputStream());
            ClientAzioni client = new ClientAzioni(dataSocket, inDalServer, outVersoIlServer);
            client.init();
        }catch(Exception e){
            System.out.println("ERRORE NELL'INSTAURAZIONE DELLA COMUNICAZIONE ||" + dtf.format(now));
        }    
    }
}