package com.eje3.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eje3.main.Eje3Contans;
import com.eje3.tools.Randomizer;


public class RandomProcess extends Thread{
	
	private Socket clientSocket;
	
	private Integer currentValue;
	protected List<Map<String, String>> clientValues = new ArrayList<>();
	private boolean status = true;
	private int turn = 0;
	
	
	public RandomProcess(Socket socket) {
        this.clientSocket = socket;
    }
	
		
	public Integer getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Integer currentValue) {
		this.currentValue = currentValue;
	}
	
	@Override
	public void run() {
		
		try (BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
	             PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true)) {

	            String input;
	            while ((input = in.readLine()) != null) {
	            	turn ++;
	                System.out.println("Mensaje del cliente: " + input);
	                Integer current = casterStrInt(input);
	                Map<String, String> context = new HashMap<String, String>();
	                context.put("userV", input);
	                	                
	                Integer systemValue = Randomizer.getInstance().getCurrent(10);
	                context.put("sysV", systemValue.toString());
	                StringBuilder messageContext = new StringBuilder();
	                
	                if(current == null) {
	                	messageContext.append(Eje3Contans.messageErrorInput);
	                	context.put("result", "ERR");
	                }else if(systemValue.equals(current)) {
	                	messageContext.append(Eje3Contans.messageYouWinTurn);
	                	context.put("result", "t");
	                	  for (Map<String, String> context_ : clientValues) {
	                		  context_.put("evaluate", "ok");
	              	    }
	                }else if(!systemValue.equals(current)) {
	                	context.put("result", "f");				
	                	messageContext.append(String.format(Eje3Contans.messageYouLostTurn, systemValue));
	                }
	                
	                
	                clientValues.add(context);
	                if(getFailTurn()) {
	                	messageContext.append(Eje3Contans.messageYouLost);
	                }
	                
	                
	                
	                if(isGameEnd(input) ) {
	                	messageContext = new StringBuilder();
	                	messageContext.append("Juego Finalizado.\t");
	                	int okTurn = 0;
	                	int errTurn = 0;
                		for(Map<String, String> context_: clientValues) {
                			if(!context_.get("result").equals("ERR")) {
	                			if (context_.get("result").equals("t")) okTurn++;
	                			if (context_.get("result").equals("f")) errTurn++;
                			}
        				}
                		
                		messageContext.append(String.format(Eje3Contans.messageTotalLostTurn, errTurn)).append("\t")
                		.append(String.format(Eje3Contans.messageTotalWinTurn, okTurn));
                		out.println(messageContext.toString());
                		this.clientSocket.close();
	        			
	                }
	                out.println(messageContext.toString());
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	            	this.clientSocket.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	}
	
	private Integer casterStrInt(String valueUndecode) {
		try {
			return Integer.decode(valueUndecode.trim());
		} catch (Exception e) {
			System.out.print("Error con el valor ingresado: \n"+e.getMessage());
			return null;
		}
	}
	
	private boolean isGameEnd(String key) {
		return key.equals("terminar");
	}
	
	private boolean getFailTurn() {
	    int turnErr = 0;
	    for (Map<String, String> context_ : clientValues) {
	        if (!"ERR".equals(context_.get("result"))  && !context_.containsKey("evaluate")) {
	            if ("f".equals(context_.get("result"))) {
	            	turnErr++;
	            }
	            if (turn == 3) {   
	            	turn = 0;
	            	context_.put("evaluate", "ok");
	            }
	        }

	        if (turnErr == 3) {
	            return true;
	        }
	    }
	    
	    return false;
	}
	
}
