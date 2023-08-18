package org.example;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost";
        final int SERVER_PORT = 1521;

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to server at " + socket.getRemoteSocketAddress());
            while (true) {
                Thread sendThread = new Thread(() -> {

                    try {
                        Thread.sleep(50);
                        String userInput;
                        while ((userInput = userInputReader.readLine()).length() >= 1) {
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
                    try {
                        Thread.sleep(50);
                        String line;
                        while ((line = reader.readLine()) != null) {
                            saveMessageToFile("client.txt", line);
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