package src.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;



public class ScoreManager {
    
    // Current scores
    private int currentScore;
    private int currentTopScore;

    private int[] board = new int[GameBoard.ROWS * GameBoard.COLS];


    // undo-redo

    private Stack<Integer> undo;
    private Stack<Integer> undoBoard;

    private Stack<Integer> redo;
    private Queue<Integer> redoBoard;

    private String temp;
    private String undoPath;
    private String redoPath;
    // private boolean setUndo = false;
    private boolean undoSet = false;
    private boolean redoSet = false;

    //File
    private String filePath;
    private GameBoard gBoard;

    // Boolean
    private boolean newGame;

    public ScoreManager(GameBoard gBoard) {
        undo = new Stack<>();
        redo = new Stack<>();

		try {
			filePath = new File("").getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		temp = "TEMP.tmp";
        undoPath = "undo.tmp";
        redoPath = "redo.tmp";
		this.gBoard = gBoard;
    }

    public void reset() {
        File f = new  File(filePath, temp);
        if (f.isFile()) {
            f.delete();
        }
        
        newGame = true;
        currentScore = 0;

    }
    

    private void createFile() {
        FileWriter output = null;
        newGame = true;

        try {
            File f = new File(filePath, temp);
            output = new FileWriter(f);
            BufferedWriter writer = new BufferedWriter(output);
            writer.write("" + 0);
            writer.newLine();
            writer.write("" + 0);
            writer.newLine();
            
            for(int row = 0; row < GameBoard.ROWS; row++) {
                for (int col = 0; col < GameBoard.COLS; col++) {
                    if (row == GameBoard.ROWS - 1 && col == GameBoard.COLS - 1) {
                        writer.write("" + 0);
                    }
                    else {
                        writer.write(0 + "-");
                    }
                }
            }
            
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveGame() {

        FileWriter output = null;
        if (newGame) newGame = false;

        try {
            File f = new File(filePath, temp);
            output = new FileWriter(f);
            BufferedWriter writer = new BufferedWriter(output);
            writer.write("" + currentScore);
            undo.push(currentScore);
            redo.push(currentScore);

            writer.newLine();
            writer.write("" + currentTopScore);
            writer.newLine();

            for(int row = 0; row < GameBoard.ROWS; row++) {
                for (int col = 0; col < GameBoard.COLS; col++) {
                    int location = row * GameBoard.COLS + col;
					this.board[location] = gBoard.getBoard()[row][col] != null ? gBoard.getBoard()[row][col].getValue() : 0;
                    if (row == GameBoard.ROWS - 1 && col == GameBoard.COLS - 1) {
                        writer.write("" + board[location]);
                    } else writer.write(board[location] + "-");          
                }
            }
            writer.close(); 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
    
    public void copyUndo() {
        undoBoard = new Stack<>();
        try {
            File f = new File(filePath, temp);

            if (!f.isFile()) {
                createFile();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      
            undo.push( Integer.parseInt(reader.readLine()));
            currentTopScore = Integer.parseInt(reader.readLine());

            String[] lastBoard = reader.readLine().split("-");

            for (int i = 0; i < lastBoard.length; i++) {
                undoBoard.push(Integer.parseInt(lastBoard[i])) ;
                
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveUndo();
    }
    public void saveUndo() {
        FileWriter output = null;
        if (newGame) newGame = false;

        try {
            File f = new File(filePath, undoPath);
            output = new FileWriter(f);
            BufferedWriter writer = new BufferedWriter(output);
            writer.write("" + undo.pop());
            writer.newLine();
            writer.write("" + currentTopScore);
            writer.newLine();

            for(int row = 0; row < GameBoard.ROWS; row++) {
                for (int col = 0; col < GameBoard.COLS; col++) {
                    if (row == GameBoard.ROWS - 1 && col == GameBoard.COLS - 1) {
                        writer.write("" + undoBoard.get(0));
                        undoBoard.remove(0);
                    } else {writer.write(undoBoard.get(0) + "-");  
                            undoBoard.remove(0);  }  
                }
            }
            writer.close();  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyRedo() {
        redoBoard = new LinkedList<>();
        try {
            File f = new File(filePath, temp);
            if (!f.isFile()) {
                createFile();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      
            redo.push(Integer.parseInt(reader.readLine()));
            currentTopScore = Integer.parseInt(reader.readLine());

            String[] tempBoard = reader.readLine().split("-");

            for (int i = 0; i < tempBoard.length; i++) {
                redoBoard.add(Integer.parseInt(tempBoard[i]));
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveRedo();
    }
    public void saveRedo() {
        FileWriter output = null;
        if (newGame) newGame = false;

        try {
            File f = new File(filePath, redoPath);
            output = new FileWriter(f);
            BufferedWriter writer = new BufferedWriter(output);
            writer.write("" + redo.pop());
            writer.newLine();
            writer.write("" + currentTopScore);
            writer.newLine();

            for(int row = 0; row < GameBoard.ROWS; row++) {
                for (int col = 0; col < GameBoard.COLS; col++) {
                    if (row == GameBoard.ROWS - 1 && col == GameBoard.COLS - 1) {
                        writer.write("" + redoBoard.poll());
                    } else {writer.write(redoBoard.poll() + "-");  
                }  
                }
            }
            writer.close();  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void loadGame() {
        try {
            File f = new File(filePath, temp);

            if (!f.isFile()) {
                createFile();
            }


            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      
            currentScore = Integer.parseInt(reader.readLine());
            currentTopScore = Integer.parseInt(reader.readLine());

            String[] board = reader.readLine().split("-");
            for (int i = 0; i < board.length; i++) {
                this.board[i] = Integer.parseInt(board[i]);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadUndo() {
        try {
            File f = new File(filePath, undoPath);

            if (!f.isFile()) {
                createFile();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      
            currentScore = Integer.parseInt(reader.readLine());

            int bestScore = Integer.parseInt(reader.readLine());


            String[] board = reader.readLine().split("-");
            for (int i = 0; i < board.length; i++) {
                this.board[i] = Integer.parseInt(board[i]);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadRedo() {
        try {
            File f = new File(filePath, redoPath);

            if (!f.isFile()) {
                createFile();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      
            currentScore = Integer.parseInt(reader.readLine());

            int bestScore = Integer.parseInt(reader.readLine());

            String[] board = reader.readLine().split("-");
            for (int i = 0; i < board.length; i++) {
                this.board[i] = Integer.parseInt(board[i]);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getCurrentTopScore() {
        return currentTopScore;
    }

    public void setCurrentTopScore(int currentTopScore) {
        this.currentTopScore = currentTopScore;
    }

    public int[] getBoard() {
        return board;
    }

    public boolean newGame() {
        return newGame;
    }

}
