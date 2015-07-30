package com.romullogirardi.huntersharklotofacilandroid.model.network;

import android.widget.Toast;

import com.romullogirardi.huntersharklotofacil.protocol.Games;
import com.romullogirardi.huntersharklotofacilandroid.utils.CustomWidgets;

public class GamesTranslator implements Translator {

	@SuppressWarnings("unused")
	private Games games;

	public GamesTranslator(Object object) {
		games = (Games) object;
	}

	//SOBRESCRITA DOS MÉTODOS DE TRANSLATOR
	@Override
	public void receiveAck(String receiver) {
		CustomWidgets.showToast("Jogos enviados para a impressão", Toast.LENGTH_LONG);
	}

	@Override
	public void receiveNack(String receiver) {
		CustomWidgets.showToast("Jogos não enviados para a impressão", Toast.LENGTH_LONG);
	}

	@Override
	public void receiveMessage() {
		CustomWidgets.showToast("Jogos recebidos", Toast.LENGTH_LONG);
	}
}