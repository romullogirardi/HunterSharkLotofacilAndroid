package com.romullogirardi.huntersharklotofacilandroid.model;

import java.io.Serializable;
import java.math.BigInteger;

public abstract class CombinationsGenerator implements Serializable {
	
	//SERIALIZATION ID
	private static final long serialVersionUID = 2752180048070354309L;
	
	//ATTRIBUTES
	private Object[] elements;
	private int k;
	private long lowestIndex = -1;
	private long highestIndex = -1;
	private long combinationsCounter = 1;
	
	//CONSTRUCTOR
	public CombinationsGenerator(Object[] elements, int k) {
		this.elements = elements;
		this.k = k;
	}
	
	//GETTERS AND SETTERS
	public void setLowestIndex(long lowestIndex) {
		this.lowestIndex = lowestIndex;
	}

	public void setHighestIndex(long highestIndex) {
		this.highestIndex = highestIndex;
	}

	//METHODS
	public void generateCombinations(){

		// get the length of the array
		// e.g. for {'A','B','C','D'} => N = 4
		int n = elements.length;
		
		if(this.k > n){
			System.out.println("Invalid input, K > N");
			return;
		}

		// calculate the possible combinations
		// e.g. c(4,2)
		c(n,this.k);

		// get the combination by index
		// e.g. 01 --> AB , 23 --> CD
		int combination[] = new int[this.k];

		// position of current index
		// if (r = 1) r*
		// index ==> 0 | 1 | 2
		// element ==> A | B | C
		int r = 0;
		int index = 0;
		while(r >= 0){
		
		// possible indexes for 1st position "r=0" are "0,1,2" --> "A,B,C"
		// possible indexes for 2nd position "r=1" are "1,2,3" --> "B,C,D"
		// for r = 0 ==> index < (4+ (0 - 2)) = 2
		if(index <= (n + (r - this.k))){

			combination[r] = index;
			
			// if we are at the last position print and increase the index
			if(r == this.k - 1){
				//do something with the combination e.g. add to list or print
				if(isHigherOrEqualToLowestLimit() && isLowerOrEqualToHighestLimit()) {
//					System.out.println("Criando a " + combinationsCounter + "ª estratégia de jogo...");
					processCombination(elements, combination);
					combinationsCounter++;
					index++;
				}
				else if(!isHigherOrEqualToLowestLimit()) {
					combinationsCounter++;
					index++;
				}
				else if (!isLowerOrEqualToHighestLimit()) {
					return;
				}
			}
			else{
				// select index for next position
				index = combination[r]+1;
				r++;
			}
		}
		else{
			r--;
			if(r > 0)
				index = combination[r]+1;
			else
				index = combination[0]+1;
			}
		}
	}
	
	public long c(final int N, final int K) {
	    BigInteger ret = BigInteger.ONE;
	    for (int k = 0; k < K; k++) {
	        ret = ret.multiply(BigInteger.valueOf(N-k)).divide(BigInteger.valueOf(k+1));
	    }
	    return ret.longValue();
	}
	
	private boolean isLowerOrEqualToHighestLimit() {
		if(!(highestIndex != -1 && combinationsCounter > highestIndex)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isHigherOrEqualToLowestLimit() {
		if(!(lowestIndex != -1 && lowestIndex > combinationsCounter)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public abstract void processCombination(Object[] elements, int[] combination);
}