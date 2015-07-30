package com.romullogirardi.huntersharklotofacilandroid.model.network;

public interface Translator {

	public void receiveAck(String receiver);

	public void receiveNack(String receiver);

	public void receiveMessage();
}