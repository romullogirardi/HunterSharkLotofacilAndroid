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

	//CONSTANTS
	public static final int N = 25;
	public static final int K = 15;

	//ATTRIBUTES
	private Vector<Contest> contests = new Vector<>();
	private Vector<NumberFrequency> numbersFrequency;
	private Vector<GameStrategy> gameStrategies;
	private CombinationsGenerator combinationsGenerator;
	private int contestsPartition = -1;
	private boolean production = false;
	
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
		
		//Initializing numbersFrequency with 0 from 0 to 99
		numbersFrequency = new Vector<>();
		for(int index = 1; index <= 25; index++) {
			numbersFrequency.add(new NumberFrequency(index, 0));
		}

//		//Initializing gameStrategies with CombinationsGenerator
//		gameStrategies = new Vector<>();
//		Integer[] elements = new Integer[N];
//		for(int index = 1; index <= N; index++) elements[index - 1] = index; 
//		CombinationsGenerator myCombinationsGenerator = new CombinationsGenerator(elements, K) {
//			
//			@Override
//			public void processCombination(Object[] elements, int[] combination) {
//				gameStrategies.add(new GameStrategy(combination));
//			}
//		};
//		myCombinationsGenerator.generateCombinations();
	}
	
	//GETTERS AND SETTERS
	public CombinationsGenerator getCombinationsGenerator() {
		return combinationsGenerator;
	}

	public void setContestsPartition(int contestsPartition) {
		this.contestsPartition = contestsPartition;
	}

	public void setGameStrategies(Vector<GameStrategy> gameStrategies) {
		this.gameStrategies = gameStrategies;
	}

	public void setProduction(boolean production) {
		this.production = production;
	}

	//METHODS
//	public void initializeGameStrategiesByCombinationsGenerator(int lowestIndex, int highestIndex) {
//
//		//Initializing gameStrategies with CombinationsGenerator
//		gameStrategies = new Vector<>();
//		Integer[] elements = new Integer[N];
//		for(int index = 1; index <= N; index++) elements[index - 1] = index; 
//		combinationsGenerator = new CombinationsGenerator(elements, K) {
//			
//			@Override
//			public void processCombination(Object[] elements, int[] combination) {
//				gameStrategies.add(new GameStrategy(combination));
//			}
//		};
//		if(lowestIndex != -1) combinationsGenerator.setLowestIndex(lowestIndex);
//		if(highestIndex != -1) combinationsGenerator.setHighestIndex(highestIndex);
//		combinationsGenerator.generateCombinations();
//	}
	
	public void computeLastContest(Contest lastContestResult, boolean print) {
		
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
		
//		//Printing, if necessary
//		if(print) {
//			print();
//		}
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
				if(production) {
					System.out.println("Concurso " + lastContestResult.getId() + ": Marquei " + numberOfPoints + " pontos e ganhei R$ " + String.format("%.2f", (float) game.getReward()));
				}
			}
			
			//Increasing total investment and reward
			totalRecommendedInvestment += game.getInvestment();
			totalRecommendedReward += game.getReward();
			if(lastContestResult.isBet()) {
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
		contests.lastElement().setBet(lastContestResult.isBet());
	}
	
	private void updateControllers(Contest lastContestResult) {

		//Updating numbersFrequency and getting selected indexes
		ArrayList<Integer> indexes = new ArrayList<>();
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
		
		ArrayList<Game> recommendedGames = new ArrayList<>();
		
		//Setting recommendedGames
		ArrayList<ArrayList<Integer>> recommendedIndexes = getRecommendedIndexes(Constants.GAMES_QUANTITY);
		for(ArrayList<Integer> indexes : recommendedIndexes) {
			ArrayList<Integer> numbers = new ArrayList<>();
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

	public void print() {
		
		if(production) {
//			System.out.println("\nTodos os concursos:\n");
//			for(int index = 0; index < contests.size() - 1; index++) {
//				System.out.println(contests.get(index).toString());
//			}
		
			System.out.println("\nJogo anterior: ");
			if((contests.size() - 2) >= 0) {
				for(Game game : contests.get(contests.size() - 2).getRecommendedGames()) {
					System.out.println(game.getPoints() + " pontos - Prêmio: R$ " + String.format("%.2f", (float) game.getReward()));
				}
			}
			else {
				System.out.println("Não há");
			}
	
			System.out.println("\nFrequência dos números");
			for(NumberFrequency numberFrequency : numbersFrequency) {
				System.out.println(numberFrequency.getNumber() + " => " + numberFrequency.getFrequency());
			}
			
			System.out.println("\nEstratégias(" + gameStrategies.size() + ")");
			for(GameStrategy gameStrategy : getRecommendedStrategies(Constants.GAMES_QUANTITY)) {
				System.out.println(gameStrategy.toString());
			}
	
			float totalRecommendedInvestment = 0;
			float totalRecommendedReward = 0;
			float totalBetInvestment = 0;
			float totalBetReward = 0;
			for(Contest contest : contests) {
				totalRecommendedInvestment += contest.getRecommendedInvestment();
				totalRecommendedReward += contest.getRecommendedReward();
				totalBetInvestment += contest.getBetInvestment();
				totalBetReward += contest.getBetReward();
			}
			System.out.println("\nInvestimento recomendado: R$ " + String.format("%.2f", (float) totalRecommendedInvestment));
			System.out.println("Recompensa recomendada: R$ " + String.format("%.2f", (float) totalRecommendedReward));
			System.out.println("Lucro recomendado: R$ " + String.format("%.2f", (float) (totalRecommendedReward - totalRecommendedInvestment)));
			System.out.println("Investimento apostado: R$ " + String.format("%.2f", (float) totalBetInvestment));
			System.out.println("Recompensa apostada: R$ " + String.format("%.2f", (float) totalBetReward));
			System.out.println("Lucro apostado: R$ " + String.format("%.2f", (float) (totalBetReward - totalBetInvestment)));
			DateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			System.out.println("Período avaliado: " + mDateFormat.format(contests.firstElement().getDate().getTime()) + " - " + mDateFormat.format(contests.get(contests.size() - 2).getDate().getTime()));
			
			System.out.println("\nPróximo jogo: ");
			for(Game game : contests.lastElement().getRecommendedGames()) {
				for(int number : game.getNumbers()) {
					System.out.print(number + "\t");
				}
				System.out.println();
			}
			System.out.println();
		}	
//		else {
//			//Updating the global GameStrategies ranker
//			Main.addToBestGameStrategies(getRecommendedStrategies(10));
//		}
	}
	
	private ArrayList<ArrayList<Integer>> getRecommendedIndexes(int gamesQuantity) {
		
		ArrayList<ArrayList<Integer>> indexes = new ArrayList<>();

		for(int index = 0; index < gamesQuantity; index++) {
			indexes.add(gameStrategies.get(index).getIndexes());
		}
		
		return indexes;
	}
	
	private ArrayList<GameStrategy> getRecommendedStrategies(int gamesQuantity) {
		
		ArrayList<GameStrategy> gameStrategiesSelected = new ArrayList<>();
		
		for(int index = 0; index < gamesQuantity; index++) {
			gameStrategiesSelected.add(gameStrategies.get(index));
		}
		
		return gameStrategiesSelected;
	}

	public void populateContests() {
		
		//Creating contests by a HTML file
		HTMLParser.readContestsFromHTMLFile(GlobalReferences.applicationContext);
	}
	
	public Vector<Contest> getContestsToShow() {
		
		Vector<Contest> contestsToShow = new Vector<>();
		for(Contest contest : contests) {
			if(contest.getId() >= Constants.INITIAL_CONTEST_ID) {
				contestsToShow.add(0, contest);
			}
		}
		return contestsToShow;
	}
	
	public String getContestDetails(Contest contest) {
		
		String contestDetails = "";
		contestDetails += "Jogo anterior:\n";
		if(contests.indexOf(contest) != 0) {
			for(Game game : contests.get(contests.indexOf(contest) - 1).getRecommendedGames()) {
				contestDetails += game.getPoints() + " pontos - Prêmio: R$ " + String.format("%.2f", (float) game.getReward()) + "\n";
			}
		}
		else {
			contestDetails += "Não há";
		}

		contestDetails += "\nFrequência dos números\n";
		for(NumberFrequency numberFrequency : numbersFrequency) {
			contestDetails += numberFrequency.getNumber() + " => " + numberFrequency.getFrequency() + "\n";
		}
		
		contestDetails += "\nEstratégias(" + gameStrategies.size() + ")\n";
		for(GameStrategy gameStrategy : getRecommendedStrategies(Constants.GAMES_QUANTITY)) {
			contestDetails += gameStrategy.toString() + "\n";
		}

		float totalRecommendedInvestment = getRecommendedInvestment();
		float totalRecommendedReward = getRecommendedReward();
		float totalBetInvestment = getBetInvestment();
		float totalBetReward = getBetReward();
		contestDetails += "\nInvestimento recomendado: R$ " + String.format("%.2f", (float) totalRecommendedInvestment) + "\n";
		contestDetails += "Recompensa recomendada: R$ " + String.format("%.2f", (float) totalRecommendedReward) + "\n";
		contestDetails += "Lucro recomendado: R$ " + String.format("%.2f", (float) (totalRecommendedReward - totalRecommendedInvestment)) + "\n";
		contestDetails += "Investimento apostado: R$ " + String.format("%.2f", (float) totalBetInvestment) + "\n";
		contestDetails += "Recompensa apostada: R$ " + String.format("%.2f", (float) totalBetReward) + "\n";
		contestDetails += "Lucro apostado: R$ " + String.format("%.2f", (float) (totalBetReward - totalBetInvestment)) + "\n";
		DateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		contestDetails += "Período avaliado: " + mDateFormat.format(contests.firstElement().getDate().getTime()) + " - " + mDateFormat.format(contests.get(contests.size() - 2).getDate().getTime()) + "\n";
		
		contestDetails += "\nPróximo jogo:\n";
		for(Game game : contests.lastElement().getRecommendedGames()) {
			for(int number : game.getNumbers()) {
				contestDetails += number + "\t";
			}
			contestDetails += "\n";
		}
		contestDetails += "\n";
		
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
		}
	}

	public void saveFile() {
		FileManipulator fileManipulator = new FileManipulator();
		fileManipulator.saveObject(Constants.FILE_NAME, this);
	}
}