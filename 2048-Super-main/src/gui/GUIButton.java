package src.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;

import src.game.AudioHandler;
import src.game.DrawUtils;
import src.game.Game;

public class GUIButton extends JButton {

    private State currentState = State.RELEASED;
    private Rectangle clickBox;

    private ArrayList<ActionListener> actionListeners;
    private ArrayList<GUIButton> childButtons;
    private String text = "";

    private Color released;
    private Color hover;
    private Color pressed;
    private Font font = Game.main.deriveFont(22f);
    private AudioHandler audio;

    public GUIButton(int x, int y, int width, int height) {
        clickBox = new Rectangle(x, y, width, height);
        actionListeners = new ArrayList<>();
        childButtons = new ArrayList<>();
        released = new Color(255, 189, 89);
        hover = new Color(232, 160, 26);
        pressed = new Color(227, 171, 68);

        audio = AudioHandler.getInstance();
        audio.load("res\\select.wav", "select");
    }

    public GUIButton(int x, int y, int width, int height, GUIButton... buttons) {
        this(x, y, width, height);
        for (GUIButton button : buttons) {
            addChildButton(button);
        }
    }

    public void update() {
    }

    public void render(Graphics2D g) {
        renderRecursive(g);
    }

    private void renderRecursive(Graphics2D g) {
        if (currentState == State.RELEASED) {
            g.setColor(released);
        } else if (currentState == State.HOVER) {
            g.setColor(hover);
        } else {
            g.setColor(pressed);
        }
        g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 20, 20);
        g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 20, 20);
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString(text, clickBox.x + clickBox.width / 2 - DrawUtils.getMessageWidth(text, font, g) / 2,
                clickBox.y + clickBox.height / 2 + DrawUtils.getMessageHeight(text, font, g) / 2);

        for (GUIButton child : childButtons) {
            child.renderRecursive(g);
        }
    }

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void addChildButton(GUIButton button) {
        childButtons.add(button);
    }

    public void mousePressed(MouseEvent e) {
        mousePressedRecursive(e);
    }

    private void mousePressedRecursive(MouseEvent e) {
        if (currentState != State.UNABLE && clickBox.contains(e.getPoint())) {
            currentState = State.PRESSED;
        }

        for (GUIButton child : childButtons) {
            child.mousePressedRecursive(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        mouseReleasedRecursive(e);
    }

    private void mouseReleasedRecursive(MouseEvent e) {
        if (currentState != State.UNABLE && clickBox.contains(e.getPoint())) {
            for (ActionListener al : actionListeners) {
                al.actionPerformed(null);
            }
            audio.play("select", 0);
            currentState = State.RELEASED;
        }

        for (GUIButton child : childButtons) {
            child.mouseReleasedRecursive(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        mouseDraggedRecursive(e);
    }

    private void mouseDraggedRecursive(MouseEvent e) {
        if (currentState != State.UNABLE) {
            if (clickBox.contains(e.getPoint())) {
                currentState = State.PRESSED;
            } else {
                currentState = State.RELEASED;
            }
        }

        for (GUIButton child : childButtons) {
            child.mouseDraggedRecursive(e);
        }
    }

    public void mouseMoved(MouseEvent e) {
        mouseMovedRecursive(e);
    }

    private void mouseMovedRecursive(MouseEvent e) {
        if (currentState != State.UNABLE) {
            if (clickBox.contains(e.getPoint())) {
                currentState = State.HOVER;
            } else {
                currentState = State.RELEASED;
            }
        }
        for (GUIButton child : childButtons) {
            child.mouseMovedRecursive(e);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            currentState = State.UNABLE;
        } else {
            currentState = State.RELEASED;
        }
    }

    public int getX() {
        return clickBox.x;
    }

    public int getY() {
        return clickBox.y;
    }

    public int getWidth() {
        return clickBox.width;
    }

    public int getHeight() {
        return clickBox.height;
    }

    public void setText(String text) {
        this.text = text;
    }

    private enum State {
        RELEASED, HOVER, PRESSED, UNABLE
    }
}
