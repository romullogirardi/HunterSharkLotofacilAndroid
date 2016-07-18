package com.romullogirardi.huntersharklotofacilandroid.model.network;

import com.romullogirardi.huntersharklotofacil.protocol.Games;

public class MessageReceiverThread extends Thread {

	//ATRIBUTO
	private Object object = null;

	//CONSTRUTOR
	public MessageReceiverThread(Object object) {
		this.object = object;
	}

	//SOBRESCRITA DO MÉTODO DE Thread
	@Override
	public void run() {
		if (object != null) {
			Translator translator = null;

			//IDENTIFICAR O TIPO DE MENSAGEM RECEBIDA
			if (object instanceof Games) {
				translator = new GamesTranslator(object);
			}
			
			//Caso de mensagem  válida
			if (translator != null) {
				//Tratar o recebimento da mensagem 
				translator.receiveMessage();
			}
		}
	}
}