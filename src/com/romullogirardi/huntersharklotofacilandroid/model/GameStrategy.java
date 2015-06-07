package com.romullogirardi.huntersharklotofacilandroid.model;

import java.io.Serializable;
import java.util.ArrayList;

public class GameStrategy implements Comparable<GameStrategy>, Serializable {

	//SERIALIZATION ID
	private static final long serialVersionUID = 6287604035284063629L;
	
	//ATTRIBUTTES
	private ArrayList<Integer> indexes;
	private int frequency15points = 0;
	private int frequency14points = 0;
	private int frequency13points = 0;
	private int frequency12points = 0;
	private int frequency11points = 0;
	private float pointsAverage = 0;
	private float reward = 0;
	
	//CONSTRUCTOR
	public GameStrategy(int[] combinationIndexes) {
		
		//Setting indexes
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int combinationIndex : combinationIndexes) {
			indexes.add(combinationIndex);
		}
		this.indexes = indexes;
	}
	
	//GETTERS AND SETTERS
	public ArrayList<Integer> getIndexes() {
		return indexes;
	}

	public void setIndexes(ArrayList<Integer> indexes) {
		this.indexes = indexes;
	}

	public int getFrequency15points() {
		return frequency15points;
	}

	public void setFrequency15points(int frequency15points) {
		this.frequency15points = frequency15points;
	}

	public int getFrequency14points() {
		return frequency14points;
	}

	public void setFrequency14points(int frequency14points) {
		this.frequency14points = frequency14points;
	}

	public int getFrequency13points() {
		return frequency13points;
	}

	public void setFrequency13points(int frequency13points) {
		this.frequency13points = frequency13points;
	}

	public int getFrequency12points() {
		return frequency12points;
	}

	public void setFrequency12points(int frequency12points) {
		this.frequency12points = frequency12points;
	}

	public int getFrequency11points() {
		return frequency11points;
	}

	public void setFrequency11points(int frequency11points) {
		this.frequency11points = frequency11points;
	}

	public float getPointsAverage() {
		return pointsAverage;
	}

	public void setPointsAverage(float pointsAverage) {
		this.pointsAverage = pointsAverage;
	}
	
	public float getReward() {
		return reward;
	}

	public void setReward(float reward) {
		this.reward = reward;
	}

	//OTHER METHODS
	public float getScore() {
		return (float) ((HTMLParser.DEFAULT_REWARD_15_POINTS * frequency15points + HTMLParser.DEFAULT_REWARD_14_POINTS * frequency14points + HTMLParser.DEFAULT_REWARD_13_POINTS * frequency13points + HTMLParser.DEFAULT_REWARD_12_POINTS * frequency12points + HTMLParser.DEFAULT_REWARD_11_POINTS * frequency11points) / (HTMLParser.DEFAULT_REWARD_15_POINTS + HTMLParser.DEFAULT_REWARD_14_POINTS + HTMLParser.DEFAULT_REWARD_13_POINTS + HTMLParser.DEFAULT_REWARD_12_POINTS + HTMLParser.DEFAULT_REWARD_11_POINTS));
	}
	
	public String toString() {
		String name = "{";
		for(int index = 0; index < indexes.size(); index++) {
			if(index == (indexes.size() - 1)) {
				name += indexes.get(index) + "}";
			}
			else {
				name += indexes.get(index) + ", ";
			}
		}
		return name + " => " + "15(" + frequency15points + "), " + "14(" + frequency14points + "), " + "13(" + frequency13points + "), " + "12(" + frequency12points + "), " + "11(" + frequency11points + ") - Média: " + pointsAverage + " - R$ " + String.format("%.2f", (float) reward) + " - Score: " + String.format("%.10f", (float) getScore()); 
	}

	@Override
	public int compareTo(GameStrategy otherGameStrategy) {
		if(getScore() > otherGameStrategy.getScore()) {
			return -1;
		}
		else if(getScore() < otherGameStrategy.getScore()) {
			return 1;
		}
		else {
			if(reward > otherGameStrategy.getReward()) {
				return -1;
			}
			else if(reward < otherGameStrategy.getReward()) {
				return 1;
			}
			else {
				if(pointsAverage > otherGameStrategy.getPointsAverage()) {
					return -1;
				}
				else if(pointsAverage < otherGameStrategy.getPointsAverage()) {
					return 1;
				}
				else {
					return 0;
				}
			}
		}
	}
}