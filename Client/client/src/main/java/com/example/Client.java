package com.example;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PORT = 3000;
        boolean continua = true; // variabile di controllo del ciclo

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in))) {

            // messaggio di benvenuto
            String welcome = serverIn.readLine();
            if (welcome != null) {
                System.out.println("Server: " + welcome);
            } else {
                continua = false;
            }

            while (continua) {
                System.out.print("Numero lista (o '!' per uscire): ");
                String lista = userIn.readLine();
                if (lista != null) {
                    serverOut.println(lista);

                    if (lista.equals("!")) {
                        continua = false;
                    } else {
                        String resp = serverIn.readLine();
                        if (resp != null) {
                            System.out.println("Server: " + resp);

                            if (resp.equals("OK")) {
                                System.out.print("Numero nominativo (o '!' per uscire): ");
                                String nominativo = userIn.readLine();
                                if (nominativo != null) {
                                    serverOut.println(nominativo);

                                    if (nominativo.equals("!")) {
                                        continua = false;
                                    } else {
                                        String resp2 = serverIn.readLine();
                                        if (resp2 != null) {
                                            System.out.println("Server: " + resp2);

                                            if (resp2.equals("OK")) {
                                                String nome = serverIn.readLine();
                                                if (nome != null) {
                                                    System.out.println("Nominativo: " + nome);
                                                } else {
                                                    continua = false;
                                                }
                                            }
                                        } else {
                                            continua = false;
                                        }
                                    }
                                } else {
                                    continua = false;
                                }
                            }
                        } else {
                            continua = false;
                        }
                    }
                } else {
                    continua = false;
                }
            }

            System.out.println("Connessione chiusa.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
