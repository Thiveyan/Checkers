/**
 * @author mztt and ItisTJ
 */

// imports
import java.awt.event.*;
import static java.awt.event.KeyEvent.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;

class Main {

  // regular variables
  static boolean pieceChosen = false;
  static int xChecker = 0;
  static int yChecker = 0;
  static int turnNum = 0;
  static boolean canMove = true;
  static boolean jump = false;
  static boolean isKing = false;
  static int yRestriction = 0;
  static boolean infiniteJump = false;
  static int whiteNum = 12;
  static int blackNum = 12;
  static int xPos = 0;
  static int yPos = 0;
  // image variables
  static ImageIcon darkSquare = new ImageIcon("Images/darkSquare.png"); 
  static ImageIcon lightSquare = new ImageIcon("Images/sandSquare.png"); 
  static ImageIcon highlightSquare = new ImageIcon("Images/highlightedSquare.png"); 
  static ImageIcon blackChecker = new ImageIcon("Images/blackChecker.png"); 
  static ImageIcon whiteChecker = new ImageIcon("Images/whiteChecker.png");
  static ImageIcon whiteKing = new ImageIcon("Images/whiteKings.png");
  static ImageIcon blackKing = new ImageIcon("Images/blackKings.png");
  static ImageIcon currentChecker = new ImageIcon("");
  static ImageIcon currentKing = new ImageIcon("");
  static ImageIcon otherChecker = new ImageIcon("");
  static ImageIcon otherKing = new ImageIcon("");
  // button variables
  static JFrame frame = new JFrame("Checkers");
  static JButton[][] gridButtons = new JButton[8][8];
  public static JButton button = new JButton();
  static final Color darkBrown = new Color(51, 00, 00);
  static final Color white = new Color(255,255,255);

  public static void instructionsFrame(){

    //Create a new instructions frame
    JFrame instructFrame = new JFrame("Instructions");
    // make the checkers fram invisible
    frame.setVisible(false);
    // size, colour, etc.
    instructFrame.setSize(512,544);
    instructFrame.setLocation(5,5);
    instructFrame.setBackground(darkBrown);
    instructFrame.setVisible(true);
    instructFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    instructFrame.setLayout(null);
    // create play button
    JButton playButton = new JButton();
    playButton.setText("PLAY");
    playButton.setLocation(200, 400);
    playButton.setSize(125,62);
    playButton.setBackground(white);
    // add the button to the fram
    instructFrame.add(playButton);
    // create new text area
    JTextArea instructions = new JTextArea();
    // set the tex
    instructions.setText(" White starts the game with alternating turns there after.\n\n Regular checkers can only move diagonally towards the opposing \n checkers. \n\n Jump your opponent's checkers to remove them from the board. \n\n If you jump a checker, and another checker can be jumped you may \n jump again. \n\n King your pieces when your checkers reach your opponent's side \n\n The king can move forward and backward diagonally \n\n Once you have captured all of your opponent’s checkers, you have \n won the game!");
    // size, location, colour of text area
    instructions.setBounds(0, 25, 512, 464);
    instructions.setBackground(white);
    instructions.setFont(new Font("Helvetica", Font.PLAIN, 15));
    instructions.setForeground(darkBrown);
    instructions.setEditable(false);
    // add the text to the frame
    instructFrame.add(instructions);
    // action listener for play button
    playButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        // hide the instructions frame
        instructFrame.setVisible(false);
        // show the checkers frame
        frame.setVisible(true);
      }
    });
 
  }
  
               

  public static void main(String[] args) {
    
    // making the frame and the desired size
    JFrame frame = new JFrame("Checkers");
    frame.setSize(512, 544);
    frame.setLocation(5, 5);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(null);

    // varaibles to store the buttons (8x8 board)
    JButton[][] gridButtons = new JButton[8][8];
    // for loops to create board
    for (int y = 0; y < 8; y++){
      for (int x = 0; x < 8; x++){
        // create button
        JButton button = new JButton();
        gridButtons[y][x] = button;
        // size of button so 64 buttons fit 
        button.setSize(64,64);
        button.setLocation(x*64, y*64);
        // makes a diagonaly pattern of light squares and dark squares to make the checkered board
        if ((y+x) % 2 == 0){
          button.setIcon(lightSquare);
        } else{
          button.setIcon(darkSquare);
        }
        button.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            // gives xPos the x location of the button pressed and yPos gets the y location of the button
            xPos = button.getLocation().x/64;
            yPos = button.getLocation().y/64;
            // reset the jump varaible everytime a button is pressed
            jump = false;
            // call the movingPieces function
            movingPieces();
            // check if white has any chekcers left
            if (whiteNum == 0){
              // call win game function
              winGame("BLACK");
            }
            // check if black has any checkers left
            if (blackNum == 0){
              // call win game function
              winGame("WHITE");
            }
          }

          public void movingPieces() {
            // if it is white's turn change all the necessary varaibles so that they match white
            if (turnNum % 2 == 0){
              currentChecker = whiteChecker;
              currentKing = whiteKing;
              otherChecker = blackChecker;
              otherKing = blackKing;
              yRestriction = 0;
            // same thing as above just with black
            } else{
              currentChecker = blackChecker;
              currentKing = blackKing;
              otherChecker = whiteChecker;
              otherKing = whiteKing;
              yRestriction = 7;
            }
            // checks to make sure the button you are pressing is a checker and makes sure that you are not in the middle of a double jump
            if ((button.getIcon() == currentChecker || (button.getIcon() == currentKing)) && (!infiniteJump)){
              // change piece chosen to false
              pieceChosen = false;
              // if piece chosen is false
              if (! pieceChosen){
                // save the location of the checker
                xChecker = button.getLocation().x/64;
                yChecker = button.getLocation().y/64;
                // check if the checker is a king
                if (button.getIcon() == currentKing ){
                  // save the type of checker
                  isKing = true;
                } else{
                  isKing = false;
                }
                // change piece chosen to true since a piece has now been chosen
                pieceChosen = true;
              }
            // if a piece has been chosen
            }else if (pieceChosen){
              // if they are clicking on an empty square
              if (button.getIcon() == darkSquare){
                // restriction so that when moving left the checker cannot be moved off the board. this also stops any index errors associated with gridButtons
                if (xChecker != 0){
                  // call the allowMove function but moving left
                  allowMove(-1);
                }
                // same as above but restriction for moving right
                if (xChecker != 7){
                  // call the allowMove function but moving right
                  allowMove(1);
                }
              // if a valid square is not pressed print "cant move that"
              }else{
                System.out.println("Can't move that");
              }
            }
          }

          public void allowMove(int xMove){
            // if the checker is white or a king and isnt at the top of the board
            if ((currentChecker == whiteChecker || (isKing)) && (yChecker != 0)){
              // if you arent in the middle of a double jump
              if(!infiniteJump){
                // try regualr moves up
                regularMoves(xMove,-1);
                }
              // try jumping moves up
              jumping(xMove,-1);
              }
            // if the checker is black or a king and isnt at the bottom of the board
            if ((currentChecker == blackChecker || (isKing)) && (yChecker != 7)){
              // if you arent in the middle of a double jump
              if(!infiniteJump){
                // try regular moves down
                regularMoves(xMove,1);
                }
              // try jumping moves down
              jumping(xMove,1);
            }
          }

          public void regularMoves(int xMove, int yMove){
            // checks if the moves are available
            if ((xPos == xChecker + xMove) && (yPos == yChecker + yMove)){
              // call ifKing function
              ifKing();
              // change the location of the checker to an empty square
              gridButtons[yChecker][xChecker].setIcon(darkSquare);
              // increase the turn number
              turnNum++;
              // if the checker is at the opponents end
              if (yPos == yRestriction){
                // change the icon to a king
                gridButtons[yPos][xPos].setIcon(currentKing); 
              }
            }
          }

          public void jumping(int xMove, int yMove){
            // if there is a checker right infront of the piece and the square behind it is empty
            if ((xPos == xChecker + (xMove * 2)) && (yPos == yChecker + (yMove * 2)) && (gridButtons[yChecker + yMove][xChecker + xMove].getIcon() == otherChecker || gridButtons[yChecker + yMove][xChecker + xMove].getIcon() == otherKing)){
              // call ifKing function
              ifKing();
              // set the checker to a empty square
              gridButtons[yChecker][xChecker].setIcon(darkSquare);
              // set the enemy checker to a empty square
              gridButtons[yChecker + yMove][xChecker + xMove].setIcon(darkSquare);
              // if the piece is a king or a white checker
              if (isKing == true || (currentChecker == whiteChecker)){
                // call the second jump function with various restrictions to allow for no errors
                if (xPos < 6 && (yPos > 1)){
                  secondJump(1,-1);
                } 
                if (xPos > 1 && (yPos > 1)){
                  secondJump(-1,-1);
                }
              } 
              // if the piece is a king or white checker
              if (isKing == true || (currentChecker == blackChecker)){
                if (yPos < 6 && (xPos < 6)){
                  secondJump(1,1);
                } 
                if (yPos < 6 && (xPos > 1)){
                  secondJump(-1,1);
                }
              }
              // if a second jump is not available
              if (!jump){
                // dont allow for another jump
                infiniteJump = false;
                // next turn
                turnNum++;
              // if a second jump is available
              }else{
                // save the location of the checker
                xChecker = xPos;
                yChecker = yPos;
                // allow for another jump
                infiniteJump = true;
              }
              // check if the piece can be turned into a king
              if (yPos == yRestriction){
                gridButtons[yPos][xPos].setIcon(currentKing); 
              }
              // if the captued piece is not white then reduce the count of black by 1
              if (currentChecker == whiteChecker){
                blackNum--;
              // if the captured piece is white then reduce the count of white by 1
              }else{
                whiteNum--; 
              }
            }
          }

          public void secondJump(int xMove, int yMove){
            // check if a second jump is available
            if ((gridButtons[yPos + yMove][xPos + xMove].getIcon() == otherChecker || gridButtons[yPos + yMove][xPos + xMove].getIcon() == otherKing) && (gridButtons[yPos + (yMove * 2)][xPos + (xMove * 2)].getIcon() == darkSquare)){
              // make jump and piece chosen true
              pieceChosen = true;
              jump = true;
            }
          }

          public void ifKing(){
            // if the piece is a king
            if (isKing){
              // change the icon to a king
              gridButtons[yPos][xPos].setIcon(currentKing);
              // if not
            } else{
              //change to a regular checker
              gridButtons[yPos][xPos].setIcon(currentChecker);
            }
          }
          public void winGame(String colour){
            // create winner frame
            JFrame winFrame = new JFrame("WINNER");
            // make the checkers frame invisible
            frame.setVisible(false);
            // frame setup
            winFrame.setSize(512,544);
            winFrame.setLocation(5,5);
            winFrame.setBackground(darkBrown);
            winFrame.setVisible(true);
            winFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            winFrame.setLayout(null);
            // create text area
            JTextArea winner = new JTextArea();
            // set the text
            winner.setText(colour + "\nIS THE WINNER! \n\nStop and run the \nprogram to play \nagain!");
            // location, colour, etc.
            winner.setBounds(0, 25, 512, 464);
            winner.setBackground(white);
            winner.setFont(new Font("Helvetica", Font.PLAIN, 50));
            winner.setForeground(darkBrown);
            winner.setEditable(false);
            winFrame.add(winner);
          }
        });
        // add the buttons
        frame.add(button);
      }
    }
    // for loops to add the checkers in the correct positions
    for (int y = 0; y < 3; y++){
      for (int x = 0; x < 8; x++){
        if ((y+x) % 2 != 0){
          gridButtons[y][x].setIcon(blackChecker);
        }
      }
    }
    for (int y = 5; y < 8; y++){
      for (int x = 0; x < 8; x++){
        if ((y+x) % 2 != 0){
          gridButtons[y][x].setIcon(whiteChecker);
        }
      }
    }
    // show frame (moved it to the end so that we didnt have to paint the tiles in)
    frame.show();
    // call the instructFrame function
    instructionsFrame();
  }
}