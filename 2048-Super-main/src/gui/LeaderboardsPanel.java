package src.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


import src.game.DrawUtils;
import src.game.Game;
import src.game.Leaderboards;

public class LeaderboardsPanel  extends GUIPanel{
	
    private Leaderboards lBoard;
	private int buttonWidth = 100;
	private int backButtonWidth = 220;
	private int spacing = 30;
	private int buttonY = 120;
	private int buttonHeight = 50;
	private int leaderboardsX = 130;
	private int leaderboardsY = buttonY + buttonHeight + 90;
	
	private String title = "Leaderboards";
	private Font titleFont = Game.main.deriveFont(48f);
	private Font scoreFont = Game.main.deriveFont(30f);
	private State currentState = State.SCORE;

    public LeaderboardsPanel(){
		super();
		lBoard = Leaderboards.getInstance();
		lBoard.loadScores();
		
		GUIButton tileButton = new GUIButton(Game.WIDTH / 2 - buttonWidth, buttonY, buttonWidth, buttonHeight);
		tileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentState = State.TILE;
			}
		});
		tileButton.setText("Tiles");
		add(tileButton);
		
		GUIButton scoreButton = new GUIButton(Game.WIDTH/2, buttonY, buttonWidth, buttonHeight);
		scoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentState = State.SCORE;
			}
		});
		scoreButton.setText("Scores");
		add(scoreButton);
		
		
		GUIButton backButton = new GUIButton(Game.WIDTH / 2 - backButtonWidth / 2, 500, backButtonWidth, 60);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUIScreen.getInstance().setCurrentPanel("Menu");
			}
		});
		backButton.setText("Back");
		add(backButton);
	}
	
	private void drawLeaderboards(Graphics2D g){
		ArrayList<String> strings = new ArrayList<String>();
		if(currentState == State.SCORE){
			strings = convertToStrings(lBoard.getTopScores());
		}
		else if(currentState == State.TILE){
			strings = convertToStrings(lBoard.getTopTiles());
		}
		
		g.setColor(Color.black);
		g.setFont(scoreFont);
		
		for(int i = 0; i < strings.size(); i++){
			String s = (i + 1) + ". " + strings.get(i);
			g.drawString(s, leaderboardsX, leaderboardsY + i * 40);
		}
	
	}
	
	private ArrayList<String> convertToStrings(ArrayList<? extends Number> list){
		ArrayList<String> ret = new ArrayList<String>();
		for(Number n : list){
			ret.add(n.toString());
		}
		return ret;
	}
	
	@Override
	public void update(){
		
	}
	
	@Override
	public void render(Graphics2D g){
		g.setColor(Color.ORANGE);
		g.drawRect(Game.WIDTH/2 - DrawUtils.getMessageWidth(title,titleFont, g)/2 - spacing ,buttonY + buttonHeight/2, Game.WIDTH/2+buttonWidth - spacing , Game.HEIGHT/2);
		super.render(g);
		g.setColor(Color.black);
		g.drawString(title, Game.WIDTH / 2 - DrawUtils.getMessageWidth(title, titleFont, g) / 2, DrawUtils.getMessageHeight(title, titleFont, g) + 40);
		drawLeaderboards(g);
	}
	
	private enum State{
		SCORE,
		TILE
	}
}
