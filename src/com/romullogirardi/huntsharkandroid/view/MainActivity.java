package com.romullogirardi.huntsharkandroid.view;

import com.romullogirardi.huntsharkandroid.R;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	//MÉTODOS DO CICLO DE VIDA DA Activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}
