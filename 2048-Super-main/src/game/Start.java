package src.game;

import javax.swing.JFrame;

public class Start {
    public static void main(String[] args) {
        Game game = new Game();

        JFrame fr = new JFrame("2048");
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setResizable(false);
        fr.add(game);
        fr.pack();
        fr.setLocationRelativeTo(null);
        fr.setVisible(true); 

        game.start();
    }
}