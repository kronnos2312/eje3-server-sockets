package com.eje3client.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {
	
    public static void main(String[] args) {
        try (Socket socket = new Socket(Eje3Contans.currentHost, Eje3Contans.portRandom);
        		
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String userInput;
            System.out.print("Conectado al servidor. Ingresa un mensaje:");
            
            while ((userInput = consoleReader.readLine()) != null) {
                out.println(userInput);
                System.out.println("Respuesta del servidor: " + in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
