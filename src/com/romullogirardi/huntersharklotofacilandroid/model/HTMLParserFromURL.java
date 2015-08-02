package com.romullogirardi.huntersharklotofacilandroid.model;

import java.io.IOException;
import java.util.GregorianCalendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.romullogirardi.huntersharklotofacilandroid.utils.CustomWidgets;
import com.romullogirardi.huntersharklotofacilandroid.view.AddContestResultDialogFragment;
import com.romullogirardi.huntersharklotofacilandroid.view.MainActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class HTMLParserFromURL extends AsyncTask<String, Void, Contest> {
	
	//CONSTANTS
	private static final String URL = "http://www.loterias.caixa.gov.br/wps/portal/loterias/landing/lotofacil";

	//VARIABLES
	String contestIDStr;
	
	//UI ELEMENTS
	ProgressDialog progressDialog;
        
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(GlobalReferences.applicationContext);
        progressDialog.setMessage("Carregando os dados do concurso...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

	@Override
    protected Contest doInBackground(String... args) {

		//Setting contestID
		this.contestIDStr = args[0];
		
		//Parsing HTML file
		Document htmlFile = null;
		try {
			htmlFile = Jsoup.connect(URL).get();
		} catch (IOException e) {
			CustomWidgets.showToast("Falha no carregamento", Toast.LENGTH_LONG);
		}
		
		//Reading contests from HTML file		
		String idDateDescription = htmlFile.select("#resultados").select("h2").select("span").text();
		String idDate = idDateDescription.replace("Concurso ", "");
		String idStr = "";
		int id = -1;
		String dayStr = "";;
		int day = -1;
		String monthStr = "";;
		int month = -1;
		String yearStr = "";;
		int year = -1;
		for(int index = 0; index < idDate.length(); index++) {
			if(index <= 3) {
				idStr += idDate.charAt(index);
			}
			else if(index == 6 || index == 7) {
				dayStr += idDate.charAt(index);
			}
			else if(index == 9 || index == 10) {
				monthStr += idDate.charAt(index);
			}
			else if(index >= 12 && index <= 15) {
				yearStr += idDate.charAt(index);
			}
		}
		id = Integer.parseInt(idStr);
		day = Integer.parseInt(dayStr);
		month = Integer.parseInt(monthStr);
		year = Integer.parseInt(yearStr);
		System.out.println("ID = " + id);
		System.out.println("\nDay = " + day);
		System.out.println("Month = " + month);
		System.out.println("Year = " + year);

		String placeDescription = htmlFile.select(".resultado-loteria").select("p.description").text();
		String place = placeDescription.substring(placeDescription.indexOf("em") + 3).replace(", ", "/");
		System.out.println("\nPlace = " + place);
		
		Element table = htmlFile.select("table").get(0);
		Elements tableRows = table.select("tr");
		int[] numbers = new int[15];
		int numberIndex = 0;
		for(Element tableRow : tableRows) {
			Elements rowElements = tableRow.select("td");			
			for(Element tableColumn : rowElements) {
				numbers[numberIndex++] = Integer.parseInt(tableColumn.text());
			}
		}
		System.out.println("\nNumbers:");
		for(int number : numbers) {
			System.out.print(number + "\t");
		}
		System.out.println();
		
		System.out.println("\nRewards:");
		Elements rewardDescriptions = htmlFile.select(".content-section.section-text.with-box.column-right.no-margin-top").select("p");
		float reward15points = -1;
		float reward14points = -1;
		float reward13points = -1;
		float reward12points = -1;
		float reward11points = -1;
		for(int rewardIndex = 0; rewardIndex <= 4; rewardIndex++) {
			String rewardDescription = rewardDescriptions.get(rewardIndex).text();
			String rewardStr = rewardDescription.substring(rewardDescription.indexOf("$") + 2);
			
			boolean validReward = (rewardStr.isEmpty()) ? false : true;
			switch (rewardIndex) {
			case 0:
				try {
					reward15points = (validReward) ? Float.parseFloat(rewardStr.replace(".", "").replace(",", ".")) : Constants.DEFAULT_REWARD_15_POINTS;
					System.out.println("Reward15points = " + reward15points);
				}
				catch(NumberFormatException e) {
					reward15points = Constants.DEFAULT_REWARD_15_POINTS;
					System.out.println("Reward15points = NÂO HOUVE ACERTADOR");
				}
				break;
			case 1:
				try {
					reward14points = (validReward) ? Float.parseFloat(rewardStr.replace(".", "").replace(",", ".")) : Constants.DEFAULT_REWARD_14_POINTS;
					System.out.println("Reward14points = " + reward14points);
				}
				catch(NumberFormatException e) {
					reward14points = Constants.DEFAULT_REWARD_14_POINTS;
					System.out.println("Reward15points = NÂO HOUVE ACERTADOR");
				}
				break;
			case 2:
				reward13points = (validReward) ? Float.parseFloat(rewardStr.replace(".", "").replace(",", ".")) : Constants.DEFAULT_REWARD_13_POINTS;
				System.out.println("Reward13points = " + reward13points);
				break;
			case 3:
				reward12points = (validReward) ? Float.parseFloat(rewardStr.replace(".", "").replace(",", ".")) : Constants.DEFAULT_REWARD_12_POINTS;
				System.out.println("Reward12points = " + reward12points);
				break;
			case 4:
				reward11points = (validReward) ? Float.parseFloat(rewardStr.replace(".", "").replace(",", ".")) : Constants.DEFAULT_REWARD_11_POINTS;
				System.out.println("Reward11points = " + reward11points);
				break;
			}
		}
		
		if(id == Integer.parseInt(contestIDStr)) {
			return new Contest(id, new GregorianCalendar(year, month - 1, day), place, numbers, reward15points, reward14points, reward13points, reward12points, reward11points);
		}
		else {
			return null;
		}
    }

    protected void onPostExecute(Contest contestResult) {

    	progressDialog.dismiss();
        if(contestResult != null) {
            new AddContestResultDialogFragment(contestResult).show(((MainActivity) GlobalReferences.applicationContext).getFragmentManager(), AddContestResultDialogFragment.class.getSimpleName());
        } else {
            new AddContestResultDialogFragment(contestIDStr).show(((MainActivity) GlobalReferences.applicationContext).getFragmentManager(), AddContestResultDialogFragment.class.getSimpleName());
        }
    }
}