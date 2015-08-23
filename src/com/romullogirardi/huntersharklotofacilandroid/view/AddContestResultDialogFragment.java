package com.romullogirardi.huntersharklotofacilandroid.view;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.romullogirardi.huntersharklotofacilandroid.R;
import com.romullogirardi.huntersharklotofacilandroid.model.Constants;
import com.romullogirardi.huntersharklotofacilandroid.model.Contest;
import com.romullogirardi.huntersharklotofacilandroid.model.ContestManager;

@SuppressLint("InflateParams")
public class AddContestResultDialogFragment extends DialogFragment{

	//UI ELEMENTS
	private View view;
	private DatePicker datePicker;
	private EditText cityEditText;
	private EditText reward15pointsEditText;
	private EditText reward14pointsEditText;
	private EditText reward13pointsEditText;
	private EditText reward12pointsEditText;
	private EditText reward11pointsEditText;
	
	//VARIABLES
	private String contestID;
	private Contest contestResult = null;
	private ToggleButton[] toggleButtons = new ToggleButton[25];
	
	//CONSTRUCTORS
	public AddContestResultDialogFragment(String contestID) {
		this.contestID = contestID;
	}

	public AddContestResultDialogFragment(Contest contestResult) {
		this.contestResult = contestResult;
		this.contestID = String.valueOf(contestResult.getId());
	}
	
	//DIALOGFRAGMENT LIFECYCLE METHODS
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		//Initializing UI elements
		LayoutInflater mInflater = getActivity().getLayoutInflater();
		view = mInflater.inflate(R.layout.dialog_add_contest_result, null);
		toggleButtons[0] = (ToggleButton) view.findViewById(R.id.toogle_button1);
		toggleButtons[1] = (ToggleButton) view.findViewById(R.id.toogle_button2);
		toggleButtons[2] = (ToggleButton) view.findViewById(R.id.toogle_button3);
		toggleButtons[3] = (ToggleButton) view.findViewById(R.id.toogle_button4);
		toggleButtons[4] = (ToggleButton) view.findViewById(R.id.toogle_button5);
		toggleButtons[5] = (ToggleButton) view.findViewById(R.id.toogle_button6);
		toggleButtons[6] = (ToggleButton) view.findViewById(R.id.toogle_button7);
		toggleButtons[7] = (ToggleButton) view.findViewById(R.id.toogle_button8);
		toggleButtons[8] = (ToggleButton) view.findViewById(R.id.toogle_button9);
		toggleButtons[9] = (ToggleButton) view.findViewById(R.id.toogle_button10);
		toggleButtons[10] = (ToggleButton) view.findViewById(R.id.toogle_button11);
		toggleButtons[11] = (ToggleButton) view.findViewById(R.id.toogle_button12);
		toggleButtons[12] = (ToggleButton) view.findViewById(R.id.toogle_button13);
		toggleButtons[13] = (ToggleButton) view.findViewById(R.id.toogle_button14);
		toggleButtons[14] = (ToggleButton) view.findViewById(R.id.toogle_button15);
		toggleButtons[15] = (ToggleButton) view.findViewById(R.id.toogle_button16);
		toggleButtons[16] = (ToggleButton) view.findViewById(R.id.toogle_button17);
		toggleButtons[17] = (ToggleButton) view.findViewById(R.id.toogle_button18);
		toggleButtons[18] = (ToggleButton) view.findViewById(R.id.toogle_button19);
		toggleButtons[19] = (ToggleButton) view.findViewById(R.id.toogle_button20);
		toggleButtons[20] = (ToggleButton) view.findViewById(R.id.toogle_button21);
		toggleButtons[21] = (ToggleButton) view.findViewById(R.id.toogle_button22);
		toggleButtons[22] = (ToggleButton) view.findViewById(R.id.toogle_button23);
		toggleButtons[23] = (ToggleButton) view.findViewById(R.id.toogle_button24);
		toggleButtons[24] = (ToggleButton) view.findViewById(R.id.toogle_button25);
		datePicker = (DatePicker) view.findViewById(R.id.date_picker);
		datePicker.setCalendarViewShown(false);
		cityEditText = (EditText) view.findViewById(R.id.edit_text_city);
		reward15pointsEditText = (EditText) view.findViewById(R.id.edit_text_reward_15_points);
		reward14pointsEditText = (EditText) view.findViewById(R.id.edit_text_reward_14_points);
		reward13pointsEditText = (EditText) view.findViewById(R.id.edit_text_reward_13_points);
		reward12pointsEditText = (EditText) view.findViewById(R.id.edit_text_reward_12_points);
		reward11pointsEditText = (EditText) view.findViewById(R.id.edit_text_reward_11_points);
		if(contestResult == null) {
			reward13pointsEditText.setText(String.format("%.2f", Constants.DEFAULT_REWARD_13_POINTS).replace(",", "."));
			reward12pointsEditText.setText(String.format("%.2f", Constants.DEFAULT_REWARD_12_POINTS).replace(",", "."));
			reward11pointsEditText.setText(String.format("%.2f", Constants.DEFAULT_REWARD_11_POINTS).replace(",", "."));
			Toast.makeText(getActivity(), "Não foi possível carregar os dados deste concurso. Insira-os manualmente", Toast.LENGTH_LONG).show();
		}
		else {
			for(int number : contestResult.getNumbers()) {
				toggleButtons[number - 1].setChecked(true); 
			}
			datePicker.updateDate(contestResult.getDate().get(Calendar.YEAR), contestResult.getDate().get(Calendar.MONTH), contestResult.getDate().get(Calendar.DAY_OF_MONTH));
			cityEditText.setText(contestResult.getPlace());
			reward15pointsEditText.setText(String.format("%.2f", contestResult.getReward15points()).replace(",", "."));
			reward14pointsEditText.setText(String.format("%.2f", contestResult.getReward14points()).replace(",", "."));
			reward13pointsEditText.setText(String.format("%.2f", contestResult.getReward13points()).replace(",", "."));
			reward12pointsEditText.setText(String.format("%.2f", contestResult.getReward12points()).replace(",", "."));
			reward11pointsEditText.setText(String.format("%.2f", contestResult.getReward11points()).replace(",", "."));
		}
		
		//Building the dialog
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		dialogBuilder.setView(view);
		dialogBuilder.setTitle(getActivity().getResources().getString(R.string.resultados_do_concurso) + contestID);

		dialogBuilder.setNegativeButton(getActivity().getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		dialogBuilder.setPositiveButton(getActivity().getResources().getString(R.string.salvar), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				addContestResults();
				((MainActivity) getActivity()).notifyReward(Integer.parseInt(contestID));
				((MainActivity) getActivity()).loadContestsListView();
				dialog.dismiss();
			}
		});
		
		//Creating the dialog
		return dialogBuilder.create();
	}
	
	private void addContestResults() {
		
		int numbers[] = new int[15];
		int numberIndex = 0;
		for(int index = 0; index < toggleButtons.length; index++) {
			if(toggleButtons[index].isChecked()) {
				numbers[numberIndex] = index + 1;
				numberIndex++;
			}
		}
		
		Calendar date = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
		String city = cityEditText.getText().toString();
		float reward15points = Float.parseFloat(reward15pointsEditText.getText().toString());
		float reward14points = Float.parseFloat(reward14pointsEditText.getText().toString());
		float reward13points = Float.parseFloat(reward13pointsEditText.getText().toString());
		float reward12points = Float.parseFloat(reward12pointsEditText.getText().toString());
		float reward11points = Float.parseFloat(reward11pointsEditText.getText().toString());
		
		Contest contestResults = new Contest(Integer.parseInt(contestID), date, city, numbers, reward15points, reward14points, reward13points, reward12points, reward11points);
		contestResults.setBet(ContestManager.getInstance().getContestsToShow().lastElement().isBet());
		ContestManager.getInstance().computeLastContest(contestResults);
	}
}