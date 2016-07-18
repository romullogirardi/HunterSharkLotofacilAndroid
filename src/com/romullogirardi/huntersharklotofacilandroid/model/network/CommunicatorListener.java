package com.romullogirardi.huntersharklotofacilandroid.model.network;

public interface CommunicatorListener {

	public void notifyIncomeMessage(Object object);

	public void notifySentMessage(Object object, String receiverIP, boolean sent);
}