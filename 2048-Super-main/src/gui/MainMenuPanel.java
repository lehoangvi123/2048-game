package src.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import src.game.DrawUtils;
import src.game.Game;

public class MainMenuPanel extends GUIPanel {

    private Font titleFont = Game.main.deriveFont(80f);
    private Font creatorFont = Game.main.deriveFont(24f);
    private String title = "2048-Super";
    private String creator = "By Sleepy Team";
    private int buttonWidth = 220;
    private int spacing = 90;
    private int buttonHeight = 60;
    private ImageIcon backgroundImage;
    private GUIButton playButton;
    private GUIButton scoresButton;
    private GUIButton quitButton;
    private Graphics2D g;

    public MainMenuPanel() {
        super.render(g);
        backgroundImage = new ImageIcon("res\\background.gif");
        playButton = new GUIButton(Game.WIDTH / 2 - buttonWidth / 2, 220, buttonWidth, buttonHeight);
        scoresButton = new GUIButton(Game.WIDTH / 2 - buttonWidth / 2, playButton.getY() + spacing, buttonWidth, buttonHeight);
        quitButton = new GUIButton(Game.WIDTH / 2 - buttonWidth / 2, scoresButton.getY() + spacing, buttonWidth, buttonHeight);
        
        playButton.setText("Play");
        scoresButton.setText("Leader Board");
        quitButton.setText("Quit");
        
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIScreen.getInstance().setCurrentPanel("Play");
            }
        });
        scoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIScreen.getInstance().setCurrentPanel("Leaderboards");
            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(playButton);
        add(scoresButton);
        add(quitButton);
    }
    @Override
    public void render(Graphics2D g) {
        g.drawImage(backgroundImage.getImage(), 0, 0, Game.WIDTH, Game.HEIGHT, this);
        super.render(g);
        g.setFont(titleFont);
        g.setColor(Color.black);
        g.drawString(title, Game.WIDTH / 2 - DrawUtils.getMessageWidth(title, titleFont, g) / 2, 140);
        g.setFont(creatorFont);
        g.drawString(creator, 40, Game.HEIGHT - 40);
    }
}
