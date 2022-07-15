import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.LinkedList;

class World {
    int height;
    int  width;

    int numHives;
    Beehive beehive[];

    Computer compOpponent;

    LinkedList<Bees> beesList;

    String state;

    //Background image
    BufferedImage backgroundImage;

    public World(int initWidth, int initHeight, int initNumHives, String state) {
        width  = initWidth;
        height = initHeight;

        numHives = initNumHives;
        beehive  = new Beehive[numHives];

        this.state = state;

        //INITIAL STATE

        //BEES
        //User Bees
        Bees userBees = new Bees("USER", Color.RED);
        //Computer Bees
        Bees compBees = new Bees("COMP", Color.BLUE);
        //No Bees
        Bees noBees = new Bees("no team", Color.GRAY);

        //BEEHIVES
        //left middle >> USER BASE <<
        Beehive b0 = beehive[0] = new Beehive(userBees, 10, 80, 250, 'A');

        //left top
        Beehive b1 = beehive[1] = new Beehive(noBees, 0, 160, 100, 'W');

        //left bottom
        Beehive b2 = beehive[2] = new Beehive(noBees, 0, 360, 400, 'X');

        //middle
        Beehive b3 = beehive[3] = new Beehive(noBees, 0, 460, 250, 'D');

        //right top
        Beehive b4 = beehive[4] = new Beehive(noBees, 0, 560, 100, 'R');

        //right bottom
        Beehive b5 = beehive[5] = new Beehive(noBees, 0, 760, 400, 'V');

        //right middle >> COMP BASE <<
        Beehive b6 = beehive[6] = new Beehive(compBees, 10, 840, 250, 'G');

        //List that keeps track of all the bees that are moving around
        beesList = new LinkedList<Bees>();

        //Adding neighbors so that the program knows which beehives are adjacent to each other
        beehive[0].neighborHivesList.add(b1);
        beehive[0].neighborHivesList.add(b2);
        //
        beehive[1].neighborHivesList.add(b0);
        beehive[1].neighborHivesList.add(b3);
        beehive[1].neighborHivesList.add(b4);
        //
        beehive[2].neighborHivesList.add(b0);
        beehive[2].neighborHivesList.add(b3);
        beehive[2].neighborHivesList.add(b5);
        //
        beehive[3].neighborHivesList.add(b1);
        beehive[3].neighborHivesList.add(b2);
        beehive[3].neighborHivesList.add(b4);
        beehive[3].neighborHivesList.add(b5);
        //
        beehive[4].neighborHivesList.add(b1);
        beehive[4].neighborHivesList.add(b3);
        beehive[4].neighborHivesList.add(b6);
        //
        beehive[5].neighborHivesList.add(b2);
        beehive[5].neighborHivesList.add(b3);
        beehive[5].neighborHivesList.add(b6);
        //
        beehive[6].neighborHivesList.add(b4);
        beehive[6].neighborHivesList.add(b5);
        //

        //Creates computer user
        compOpponent = new Computer(this, "");
    }

    //Sets the background image of the game
    public void setBackgroundImage(){

        //Code for this method was based on the code posted on https://docs.oracle.com/javase/8/docs/technotes/guides/imageio/spec/apps.fm1.html

        BufferedImage image = null;
        File background = null;
        try {
            background = new File("Background.jpeg");
        } catch (NullPointerException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
            System.out.println("Please load Background.jpeg || See Main.java, setBackgroundImage() method");
        }

        try {
            image = ImageIO.read(background);
        } catch (IOException | NullPointerException | SecurityException e) {
            e.printStackTrace();
            System.out.println("Please load Background.jpeg || See Main.java, setBackgroundImage() method");
        }

        this.backgroundImage = image;
    }

    //Draws the road on the map
    public void drawRoads(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(20));
        g.setColor(Color.WHITE);
        //left top to left middle
        g.drawLine((int) (beehive[1].position.x + 30), (int) (beehive[1].position.y + 30), (int) (beehive[0].position.x + 30), (int) (beehive[0].position.y + 30));
        //right top to left top
        g.drawLine((int) (beehive[4].position.x + 30), (int) (beehive[4].position.y + 30), (int) (beehive[1].position.x + 30), (int) (beehive[1].position.y + 30));
        //right middle to right top
        g.drawLine((int) (beehive[6].position.x + 30), (int) (beehive[6].position.y + 30), (int) (beehive[4].position.x + 30), (int) (beehive[4].position.y + 30));
        //right bottom to right middle
        g.drawLine((int) (beehive[5].position.x + 30), (int) (beehive[5].position.y + 30), (int) (beehive[6].position.x + 30), (int) (beehive[6].position.y + 30));
        //left bottom to right bottom
        g.drawLine((int) (beehive[2].position.x + 30), (int) (beehive[2].position.y + 30), (int) (beehive[5].position.x + 30), (int) (beehive[5].position.y + 30));
        //left middle to left bottom
        g.drawLine((int) (beehive[0].position.x + 30), (int) (beehive[0].position.y + 30), (int) (beehive[2].position.x + 30), (int) (beehive[2].position.y + 30));

        //Draw four lines that connect the central beehive[3] to left top[1], left bottom[2], right top[4], and right bottom[5]
        for (int i = 1; i < 6; i++) {
            if (i != 3) {
                g.drawLine((int) (beehive[3].position.x + 30), (int) (beehive[3].position.y + 30), (int) (beehive[i].position.x + 30), (int) (beehive[i].position.y + 30));
            }
        }
    }

    /*
    Calls in paintComponent method
    loops through all beehives and calls the draw method in the Beehive class to draw them.
     */
    public void drawBeehives(Graphics g) {
        for (Beehive b : beehive) {
            b.draw(g);
        }
    }

    /*
    Takes in a string to figure out who is the winner
    calls the drawWinner method in the screen class to draw thee end screen accordingly.
     */
    public void drawEndScreen (String s, Graphics g, int WIDTH, int HEIGHT){
        if (s.equals("userWins")){
            Screen userWins = new Screen("USER", WIDTH / 2, HEIGHT / 2);
            userWins.drawWinner(g, WIDTH, HEIGHT);
        }
        else if (s.equals("compWins")){
            Screen compWins = new Screen("COMP", WIDTH / 2, HEIGHT / 2);
            compWins.drawWinner(g, WIDTH, HEIGHT);
        }
    }

    /*
    Loops through all the beehives
    calls the update method in the Beehive class,
    which will produce bees and set the beehive to neutral if its count is 0
    */
    public void updateBeehives() {
        for (Beehive b : beehive) {
            try {
                b.update();
            } catch (NullPointerException | ConcurrentModificationException e) {
            }
        }
    }

    /*
    Loops through all the bees that are sent out from a beehive
    calls the draw method in the Bees class to draw them
     */
    public void drawBees(Graphics g) {
        while (true) {
            try {
                for (Bees b : beesList) {
                    b.draw(g);
                }
                break;
            } catch (ConcurrentModificationException e) {
            }
        }
    }

    /*
    Loops through all the bees that are sent out from a beehive
    calls the update method in the Bees class,
    which updates its movement and makes it fly from on beehive to another
    removes bees that reach its goal beehive and that collide with opponent bees.
    */
    public void updateBees(double time) {
        boolean runLoop = true;
        while (runLoop) {
            try {
                for (Bees b : beesList) {
                    b.update(time);
                }
                runLoop = false;
            } catch (ConcurrentModificationException e) {
            }
        }

        //Check bees for 0 distance. If so, remove from list.
        Bees.updateRemove(this);

        //Check for bees that overlap. If so, remove from list.
        Bees.collisionWithBee(this);
    }

    /*
    Calls the update method in the Computer,
    which will decide the computer strategy and add/remove computer beehive if it occupies/loses a beehive
    */
    public void updateComputer(World w) {
        try {
            w.compOpponent.update(this);
        } catch (NullPointerException | ConcurrentModificationException e) {
        }
    }

    /*
    Loops through all the beehives and checks their ownerships
    if all thee beehives belong to one player, that player will be the winner
    */
    public static boolean isWinner(World w, String winner) {
        boolean isWinner = true;
        for (Beehive b : w.beehive) {
            if (!b.getOwnership().equals(winner)) {
                isWinner = false;
                break;
            }
        }
        return isWinner;
    }

    /*
    Checks if the loser has any beehive left
    if yes, returns false => not loser
     */
    public boolean noPlayerBeesRemain(World w, String loser) {
        boolean isTrue = true;

        LinkedList<Bees> newTempList = new LinkedList<Bees>(w.beesList);

        for (Bees b : newTempList) {
            if (b.getOwnership().equals(loser)) {
                isTrue = false;
                break;
            }
        }
        return isTrue;
    }


}

public class Main extends JPanel implements KeyListener {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 540;
    public static final int FPS = 10000;
    World world;

    public void run(String level, String gameState) {

        //The ScheduledExecutorService code for this method was based on the code posted on https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ScheduledExecutorService.html

        //Sets the game to "start" and sets the difficulty level based on the User's choice
        world.state = gameState;
        world.compOpponent.level = level;

        //If the state is "Game", update the beehives and set the time for the computer according to the difficulty level
        if (world.state.equals("Game")) {

            //Beehive Reproduction
            final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    world.updateBeehives();
                }
            }, 0, 2, TimeUnit.SECONDS);

            //Computer Strategy
            final ScheduledExecutorService executorCompUser = Executors.newSingleThreadScheduledExecutor();
            executorCompUser.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    //Computer player does the strategy
                    world.updateComputer(world);
                }
            }, 3, world.compOpponent.getTime(), TimeUnit.SECONDS);

        }

        //If the state is "Game", update the bees
        while (world.state.equals("Game")) {
            //Bee Movement
            world.updateBees(1.0 / (double) FPS);

            repaint();

            //If there is a winner, break out of the loop.
            if (World.isWinner(world, "USER") && world.noPlayerBeesRemain(world, "COMP")) {
                //draw winner screen for User
                world.state = "userWins";
                repaint();
                break;
            }
            if (World.isWinner(world, "COMP") && world.noPlayerBeesRemain(world, "USER")) {
                //draws winner screen for Computer
                world.state = "compWins";
                repaint();
                break;
            }

            try {
                Thread.sleep(1000 * 50 / FPS);
            } catch (NullPointerException | ConcurrentModificationException | InterruptedException e) {
            }
        }

    }

    public Main() {
        world = new World(WIDTH, HEIGHT, 7, "Start");
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //Loads background image
        world.setBackgroundImage();
        //Adds KeyListener
        addKeyListener(this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Beehive Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Menu startInstance = new Menu();
        frame.setContentPane(startInstance);

        startInstance.AddButtons(frame);

        frame.setLayout(null);  //Uses no layout managers
        frame.setVisible(true); //Makes the frame visible
        frame.pack();

        sound();//Begins the soundtrack

        //Ends the start screen after the user chooses the difficulty level and clicks "start game"
        while (!startInstance.beginGame()) {

            try {
                Thread.sleep(1 / FPS);
            } catch (NullPointerException | ConcurrentModificationException | InterruptedException e) {
            }
        }

        Main mainInstance = new Main();
        frame.setContentPane(mainInstance);

        frame.setVisible(true);
        frame.pack();
        mainInstance.run(startInstance.getLevel(), startInstance.getGamestate());
    }

    private static void sound() {

        //Code for this method was based on the code posted on https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html

        try {
            // Open an audio input stream.
            File soundFile = new File("BumblebeeTrack.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
            System.out.println("Please load BumblebeeTrack.wav || See Main.java, sound() method");
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        switch (world.state) {
            //If the state is "Game", draws the map, the beehives, the bees, and the background.
            case "Game":
                //Draws background image.
                g.drawImage(world.backgroundImage, 0, 0, null);

                //draws the map
                world.drawRoads(g);
                world.drawBeehives(g);
                world.drawBees(g);
                break;
            //If the state is changed to "userWins", draws the end screen.
            case "userWins":
                //Draws an end screen that says the user wins.
                world.drawEndScreen("userWins", g, WIDTH, HEIGHT);
                break;
            //If the state is changed to "comprWins", draws the end screen.
            case "compWins":
                //Draws an end screen that says the computer wins.
                world.drawEndScreen("compWins", g, WIDTH, HEIGHT);
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();
    }

    //Sets the selectedIndex and releasedIndex to default (-1)
    private static int selectedIndex = -1;
    private static int releasedIndex = -1;

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        /*
        Takes in what the user puts on the keyboard and sets the index to corresponding beehives.
        e.g. 'a' or 'A' corresponds to beehive[0]
        */

        int index;
        switch (c) {
            case 'a':
            case 'A':
                index = 0;
                break;
            case 'w':
            case 'W':
                index = 1;
                break;
            case 'x':
            case 'X':
                index = 2;
                break;
            case 'd':
            case 'D':
                index = 3;
                break;
            case 'r':
            case 'R':
                index = 4;
                break;
            case 'v':
            case 'V':
                index = 5;
                break;
            case 'g':
            case 'G':
                index = 6;
                break;
            //The default index is -1
            default:
                index = -1;
                try {
                    //Sets the Selected variable and the Released variable to default (false)
                    world.beehive[selectedIndex].setBooleanDefault();
                    world.beehive[releasedIndex].setBooleanDefault();
                } catch (ArrayIndexOutOfBoundsException error) {
                    /*
                    If the user presses a key that does not correspond to any of the beehives, e.g. esc,
                    selectedIndex and releasedIndex are set back to default (-1)
                    */
                    selectedIndex = -1;
                    releasedIndex = -1;
                }
                break;
        }

        /*
        If a beehive is selected, the selectedIndex is changed to that beehive's index
        and sets the boolean variable (Selected) in beehive to be true.
        */
        if (selectedIndex == -1 && index != -1) {
            selectedIndex = index;
            world.beehive[selectedIndex].Selected = true;
        }

        else if (selectedIndex != -1 && releasedIndex == -1 && index != -1) {
            releasedIndex = index;
        }

        /*
        If a beehive is selected as the starthive and another one is selected as the goalhive,
        it creates a new bee flying from the starthive to the goalhive
        and sets Selected and Released variable back to default (false)
        */

        if (selectedIndex != -1 && releasedIndex != -1) {
            //Creates new bee
            Bees.createBee(world, world.beehive[selectedIndex], world.beehive[releasedIndex], "USER");

            //Resets to default
            world.beehive[selectedIndex].setBooleanDefault();
            world.beehive[releasedIndex].setBooleanDefault();

            //Resets indexes
            selectedIndex = -1;
            releasedIndex = -1;
        }
    }

    //Need this for KeyListener. Reads input.
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }
}