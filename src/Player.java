
public class Player {
	final static int MAX_CARDS = 52;
	public Card[] cards = new Card[MAX_CARDS];
	private int N = 0;
	private String name;
	public Player(String name) {
		this.name = name;
	}
	public int inHand() {
		return N; //손에 있는 카드의 개수
	}
	public void addCard(Card c) {
		cards[N++] = c;
	}
	public void reset() {
		N=0;
	}
	public int value() { //점수 계산하는 메소드
		int result = 0; //점수
	
		//해당 value의 개수를 카운트
		int[] numCnt = new int[13];
		for(int i = 0; i < numCnt.length; i++) 
			numCnt[i] = 0;
		for(int i = 0; i < N; i++)
			numCnt[cards[i].getValue() - 1]++;
		
		//같은 숫자인 카드의 개수쌍을 카운트
		int[] cntCheck = new int[5];
		for(int i = 0; i < cntCheck.length; i++) 
			cntCheck[i] = 0;
		for(int i = 0; i < numCnt.length; i++)
			cntCheck[numCnt[i]]++;
		
		//해당 pattern의 개수를 카운트
		int[] patCnt = new int[4];
		for(int i = 0; i < patCnt.length; i++) 
			patCnt[i] = 0;
		for(int i = 0; i < N; i++) {
			patCnt[cards[i].getX()]++;
		}
		
		int[] powerCnt = new int[52];
		for(int i = 0; i < powerCnt.length; i++) 
			powerCnt[i] = 0;
		for(int i = 0; i < N; i++) {
			powerCnt[cards[i].getPower()]++;
		}
		
		
		//Royal Straight Flush
		for(int i = 0; i < 4; i++) {
			if(powerCnt[i * 13] >= 1 && powerCnt[i * 13 + 1] >= 1 && powerCnt[i * 13 + 2] >= 1 && powerCnt[i * 13 + 3] >= 1 && powerCnt[i * 13 + 4] >= 1) {
				return 1200;
			}
		}
		
		//Back Straight Flush
		for(int i = 0; i < 4; i++) {
			if(powerCnt[i * 13] >= 1 && powerCnt[i * 13 + 9] >= 1 && powerCnt[i * 13 + 10] >= 1 && powerCnt[i * 13 + 11] >= 1 && powerCnt[i * 13 + 12] >= 1) {
				return 1100;
			}
		}
		
		//Straight Flush
		for(int i = 0; i < powerCnt.length - 4; i++) {
			if((powerCnt[i] >= 1 && powerCnt[i+1] >= 1 && powerCnt[i+2] >= 1 && powerCnt[i+3] >= 1 && powerCnt[i+4] >= 1 &&
				!(i >= 9 && i <= 12) && !(i >= 22 && i <= 25) && !(i >= 35 && i <= 38) && !(i >= 48 && i <= 51)))  {
				return 1200;
			}
		}
		
		//Four Cards
		if(cntCheck[3] == 1) {
			int maxNum = -1;
			for(int i = 0; i < inHand(); i++) {
				if(cards[i].getValue() > maxNum && numCnt[cards[i].getValue() - 1] == 4)
					maxNum = cards[i].getValue();
			}
			int maxPower = -1;
			for(int i = 0; i < inHand(); i++) {
				if(cards[i].getValue() == maxNum && cards[i].getPower() > maxPower) {
					maxPower = cards[i].getPower();
				}
			}
			return 900 + maxPower;
		}
		
		//Full House
		if(cntCheck[2] >= 1 && cntCheck[3] >= 1)
			return 800;
		
		//Flush
		if(patCnt[0] >= 5 || patCnt[1] >= 5 || patCnt[2] >= 5 || patCnt[3] >= 5) {
			int maxNum = -1;
			for(int i = 0; i < inHand(); i++) {
				if(cards[i].getX() > maxNum && patCnt[cards[i].getX()] >= 5)
					maxNum = cards[i].getX();
			}
			int maxPower = -1;
			for(int i = 0; i < inHand(); i++) {
				if(cards[i].getX() == maxNum && cards[i].getPower() > maxPower) {
					maxPower = cards[i].getPower();
				}
			}
			return 700 + maxPower;
		}
		
		//Mountain
		if(numCnt[0] >= 1 && numCnt[9] >= 1 && numCnt[10] >= 1 && numCnt[11] >= 1 && numCnt[12] >= 1)
			return 600;
		
		//Back Straight
		if(numCnt[0] >= 1 && numCnt[1] >= 1 && numCnt[2] >= 1 && numCnt[3] >= 1 && numCnt[4] >= 1)
			return 500;
		
		//Straight
		for(int i = 0; i < numCnt.length - 4; i++) {
			if(numCnt[i] >= 1 && numCnt[i + 1] >= 1 && numCnt[i + 2] >= 1 && numCnt[i + 3] >= 1 && numCnt[i + 4] >= 1) {
				return 400;
			}
		}
		
		//Triple
		if(cntCheck[3] == 1) {
			result += 300;
			int maxNum = -1;
			for(int i = 0; i < inHand(); i++) {
				if(cards[i].getValue() > maxNum && numCnt[cards[i].getValue() - 1] == 3)
					maxNum = cards[i].getValue();
				if(cards[i].getValue() == 1 && numCnt[cards[i].getValue() - 1] == 3)
				{
					maxNum = 1;
					break;
				}
			}
			int maxPower = -1;
			for(int i = 0; i < inHand(); i++) {
				if(cards[i].getValue() == maxNum && cards[i].getPower() > maxPower) {
					maxPower = cards[i].getPower();
				}
			}
			result += maxPower;
		}
		
		
		//TwoPair
		else if(cntCheck[2] >= 2) {
			result += 200;
			int maxNum = -1;
			for(int i = 0; i < inHand(); i++) {
				if(cards[i].getValue() > maxNum && numCnt[cards[i].getValue() - 1] >= 2)
					maxNum = cards[i].getValue();
				if(cards[i].getValue() == 1 && numCnt[cards[i].getValue() - 1] >= 2)
				{
					maxNum = 1;
					break;
				}
			}
			int maxPower = -1;
			for(int i = 0; i < inHand(); i++) {
				if(cards[i].getValue() == maxNum && cards[i].getPower() > maxPower) {
					maxPower = cards[i].getPower();
				}
			}
			result += maxPower;
		}
		
		//One Pair
		else if(cntCheck[2] == 1) {
			result += 100;
			int maxNum = -1;
			for(int i = 0; i < inHand(); i++) {
				if(cards[i].getValue() > maxNum && numCnt[cards[i].getValue() - 1] == 2)
					maxNum = cards[i].getValue();
				if(cards[i].getValue() == 1 && numCnt[cards[i].getValue() - 1] == 2)
				{
					maxNum = 1;
					break;
				}
			}
			int maxPower = -1;
			for(int i = 0; i < inHand(); i++) {
				if(cards[i].getValue() == maxNum && cards[i].getPower() > maxPower) {
					maxPower = cards[i].getPower();
				}
			}
			result += maxPower;
		}

		//No Pair
		else {
			int max = -1;
			for(int i = 0; i < N; i++) {
				if(cards[i].getPower() > max)
					max = cards[i].getPower();
			}
			result += max;
		}
		
		return result;
	}
	
	String getMadeName() {
		String made = "";
		if(value() < 100)
			made = "No Pair";
		else if(value() < 200)
			made = "One Pair";
		else if(value() < 300)
			made = "Two Pair";
		else if(value() < 400)
			made = "Triple";
		else if(value() < 500)
			made = "Straight";
		else if(value() < 600)
			made = "Back Straight";
		else if(value() < 700)
			made = "Mountain";
		else if(value() < 800)
			made = "Flush";
		else if(value() < 900)
			made = "Full House";
		else if(value() < 1000)
			made = "Four Cards";
		else if(value() < 1100)
			made = "Straight Flush";
		else if(value() < 1200)
			made = "Back Straight Flush";
		else
			made = "Royal Straight Flush";
		
		return made;
	}
}
