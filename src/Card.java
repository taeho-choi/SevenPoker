
public class Card {
	private int value, x;
	private String suit;
	private boolean isMade;
	public Card(int num) {
		value = num % 13 +1; //1,2,3...13
		x = num/13; //0,1,2,3
		isMade = false;
	}
	public boolean getisMade() {
		return isMade;
	}
	public void setisMade(boolean b) {
		isMade = b;
	}
	public int getValue() {
		int y=value;
		return y;
	}
	public int getX() {
		return x;
	}
	public String getsuit() {
		switch(x) {
		case 0: return suit="Clubs";
		case 1: return suit="Hearts";
		case 2: return suit="Diamonds";
		default: return suit="Spades";
		}
	}
	public int getPower() { //같은 족보일 때 카드의 파워 비교
		int numberPower, patternPower;
		numberPower = value * 4;
		if(numberPower == 4) numberPower = 56;
		patternPower = x;
		return numberPower + patternPower - 8;
	}
	public String filename() {
		return getsuit()+""+value+".png";
	}
}
