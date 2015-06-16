package com.romullogirardi.huntersharklotofacilandroid.view;

import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.romullogirardi.huntersharklotofacilandroid.R;
import com.romullogirardi.huntersharklotofacilandroid.model.Contest;
import com.romullogirardi.huntersharklotofacilandroid.model.ContestManager;
import com.romullogirardi.huntersharklotofacilandroid.model.ContestsAdapter;
import com.romullogirardi.huntersharklotofacilandroid.model.GameStrategy;
import com.romullogirardi.huntersharklotofacilandroid.model.GlobalReferences;

public class MainActivity extends Activity implements OnItemClickListener {
	
	//UI ELEMENTS
	private ListView contestsListView;
	private TextView rewardTextView;
	private TextView investmentTextView;
	private TextView profitTextView;
	private TextView emptyContestsTextView;

	//ACTIVITY LIFECYCLE METHODS
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GlobalReferences.applicationContext = this;

		//Setting application
		int[] indexes1 = {2, 3, 4, 5, 7, 10, 12, 13, 16, 17, 18, 19, 21, 23, 24};
		int[] indexes2 = {2, 5, 6, 7, 8, 9, 10, 14, 15, 16, 18, 19, 20, 21, 23};
		int[] indexes3 = {0, 2, 3, 4, 7, 11, 12, 13, 14, 16, 17, 18, 20, 21, 22};
		int[] indexes4 = {0, 3, 5, 6, 8, 11, 12, 13, 14, 16, 17, 18, 20, 21, 23} ;
		Vector<GameStrategy> gameStrategies = new Vector<GameStrategy>();
		gameStrategies.add(new GameStrategy(indexes1));
		gameStrategies.add(new GameStrategy(indexes2));
		gameStrategies.add(new GameStrategy(indexes3));
		gameStrategies.add(new GameStrategy(indexes4));
		ContestManager.getInstance().setGameStrategies(gameStrategies);
		ContestManager.getInstance().setContestsPartition(30);

		//Reading application persistence file
		ContestManager.getInstance().readFile();
		ContestManager.getInstance().setSaveModifications(true);

		//Initializing UI elements
		contestsListView = (ListView) findViewById(R.id.list_view_contests);
		rewardTextView = (TextView) findViewById(R.id.text_view_reward);
		investmentTextView = (TextView) findViewById(R.id.text_view_investment);
		profitTextView = (TextView) findViewById(R.id.text_view_profit);
		emptyContestsTextView = (TextView) findViewById(R.id.text_view_empty_contests);

		//Setting UI elements listeners
		contestsListView.setOnItemClickListener(this);
		
		//Loading listView with contests
		loadContestsListView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadContestsListView();
	}	
		
	@Override
	public void onBackPressed() {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(getResources().getString(R.string.sair));
		dialogBuilder.setMessage(getResources().getString(R.string.sair_pergunta));

		dialogBuilder.setNegativeButton(getResources().getString(R.string.nao), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		dialogBuilder.setPositiveButton(getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ContestManager.getInstance().saveFile();
				dialog.dismiss();
				finish();
			}
		});
		
		dialogBuilder.create().show();
	}

	//UI ELEMENTS LISTENERS METHODS
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		//Getting the contest selected
		Contest contestSelected = (Contest) ContestManager.getInstance().getContestsToShow().get(position);
		
		//Showing contest details
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Concurso " + contestSelected.getId());
		dialogBuilder.setMessage(ContestManager.getInstance().getContestDetails(contestSelected));
		dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialogBuilder.create().show();
	}
	
	//OTHER METHODS
	@SuppressWarnings("deprecation")
	public void loadContestsListView() {
		if (!ContestManager.getInstance().getContestsToShow().isEmpty()) {
			contestsListView.setAdapter(new ContestsAdapter(this, ContestManager.getInstance().getContestsToShow()));
			rewardTextView.setText(getResources().getString(R.string.recompensa_dois_pontos) + String.format("%.2f", ContestManager.getInstance().getBetReward()));
			investmentTextView.setText(getResources().getString(R.string.investimento_dois_pontos) + String.format("%.2f", ContestManager.getInstance().getBetInvestment()));
			profitTextView.setText(getResources().getString(R.string.lucro_dois_pontos) + String.format("%.2f", ContestManager.getInstance().getBetReward() - ContestManager.getInstance().getBetInvestment()));
			if((ContestManager.getInstance().getBetReward() - ContestManager.getInstance().getBetInvestment()) < 0) {
				profitTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bg_red));
			}
			else {
				profitTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bg_green));
			}
			contestsListView.setVisibility(View.VISIBLE);
			rewardTextView.setVisibility(View.VISIBLE);
			investmentTextView.setVisibility(View.VISIBLE);
			profitTextView.setVisibility(View.VISIBLE);
			emptyContestsTextView.setVisibility(View.GONE);
		}
		else {
			contestsListView.setVisibility(View.GONE);
			rewardTextView.setVisibility(View.GONE);
			investmentTextView.setVisibility(View.GONE);
			profitTextView.setVisibility(View.GONE);
			emptyContestsTextView.setVisibility(View.VISIBLE);
		}
	}
}