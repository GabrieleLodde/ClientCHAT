package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class App 
{
    public static void main( String[] args )
    {
        try {
            Socket s = new Socket("localhost", 4500);

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

                // stringaRicevuta = inDalServer.readLine();
                // if(!stringaRicevuta.equals("")){
                //     System.out.println(stringaRicevuta);
                // }

                //questo mi blocca il client, perchè mi resta in attesa di un messaggio all'infinito

                System.out.println("-----MENU-----");
                System.out.println("> Invia un messaggio a tutti i partecipanti --> (1)");
                System.out.println("> Invia un messaggio ad un partecipante specifico --> (2)");
                System.out.println("> Abbandona il gruppo --> (3)");
                System.out.println("--------------");

                stringaTastiera = inputTastiera.readLine();
                scelta = Integer.parseInt(stringaTastiera);

                switch (scelta) {
                    case 1:        //invio di un messaggio in broadcast
                        
                        outVersoIlServer.writeBytes("1" + "\n");

                        System.out.println("> Inserisci il messaggio da inviare...");
                        stringaTastiera = inputTastiera.readLine();
                        outVersoIlServer.writeBytes(stringaTastiera + "\n");
                        break;

                    case 2:        //invio di un messaggio ad un partecipante specifico

                        System.out.println("> Inserisci il nickname del partecipante con cui vuoi comunicare...");
                        stringaTastiera = inputTastiera.readLine();
                        //.............
                        break;

                    case 3:        // termine della comunicazione 

                        esci = true;
                        outVersoIlServer.writeBytes("-1" + "\n");
                        System.out.println("Peccato, hai abbandonato il gruppo!");
                        break;
                }
            }

            s.close();
        } catch (IOException e) {
            System.out.println("> Attenzione, errore nella creazione della comunicazione");
        }
            
    }
}