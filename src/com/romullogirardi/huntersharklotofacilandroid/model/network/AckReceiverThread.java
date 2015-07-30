package com.romullogirardi.huntersharklotofacilandroid.model.network;

import com.romullogirardi.huntersharklotofacil.protocol.Games;

public class AckReceiverThread extends Thread {

	//ATRIBUTOS
	private Object object = null;
	private String receiver = "";
	private boolean sent = true;
	
	//CONSTRUTOR
	public AckReceiverThread(Object object, String receiverIP, boolean sent) {
		this.object = object;
		this.receiver = receiverIP;
		this.sent = sent;
	}

	//SOBRESCRITA DO MÉTODO DE Thread
	@Override
	public void run() {
		if (object != null && !receiver.isEmpty()) {
			Translator translator = null;

			//IDENTIFICAR O TIPO DE MENSAGEM RELACIONADA AO ACK OU AO NACK
			if (object instanceof Games) {
				translator = new GamesTranslator(object);
			}

			//Caso de mensagem  válida
			if (translator != null) {
				
				//Tratar recebimento de Ack ou Nack
				if (sent) {
					translator.receiveAck(receiver);
				}
				else {
					translator.receiveNack(receiver);
				}
			}
		}
	}
}