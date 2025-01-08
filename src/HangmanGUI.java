import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;                                         // for setting the inside margins of the buttons on the keyboard
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

public class HangmanGUI extends JFrame {
    GameManager game = new GameManager();
    Gallows gallowsPanel;

    HangmanGUI() {                                              // HangmanGUI is an extended JFrame - so set up the frame
        setTitle("Hangman");                              // application title
        setSize(500, 600);                         // canvas size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         // when you close the application the code will stop
        setLayout(new BorderLayout(5, 5));            // specifies the horizontal and vertical gap between the panels
        setMinimumSize(new Dimension(500,600));    // stop the user resizing smaller than the minimum size for the game

        // The top panel used for drawing the gallows
        gallowsPanel = new Gallows();
        gallowsPanel.setSize(700, 300);            // note the drawing is a fixed size and isn't reactive
        gallowsPanel.setMinimumSize(new Dimension(500,300));
        add(gallowsPanel, BorderLayout.NORTH);                  // goes to the top of the window

        // The middle panel used for displaying correct guesses and _ for letters not guessed
        Font wordLabelFont = new Font("SansSerif", Font.BOLD, 25);
        JPanel middlePanel = new JPanel();
        middlePanel.setSize(700, 100);    
        middlePanel.setMinimumSize(new Dimension(500,100));
        JPanel middleGridPanel = new JPanel(new GridLayout(3, 1));     // create a gridpanel inside middlepanel to help control layout
        middlePanel.add(middleGridPanel, BorderLayout.CENTER); 

        // middle panel row 1 - this is the row that shows the current guessed letters in their correct positions in the word
        JLabel wordLabel = new JLabel(game.getGuessDisplayText(), JLabel.CENTER);  // centers the wordLabel
        JPanel wordLabelPanel = new JPanel(new BorderLayout());  // use border layout inside the grid panel so we can control the min height and width
        wordLabel.setFont(wordLabelFont);                                          
        wordLabelPanel.add(wordLabel, BorderLayout.CENTER);
        middleGridPanel.add(wordLabelPanel);

        // middle panel row 2 this is a row for showing instructions or the result
        JPanel eventPanel = new JPanel(new BorderLayout());
        JLabel gameMessageLabel = new JLabel("Click a letter below to make a guess.", JLabel.CENTER);
        eventPanel.add(gameMessageLabel, BorderLayout.SOUTH);                        
        middleGridPanel.add(eventPanel);

        // middle panel row 3 this is where the restart game button is displayed
        JPanel restartGamePanel = new JPanel(new BorderLayout());
        JButton restartGameButton = new JButton("");
        restartGamePanel.add(restartGameButton, BorderLayout.SOUTH);                        
        middleGridPanel.add(restartGamePanel);

        restartGameButton.setEnabled(false);                  // initially restart button is not enabled and the same colour as the backgroud so it's invisibe
        restartGameButton.setBackground(restartGameButton.getBackground());
        restartGameButton.setBorderPainted(false);
        restartGameButton.setFocusPainted(false);

        add(middlePanel, BorderLayout.CENTER);
    
        // Bottom panel for the keyboard
        Font keyFont = new Font("SansSerif", Font.BOLD, 20);                      // larger font for the letter buttons 
        JPanel keyboardPanel = new JPanel(new GridLayout(3, 10, 5, 5)); // creates a grid layout for the keyboard
        String[] keys = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",                      
                         "A", "S", "D", "F", "G", "H", "J", "K", "L", "",
                         "", "Z", "X", "C", "V", "B", "N", "M"};                            // blank array items are for spacing the keyboard
        for (String key : keys) {                                                           // loop through all keys in array, create key objects and events for each one in turn, this is more efficient that specifying each key separately
            JButton keyButton = new JButton(key);                                           // create a button for this key
            keyButton.setFont(keyFont);           
            keyButton.setBackground(Color.WHITE);                            
            keyButton.setMargin(new Insets(2, 2, 2, 2));              // reduce the margin inside the button
            if (key.isEmpty()) {                                                            // disable and "hide" empty buttons - used to space out keys
                keyButton.setEnabled(false);
                keyButton.setBackground(keyboardPanel.getBackground());
                keyButton.setBorderPainted(false);
                keyButton.setFocusPainted(false);
            } else {                                                                        // manage the click action for key on the keyboard that is enabled
                keyButton.addActionListener(new ActionListener() {                          // this is in the for loop so creates one actionlistener per key
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton source = (JButton) e.getSource();                           // get the object for the button clicked
                        if (game.getIsGameOver() == false) {
                            if (game.checkGuess(source.getText().charAt(0)) == true) {  // check the guess to see if the letter is in the word
                                source.setBackground(Color.GREEN);                          // successful guess - green background and disable
                                source.setEnabled(false);
                                wordLabel.setText(game.getGuessDisplayText());              // update the display text showing the letters in the right place
                                boolean isWordGuessed = game.getIsWordGuessed();            // check if word completely guessed
                                if (isWordGuessed) {                                        // if guessed do win ending, enable and show new game button
                                    game.setIsGameOver(true);
                                    gameMessageLabel.setText("You Win!!");
                                    restartGameButton.setEnabled(true);
                                    restartGameButton.setBorderPainted(true);
                                    restartGameButton.setFocusPainted(true);
                                    restartGameButton.setText("Play Again");
                                }
                            } else {
                                source.setBackground(Color.GRAY);                           // change color when pressed
                                source.setEnabled(false);
                                if (game.getGuessesRemaining() <= 0) {                      // if out of guesses do lose ending
                                    game.setIsGameOver(true);
                                    gameMessageLabel.setText("You Lose!!  The word was \""+game.getWord()+"\"");
                                    restartGameButton.setEnabled(true);
                                    restartGameButton.setBorderPainted(true);
                                    restartGameButton.setFocusPainted(true);
                                    restartGameButton.setText("Play Again");
                                }
                                gallowsPanel.updateGallows(game.getGuessesRemaining(), game.getIsGameOver());   // update the gallows
                            }
                        }
                    }
                });
            }
            keyboardPanel.add(keyButton);                                                   // add this key to the panel
        }
        add(keyboardPanel, BorderLayout.SOUTH);                                             // when all keys have action listeners add to the keyboardPanel

        // this action listener is for the new game button, it has to be here because part of what it does it to 
        // enable all of the keys on the keyboard and reset their colours ready for the new game
        // and that means that all the buttons for the keyboard must have been defined before we define this action listener
        restartGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startGame();                                                                   // reset game data
                gallowsPanel.updateGallows(game.getGuessesRemaining(), game.getIsGameOver());       // reset gallows
                gameMessageLabel.setText("");                                                  // remove win/lose message
                // hide new game button 
                restartGameButton.setText("");                                                 // remove text disable and hide new game button
                restartGameButton.setEnabled(false);                            
                restartGameButton.setBackground(restartGameButton.getBackground());
                restartGameButton.setBorderPainted(false);                  
                restartGameButton.setFocusPainted(false);              
                wordLabel.setText(game.getGuessDisplayText());                                      // reset the current word display
                Component[] components = keyboardPanel.getComponents();                             // get the compnents on the panel
                for (Component component : components) {                                            // loop through each component (key) on the panel
                    if (component instanceof JButton && !((JButton) component).getText().isEmpty()) {
                        ((JButton) component).setEnabled(true);                                   // enable all keys and reset colours
                        ((JButton) component).setBackground(Color.WHITE); 
                    }
                }
            }
        });
        setVisible(true);
    }
}
