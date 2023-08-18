package org.example;

import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 1521;
    public static void main(String[] args) throws IOException {
        final int PORT = 1521;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected to client: " + clientSocket.getRemoteSocketAddress());

                Thread sendThread = new Thread(() -> {
                    try (
                            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in))
                    ) {
                        String userInput;
                        Thread.sleep(50);
                        while ((userInput = userInputReader.readLine()) != null) {
                            writer.println(userInput);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                sendThread.start();

                Thread receiveThread = new Thread(() -> {
                    try (
                            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                    ) {
                        Thread.sleep(50);
                        String line;
                        while ((line = reader.readLine()) != null) {
                            saveMessageToFile("server.txt", line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                receiveThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void saveMessageToFile(String fileName, String message) {
        // Implement saving the message to the file
        try {
            FileWriter fileWriter = new FileWriter(fileName, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(message);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
