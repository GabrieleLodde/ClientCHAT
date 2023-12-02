package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class App {
    public static void main(String[] args) {
        // Declarations of time and color variables
        ColorExample color = new ColorExample();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();
        try {
            // Declaration of the socket and Input/Output channels to communicate with the server
            Socket dataSocket = new Socket("localhost", 4500);
            BufferedReader inDalServer = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
            DataOutputStream outVersoIlServer = new DataOutputStream(dataSocket.getOutputStream());
            // Declaring an object of type ClientAzioni and calling the init() method on it to start the series of possible choices
            ClientAzioni client = new ClientAzioni(dataSocket, inDalServer, outVersoIlServer);
            client.init();
        } catch (Exception e) {
            System.out.println(color.RED_BOLD_BRIGHT + "ERRORE NELL'INSTAURAZIONE DELLA COMUNICAZIONE " + color.RESET
                    + color.BLACK_BACKGROUND_BRIGHT + dtf.format(now) + color.RESET);
        }
    }
}