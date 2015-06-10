package com.romullogirardi.huntersharklotofacilandroid.model;

import java.util.Vector;

import com.romullogirardi.huntersharklotofacilandroid.R;
import com.romullogirardi.huntersharklotofacilandroid.view.AddContestResultDialogFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class ContestsAdapter extends BaseAdapter {
	
	//ATRIBUTOS
	private Activity context;
	private LayoutInflater inflater;
	private Vector<Contest> contests;

	//CONSTRUTOR
	public ContestsAdapter(Activity context, Vector<Contest> contests) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.contests = contests;
	}

	//IMPLEMENTAÇÃO DE MÉTODOS DE BaseAdapter
	@Override
	public int getCount() {
		return contests.size();
	}

	@Override
	public Object getItem(int position) {
		return contests.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.content_list_view_contests, null);
			viewHolder = new ViewHolder();
			viewHolder.idTextView = (TextView) convertView.findViewById(R.id.text_view_contest_id);
			viewHolder.game1TextView = (TextView) convertView.findViewById(R.id.text_view_contest_game1);
			viewHolder.game2TextView = (TextView) convertView.findViewById(R.id.text_view_contest_game2);
			viewHolder.game3TextView = (TextView) convertView.findViewById(R.id.text_view_contest_game3);
			viewHolder.game4TextView = (TextView) convertView.findViewById(R.id.text_view_contest_game4);
			viewHolder.printImageView = (ImageView) convertView.findViewById(R.id.image_view_print);
			viewHolder.betImageView = (ImageView) convertView.findViewById(R.id.image_view_bet);
			viewHolder.resultsImageView = (ImageView) convertView.findViewById(R.id.image_view_results);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Contest contest = contests.get(position);
		viewHolder.idTextView.setText(String.valueOf(contest.getId()));
		if(contest.getNumbers() != null) {
			viewHolder.game1TextView.setText(String.valueOf(contest.getRecommendedGames().get(0).getPoints()));
			viewHolder.game2TextView.setText(String.valueOf(contest.getRecommendedGames().get(1).getPoints()));
			viewHolder.game3TextView.setText(String.valueOf(contest.getRecommendedGames().get(2).getPoints()));
			viewHolder.game4TextView.setText(String.valueOf(contest.getRecommendedGames().get(3).getPoints())); 
		}
		else {
			viewHolder.game1TextView.setText(" ---- ");
			viewHolder.game2TextView.setText(" ---- ");
			viewHolder.game3TextView.setText(" ---- ");
			viewHolder.game4TextView.setText(" ---- ");
		}
		
		viewHolder.printImageView.setEnabled(contest.getNumbers() == null);
		viewHolder.printImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Imprimir", Toast.LENGTH_LONG).show();
			}
		});
		
		viewHolder.betImageView.setEnabled(!contest.isBet());
		viewHolder.betImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Apostar", Toast.LENGTH_LONG).show();
			}
		});
		
		viewHolder.resultsImageView.setEnabled(contest.getNumbers() == null);
		viewHolder.resultsImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AddContestResultDialogFragment dialog = new AddContestResultDialogFragment(String.valueOf(contest.getId()));
				dialog.show(context.getFragmentManager(), dialog.getClass().getSimpleName());
			}
		});
		
		return convertView;
	}

	//CLASSE INTERNA ViewHolder 
	private class ViewHolder {
		TextView idTextView;
		TextView game1TextView;
		TextView game2TextView;
		TextView game3TextView;
		TextView game4TextView;
		ImageView printImageView;
		ImageView betImageView;
		ImageView resultsImageView;
	}
}