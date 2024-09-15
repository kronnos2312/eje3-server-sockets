package com.eje3.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Randomizer {
	
	 
	private static Randomizer instance; 
		
	private List<Integer> currentGenerate = new ArrayList<>();
	private Integer current;
	private Integer size;
	
	public Integer getCurrent() {
		size = 100;
		if( currentGenerate.size() == 3) currentGenerate.clear();
		Random random = new Random();
		current = random.nextInt(size); 
		currentGenerate.add(current);
		return current;
	}
	
	public Integer getCurrent(Integer size) {
		
		if( currentGenerate.size() == 3) currentGenerate.clear();
		Random random = new Random();
		current = random.nextInt(size); 
		currentGenerate.add(current);
		return current;
	}
	
	public List<Integer> getLastResults(){
		return this.currentGenerate;
	}
	
	public static Randomizer getInstance() {
		if(instance==null) {
			instance = new Randomizer(); 
		}
		return instance;
	}
	
}
