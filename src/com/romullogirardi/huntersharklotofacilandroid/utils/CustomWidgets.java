package com.romullogirardi.huntersharklotofacilandroid.utils;

import android.widget.Toast;

import com.romullogirardi.huntersharklotofacilandroid.model.GlobalReferences;
import com.romullogirardi.huntersharklotofacilandroid.view.MainActivity;

public class CustomWidgets {

	public static void showToast(final String message, final int duration) {
		
		((MainActivity) GlobalReferences.applicationContext).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(GlobalReferences.applicationContext, message, duration).show();
			}
		});
	}
}
