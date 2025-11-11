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

        String utente = "";      // memorizza il nome dell'utente connesso

        out.println("WELCOME"); 

        boolean success = false; // login è avvenuto con successo

        
        while (!success) {

            String cmd = "";
            try {
                cmd = in.readLine(); // Legge client
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (cmd == null) 
                break;

            //"LOGIN Luca")
            String[] messaggio = cmd.split(" ", 2);

            // "LOGIN" con un nome valido
            if (messaggio.length == 2 && messaggio[0].equals("LOGIN") || !(messaggio[1] == "")) {
              
                success = true; // Presume login riuscito
                utente = messaggio[1]; // Salva il nome utente

                
                for (String ut : utentiConn) {
                    if (messaggio[1].equals(ut)) {
                        success = false;
                        out.println("ERR USERINUSE"); 
                        break;
                    }
                }
            } else
                out.println("ERR LOGINREQUIRED"); 
        }

        // Aggiunge l’utente alla lista di utenti connessi
        utentiConn.add(utente);
        out.println("OK"); 

        // comando e argomento
        String[] command = { "", "" };

        //  comandi dopo il login
        while (true) {
            String cmd = "";
            try {
                cmd = in.readLine(); // Legge c client
            } catch (IOException e) {
                e.printStackTrace();
            }

            
            command = cmd.split(" ", 2);

            // client  disconnettersi
            if (command[0].equals("QUIT")) {
                out.println("BYE"); 
                try {
                    socket.close(); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

            // Gestisce i vari comandi
            switch (command[0]) {

                case "N":
                    //  disponibilità dei biglietti
                    out.println("AVAIL Gold:" + disponibilitaGold + " PIT:" + disponibilitaPit
                            + " Parterre:" + disponibilitaParterre);
                    break;

                case "BUY":
                    // Il client vuole acquistare biglietti

                    String com = "";
                    try {
                        com = in.readLine(); // legge il tipo e quantità
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Divide il comando (es. "Gold 2")
                    String[] buy = com.split(" ", 2);

                    // C scritto correttamente
                    if (buy[1].equals("")) {
                        out.println("ERR SYNTAX");
                        break;
                    }

                    // Converte la quantità in numero
                    int quantita = Integer.parseInt(buy[1]);

                    // Controlla il tipo di biglietto
                    switch (buy[0]) {

                        case "Gold":
                            
                            if (quantita > disponibilitaGold)
                                out.println("KO");
                            else
                                disponibilitaGold -= quantita; // Riduce la disponibilità
                                out.println("OK"); 
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
                    
                    out.println("ERR SYNTAX");
                    break;
            }
        }
    }
}
