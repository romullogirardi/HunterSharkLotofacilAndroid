package com.romullogirardi.huntersharklotofacilandroid.model.network;

import server.udp.EnumResultado;
import server.udp.ServerReturn;
import server.udp.ServidorUDP2;


public class Communicator implements ServerReturn {

	//ATRIBUTO
	private CommunicatorListener listener;

	//CONSTRUTOR
	public Communicator(CommunicatorListener listener) {
		this.listener = listener;
		ServidorUDP2.getInstance().newServer(NetworkConstants.COMMUNICATOR_SENDER_PORT, this);
	}
	
	//DESTRUTOR
	public void destroy() {
		ServidorUDP2.getInstance().fechar();
	}
	
	//IMPLEMENTAÇÂO COMO SINGLETON
	private static Communicator instance = null;

	public static Communicator getInstance() {
		return instance;
	}

	public static void setInstance(Communicator instance) {
		Communicator.instance = instance;
	}

	//SOBRESCRITA DOS MÉTODOS DE ServerReturn
	@Override
	public void returnMsg_(String receiverIP, Object object, EnumResultado resultado) {
		if (listener != null) {
			boolean sent = true;
			if (resultado == EnumResultado.ERRO) {
				sent = false;
			}
			listener.notifySentMessage(object, receiverIP, sent);
		}
	}

	@Override
	public void incomingMsg(Object object) {
		if (listener != null) {
			listener.notifyIncomeMessage(object);
		}
	}

	@Override
	public void incomingMsg(String ipOrigem, int portaOrigem, Object obj) {
	}

	//MÉTODO DE ENVIO DE MENSAGEM
	public boolean sendMessage(Object object, String receiverIP) {
		ServidorUDP2.getInstance().sendObject(object, receiverIP, NetworkConstants.COMMUNICATOR_RECEIVER_PORT, NetworkConstants.COMMUNICATOR_TRIES, NetworkConstants.COMMUNICATOR_TIMEOUT);
		return true;
	}
}