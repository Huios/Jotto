import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * JottoModel.java 
 * Tanaka Jimha

 */

public class JottoModel {
	// The View. We store this so that we can call its methods, 
	// like "Show a message."
	private JottoViewer myView; 

	// The list of legal words.
	private ArrayList<String> myWordList;
	private ArrayList<String> myTempWordList;
	private int numOfGuessesMade;
	private String guessWord;
	Random generator = new Random();
	int random;

	public JottoModel() {
		// We do the variable we already have.
		myWordList = new ArrayList<String>();
		myTempWordList = new ArrayList<String>();




	}


	public void addView(JottoViewer view) {
		myView = view;
	}


	private void showModalMessage(String s){
		myView.showModalInfo(s);
	}


	private void messageViews(String s) {
		myView.showMessage(s);
	}


	private void doGuess(String s){
		myView.processGuess(s);
	}


	public void initialize(Scanner s) {
		myWordList.clear();
		while (s.hasNext()) {
			myWordList.add(s.next());
		}

		messageViews("Choose \"New Game\" from the menubar.");
	}


	public void process(Object o) {
		String response = (String) o;
		if (response.length() == 0) {
			myView.badUserResponse("Not a number!");
			return;
		}
		try {
			int n = Integer.parseInt(response);
			if (n < 0 || n > 6) {
				myView.badUserResponse("Out of range: " + n);
				return;
			}
			processResponse(n);
		} catch (NumberFormatException e) {
			myView.badUserResponse("Not a number: " + response);
		}
	}


	public void stopGame() {
		myView.setStopped();
	}



	public void processResponse(int n) {
		// TODO: Make this actually play Jotto.

		if(n==6) {
			showModalMessage("Oh Yeah! I guessed your word! :)");
			stopGame();

		}
		else if(numOfGuessesMade == 15){
			showModalMessage("You win, I ran out of chances");
			stopGame();
		}


		else {
			for(int i = 0; i < myTempWordList.size();i++) {

				if(commonCount(guessWord, myTempWordList.get(i)) != n) {

					myTempWordList.remove(i);
					i--;
				}
			}
			if(myTempWordList.size() == 0) {
				showModalMessage("Hmmm, it seems like I don't know your word, or you entered conflicting common counts");
				stopGame();
			}
			else {
				Random generator = new Random();
				random = generator.nextInt(myTempWordList.size());
				guessWord = myTempWordList.get(random);
				messageViews("Guesses left: " + (15- numOfGuessesMade));
				numOfGuessesMade++;
			}
		}


		doGuess(guessWord);
	}


	//Starts a new game
	public void newGame() {

		messageViews("Start playing");

		myTempWordList = (ArrayList<String>) myWordList.clone();
		random = generator.nextInt(myTempWordList.size());

		guessWord = myTempWordList.get(random);
		numOfGuessesMade = 1;

		doGuess(guessWord);
	}


	/**
	 * Extra credit! If the player selects the "Smarter AI" choice from 
	 * the menu, the view calls this method. This method should set some 
	 * instance variable that tells the rest of the code to do a better 
	 * job of guessing.
	 */
	public void playSmarter() {

	}


	private int commonCount(String a, String b) {

		int count = 0;
		HashMap<Character, Integer > aLetters = new HashMap<Character, Integer>();
		HashMap<Character, Integer> bLetters = new HashMap<Character, Integer>();

		char[] aCharArray = a.toCharArray();
		char[] bCharArray = b.toCharArray();

		for(char c : aCharArray ) {
			if(aLetters.get(c) == null){
				aLetters.put(c,  0);

			}
			aLetters.put(c, aLetters.get(c) + 1);
		}
		for(char c : bCharArray ) {
			if(bLetters.get(c) == null){
				bLetters.put(c,  0);
			}
			bLetters.put(c, bLetters.get(c) + 1);
		}

		for(char c: aLetters.keySet()) {
			if (bLetters.containsKey(c)) {
				if(bLetters.get(c) > aLetters.get(c)) {
					count += aLetters.get(c);
				}
				else {
					count += bLetters.get(c);
				}
			}
		}


		return count;
	}

}
