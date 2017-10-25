package model;

import java.util.Random;

public class YatzyDice {
	// Face values of the 5 dice.
	// 1 <= values[i] <= 6 for i in [0..5]
	private int[] values = new int[5];

	// Number of times the 5 dice have been thrown.
	// 0 <= throwCount <= 3.
	private int throwCount = 0;

	// Random number generator.
	private Random random = new Random();

	/**
	 * Returns the 5 face values of the dice.
	 */
	public int[] getValues() {
		return values;
	}

	/**
	 * Sets the 5 face values of the dice.<br/>
	 * Pre: 1 <= values[i] <= 6 for i in [0..5].<br/>
	 * Note: This method is only meant to be used in tests.
	 */
	void setValues(int[] values) {
		this.values = values;
	}

	/**
	 * Returns the number of times the 5 dice has been thrown.
	 */
	public int getThrowCount() {
		return throwCount;
	}

	/**
	 * Resets the throw count.
	 */
	public void resetThrowCount() {
		this.throwCount = 0;
	}

	/**
	 * Rolls the 5 dice. Only rolls dice that are not hold.<br/>
	 * Random generator is used to generate random numbers. Note: holds[i] is true,
	 * if die no. i is hold.
	 */

	private Random generator;

	public void throwDice(boolean[] holds) {
		generator = new Random();
		this.throwCount++;

		for (int i = 0; i < 5; i++) {
			if (holds[i] == false)
				this.values[i] = 1 + generator.nextInt(6);
		}
	}

	// -------------------------------------------------------------------------

	/**
	 * Returns all results possible with the current face values.<br/>
	 * The order of the results is the same as on the score board.<br/>
	 * Note: This is an optional method. Comment this method out,<br/>
	 * if you don't want use it.
	 */
	public int[] getResults() {
		int[] results = new int[15];
		for (int i = 0; i <= 5; i++) {
			results[i] = this.sameValuePoints(i + 1);
		}
		results[6] = this.onePairPoints();
		results[7] = this.twoPairPoints();
		results[8] = this.threeSamePoints();
		results[9] = this.fourSamePoints();
		results[10] = this.fullHousePoints();
		results[11] = this.smallStraightPoints();
		results[12] = this.largeStraightPoints();
		results[13] = this.chancePoints();
		results[14] = this.yatzyPoints();

		return results;
	}

	// -------------------------------------------------------------------------

	// Returns an int[7] containing the frequency of face values.
	// Frequency at index v is the number of dice with the face value v, 1 <= v <=
	// 6.
	// Index 0 is not used.
	// Note: This method can be used in several of the following methods.
	private int[] frequency() {
		int[] frequency = new int[7];

		for (int i = 1; i < 7; i++) {
			for (int o = 0; o < 5; o++) {
				if (i == values[o]) {
					frequency[i]++;
				}
			}
		}
		return frequency;
	}

	/**
	 * Returns same-value points for the given face value.<br/>
	 * Returns 0, if no dice has the given face value.<br/>
	 * Pre: 1 <= value <= 6;
	 */
	public int sameValuePoints(int value) {
		int[] frequency = frequency();
		return frequency[value] * value;
	}

	/**
	 * Returns points for one pair (for the face value giving highest points).<br/>
	 * Returns 0, if there aren't 2 dice with the same face value.
	 */
	public int onePairPoints() {
		int[] frequency = frequency();
		for (int i = 6; i > 0; i--) {
			if (frequency[i] >= 2) {
				return i * 2;
			}
		}

		return 0;
	}

	/**
	 * Returns points for two pairs<br/>
	 * (for the 2 face values giving highest points).<br/>
	 * Returns 0, if there aren't 2 dice with the same face value<br/>
	 * and 2 other dice with the same but different face value.
	 */
	public int twoPairPoints() {
		int[] frequency = frequency();
		int numberPairs = 0;
		int points = 0;

		for (int i = 6; i > 0; i--) {
			if (frequency[i] >= 2) {
				numberPairs++;
				points = i * 2 + points;
			}
		}

		if (numberPairs >= 2) {
			return points;
		} else
			return 0;
	}

	/**
	 * Returns points for 3 of a kind.<br/>
	 * Returns 0, if there aren't 3 dice with the same face value.
	 */
	public int threeSamePoints() {
		int[] frequency = frequency();
		for (int i = 6; i > 0; i--) {
			if (frequency[i] >= 3) {
				return i * 3;
			}
		}

		return 0;
	}

	/**
	 * Returns points for 4 of a kind.<br/>
	 * Returns 0, if there aren't 4 dice with the same face value.
	 */
	public int fourSamePoints() {
		int[] frequency = frequency();
		for (int i = 6; i > 0; i--) {
			if (frequency[i] >= 4) {
				return i * 4;
			}
		}

		return 0;
	}

	/**
	 * Returns points for full house.<br/>
	 * Returns 0, if there aren't 3 dice with the same face value<br/>
	 * and 2 other dice with the same but different face value.
	 */
	public int fullHousePoints() {
		int[] frequency = frequency();
		boolean hasThree = false;
		boolean hasPair = false;
		int pairPoints = 0, threePoints = 0;

		for (int i = 1; i < 7; i++) {
			if (frequency[i] == 2) {
				hasPair = true;
				pairPoints = i * 2;
			}
			if (frequency[i] == 3) {
				hasThree = true;
				threePoints = i * 3;
			}
		}

		if (hasThree && hasPair) {
			return pairPoints + threePoints;
		}
		return 0;
	}

	/**
	 * Returns points for small straight.<br/>
	 * Returns 0, if the dice are not showing 1,2,3,4,5.
	 */
	public int smallStraightPoints() {
		int[] frequency = frequency();
		boolean isSmallStraight = true;

		for (int i = 1; i < 6; i++) {
			if (frequency[i] != 1) {
				isSmallStraight = false;
				return 0;
			}
		}
		return 15;
	}

	/**
	 * Returns points for large straight.<br/>
	 * Returns 0, if the dice are not showing 2,3,4,5,6.
	 */
	public int largeStraightPoints() {
		int[] frequency = frequency();
		boolean isLargeStraight = true;

		for (int i = 2; i < 7; i++) {
			if (frequency[i] != 1) {
				isLargeStraight = false;
				return 0;
			}
		}
		return 20;
	}

	/**
	 * Returns points for chance (the sum of face values).
	 */
	public int chancePoints() {
		int sum = 0;
		for (int i = 0; i < 5; i++) {
			sum = values[i] + sum;
		}

		return sum;
	}

	/**
	 * Returns points for yatzy (50 points).<br/>
	 * Returns 0, if there aren't 5 dice with the same face value.
	 */
	public int yatzyPoints() {
		int[] frequency = frequency();
		for (int i = 6; i > 0; i--) {
			if (frequency[i] == 5) {
				return 50;
			}
		}

		return 0;
	}

}
