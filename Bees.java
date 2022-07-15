import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;

class Bees{
    public String ownership;
    public Color color;
    Beehive startHive;
    Beehive goalHive;
    Pair startPosition;
    Pair goalPosition;
    double d;
    Pair position;
    Pair velocity;
    int width;

    static BufferedImage beeSprite;

    //For debugging
    int beeNumber;

    private static int beeCount = 0;

    //This constructor is just for the initialization of the beehives. Bees are created using the constructor below this.
    public Bees (String ownership, Color color){
        this.ownership = ownership;
        this.color     = color;

        //Sets this.beeSprite
        setBeeSprite();
    }

    //The constructor is used to create bees when they are sent from one beehive to another
    public Bees (String ownership, Color color, Beehive startHive, Beehive goalHive){
        this.ownership = ownership;
        this.color     = color;
        this.position  = new Pair (startHive.position.x, startHive.position.y);
        this.startHive = startHive;
        this.goalHive  = goalHive;

        this.startPosition = new Pair (startHive.position.x, startHive.position.y);
        this.goalPosition  = new Pair (goalHive.position.x, goalHive.position.y);

        //This is used to make the speed of all the bees constant no matter which beehives they fly from and are flying to
        double l = Math.sqrt((goalPosition.x - startPosition.x)*(goalPosition.x - startPosition.x) + (goalPosition.y - startPosition.y)*(goalPosition.y - startPosition.y));
        //The velocity of each bee is calculated based on the coordinates of their starting point and destination
        this.velocity = new Pair (((goalPosition.x - startPosition.x)/l) * 4000, ((goalPosition.y - startPosition.y)/l) * 4000);

        this.width = 10;

        //For debugging
        this.beeNumber = beeCount;
        beeCount++;
    }

    //Draws the bees
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(12));
        g.setColor(color);
        g.drawLine((int)(position.x) + 20, (int)(position.y)+ 20, (int)(position.x) + 20, (int)(position.y) + 31);

        //Draw Bee Sprite
        g.drawImage(beeSprite, (int)(position.x), (int)(position.y), null);

    }

    //Sets up the image of bees
    public void setBeeSprite(){

        //Code for this method was based on the code posted on https://docs.oracle.com/javase/8/docs/technotes/guides/imageio/spec/apps.fm1.html

        BufferedImage image = null;
        File background = null;
        try {
            background = new File("Bee.png");
        } catch (NullPointerException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
            System.out.println("Please load Bee.png || See Bees.java, setBeeSprite() method");
        }

        try {
            image = ImageIO.read(background);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("Error in loading Bee sprite || See Beehive.java, setHive() method");
        }

        beeSprite = image;
    }

    //Returns the ownership of a bee
    public String getOwnership(){
        return ownership;
    }

    /*
    Removes a bee when it reaches its destination beehive
    calls the collideWithBee method in Beehive to update the state of the destination beehive
     */
    public static void updateRemove(World w){
        //creates a linked list of bees that are removed
        LinkedList<Bees> removeList = new LinkedList<Bees>();

        while(true) {
            try {
                for (Bees b : w.beesList) {
                    //If the distance between a bee and its goalhive is equal or smaller than 0
                    if (b.d <= 0) {

                        //Calls the collideWithBee method in the Beehive class to update the goalhive
                        b.goalHive.collideWithBee(b.getOwnership());

                        //Adds this bee to the list of removed bees
                        removeList.add(b);
                    }
                }
                break;
            } catch (NullPointerException | ConcurrentModificationException e) {
            }
        }

        //Removes the bee that arrives from the list of bees
        for(Bees b : removeList) {
            w.beesList.remove(b);
        }

        //Resets the remove list
        removeList.clear();
    }

    //updates the movement of bees
    public void update(double time){
        //Variable d is the distance between a bee and its goal beehive
        d = getD();
        //If the bee has not reached its goal beehive, keep updating its position
        if (d > 0){
            position = position.add(velocity.times(time));
        }
    }

    //Calculates the distance between a bee and its goal beehive
    public double getD(){
        return (int)(Math.sqrt((goalPosition.x - position.x)*(goalPosition.x - position.x) + (goalPosition.y - position.y)*(goalPosition.y - position.y)));
    }

    //Creates a beehive according to the selected and released beehive
    public static void createBee(World w, Beehive selected, Beehive released, String me) {
        if ((selected != released)                 //Beehive is not selecting itself
                && (selected.count > 0)            //Beehive has bees
                && (selected.ownership.equals(me)) //Beehive is owned by player
                && (Beehive.A_has_B_as_Neighbor(selected, released))) { //Beehive A has B as neighbor

            //Create new bee and append to beesDS
            w.beesList.add(new Bees(selected.ownership, selected.color, selected, released));

            //Subtract 1 from selected beehive.
            selected.count--;

            //If Computer creates a bee, wait for a bit.
            if(selected.getOwnership().equals("COMP")){
                try{Thread.sleep(100);}
                catch(InterruptedException ignored){}
            }
        }
    }

    //Makes a bee disappear when it collides with an opponent bee
    public static void collisionWithBee(World w){
        //Creates a list to record bees that collide with an opponent's bee
        LinkedList<Bees> removeList = new LinkedList<Bees>();

        while(true) {
            try {
                for (Bees b1 : w.beesList) {
                    //if b1 is not in the remove list
                    if(!(removeList.contains(b1))){
                        for (Bees b2 : w.beesList) {
                            //if b1 and b2 are not the same bee
                            if (b1 != b2
                                    //and they are opponents
                                    && !(b1.getOwnership().equals(b2.getOwnership()))
                                    //and b2 is not in the remove list
                                    && !(removeList.contains(b2))
                                    //and b1 and b2 collides
                                    && b1.position.hasCollided(b2.position)) {
                                //add both b1 and b2 to the remove list
                                removeList.add(b1);
                                removeList.add(b2);
                            }
                        }
                    }
                }
                break;
            } catch (NullPointerException | ConcurrentModificationException e) {
            }
        }

        //Remove all the bees on the remove list from the bee list
        for(Bees b : removeList) {
            w.beesList.remove(b);
        }

        //Resets the remove list
        removeList.clear();
    }

    //Returns information about a specific bee
    public String toString() {
        return "BEE #: " + beeNumber + "|| owner: " + this.ownership
                + "startHive: " + this.startHive.hiveChar + "goalHive: " + this.goalHive
                + "Distance (b & hive): " +  this.d + "velocity: " + this.velocity;
    }
}

//A pair class to handle the movement of bees
class Pair {
    double x;
    double y;

    public Pair(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //Updates the position of a bee
    public Pair add(Pair p) {
        return new Pair(x + p.x, y + p.y);
    }

    //Calculates a bee's position after a period of time
    public Pair times(double t) {
        return new Pair(x * t, y * t);
    }

    //Determines whether one bee collides with another
    public boolean hasCollided(Pair p) {
        //Returns true if the distance between the two bees is less than 0.5
        return (Math.sqrt((this.y - p.y)*(this.y - p.y) + (this.x - p.x)*(this.x - p.x))) < 0.5;

    }
}
