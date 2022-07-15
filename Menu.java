import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//Code for the buttons was based on the code posted on https://www.mainjava.com/swing/java-jbutton-tutorial-with-actionlistener-programming-examples/

class Menu extends JPanel {
    //WIDTH and HEIGHT sets the frame of the program graphics.
    public static final int WIDTH  = 960;
    public static final int HEIGHT = 540;

    protected JButton easy, medium, hard, startgame; //Creates variables for the buttons to be used on the frame.
    private BufferedImage image;                     //Creates the variable for the image background.
    private String level = "";                       //Will store the selected level of difficulty
    private String gamestate = "Start";              //Will store whether the game should begin or not.

    public void AddButtons(Frame frame) {
        hard = new JButton("HARD");     //creating instance of JButton for "HARD"
        hard.setBounds(730,220,120,40);
        hard.addActionListener(actions);

        medium = new JButton("MEDIUM"); //creating instance of JButton for "MEDIUM"
        medium.setBounds(730,170,120,40);
        medium.addActionListener(actions);

        easy = new JButton("EASY");    //creating instance of JButton for "EASY"
        easy.setBounds(730,120,120,40);
        easy.addActionListener(actions);

        startgame = new JButton("Start Game");//creating instance of JButton
        startgame.setBounds(600,400,120, 40);
        startgame.addActionListener(actions);

        //Adding buttons in JFrame
        frame.add(this.hard);
        frame.add(this.medium);
        frame.add(this.easy);
        frame.add(this.startgame);
    }

    private ActionListener actions = new ActionListener() {

        //Shows the actions performed when each button is clicked.

        @Override
        public void actionPerformed(ActionEvent e)
        {

            if(e.getSource() == hard)
            {
                level = "HARD";
            }
            if(e.getSource() == medium)
            {
                level = "MEDIUM";
            }
            if(e.getSource() == easy)
            {
                level = "EASY";
            }
            else if(e.getSource() == startgame)
            {
                /*
                If the User fails to selected a level before clicking "Start Game,"
                a pop-up will appear and ask the user to select a level.
                 */
                if(level.equals("")) msg(); //Calls pop-up method.

                else {
                    gamestate = "Game";
                }
            }
        }
    };

    //Returns true if game level has been selected and the user clicked on "Start Game"
    public boolean beginGame(){
        return !(this.level.equals("")) && this.gamestate.equals("Game");
    }

    public String getLevel(){return level;}

    public String getGamestate(){return gamestate;}

    public void msg() {
        //Message pops up when the user does not select a level of difficulty
        JOptionPane.showMessageDialog(null, "Please select Computer difficulty.");
    }

    public Menu() {
        //Sets the dimensions of the screen to Width and Height
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //Sets the background image of the game
        this.setBackgroundImage();
    }

    public void paintComponent(Graphics g) {
        //This is the image that first appears on the start screen and the information about the game rules

        super.paintComponent(g);
        g.drawImage(image,0,0,null);
        g.drawString("In this game, you and the computer will each have a beehive to ", 80, 120);
        g.drawString("begin with (beehive A and beehive G respectively). All the other ", 80, 140);
        g.drawString("beehives are unoccupied. Your task is to annihilate the computer’s ", 80, 160);
        g.drawString("bees and occupy every beehive. Each beehive that has bees in it ", 80, 180);
        g.drawString("will reproduce. The number of bees in every beehive is displayed ", 80, 200);
        g.drawString("in a circle next to it.", 80, 220);
        g.drawString("You can only send your bees to adjacent beehives connected by roads.", 80, 250);
        g.drawString("You can select a beehive that you wish to send bees from ", 80, 270);
        g.drawString("by pressing the letter that corresponds to it on the keyboard,", 80, 290);
        g.drawString("and a label “SELECTED” will be displayed above that beehive.", 80, 310);
        g.drawString("Then you can press the letter that corresponds to the beehive", 80, 330);
        g.drawString("you want to send bees to. A bee will then be successfully sent.", 80, 350);
        g.drawString("You can deselect a selected beehive by pressing on any key that", 80, 370);
        g.drawString("does not point to the selected beehive's neighbors.", 80, 390);
        g.drawString("Choose your opponent's difficulty level and start the game!", 80, 420);

        g.drawString("Select your opponent's difficulty level:", 670, 90);
    }

    private void setBackgroundImage(){
        //Imports the image background from files and set the variable 'image' to the image

        //Code for this method was based on the code posted on https://docs.oracle.com/javase/8/docs/technotes/guides/imageio/spec/apps.fm1.html

        BufferedImage image = null;
        File background = null;
        try {
            background = new File("Background1.jpg");
        } catch (NullPointerException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
            System.out.println("Please load Background1.jpg || See Menu.java, setBackgroundImage() method");
        }

        try {
            image = ImageIO.read(background);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        this.image = image;

    }
}