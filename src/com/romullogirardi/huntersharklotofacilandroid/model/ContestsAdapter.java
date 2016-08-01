package com.romullogirardi.huntersharklotofacilandroid.model;

import java.util.Iterator;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.romullogirardi.huntersharklotofacil.protocol.Games;
import com.romullogirardi.huntersharklotofacilandroid.R;
import com.romullogirardi.huntersharklotofacilandroid.model.network.Communicator;

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
			viewHolder.game1TextView.setBackgroundColor((contest.getRecommendedGames().get(0).getPoints() >= 11) ? context.getResources().getColor(R.color.green) : context.getResources().getColor(android.R.color.white));
			viewHolder.game1TextView.setTextColor((contest.getRecommendedGames().get(0).getPoints() >= 11) ? context.getResources().getColor(android.R.color.white) : context.getResources().getColor(android.R.color.black));
			viewHolder.game1TextView.setTypeface(null, (contest.getRecommendedGames().get(0).getPoints() >= 11) ? Typeface.BOLD : Typeface.NORMAL);
			viewHolder.game2TextView.setText(String.valueOf(contest.getRecommendedGames().get(1).getPoints()));
			viewHolder.game2TextView.setBackgroundColor((contest.getRecommendedGames().get(1).getPoints() >= 11) ? context.getResources().getColor(R.color.green) : context.getResources().getColor(android.R.color.white));
			viewHolder.game2TextView.setTextColor((contest.getRecommendedGames().get(1).getPoints() >= 11) ? context.getResources().getColor(android.R.color.white) : context.getResources().getColor(android.R.color.black));
			viewHolder.game2TextView.setTypeface(null, (contest.getRecommendedGames().get(1).getPoints() >= 11) ? Typeface.BOLD : Typeface.NORMAL);
		}
		else {
			viewHolder.game1TextView.setText(" ---- ");
			viewHolder.game1TextView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
			viewHolder.game2TextView.setText(" ---- ");
			viewHolder.game2TextView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
		}
		
		viewHolder.printImageView.setEnabled(contest.getNumbers() == null);
		viewHolder.printImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
				dialogBuilder.setTitle("Imprimir");
				dialogBuilder.setMessage("Enviar os jogos do concurso " + contest.getId() + " para impressão?");
				
				dialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				dialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int index = 0;
						int[][] gamesNumbers = new int[Constants.GAMES_QUANTITY][15];
						for(Game game : contest.getRecommendedGames()) {
							int[] numbers = new int[game.getNumbers().size()];
						    Iterator<Integer> iterator = game.getNumbers().iterator();
						    for (int i = 0; i < numbers.length; i++) {
						        numbers[i] = iterator.next().intValue();
						    }
							gamesNumbers[index++] = numbers;
						}
						Communicator.getInstance().sendMessage(new Games(gamesNumbers), Constants.PC_IP_ADDRESS);
						dialog.dismiss();
					}
				});
				
				dialogBuilder.create().show();
			}
		});
		
		viewHolder.betImageView.setImageDrawable((contest.isBet()) ? 
				context.getResources().getDrawable(R.drawable.ic_bet) :
					context.getResources().getDrawable(R.drawable.ic_not_bet));
		viewHolder.betImageView.setEnabled(contest.getNumbers() == null && !contest.isBet());
		viewHolder.betImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
				dialogBuilder.setTitle("Apostar");
				dialogBuilder.setMessage("Os jogos do concurso " + contest.getId() + " foram apostados?");
				
				dialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				dialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						contest.setBet(true);
						ContestsAdapter.this.notifyDataSetChanged();
						dialog.dismiss();
					}
				});

				dialogBuilder.create().show();
			}
		});
		
		viewHolder.resultsImageView.setEnabled(contest.getNumbers() == null);
		viewHolder.resultsImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(!contest.isBet()) {
					AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GlobalReferences.applicationContext);
					dialogBuilder.setTitle("Jogo não apostado");
					dialogBuilder.setMessage("Este jogo ainda não consta como apostado. Tem certeza que deseja prosseguir com o carregamento dos resultados do concurso?");
	
					dialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
	
					dialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							new HTMLParserFromURL().execute(String.valueOf(contest.getId()));
						}
					});
	
					dialogBuilder.create().show();
				}
				else {
					new HTMLParserFromURL().execute(String.valueOf(contest.getId()));
				}
			}
		});
		
		
		
		return convertView;
	}

	//CLASSE INTERNA ViewHolder 
	private class ViewHolder {
		TextView idTextView;
		TextView game1TextView;
		TextView game2TextView;
		ImageView printImageView;
		ImageView betImageView;
		ImageView resultsImageView;
	}
}