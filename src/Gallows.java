import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/*
 * this class is just for drawing the gallows and the hang man
 */
public class Gallows extends JPanel {
    private int guessesRemaining= -1;
    boolean isGameOver;
    int xBodyStart = 225;
    int yBodyStart = 150;
    int diameterHead = 48;                                                          // body parts relative to head size, so I could adjust the drawing easier
    int xBodyEnd = xBodyStart;
    int yBodyEnd = diameterHead+yBodyStart;                                         // length of body 1 * head size
    int yHeadPos = (int) yBodyStart - (diameterHead);
    int xArmStart = xBodyStart;
    int xLegStart = xBodyStart;
    int yArmStart = (int) (yBodyStart + (0.2 * (yBodyEnd - yBodyStart)));           // arms start 1/5th along the body           
    int xRightArmEnd = (int) (((0.9 * (yBodyEnd - yBodyStart))) + xBodyStart);      
    int xLeftArmEnd = (int) (xBodyStart - ((0.9 * (yBodyEnd - yBodyStart))));            
    int yArmEnd = (int) (yBodyStart - (0.2 * (yBodyEnd - yBodyStart)));             // arms raised by 1/5ths of body length
    int yLegStart = yBodyEnd;
    int xRightLegEnd = (xLegStart + (int) (0.8 * (yBodyEnd - yBodyStart)));
    int xLeftLegEnd =  (xLegStart - (int) (0.8 *  (yBodyEnd - yBodyStart)));
    int yLegEnd = (yLegStart + (int) (0.8 * (yBodyEnd - yBodyStart)));            

    public Gallows() {
        setPreferredSize(new Dimension(300, 300));                     // constructor sets size for drawing
    }

    /**
     * This updates the gallows, based on the number of guesses remaining and if the game is over
     * @param guessesRemaining
     * @param isGameOver
     */
    public void updateGallows(int guessesRemaining, boolean isGameOver) {
        this.guessesRemaining = guessesRemaining;
        this.isGameOver = isGameOver;
        repaint(); // Repaint the panel to update the drawing
    }

    /**
     * create a paint component and draw the gallows based on the current number of guesses remaining
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGallows(g, guessesRemaining);
    }

    /**
     * on the paint component passed (g) draw the gallowa and hangman based on how many guesses remaining
     * @param g
     * @param guessesRemaining
     */
    private void drawGallows(Graphics g, int guessesRemaining) {
        g.setColor(Color.BLUE);
        switch (guessesRemaining) {               
            case 0: // right leg
                if (!isGameOver) g.drawLine(xLegStart, yLegStart, xRightLegEnd, yLegEnd);
                else g.drawLine(xLegStart-4, yLegStart, (int)(xRightLegEnd-(0.6*diameterHead)), yLegEnd);
            case 1: // left leg
                if (!isGameOver) g.drawLine(xLegStart, yLegStart, xLeftLegEnd, yLegEnd);
                else g.drawLine(xLegStart-4, yLegStart, (int) (xLeftLegEnd+(0.6*diameterHead)), yLegEnd);
            case 2: // right arm
                if (!isGameOver) g.drawLine(xArmStart,yArmStart, xRightArmEnd, yArmEnd);
                else g.drawLine(xArmStart+4,yArmStart, (int) (xRightArmEnd-(0.7*diameterHead)), (int) yArmEnd+(diameterHead));
            case 3: // left arm
                if (!isGameOver) g.drawLine(xArmStart,yArmStart, xLeftArmEnd, yArmEnd);
                else g.drawLine(xArmStart+4,yArmStart, (int) (xLeftArmEnd+(0.7*diameterHead)), (int) yArmEnd+(diameterHead));
            case 4: // body
                if (!isGameOver) g.drawLine(xBodyStart, yBodyStart,xBodyEnd, yBodyEnd);
                else g.drawLine(xBodyStart+4, yBodyStart,xBodyEnd-4, yBodyEnd);
            case 5: // head
                g.drawOval(xBodyStart-(diameterHead/2), yHeadPos, diameterHead, diameterHead);
            case 6: // gallows
                g.setColor(Color.BLACK);
                g.fillRect(85, 250, 100, 15);              // base left
                g.fillRect(185+100-30, 250, 50, 15);         // base right  
                g.fillRect(125, 50, 15, 200);              // vertical
                g.fillRect(125, 50, 100, 15);              // top
                g.drawLine(225, 60, 225, 100);                  // rope
                if(!isGameOver) {
                    g.fillRect(185-10, 250-10, 90, 10);       // box - gets removed when game over
                }
                
        }
    }
}
