package com.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) {
        final int PORT = 3000;
        System.out.println("Server avviato sulla porta " + PORT);

        // Liste di esempio
        ArrayList<String> lista1 = new ArrayList<>(Arrays.asList("Marco Rossi","Ivan Bruno","Giulia Neri","Luca Bianchi","Sara Galli"));
        ArrayList<String> lista2 = new ArrayList<>(Arrays.asList("Ciccio Bello","Francesca Pini","Giorgio Verdi","Marta Lodi","Claudio Benvenuti","Pippo Baudo"));
        ArrayList<String> lista3 = new ArrayList<>(Arrays.asList("Anna Rosa","Paolo Conti","Davide Leone","Chiara Valli","Elisa Greco"));

        ArrayList<ArrayList<String>> liste = new ArrayList<>(Arrays.asList(lista1, lista2, lista3));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Nuovo client: " + client.getRemoteSocketAddress());

                try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true)
                ) {
                    out.println("BenveServer v1.2"); // welcome

                    boolean continua = true;

                    while (continua) {
                        String listaStr = in.readLine();
                        if (listaStr == null) {
                            continua = false;
                        } else if (listaStr.equals("!")) {
                            continua = false;
                        } else {
                            int listaNum;
                            try {
                                listaNum = Integer.parseInt(listaStr);
                            } catch (NumberFormatException e) {
                                out.println("KO");
                                listaNum = -1; // valore invalido
                            }

                            if (listaNum < 1 || listaNum > liste.size()) {
                                out.println("KO");
                            } else if (listaNum != -1) {
                                out.println("OK");

                                String nominativoStr = in.readLine();
                                if (nominativoStr == null) {
                                    continua = false;
                                } else if (nominativoStr.equals("!")) {
                                    continua = false;
                                } else {
                                    int nominativoNum;
                                    try {
                                        nominativoNum = Integer.parseInt(nominativoStr);
                                    } catch (NumberFormatException e) {
                                        out.println("KO");
                                        nominativoNum = -1;
                                    }

                                    ArrayList<String> listaSelezionata = liste.get(listaNum - 1);
                                    if (nominativoNum < 1 || nominativoNum > listaSelezionata.size() || nominativoNum == -1) {
                                        out.println("KO");
                                    } else {
                                        out.println("OK");
                                        out.println(listaSelezionata.get(nominativoNum - 1));
                                    }
                                }
                            }
                        }
                    }

                    client.close();
                    System.out.println("Client disconnesso.");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
