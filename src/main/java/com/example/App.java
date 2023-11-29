package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class App {
    public static void main(String[] args) {
        try{
            Socket dataSocket = new Socket("localhost", 4500);
            BufferedReader inDalServer = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
            DataOutputStream outVersoIlServer = new DataOutputStream(dataSocket.getOutputStream());
            ClientAzioni client = new ClientAzioni(dataSocket, inDalServer, outVersoIlServer);
            client.init();
        }catch(Exception e){
            System.out.println("ERRORE NELL'INSTAURAZIONE DELLA COMUNICAZIONE");
        }    
    }
}