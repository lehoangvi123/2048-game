package src.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import src.game.DrawUtils;
import src.game.Game;
import src.game.GameBoard;
import src.game.ScoreManager;

public class PlayPanel extends GUIPanel{
    
    private GameBoard board;
    private ScoreManager scores;
    private Font scoreFont; 

    private GUIButton undoButton; 
    private GUIButton redoButton;

    // Game Over
    private GUIButton tryAgain;
    private GUIButton mainMenu;
    private GUIButton screenShot;
    private GUIButton resetButton;
    private GUIButton menu;
    private int smallButtonWidth = 160;
    private int spacing = 18;
    private int largeButtonWidth = smallButtonWidth * 2 + spacing;
    private int buttonHeight = 50;
    private boolean added;
    private int alpha = 0;
    private int countUndo = 0;
    private int countRedo = 0;
    private Font gameOverFont;
    private boolean screenshot;
    private ImageIcon background;

    public PlayPanel()  {

        background = new ImageIcon("res\\backgroundGame.png");
        scoreFont = Game.main.deriveFont(24f);
        gameOverFont = Game.main.deriveFont(70f);
        board = new GameBoard(Game.WIDTH / 2 - GameBoard.BOARD_WIDTH / 2, Game.HEIGHT - GameBoard.BOARD_HEIGHT - 20);
        scores = board.getScore();

        undoButton = new GUIButton(80 + spacing,120,80,40);
        redoButton = new GUIButton(2*80 + 2*spacing,120,80,40); 
        menu = new GUIButton(3*80 + 3*spacing,120,80,40);
        resetButton = new GUIButton(4*80+ 4*spacing,120,80,40);  

        
        mainMenu = new GUIButton(Game.WIDTH / 2 - largeButtonWidth / 2, 450, largeButtonWidth, buttonHeight);
        tryAgain = new GUIButton(mainMenu.getX(), mainMenu.getY() - spacing - buttonHeight, smallButtonWidth, buttonHeight);
        screenShot = new GUIButton(tryAgain.getX() + tryAgain.getWidth() + spacing, tryAgain.getY(), smallButtonWidth, buttonHeight);


        undoButton.setText("Undo"); 
        redoButton.setText("Redo");
        tryAgain.setText("Try Again");
        screenShot.setText("Screenshot");
        mainMenu.setText("Back to Main Menu");
        menu.setText("Menu");
        resetButton.setText("Reset");
        
        tryAgain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.getScore().reset();
                board.reset();
                alpha = 0;
                remove(tryAgain);
                remove(screenShot);
                remove(mainMenu);
                add(redoButton);
                add(undoButton);
                add(menu);
                add(resetButton);
                redoButton.setEnabled(false);
                added = false;
                countUndo = 0;
                board.setCount(0);
}
        });

        screenShot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                screenshot = true;
            }
        });
        

        mainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIScreen.getInstance().setCurrentPanel("Menu");
            }
        }); 

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIScreen.getInstance().setCurrentPanel("Menu");
            }
        }); 
        redoButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                board.redoBoard();
                countRedo++;
            }
        } );

        undoButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                board.undoBoard();
                countUndo++;
                redoButton.setEnabled(true);
                countRedo = 0;
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.resetGame();
                countRedo = 0;
                countUndo = 0;
            }
        }); 

        add(undoButton);
        add(redoButton);
        add(menu);
        add(resetButton);

    }

    private void drawGUI(Graphics2D g) {


        // Draw it
        g.drawImage(background.getImage(), 0, 0, Game.WIDTH, Game.HEIGHT, this);
        g.setColor(Color.BLACK);
        g.setFont(scoreFont);
        g.drawString("Score: " + scores.getCurrentScore(), 40, 50);
        g.setColor(Color.red);
        g.drawString("Best: " + scores.getCurrentTopScore(), 
                        Game.WIDTH - DrawUtils.getMessageWidth("Best: " + scores.getCurrentTopScore(), scoreFont, g) - 40, 50);
    }
  

    public void drawGameOver(Graphics2D g) {
        g.setColor(new Color(222, 222, 222, alpha));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        g.setColor(Color.red);
        g.setFont(gameOverFont);
        g.drawString("Game Over!", Game.WIDTH / 2 - DrawUtils.getMessageWidth("Game Over!", gameOverFont, g) / 2, 250);
    }

    @Override
    public void update() {
        board.update();
        if (board.isDead()) {
            alpha++;
            if (alpha > 170) alpha = 170;
        }

        if(board.getCount() == 0){
            undoButton.setEnabled(false);
            redoButton.setEnabled(false);
        } else{
            undoButton.setEnabled(true);
        }
        if( countRedo != 0){
            redoButton.setEnabled(false);
        }
        if(countUndo >=3){
            undoButton.setEnabled(false);
        }
        if(countUndo >3){
        redoButton.setEnabled(false);
        }
    }

    @Override
    public void render(Graphics2D g) {
        drawGUI(g);
        board.render(g);
        if (screenshot) {
            BufferedImage bi = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) bi.getGraphics();
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            drawGUI(g2d);
            board.render(g2d);
            try {
                ImageIO.write(bi, "gif", new File(System.getProperty("src\\bin\\screeshot"), "screenshot" + System.nanoTime() + ".gif"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            screenshot = false;
        }
        if (board.isDead()) {
            if (!added) {
                added = true;
                add(mainMenu);
                add(screenShot);
                add(tryAgain);
            }
            remove(redoButton);
            remove(undoButton);
            remove(resetButton);
            remove(menu);
            drawGameOver(g);

        }
        super.render(g);
    }
}
