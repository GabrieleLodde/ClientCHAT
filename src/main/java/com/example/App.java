package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        try {
            Socket s = new Socket("localhost", 4500);
            Socket sBroadcast = new Socket("localhost", 4501);

            Scanner inputTastiera = new Scanner(System.in);
            BufferedReader inDalServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream outVersoIlServer = new DataOutputStream(s.getOutputStream());
            String stringaTastiera = "";
            String stringaRicevuta = "";
            int scelta;
            boolean esci = false;


            System.out.println("> Benvenuto, inserisci il tuo nickname");
            stringaTastiera = inputTastiera.nextLine();
            System.out.println(stringaRicevuta);
            outVersoIlServer.writeBytes(stringaTastiera + "\n");

            ClientThread canaleBroadcast = new ClientThread(sBroadcast);
            canaleBroadcast.start();

            while(!esci){
                richiamaMenu();

                stringaTastiera = inputTastiera.nextLine();
                scelta = Integer.parseInt(stringaTastiera);

                switch (scelta) {
                    case 1:        //invio di un messaggio in broadcast
                        System.out.println("Inserisci il testo del messaggio...");
                        stringaTastiera = inputTastiera.nextLine();
                        outVersoIlServer.writeBytes("@all:" + stringaTastiera + "\n");
                        break;

                    case 2:        //invio di un messaggio ad un partecipante specifico

                        System.out.println("Inserisci il nome del partecipante");
                        stringaTastiera = inputTastiera.nextLine();
                        //.............
                        break;

                    case 3:        // termine della comunicazione 

                        esci = true;
                        outVersoIlServer.writeBytes("-1" + "\n");
                        System.out.println("Peccato, hai abbandonato il gruppo!");
                        canaleBroadcast.terminate();
                        break;
                    
                    case 4 : 

                        richiamaMenu();
                        break;
                }
            }
            s.close();
        } catch (IOException e) {
            System.out.println("> Attenzione, errore nella creazione della comunicazione");
        }
            
    }

    public static void richiamaMenu(){
        System.out.println("-----MENU-----" +   
               "\n> Invia un messaggio a tutti i partecipanti --> (1)" + 
               "\n> Invia un messaggio ad un partecipante specifico --> (2)" +  
               "\n> Abbandona il gruppo --> (3)" + 
               "\n> Digita /menu per visualizzare nuovamente il menu" + 
               "\n--------------");
    }
}