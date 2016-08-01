package com.romullogirardi.huntersharklotofacilandroid.view;

import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.romullogirardi.huntersharklotofacilandroid.R;
import com.romullogirardi.huntersharklotofacilandroid.model.Contest;
import com.romullogirardi.huntersharklotofacilandroid.model.ContestManager;
import com.romullogirardi.huntersharklotofacilandroid.model.ContestsAdapter;
import com.romullogirardi.huntersharklotofacilandroid.model.Game;
import com.romullogirardi.huntersharklotofacilandroid.model.GameStrategy;
import com.romullogirardi.huntersharklotofacilandroid.model.GlobalReferences;
import com.romullogirardi.huntersharklotofacilandroid.model.network.AckReceiverThread;
import com.romullogirardi.huntersharklotofacilandroid.model.network.Communicator;
import com.romullogirardi.huntersharklotofacilandroid.model.network.CommunicatorListener;
import com.romullogirardi.huntersharklotofacilandroid.model.network.MessageReceiverThread;

public class MainActivity extends Activity implements OnItemClickListener, CommunicatorListener {
	
	//UI ELEMENTS
	private ListView contestsListView;
	private TextView rewardTextView;
	private TextView investmentTextView;
	private TextView profitTextView;
	private TextView emptyContestsTextView;
	
	//SOUND ELEMENTS
	//AudioManager
	private AudioManager mAudioManager;
	//SoundPool
	private SoundPool mSoundPool;
	//SoundIDs
	private int shortSharkAttackSoundID;
	private int sharkAttackSoundID;
	//Audio volume
	private float mStreamVolume;
	
	//ACTIVITY LIFECYCLE METHODS
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GlobalReferences.applicationContext = this;

		//Setting application
		int[] indexes1 = {2, 3, 4, 5, 7, 10, 12, 13, 16, 17, 18, 19, 21, 23, 24};
//		int[] indexes2 = {2, 5, 6, 7, 8, 9, 10, 14, 15, 16, 18, 19, 20, 21, 23};
		int[] indexes3 = {0, 2, 3, 4, 7, 11, 12, 13, 14, 16, 17, 18, 20, 21, 22};
		Vector<GameStrategy> gameStrategies = new Vector<GameStrategy>();
		gameStrategies.add(new GameStrategy(indexes1));
//		gameStrategies.add(new GameStrategy(indexes2));
		gameStrategies.add(new GameStrategy(indexes3));
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
		
		//Setting Communicator
		Communicator.setInstance(new Communicator(this));
		
		//Loading listView with contests
		loadContestsListView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//Manage sound using AudioManager.STREAM_MUSIC as stream type
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		mStreamVolume = (float) mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		//Make a new SoundPool, allowing up to 10 streams
		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

		//Set a SoundPool OnLoadCompletedListener
		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				if (status == 0) {
				}
			}
		});

		//Load the sounds from res/raw (.wav)
		shortSharkAttackSoundID = mSoundPool.load(this, R.raw.short_shark_attack, 1);
		sharkAttackSoundID = mSoundPool.load(this, R.raw.shark_attack, 1);

		mAudioManager.setSpeakerphoneOn(true);
		mAudioManager.loadSoundEffects();

		loadContestsListView();
	}	
		
	@Override
	protected void onPause() {

		//Release all SoundPool resources
		if (mSoundPool != null) {
			mSoundPool.unload(shortSharkAttackSoundID);
			mSoundPool.unload(sharkAttackSoundID);
			mSoundPool.release();
			mSoundPool = null;
		}

		mAudioManager.setSpeakerphoneOn(false);
		mAudioManager.unloadSoundEffects();

		super.onPause();
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
	
	//CommunicationListener METHODS
	@Override
	public void notifyIncomeMessage(Object object) {

		if (object != null) {
			new MessageReceiverThread(object).start();
		}
	}

	@Override
	public void notifySentMessage(Object object, String receiverIP, boolean sent) {

		if (object != null) {
			new AckReceiverThread(object, receiverIP, sent).start();
		}
	}	
	
	//OTHER METHODS
	public void loadContestsListView() {
		if (!ContestManager.getInstance().getContestsToShow().isEmpty()) {
			contestsListView.setAdapter(new ContestsAdapter(this, ContestManager.getInstance().getContestsToShow()));
			rewardTextView.setText(getResources().getString(R.string.recompensa_dois_pontos) + String.format("%.2f", ContestManager.getInstance().getBetReward()));
			investmentTextView.setText(getResources().getString(R.string.investimento_dois_pontos) + String.format("%.2f", ContestManager.getInstance().getBetInvestment()));
			profitTextView.setText(getResources().getString(R.string.lucro_dois_pontos) + String.format("%.2f", ContestManager.getInstance().getBetReward() - ContestManager.getInstance().getBetInvestment()));
			if((ContestManager.getInstance().getBetReward() - ContestManager.getInstance().getBetInvestment()) < 0) {
				profitTextView.setBackgroundColor(getResources().getColor(R.color.orange));
			}
			else {
				profitTextView.setBackgroundColor(getResources().getColor(R.color.green));
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
	
	public void notifyReward(int contestID) {
		Contest contestRewarded = ContestManager.getInstance().getContest(contestID);
		if(contestRewarded != null && contestRewarded.isBet()) {
			float reward = 0;
			for(Game game : contestRewarded.getRecommendedGames()) {
				reward += game.getReward();
			}
			if(reward > 0) {
				int soundID;
				if(reward > 80) {
					soundID = sharkAttackSoundID;
				}
				else {
					soundID = shortSharkAttackSoundID;
				}
				//Notify reward
				mSoundPool.play(soundID, mStreamVolume, mStreamVolume, 1, 0, 1.0f);
				Toast.makeText(this, "R$ " + reward + " ganhos no concurso " + contestRewarded.getId(), Toast.LENGTH_LONG).show();
			}
		}
	}
}
