package com.romullogirardi.huntersharklotofacilandroid.model;

import android.annotation.SuppressLint;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("SimpleDateFormat")
public class Contest implements Serializable { 

	//SERIALIZATION ID
	private static final long serialVersionUID = 9091844002396266810L;

	//ATTRIBUTES
	private int id;
	private Calendar date;
	private String place;
	private int[] numbers = null;
	private float reward15points;
	private float reward14points;
	private float reward13points;
	private float reward12points;
	private float reward11points;
	private ArrayList<Game> recommendedGames = new ArrayList<>();
	private float recommendedInvestment = 0;
	private float recommendedReward = 0;
	private boolean bet = false;
	private float betInvestment = 0;
	private float betReward = 0;
	
	//CONSTRUCTORS
	public Contest(int id, Calendar date, String place, int[] numbers,
			float reward15points, float reward14points, float reward13points,
			float reward12points, float reward11points, boolean bet) {
		this.id = id;
		this.date = date;
		this.place = place;
		this.numbers = numbers;
		this.reward15points = reward15points;
		this.reward14points = reward14points;
		this.reward13points = reward13points;
		this.reward12points = reward12points;
		this.reward11points = reward11points;
		this.bet = bet;
	}

	public Contest(int id, ArrayList<Game> recommendedGames) {
		this.id = id;
		this.recommendedGames = recommendedGames;
	}

	//GETTERS AND SETTERS
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public int[] getNumbers() {
		return numbers;
	}

	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	} 


	public float getReward15points() {
		return reward15points;
	}

	public void setReward15points(float reward15points) {
		this.reward15points = reward15points;
	}

	public float getReward14points() {
		return reward14points;
	}

	public void setReward14points(float reward14points) {
		this.reward14points = reward14points;
	}

	public float getReward13points() {
		return reward13points;
	}

	public void setReward13points(float reward13points) {
		this.reward13points = reward13points;
	}

	public float getReward12points() {
		return reward12points;
	}

	public void setReward12points(float reward12points) {
		this.reward12points = reward12points;
	}

	public float getReward11points() {
		return reward11points;
	}

	public void setReward11points(float reward11points) {
		this.reward11points = reward11points;
	}

	public ArrayList<Game> getRecommendedGames() {
		return recommendedGames;
	}

	public void setRecommendedGames(ArrayList<Game> recommendedGames) {
		this.recommendedGames = recommendedGames;
	}

	public float getRecommendedInvestment() {
		return recommendedInvestment;
	}

	public void setRecommendedInvestment(float recommendedInvestment) {
		this.recommendedInvestment = recommendedInvestment;
	}

	public float getRecommendedReward() {
		return recommendedReward;
	}

	public void setRecommendedReward(float recommendedReward) {
		this.recommendedReward = recommendedReward;
	}
	
	public boolean isBet() {
		return bet;
	}

	public void setBet(boolean bet) {
		this.bet = bet;
	}

	public float getBetInvestment() {
		return betInvestment;
	}

	public void setBetInvestment(float betInvestment) {
		this.betInvestment = betInvestment;
	}

	public float getBetReward() {
		return betReward;
	}

	public void setBetReward(float betReward) {
		this.betReward = betReward;
	}

	//OTHER METHODS
	public Game getBestGame() {
		Game bestGame = null;
		int points = 0;
		if(!recommendedGames.isEmpty()) {
			for(Game game : recommendedGames) {
				if(points < game.getPoints()) {
					points = game.getPoints();
					bestGame = game;
				}
			}
		}
		return bestGame;
	}
	
	public String toString() {
		Game bestGame = getBestGame();
		DateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return String.valueOf(id) + "\t" + mDateFormat.format(date.getTime()) + "\t" + numbers[0] + " ... " + numbers[14] + "\t" + place + "\t" + reward15points + "\t" + reward14points + "\t" + reward13points + "\t" + reward12points + "\t" + reward11points + "\t" +
				((bestGame == null) ? "Sem jogos" : bestGame.getPoints() + " pontos - R$ " + bestGame.getReward()) + "\t" + 
				"Saldo total: " + String.valueOf(recommendedReward - recommendedInvestment);
	}
}