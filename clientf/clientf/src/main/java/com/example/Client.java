package com.example;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        Socket clientSocket = new Socket("localhost", 3000);
        BufferedReader in = new BufferedReader(new java.io.InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        if (in.readLine().equals("WELCOME")) {
            System.out.println("\nConnessione effettuata.");
        }

        do {
            System.out.println("Inserire nome utente: ");
            String user = scanner.nextLine();
            out.println("LOGIN " + user);
            String answer = in.readLine();
            System.out.println(answer);

            if (answer.equals("OK")) {
                System.out.println("\nLogin effettuato con successo.");
                break;
            } else {
                System.out.println("\nErrore reinserire credenziali.");
            }
        } while (true);

        String command;
        do {
            System.out.println("\nInserisci l'operazione:\n1: visualizza\n2: compra\n3: quit");
            command = scanner.nextLine();
        switch (command) {
                case "visualizza":
                out.println("N");
                   String s = in.readLine();
                   System.out.println(s);

                    break;

                case "compra":
                out.println("BUY");
                    System.out.println("Inserisci il tipo e la quantità dei biglietti: ");
                    String id = scanner.nextLine();
                    out.println(id);
                    String str = in.readLine();
                    if(str.equals("OK"))System.out.println("compra andata a successo");
                    else System.out.println("compra non riuscita");
                    break;

                case "quit":
                    out.println("QUIT");
                    //chiusura del socket e quindi del client
                    System.out.println("Connessione con il server interrotta");
                    return;

                default:
                    System.out.println("comando inesistente reinserire.\n");
                    break;
            }
        } while (!command.equals("quit"));

    }
}