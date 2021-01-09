import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/*
class MyPanel extends JPanel {
	private ImageIcon icon = new ImageIcon("cards/table.jpg");
	private Image img = icon.getImage();
	public void paintComponet(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img,0,0,getWidth(),getHeight(),this);
	}
}
*/
class MyPanel extends JPanel {
	private ImageIcon icon = new ImageIcon("cards/table.jpg");
	private Image img = icon.getImage(); 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);

	}
}


public class SevenPoker extends JFrame implements ActionListener{
	public Player player = new Player("player");
	public Player dealer = new Player("dealer");
	
	private JButton jbtnCheck = new JButton("Check");
	private JButton jbtnBetx1 = new JButton("Bet x1");
	private JButton jbtnBetx2 = new JButton("Bet x2");	    		
	private JButton jbtnDeal = new JButton("Deal");
	private JButton jbtnAgain = new JButton("Again");
	
	private JLabel jlblBetMoney = new JLabel("$10");
	private JLabel jlblPlayerMoney = new JLabel("You have $1000");
	private JLabel jlblPlayerPower = new JLabel("");
	private JLabel jlblDealerPower = new JLabel("");
	private JLabel jlblStatus = new JLabel(" ");
	private JLabel jlblDealerRes = new JLabel("");
	private JLabel jlblPlayerRes = new JLabel("");
	    
	private Font fontstyle = new Font("Times",Font.BOLD,24);
	private Font fontstyle2 = new Font("Times",Font.BOLD,16);
	
	private int betMoney = 0;
	private int playerMoney = 1000;//초기자본 1000달러
	private JLabel[] jlblCardsPlayer = new JLabel[7];
	private JLabel[] jlblCardsDealer = new JLabel[7];
	private JLabel[] jlblhiddenCard = new JLabel[3];
	
	private boolean isFirstTurn;
	private boolean[] isPickedCard = new boolean[52];
	
	MyPanel tablePanel = new MyPanel();
	private Clip chipclip, flipclip, againclip, winclip,loseclip;
	
	SevenPoker(){
		for(int i = 0; i < 52; i++) {
			isPickedCard[i] = false;
		}
		isFirstTurn = true;
		betMoney += 10;
		playerMoney -= 10;
		jlblBetMoney.setText("$"+betMoney);
		jlblPlayerMoney.setText("You have $"+playerMoney);
		setupSound();
		JFrame gameFrame = new JFrame("BlackJack");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tablePanel.setLayout(null);
	    jbtnCheck.setBounds(50,500,80,40);
	    jbtnBetx1.setBounds(150,500,80,40);
	    jbtnBetx2.setBounds(250,500,80,40);
	    jbtnDeal.setBounds(600,500,80,40);
	    jbtnAgain.setBounds(700,500,80,40);
	        
        jlblBetMoney.setBounds(200,450,100,50);
        jlblBetMoney.setFont(fontstyle);
        jlblBetMoney.setForeground(Color.ORANGE);
        tablePanel.add(jlblBetMoney);
        
        jlblPlayerMoney.setBounds(500,450,200,50);
        jlblPlayerMoney.setFont(fontstyle);
        jlblPlayerMoney.setForeground(Color.ORANGE);
        tablePanel.add(jlblPlayerMoney);
        
        jlblPlayerPower.setBounds(350,300,100,50);
        jlblPlayerPower.setFont(fontstyle2);
        jlblPlayerPower.setForeground(Color.BLUE);
        tablePanel.add(jlblPlayerPower);
        
        jlblDealerPower.setBounds(350,200,100,50);
        jlblDealerPower.setFont(fontstyle2);
        jlblDealerPower.setForeground(Color.RED);
        tablePanel.add(jlblDealerPower);
        //
        jlblPlayerRes.setBounds(450,300,100,50);
        jlblPlayerRes.setFont(fontstyle2);
        jlblPlayerRes.setForeground(Color.WHITE);
        tablePanel.add(jlblPlayerRes);
        
        jlblDealerRes.setBounds(450,200,100,50);
        jlblDealerRes.setFont(fontstyle2);
        jlblDealerRes.setForeground(Color.WHITE);
        tablePanel.add(jlblDealerRes);
        
        jlblStatus.setBounds(500,300,200,50);
        jlblStatus.setFont(fontstyle);
        jlblStatus.setForeground(Color.WHITE);
        tablePanel.add(jlblStatus);

	    tablePanel.add(jbtnCheck);
	    tablePanel.add(jbtnBetx1);
	    tablePanel.add(jbtnBetx2);
	    tablePanel.add(jbtnDeal);
	    tablePanel.add(jbtnAgain);
	    
	    jbtnCheck.addActionListener(this);
	    jbtnBetx1.addActionListener(this);
	    jbtnBetx2.addActionListener(this);
	    jbtnDeal.addActionListener(this);
	    jbtnAgain.addActionListener(this);
	    
	    jbtnDeal.setEnabled(false);
	    jbtnAgain.setEnabled(false);
	        
		gameFrame.add(tablePanel);
		gameFrame.setSize(800,600);
		gameFrame.setVisible(true);
	}
	private void setupSound() {
		String flipSound="sounds/cardFlip1.wav";
		try {
			AudioInputStream flipAudioInputStream =
					AudioSystem.getAudioInputStream(new File(flipSound));
			flipclip = AudioSystem.getClip();
			flipclip.open(flipAudioInputStream);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		String chipSound="sounds/chip.wav";
		try {
			AudioInputStream chipAudioInputStream =
					AudioSystem.getAudioInputStream(new File(chipSound));
			chipclip = AudioSystem.getClip();
			chipclip.open(chipAudioInputStream);
		}
		catch(Exception e) {
			System.out.println(e);
		}

		String loseSound="sounds/wrong.wav";
		try {
			AudioInputStream loseAudioInputStream =
					AudioSystem.getAudioInputStream(new File(loseSound));
			loseclip = AudioSystem.getClip();
			loseclip.open(loseAudioInputStream);
		}
		catch(Exception e) {
			System.out.println(e);
		}

		String againSound="sounds/ding.wav";
		try {
			AudioInputStream againAudioInputStream =
					AudioSystem.getAudioInputStream(new File(againSound));
			againclip = AudioSystem.getClip();
			againclip.open(againAudioInputStream);
		}
		catch(Exception e) {
			System.out.println(e);
		}

		String winSound="sounds/win.wav";
		try {
			AudioInputStream winAudioInputStream =
					AudioSystem.getAudioInputStream(new File(winSound));
			winclip = AudioSystem.getClip();
			winclip.open(winAudioInputStream);
		}
		catch(Exception e) {
			System.out.println(e);
		}

	}
	private void hitPlayer(int n) {
		int temp;
		do {
		temp=(int)(Math.random()*52);//0,..51
		}  while(isPickedCard[temp]);
		isPickedCard[temp] = true;
		Card newCard = new Card(temp);
		player.addCard(newCard);
		jlblCardsPlayer[player.inHand()-1] = 
				new JLabel(new ImageIcon("cards/"+newCard.filename()));
		jlblCardsPlayer[player.inHand()-1].setBounds(100+n*75,350, 80, 100);
		tablePanel.add(jlblCardsPlayer[player.inHand()-1]);
		tablePanel.updateUI();
		
		flipclip.stop();
		flipclip.setFramePosition(0);
		flipclip.start();
	}
	private void hitDealer(int n) {
		int temp;
		do {
		temp=(int)(Math.random()*52);//0,..51
		}  while(isPickedCard[temp]);
		isPickedCard[temp] = true;
		Card newCard = new Card(temp);
		dealer.addCard(newCard);
		jlblCardsDealer[dealer.inHand()-1] = 
				new JLabel(new ImageIcon("cards/"+newCard.filename()));
		jlblCardsDealer[dealer.inHand()-1].setBounds(100+n*75,100, 80, 100);
		tablePanel.add(jlblCardsDealer[dealer.inHand()-1]);
		tablePanel.updateUI();
	
		flipclip.stop();
		flipclip.setFramePosition(0);
		flipclip.start();
	}
	private void gameEnd() {
	    for(int i = 0; i < 3; i++) {
			tablePanel.remove(jlblhiddenCard[i]);
	    }
	    if(dealer.value() < player.value()) {
	    	playerMoney += betMoney * 2;
			jlblPlayerRes.setText("WIN");
			jlblDealerRes.setText("LOSE");
			winclip.stop();
			winclip.setFramePosition(0);
			winclip.start();
	    }
	    else {
			jlblPlayerRes.setText("LOSE");
			jlblDealerRes.setText("WIN");
			loseclip.stop();
			loseclip.setFramePosition(0);
			loseclip.start();
	    }
	    
	    for(int i = 0; i < 52; i++) {
			isPickedCard[i] = false;
		}
	    
	    betMoney = 0;
		jlblBetMoney.setText("$"+betMoney);
		jlblPlayerMoney.setText("You have $"+playerMoney);
		
		jlblPlayerPower.setText(""+player.getMadeName());
		jlblDealerPower.setText(""+dealer.getMadeName());
		tablePanel.updateUI();
	}
	public void actionPerformed(ActionEvent e) {

		if (e.getSource()==jbtnDeal) {
			if(isFirstTurn) {
				jlblhiddenCard[0] = 
						new JLabel(new ImageIcon("cards/b1fv.png"));
				jlblhiddenCard[0].setBounds(100,100,80,100);
				tablePanel.add(jlblhiddenCard[0]);
				
				jlblhiddenCard[1] = 
						new JLabel(new ImageIcon("cards/b1fv.png"));
				jlblhiddenCard[1].setBounds(175,100,80,100);
				tablePanel.add(jlblhiddenCard[1]);
				tablePanel.updateUI();
				hitPlayer(0);
				hitPlayer(1);
				hitDealer(0);
				hitDealer(1);
			    jbtnDeal.setEnabled(false);
			    jbtnCheck.setEnabled(true);
			    jbtnBetx1.setEnabled(true);
			    jbtnBetx2.setEnabled(true);
			    isFirstTurn = false;
			}
			else if(player.inHand() < 7){
				if(dealer.inHand() == 6) {
					jlblhiddenCard[2] = 
							new JLabel(new ImageIcon("cards/b1fv.png"));
					jlblhiddenCard[2].setBounds(550,100,80,100);
					tablePanel.add(jlblhiddenCard[2]);
					tablePanel.updateUI();
				}
				hitPlayer(player.inHand());
				hitDealer(dealer.inHand());
			    jbtnDeal.setEnabled(false);
			    jbtnCheck.setEnabled(true);
			    jbtnBetx1.setEnabled(true);
			    jbtnBetx2.setEnabled(true);
			}
			else
			{
			    jbtnDeal.setEnabled(false);
			    jbtnAgain.setEnabled(true);
			    jbtnCheck.setEnabled(false);
			    jbtnBetx1.setEnabled(false);
			    jbtnBetx2.setEnabled(false);
			    gameEnd();
			}
			
			if(playerMoney == 0) {
			    jbtnBetx1.setEnabled(false);
			    jbtnBetx2.setEnabled(false);
			}
		}
		if (e.getSource()==jbtnCheck) {
		    jbtnDeal.setEnabled(true);
		    jbtnCheck.setEnabled(false);
		    jbtnBetx1.setEnabled(false);
		    jbtnBetx2.setEnabled(false);
			chipclip.stop();
			chipclip.setFramePosition(0);
			chipclip.start();
		}
		if (e.getSource()==jbtnBetx1) {
			if (playerMoney < betMoney) {
				betMoney += playerMoney;
				playerMoney = 0;
			}
			else
			{
				playerMoney -= betMoney;
				betMoney += betMoney;
			}
			jlblBetMoney.setText("$"+betMoney);
			jlblPlayerMoney.setText("You have $"+playerMoney);
		    jbtnDeal.setEnabled(true);
		    jbtnCheck.setEnabled(false);
		    jbtnBetx1.setEnabled(false);
		    jbtnBetx2.setEnabled(false);
		    
			chipclip.stop();
			chipclip.setFramePosition(0);
			chipclip.start();
		}
		if (e.getSource()==jbtnBetx2) {
			if (playerMoney < betMoney * 2) {
				betMoney += playerMoney;
				playerMoney = 0;
			}
			else
			{
				playerMoney -= betMoney * 2;
				betMoney += betMoney * 2;
			}
			jlblBetMoney.setText("$"+betMoney);
			jlblPlayerMoney.setText("You have $"+playerMoney);
		    jbtnDeal.setEnabled(true);
		    jbtnCheck.setEnabled(false);
		    jbtnBetx1.setEnabled(false);
		    jbtnBetx2.setEnabled(false);
		    
			chipclip.stop();
			chipclip.setFramePosition(0);
			chipclip.start();
		}
		if (e.getSource()==jbtnAgain) {
		    for(int i = 0; i < dealer.inHand(); i++) {
				tablePanel.remove(jlblCardsDealer[i]);
		    }
		    for(int i = 0; i < player.inHand(); i++) {
				tablePanel.remove(jlblCardsPlayer[i]);
		    }
			jlblPlayerPower.setText("");
			jlblDealerPower.setText("");
			jlblDealerRes.setText("");
			jlblPlayerRes.setText("");
		    player.reset();
		    dealer.reset();
		    
		    if(playerMoney <= 0) {
			    jbtnCheck.setEnabled(false);
			    jbtnBetx1.setEnabled(false);
			    jbtnBetx2.setEnabled(false);
			    betMoney = 0;
		    }
		    else {
			    jbtnDeal.setEnabled(false);
			    jbtnAgain.setEnabled(false);
			    jbtnCheck.setEnabled(true);
			    jbtnBetx1.setEnabled(true);
			    jbtnBetx2.setEnabled(true);
			    betMoney = 10;
			    playerMoney -= 10;
		    }
			jlblBetMoney.setText("$"+betMoney);
			jlblPlayerMoney.setText("You have $"+playerMoney);
			

		    
			tablePanel.updateUI();
			againclip.stop();
			againclip.setFramePosition(0);
			againclip.start();
			isFirstTurn = true;
		}
	}
	public static void main(String[] args) {
		new SevenPoker();
	}
}








