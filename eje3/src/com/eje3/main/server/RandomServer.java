package com.eje3.main.server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.eje3.main.Eje3Contans;
import com.eje3.process.RandomProcess;

public class RandomServer {
	public RandomServer() {
		 ExecutorService threadPool = Executors.newFixedThreadPool(10); 
	        try (ServerSocket serverSocket = new ServerSocket(Eje3Contans.portRandom)) {
	            System.out.println("Servidor iniciado en el puerto " + Eje3Contans.portRandom);
	            
	            while (true) {
	                Socket clientSocket = serverSocket.accept(); 
	                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
	                threadPool.execute(new RandomProcess(clientSocket)); 
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
}
