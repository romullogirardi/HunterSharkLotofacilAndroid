package com.romullogirardi.huntersharklotofacilandroid.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import android.annotation.SuppressLint;

import com.romullogirardi.huntersharklotofacilandroid.utils.FileManipulator;

@SuppressLint("SimpleDateFormat")
public class ContestManager implements Serializable {
	
	//SERIALIZATION ID
	private static final long serialVersionUID = -2791436821648213068L;

	//ATTRIBUTES
	private Vector<Contest> contests = new Vector<Contest>();
	private Vector<NumberFrequency> numbersFrequency;
	private Vector<GameStrategy> gameStrategies;
	private int contestsPartition = -1;
	private boolean saveModifications = false;
	
	//IMPLEMENTING AS A SINGLETON
	private static ContestManager instance = null;
	
	public static void newInstance() {
		instance = null;
	}
	
	public static ContestManager getInstance() {
		if (instance == null) {
			instance = new ContestManager();
		}
		return instance;
	}
	
	//CONSTRUCTORS
	public ContestManager() {
		
		//Initializing numbersFrequency with 0 from 0 to 24
		numbersFrequency = new Vector<NumberFrequency>();
		for(int index = 1; index <= 25; index++) {
			numbersFrequency.add(new NumberFrequency(index, 0));
		}
	}
	
	//GETTERS AND SETTERS
	public void setContestsPartition(int contestsPartition) {
		this.contestsPartition = contestsPartition;
	}

	public void setGameStrategies(Vector<GameStrategy> gameStrategies) {
		this.gameStrategies = gameStrategies;
	}

	public void setSaveModifications(boolean saveModifications) {
		this.saveModifications = saveModifications;
	}

	//METHODS
	public void computeLastContest(Contest lastContestResult) {
		
		//If exists a contest
		if(!contests.isEmpty()) {

			//Checking last recommended games, if it exists
			if(!contests.lastElement().getRecommendedGames().isEmpty()) {
				checkLastGames(lastContestResult);
			}
	
			//Updating last contest
			updateLastContest(lastContestResult);
		}
		
		//Updating numbersFrequency and gameStrategy
		updateControllers(lastContestResult);
		
		//Setting next contest recommended games
		setNextContestRecommendedGames();
		
		//Updating the persistence file
		if(saveModifications) {
			saveFile();
		}
	}
	
	private void checkLastGames(Contest lastContestResult) {

		float totalRecommendedInvestment = 0;
		float totalRecommendedReward = 0;
		float totalBetInvestment = 0;
		float totalBetReward = 0;
		
		//Calculating the number of points, investment and reward of the last recommended games
		for(Game game: contests.lastElement().getRecommendedGames()) {
			
			int numberOfPoints = 0;
			for(int number : lastContestResult.getNumbers()) {
				if(game.getNumbers().contains(number)) {
					numberOfPoints++;
				}
			}
			
			//Setting points and reward, if it exists
			game.setPoints(numberOfPoints);
			if(numberOfPoints >= 11) {
				switch (numberOfPoints) {
					case 15:
						game.setReward(lastContestResult.getReward15points());
						break;
					case 14:
						game.setReward(lastContestResult.getReward14points());
						break;
					case 13:
						game.setReward(lastContestResult.getReward13points());
						break;
					case 12:
						game.setReward(lastContestResult.getReward12points());
						break;
					case 11:
						game.setReward(lastContestResult.getReward11points());
						break;
					default:
						break;
				}
			}
			
			//Increasing total investment and reward
			totalRecommendedInvestment += game.getInvestment();
			totalRecommendedReward += game.getReward();
			if(contests.lastElement().isBet()) {
				totalBetInvestment += game.getInvestment();
				totalBetReward += game.getReward();
			}
		}
		
		//Setting total investment and reward
		contests.lastElement().setRecommendedInvestment(totalRecommendedInvestment);
		contests.lastElement().setRecommendedReward(totalRecommendedReward);
		contests.lastElement().setBetInvestment(totalBetInvestment);
		contests.lastElement().setBetReward(totalBetReward);
	}
	
	private void updateLastContest(Contest lastContestResult) {
		
		contests.lastElement().setId(lastContestResult.getId());
		contests.lastElement().setDate(lastContestResult.getDate());
		contests.lastElement().setPlace(lastContestResult.getPlace());
		contests.lastElement().setNumbers(lastContestResult.getNumbers());
		contests.lastElement().setReward15points(lastContestResult.getReward15points());
		contests.lastElement().setReward14points(lastContestResult.getReward14points());
		contests.lastElement().setReward13points(lastContestResult.getReward13points());
		contests.lastElement().setReward12points(lastContestResult.getReward12points());
		contests.lastElement().setReward11points(lastContestResult.getReward11points());
	}
	
	private void updateControllers(Contest lastContestResult) {

		//Updating numbersFrequency and getting selected indexes
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int number : lastContestResult.getNumbers()) {
			for(NumberFrequency numberFrequency : numbersFrequency) {
				if(numberFrequency.getNumber() == number) {
					indexes.add(numbersFrequency.indexOf(numberFrequency));
					numberFrequency.setFrequency(numberFrequency.getFrequency() + 1);
				}
			}
		}
		
		//Updating gameStrategy
		Collections.sort(indexes);
		for(GameStrategy gameStrategy : gameStrategies) {
			int points = 0;
			float reward = 0;
			for(int index : indexes) {
				if(gameStrategy.getIndexes().contains(index)) {
					points++;
				}
			}
			
			if(points >= 11) {
				switch (points) {
					case 15:
						gameStrategy.setFrequency15points(gameStrategy.getFrequency15points() + 1);
						reward = lastContestResult.getReward15points();
						break;
					case 14:
						gameStrategy.setFrequency14points(gameStrategy.getFrequency14points() + 1);
						reward = lastContestResult.getReward14points();
						break;
					case 13:
						gameStrategy.setFrequency13points(gameStrategy.getFrequency13points() + 1);
						reward = lastContestResult.getReward13points();
						break;
					case 12:
						gameStrategy.setFrequency12points(gameStrategy.getFrequency12points() + 1);
						reward = lastContestResult.getReward12points();
						break;
					case 11:
						gameStrategy.setFrequency11points(gameStrategy.getFrequency11points() + 1);
						reward = lastContestResult.getReward11points();
						break;
					default:
						break;
				}
			}
			gameStrategy.setReward(gameStrategy.getReward() + reward - Constants.GAME_PRIZE);
			gameStrategy.setPointsAverage(((gameStrategy.getPointsAverage() * contests.size())  + points)/(contests.size() + 1));
		}
		Collections.sort(gameStrategies);
		
		//Updating and sorting numbersFrequency
		if(contestsPartition != -1 && contestsPartition < contests.size()) {
			int[] numbersToBeDiscounted = contests.get(contests.size() - contestsPartition - 1).getNumbers();
			for(int number : numbersToBeDiscounted) {
				for(NumberFrequency numberFrequency : numbersFrequency) {
					if(numberFrequency.getNumber() == number) {
						numberFrequency.setFrequency(numberFrequency.getFrequency() - 1);
					}
				}
			}
		}
		Collections.sort(numbersFrequency);
	}
	
	private void setNextContestRecommendedGames() {
		
		ArrayList<Game> recommendedGames = new ArrayList<Game>();
		
		//Setting recommendedGames
		ArrayList<ArrayList<Integer>> recommendedIndexes = getRecommendedIndexes(Constants.GAMES_QUANTITY);
		for(ArrayList<Integer> indexes : recommendedIndexes) {
			ArrayList<Integer> numbers = new ArrayList<Integer>();
			for(Integer index : indexes) {
				numbers.add(numbersFrequency.get(index).getNumber());
			}
			Collections.sort(numbers);
			recommendedGames.add(new Game(numbers));
		}
		
		//Adding a new contest with recommendedGames
		int nextID = (contests.isEmpty()) ? 1 : (contests.lastElement().getId() + 1);
		contests.add(new Contest(nextID, recommendedGames));
	}

	private ArrayList<ArrayList<Integer>> getRecommendedIndexes(int gamesQuantity) {
		
		ArrayList<ArrayList<Integer>> indexes = new ArrayList<ArrayList<Integer>>();

		for(int index = 0; index < gamesQuantity; index++) {
			indexes.add(gameStrategies.get(index).getIndexes());
		}
		
		return indexes;
	}
	
	public void populateContests() {
		
		//Creating contests by a HTML file
		HTMLParserFromFile.readContestsFromHTMLFile(GlobalReferences.applicationContext);
	}
	
	private void removeUselessContests() {
		Vector<Contest> usefulContests = new Vector<Contest>();
		for(Contest contest : contests) {
			if(contest.getId() >= Constants.INITIAL_CONTEST_ID) {
				usefulContests.add(contest);
			}
		}
		contests = new Vector<Contest>(usefulContests);
	}
	
	public Vector<Contest> getContestsToShow() {
		
		Vector<Contest> contestsToShow = new Vector<Contest>();
		for(Contest contest : contests) {
			if(contest.getId() >= Constants.INITIAL_SHOWN_CONTEST_ID) {
				contestsToShow.add(0, contest);
			}
		}
		return contestsToShow;
	}
	
	public String getContestDetails(Contest contest) {
		
		String contestDetails = "";
		
		contestDetails += "\nJogos " + ((contest.isBet()) ? "(APOSTADOS)" : "(NÃO APOSTADOS)") + ":\n\n";
		for(Game game : contest.getRecommendedGames()) {
			for(int number : game.getNumbers()) {
				contestDetails += number + "\t";
			}
			contestDetails += "\n\n";
		}

		DateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		contestDetails += "Data: " + ((contest.getNumbers() == null) ? "Aguardando..." : mDateFormat.format(contest.getDate().getTime())) + "\n";
		contestDetails += "Cidade: " + ((contest.getNumbers() == null) ? "Aguardando..." : contest.getPlace()) + "\n";

		contestDetails += "\nNúmeros sorteados:\n";
		if(contest.getNumbers() == null) {
			contestDetails += "Aguardando...\n";
		}
		else {
			for(int number : contest.getNumbers()) {
				contestDetails += number + "\t";
			}
			contestDetails += "\n";
		}

		contestDetails += "\nResultados:\n";
		if(contest.getNumbers() == null) {
			contestDetails += "Aguardando...\n";
		}
		else {
			for(Game game : contest.getRecommendedGames()) {
				contestDetails += game.getPoints() + " pontos - Prêmio: R$ " + String.format("%.2f", (float) game.getReward()) + "\n";
			}
		}

		contestDetails += "\nValor dos prêmios:\n";
		if(contest.getNumbers() == null) {
			contestDetails += "Aguardando...\n";
		}
		else {
			contestDetails += "15 pontos: R$ " + String.format("%.2f", contest.getReward15points()) + "\n";
			contestDetails += "14 pontos: R$ " + String.format("%.2f", contest.getReward14points()) + "\n";
			contestDetails += "13 pontos: R$ " + String.format("%.2f", contest.getReward13points()) + "\n";
			contestDetails += "12 pontos: R$ " + String.format("%.2f", contest.getReward12points()) + "\n";
			contestDetails += "11 pontos: R$ " + String.format("%.2f", contest.getReward11points()) + "\n";
		}

		contestDetails += "\nBalanço:\n";
		if(contest.getNumbers() == null) {
			contestDetails += "Aguardando...\n";
		}
		else {
			contestDetails += "Recompensa: R$ " + String.format("%.2f", contest.getBetReward()) + "\n";
			contestDetails += "Investimento: R$ " + String.format("%.2f", contest.getBetInvestment()) + "\n";
			contestDetails += "Lucro: R$ " + String.format("%.2f", (contest.getBetReward() - contest.getBetInvestment())) + "\n";
		}
		
		contestDetails += "\nFrequência dos números:\n";
		for(NumberFrequency numberFrequency : numbersFrequency) {
			contestDetails += numberFrequency.getNumber() + " => " + numberFrequency.getFrequency() + "\n";
		}

		contestDetails += "\nTodos os concursos:\n";
		for(int index = 0; index < contests.size() - 1; index++) {
			contestDetails += contests.get(index).toString() + "\n";
		}

		return contestDetails;
	}
	
	public float getRecommendedReward() {
		
		float totalRecommendedReward = 0;
		for(Contest contest : contests) {
			totalRecommendedReward += contest.getRecommendedReward();
		}
		return totalRecommendedReward;
	}

	public float getRecommendedInvestment() {
		
		float totalRecommendedInvestment = 0;
		for(Contest contest : contests) {
			totalRecommendedInvestment += contest.getRecommendedInvestment();
		}
		return totalRecommendedInvestment;
	}

	public float getBetReward() {
		
		float totalBetReward = 0;
		for(Contest contest : contests) {
			totalBetReward += contest.getBetReward();
		}
		return totalBetReward;
	}

	public float getBetInvestment() {
		
		float totalBetInvestment = 0;
		for(Contest contest : contests) {
			totalBetInvestment += contest.getBetInvestment();
		}
		return totalBetInvestment;
	}

	//FILE MANIPULATION METHODS
	public void readFile() {
		FileManipulator fileManipulator = new FileManipulator();
		Object object = fileManipulator.readObj(Constants.FILE_NAME);
		if (object != null) {
			instance = (ContestManager) object;
		}
		else {
			populateContests();
			removeUselessContests();
		}
	}

	public void saveFile() {
		FileManipulator fileManipulator = new FileManipulator();
		fileManipulator.saveObject(Constants.FILE_NAME, this);
	}
}