package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class App 
{
    public static void main( String[] args ) throws UnknownHostException, IOException
    {
        Socket s = new Socket("localhost", 4500);
        try {
            BufferedReader inputTastiera = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader inDalServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream outVersoIlServer = new DataOutputStream(s.getOutputStream());
            String stringaTastiera = "";
            String stringaRicevuta = "";
            int scelta;
            boolean esci = false;

            System.out.println("> Benvenuto, inserisci il tuo nickname");
            stringaTastiera = inputTastiera.readLine();
            outVersoIlServer.writeBytes(stringaTastiera + "\n");

            while(!esci){
                System.out.println("--MENU---");
                System.out.println("> Invia un messaggio a tutti i partecipanti --> (1)");
                System.out.println("> Invia un messaggio ad un partecipante specifico --> (2)");
                System.out.println("> Abbandona il gruppo --> (3)");

                stringaTastiera = inputTastiera.readLine();
                scelta = Integer.parseInt(stringaTastiera);

                switch (scelta) {
                    case 1:
                        
                        break;

                    case 2:

                        break;

                    case 3:
                        esci = true;
                        outVersoIlServer.writeBytes("-1" + "\n");
                        System.out.println("Peccato, hai abbandonato il gruppo!");
                        break;
                }
            }
            s.close();
        } catch (IOException e) {
            e.getMessage();
            System.out.println("> Attenzione, errore nella creazione della comunicazione");
        }
            
    }
}