import java.awt.*;

class Screen {

    String s; // Stores the winner
       int x; // Center coordinate for screen's x-axis
       int y; // Center coordinate for screen's y-axis

    public Screen(String s, int x, int y) {
        this.s = s; // User information
        this.x = x; // For storing the center.
        this.y = y; // For storing the center.
    }

    public void drawWinner(Graphics g, int WIDTH, int HEIGHT) {

        // If the user wins, draw the winner screen.
        if (s.equals("USER")) {
            Color col = new Color(0,0,0, 50);
            g.setColor(col);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setFont(new Font("Verdana", Font.PLAIN, 120));
            g.setColor(Color.GREEN);
            g.drawString(" YOU WIN! ", this.x / 4, this.y);

            g.setFont(new Font("Verdana", Font.PLAIN, 40));
            g.setColor(Color.RED);
            g.drawString("Queen bee!", this.x / 2, this.y + 100);
        }

        // If the Computer wins, draw the defeated screen.
        else if (s.equals("COMP")){
            Color col = new Color(0,0,0, 50);
            g.setColor(col);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setFont(new Font("Verdana", Font.PLAIN, 120));
            g.setColor(Color.RED);
            g.drawString("GAME OVER.", this.x / 4, this.y);

            g.setFont(new Font("Verdana", Font.PLAIN, 40));
            g.setColor(Color.BLUE);
            g.drawString("You were stung...", this.x / 2, this.y + 100);
        }

    }
}