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
        //HO SOLO DA GESTIRE LA STAMPA DEL MESSAGGIO PRIVATO, NON SO DOVE STAMPARE IL MESSAGGIO PRIVATO INOLTRATO DAL SERVER AD UNO SPECIFICO CLIENT

        try {
            Socket dataSocket = new Socket("localhost", 4500);
            Socket broadcastSocket = new Socket("localhost", 4501);

            BufferedReader inputTastiera = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader inDalServer = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
            DataOutputStream outVersoIlServer = new DataOutputStream(dataSocket.getOutputStream());
            ClientThread canaleBroadcast = new ClientThread(broadcastSocket, "");
            String stringaTastiera = "";
            String stringaRicevuta = "";
            int scelta;
            boolean esci = false;
            boolean firstTime = true;
            String nicknameRicerca = "";
            String nickname = "";

            System.out.println("> Benvenuto/a, inserisci il tuo nickname");
            stringaTastiera = inputTastiera.readLine();
            outVersoIlServer.writeBytes(stringaTastiera + "\n");
            nickname = stringaTastiera;

            canaleBroadcast.setName(stringaTastiera);
            canaleBroadcast.start();

            while(!esci){
                if(firstTime){
                    richiamaMenu();
                }
                
                firstTime = false;
                stringaTastiera = inputTastiera.readLine();
                if(!stringaTastiera.equals(null) && !stringaTastiera.equals("")){
                    scelta = Integer.parseInt(stringaTastiera);
                
                    switch (scelta) {
                        case 1:        
                            System.out.println("> Inserisci il testo del messaggio");
                            stringaTastiera = inputTastiera.readLine();
                            outVersoIlServer.writeBytes("/all:" + stringaTastiera + "\n");
                            System.out.println("> Messaggio inviato!\n");
                            break;

                        case 2:        
                            outVersoIlServer.writeBytes("/lista:" + nickname + "\n");
                            stringaRicevuta = inDalServer.readLine();
                            getListaNicknamesCorretta(stringaRicevuta);
                            System.out.println("> Inserisci il nome del partecipante");
                            stringaTastiera = inputTastiera.readLine();
                            outVersoIlServer.writeBytes("/verify:" + stringaTastiera + "\n");
                            nicknameRicerca = stringaTastiera;

                            stringaRicevuta = inDalServer.readLine();
                            if(stringaRicevuta.equals("/ok")){
                                System.out.println("> Inserisci il messaggio da inviare a " + nicknameRicerca);
                                stringaTastiera = inputTastiera.readLine();
                                outVersoIlServer.writeBytes("/only:" + nicknameRicerca + ":" + stringaTastiera + "\n");
                                System.out.println("> Messaggio inviato a " + nicknameRicerca + "\n");
                            }
                            else if(stringaRicevuta.equals("/not")){
                                System.out.println("> " + nicknameRicerca + " non appartiene al gruppo!");
                            }
                            else if(stringaRicevuta.equals("/you")){
                                System.out.println("> Non puoi inviare un messaggio a te stesso/a"); 
                            }
                            break;

                        case 3:
                            esci = true;
                            outVersoIlServer.writeBytes("/esci" + "\n");
                            System.out.println("Peccato, hai abbandonato il gruppo!\n");
                            canaleBroadcast.terminaEsecuzione();
                            break;

                        case 4 : 
                            richiamaMenu();
                        break;
                    }
                }
                else{
                    richiamaMenu();
                }
            }
            dataSocket.close();
        } catch (IOException e) {
            System.out.println("> Attenzione, errore nella creazione della comunicazione");
        }
    }

    public static void richiamaMenu(){
        System.out.println("-----MENU-----" +   
               "\n> Invia un messaggio a tutti i partecipanti --> (1)" + 
               "\n> Invia un messaggio ad un partecipante specifico --> (2)" +  
               "\n> Abbandona il gruppo --> (3)" + 
               "\n> Visualizza nuovamente il menu --> (4)" + 
               "\n--------------");
    }

    public static void getListaNicknamesCorretta(String listaPartecipanti){
        listaPartecipanti = listaPartecipanti.replaceAll(";", "\n");
        System.out.println("---LISTA PARTECIPANTI---\n" + listaPartecipanti);
    }
}