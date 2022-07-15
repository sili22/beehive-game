import java.awt.*;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


class Beehive{
    double count;
    Bees bee;
    Color color;
    String ownership;
    Pair position;
    char hiveChar;
    LinkedList<Beehive> neighborHivesList;
    boolean Selected;

    static BufferedImage Hive;

    public Beehive(Bees bee, int count, int x, int y, char hiveChar){
        //Each beehive has a count of the bees,
        //the owner and color of the beehive.
        //the hiveChar represents the key that the User presses for selection.

        this.bee = bee;
        this.color = bee.color;          // Initial beehive color determined by bees in the hive.
        this.ownership = bee.ownership;  // Initial beehive ownership determined by bees in the hive.
        this.count = count;              // Number of bees in hive
        this.position = new Pair (x, y); // Coordinates
        this.hiveChar = hiveChar;        // The key that the User presses for selection.
        Selected = false;

        this.neighborHivesList = new LinkedList<>(); // Neighbor beehives stored in LinkedList

        //Sets this.hive to the imported image with a colored layout background
        setHive();
    }
    public void draw(Graphics g){
        Color c = g.getColor();
        Graphics2D g2d = (Graphics2D) g;

        //Draw background color
        g.setColor(color);
        //               right 1                       right 2                    right 3                    right 4                    center                     left 4                     left 3                   left 2                     left 1                   bottom left                 bottom right
        int[] xpoints = {(int)(this.position.x) + 65, (int)this.position.x + 63, (int)this.position.x + 55, (int)this.position.x + 40, (int)this.position.x + 28, (int)this.position.x + 16, (int)this.position.x + 3, (int)this.position.x - 5, (int)this.position.x - 5, (int)this.position.x + 15, (int)this.position.x + 48};
        int[] ypoints = {(int)this.position.y + 52, (int)this.position.y + 30, (int)this.position.y + 10, (int)this.position.y - 5, (int)this.position.y - 9, (int)this.position.y - 4, (int)this.position.y + 10, (int)this.position.y + 30, (int)this.position.y + 55, (int)this.position.y + 60, (int)this.position.y + 60};

        //Beehive polygon that represents the colored layout of the beehive
        g.fillPolygon(xpoints, ypoints, 11);

        //Draws hive sprite
        g.drawImage(Hive, (int)(position.x) - 32, (int)(position.y) - 34, null);

        //Draws the count of bees in each beehive
        g.setColor(this.color);
        g.fillOval((int)(this.position.x) - 12, (int)(this.position.y) - 33, 37, 25);
        g.setColor(Color.WHITE);
        Font font1 = new Font("Verdana", Font.BOLD, 13);
        g.setFont(font1);
        g.drawString((count < 10) ? " " + (int)count : Integer.toString((int)count), (int)(this.position.x) - ((count >= 100) ? 6 : 2), (int)(this.position.y) - 16);

        //Draws the character of the beehive to be selected
        g2d.setStroke(new BasicStroke(12));
        g.setColor(Color.WHITE);
        Font font2 = new Font("Chalkboard", Font.BOLD, 30);
        g.setFont(font2);
        g.drawString(Character.toString(hiveChar), (int)this.position.x + 18, (int)this.position.y + 35);

        //When selected, it draws on top of the selected beehive that the beehive has been selected
        if(Selected){
            g2d.setStroke(new BasicStroke(15));
            g.setColor(Color.WHITE);
            g.drawLine((int)(position.x), (int)(position.y) - 5 , (int)(position.x) +  55, (int)(position.y) - 5);
            g.setColor(Color.RED);
            Font font3 = new Font("Verdana", Font.BOLD, 10);
            g.setFont(font3);
            g.drawString("SELECTED", (int)this.position.x, (int)this.position.y);
        }
    }

    public void setHive(){
        //Sets the image of the hive onto a colored background

        //Code for this method was based on the code posted on https://docs.oracle.com/javase/8/docs/technotes/guides/imageio/spec/apps.fm1.html

        BufferedImage image = null;
        File background = null;
        try {
            background = new File("Hive.png");
        } catch (NullPointerException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
            System.out.println("Please load Hive.png || See Beehive.java, setHive() method");
        }

        try {
            image = ImageIO.read(background);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        Hive = image;
    }

    public void update(){
        //Check if hive count is 0, if so set to neutral
        if (this.count == 0) {
            this.changeOwnership("no team");
        }
        else { //If count !=0, produce bees.
            produceBees();
        }
    }

    public void produceBees(){
        //Increases the count of bees in each beehive when the beehive count is over zero.
        if(count > 0){
            this.count += 1;
        }
    }

    public static boolean A_has_B_as_Neighbor(Beehive a, Beehive b){
        //Identifies the neighbours of a beehive
        boolean isNeighbor = false;

        for(Beehive aNeighbor : a.neighborHivesList){
            if(aNeighbor == b){
                isNeighbor = true;
                break;
            }
        }
        return isNeighbor;
    }

    public void collideWithBee(String beeOwnership){
        if (!ownership.equals("no team")){
            //Check if the owners are the same
            if (beeOwnership.equals(ownership)){
                count++;
            }
            else {
                count--;
                /*
                When the beehive does not have any bee,
                the ownership of the beehive is set to "no team".
                 */
                if (count == 0){
                    changeOwnership("no team");
                }
                /*
                When the beehive count is less than one,
                the ownership is changed to the ownership of the arriving bee.
                 */
                else if (count < 0){
                    changeOwnership(beeOwnership);
                    count = -1 * count;
                }
            }
        }
        else {
            //Changes the ownership when one user attacks the other user or takes over the other user's beehive
            changeOwnership(beeOwnership);
            count++;
        }
    }

    public double getCount(){
        return count;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setBooleanDefault(){
        this.Selected = false;
    }

    public void changeOwnership (String o){

        //Assigns new ownership to Beehive
        ownership = o;

        //Assigns new color to Beehive
        if (ownership.equals("no team")) {
            this.color = Color.gray;
        }
        else if (ownership.equals("USER")){
            this.color = Color.red;
        }
        else if(ownership.equals("COMP")){
            this.color = Color.blue;
        }
    }

    public String toString() {
        return "The owner is: " + this.ownership + "The count is: " + this.count + "The color is :" +
                this.color + "The position x is: " + this.position.x + "The position y is:" + this.position.y + bee;
    }

}

