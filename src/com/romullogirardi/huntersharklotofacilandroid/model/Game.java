package com.romullogirardi.huntersharklotofacilandroid.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

	//SERIALIZATION ID
	private static final long serialVersionUID = -611436311818024239L;
	
	//ATTRIBUTES
	private ArrayList<Integer> numbers = new ArrayList<Integer>();
	private int points = -1;
	private float investment = Constants.GAME_PRIZE;
	private float reward = 0;
	
	//CONSTRUCTOR
	public Game(ArrayList<Integer> numbers) {
		this.numbers = numbers;
	}
	
	//GETTERS AND SETTERS
	public ArrayList<Integer> getNumbers() {
		return numbers;
	}
	
	public void setNumbers(ArrayList<Integer> numbers) {
		this.numbers = numbers;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public float getInvestment() {
		return investment;
	}
	
	public float getReward() {
		return reward;
	}
	
	public void setReward(float reward) {
		this.reward = reward;
	}
}