package com.example; 

import java.io.BufferedReader; 
import java.io.IOException;    
import java.io.InputStreamReader; 
import java.io.PrintWriter;    
import java.net.Socket;       
import java.util.ArrayList;   


public class MioThread extends Thread {

    Socket socket;       
    BufferedReader in;  
    PrintWriter out;    

    
    private static int disponibilitaGold = 10;
    private static int disponibilitaPit = 30;
    private static int disponibilitaParterre = 60;

    
    private static ArrayList<String> utentiConn = new ArrayList<>();

    
    public MioThread(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    
    @Override
    public void run() {

        String utente = ""; // memorizza il nome dell'utente connesso

        out.println("WELCOME"); // Messaggio di benvenuto inviato al client

        boolean success = false; // Flag per verificare se il login è avvenuto con successo

        // Ciclo che continua finché l'utente non effettua il login corretto
        while (!success) {

            String cmd = "";
            try {
                cmd = in.readLine(); // Legge una riga inviata dal client
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (cmd == null) // Se il client chiude la connessione
                break;

            // Divide il comando in due parti: comando e argomento (es. "LOGIN Luca")
            String[] messaggio = cmd.split(" ", 2);

            // Controlla che il comando sia "LOGIN" con un nome valido
            if (messaggio.length == 2 && messaggio[0].equals("LOGIN") || !(messaggio[1] == "")) {
              
                success = true; // Presume login riuscito
                utente = messaggio[1]; // Salva il nome utente

                // Controlla se l’utente è già connesso
                for (String ut : utentiConn) {
                    if (messaggio[1].equals(ut)) {
                        success = false; // Login fallito perché già in uso
                        out.println("ERR USERINUSE"); // Messaggio di errore al client
                        break;
                    }
                }
            } else
                out.println("ERR LOGINREQUIRED"); // Messaggio se il comando non è valido
        }

        // Aggiunge l’utente alla lista di utenti connessi
        utentiConn.add(utente);
        out.println("OK"); // Conferma che il login è riuscito

        // Array per contenere comando e argomento
        String[] command = { "", "" };

        // Ciclo principale che gestisce i comandi dopo il login
        while (true) {
            String cmd = "";
            try {
                cmd = in.readLine(); // Legge un comando dal client
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Divide il comando ricevuto (es. "BUY", "N", "QUIT", ecc.)
            command = cmd.split(" ", 2);

            // Se il client vuole disconnettersi
            if (command[0].equals("QUIT")) {
                out.println("BYE"); // Risponde con BYE
                try {
                    socket.close(); // Chiude la connessione
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break; // Esce dal ciclo
            }

            // Gestisce i vari comandi
            switch (command[0]) {

                case "N":
                    // Comando per mostrare la disponibilità dei biglietti
                    out.println("AVAIL Gold:" + disponibilitaGold + " PIT:" + disponibilitaPit
                            + " Parterre:" + disponibilitaParterre);
                    break;

                case "BUY":
                    // Il client vuole acquistare biglietti

                    String com = "";
                    try {
                        com = in.readLine(); // Legge il tipo di biglietto e la quantità
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Divide il comando (es. "Gold 2")
                    String[] buy = com.split(" ", 2);

                    // Controlla che sia scritto correttamente
                    if (buy[1].equals("")) {
                        out.println("ERR SYNTAX");
                        break;
                    }

                    // Converte la quantità in numero
                    int quantita = Integer.parseInt(buy[1]);

                    // Controlla il tipo di biglietto
                    switch (buy[0]) {

                        case "Gold":
                            // Se i biglietti richiesti sono più di quelli disponibili → errore
                            if (quantita > disponibilitaGold)
                                out.println("KO");
                            else
                                disponibilitaGold -= quantita; // Riduce la disponibilità
                                out.println("OK"); // Conferma l’acquisto
                            break;

                        case "Pit":
                            if (quantita > disponibilitaPit)
                                out.println("KO");
                            else
                                disponibilitaPit -= quantita;
                                out.println("OK");
                            break;

                        case "Parterre":
                            if (quantita > disponibilitaParterre)
                                out.println("KO");
                            else
                                disponibilitaParterre -= quantita;
                                out.println("OK");
                            break;
                    }

                    break;

                default:
                    // Se il comando non è riconosciuto
                    out.println("ERR SYNTAX");
                    break;
            }
        }
    }
}
