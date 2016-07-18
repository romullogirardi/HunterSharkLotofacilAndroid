package com.romullogirardi.huntersharklotofacil.protocol;

import java.io.Serializable;

public class Games implements Serializable {

	//SERIALIZATION ID
	private static final long serialVersionUID = 2369632703965476687L;

	//ATTRIBUTES
	int[][] games;

	//CONSTRUCTOR
	public Games(int[][] games) {
		this.games = games;
	}

	//GETTERS AND SETTERS
	public int[][] getGames() {
		return games;
	}

	public void setGames(int[][] games) {
		this.games = games;
	}
}