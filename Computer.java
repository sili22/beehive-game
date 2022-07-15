import java.util.LinkedList;

class Computer {
    String level; //One of the following: "EASY" "MEDIUM" or "HARD"
    long   time;
    LinkedList<Beehive> compHivesList; // Keeps track of all beehives owned by the Computer.

    public Computer(World w, String s) {
        this.level = s;

        this.compHivesList = new LinkedList<Beehive>();

        addInitialCompHive(w); // Adds initial beehives owned by Computer into the compHivesList.
    }

    // Adds initial beehives owned by Computer into the compHivesList.
    public void addInitialCompHive(World w) {
        for(Beehive b : w.beehive){
            if (b.getOwnership().equals("COMP")) {
                compHivesList.add(b);
            }
        }
    }

    //Computer difficulty level determines how often the Computer makes a move.
    public long getTime(){
        switch (this.getLevel()) {
            case "EASY":
                this.time = 5; // EASY mode makes a move every 5 seconds.
                break;
            case "MEDIUM":
                this.time = 3; // MEDIUM mode makes a move every 3 seconds.
                break;
            case "HARD":
                this.time = 1; // HARD mode makes a move every second.
                break;
            default:
                this.time = 0; // Will never be reached.
                break;
        }
        return this.time; // Returns time according to difficulty.
    }

    //Computer decision making.
    //doStrategy() determines when and how the Computer should send bees from one hive to another.
    public void doStrategy(World w) {

        // If there are no opponent bees, invade all the beehives.
        if(calcOpponentTotal(w) == 0){
            invadeAll(w);
        }

        // If the Computer has 50+ more bees than the Human player:
        else if(calcOpponentTotal(w) < calcCompTotal(w) - 50){
            // Activate breakStalemate method and run it 3 times. (see explanation below)
            for(int i = 0; i < 3; i++) breakStalemate(w);
            // Activate defense method (see explanation below)
            defense(w);
        }
        else{
            // Default: activate offense method. (see explanation below)
            offense(w);
        }

    }

    //DEFENSE METHOD
    /*
    This method makes sure that all beehives have around the same number of bees in each Computer-owned hive.
    If one beehive has less than the beehive average of Computer minus 5, the Computer will send bees to that
    hive from its neighbor hives.
    */
    private void defense(World w){
        Beehive min = null; // min will hold the beehive with the minimum value
        try {
            int avg = calcAverage();
            for(Beehive b : compHivesList){
                if(b.getCount() < avg - 5){
                    min = b;
                }
            }
            for(Beehive b : min.neighborHivesList){
                Bees.createBee(w, b, min, "COMP"); // Send a bee to the min beehive from all of its neighbors.
            }

        }catch(NullPointerException e){}
        // If there is no Beehive that meets the condition, a NullPointerException will be thrown and ignored.
    }

    //Calculate the average of Computer's bees in beehives.
    private int calcAverage() {
        int totalcount = 0;
        for(Beehive b : compHivesList){
            totalcount += b.getCount();
        }
        return totalcount/compHivesList.size();
    }

    //Calculate the total number of bees in Computer-owned beehives.
    private static int calcCompTotal(World w){
        int totalcount = 0;
        for(Beehive b : w.beehive){
            if(b.getOwnership().equals(("COMP"))){
                totalcount += b.getCount();
            }
        }
        return totalcount;
    }

    //Calculate the total number of bees in the Human player's beehives.
    private static int calcOpponentTotal(World w){
        int totalcount = 0;
        for(Beehive b : w.beehive){
            if(b.getOwnership().equals(("USER"))){
                totalcount += b.getCount();
            }
        }
        return totalcount;
    }

    //OFFENSE METHOD
    /*
    The default tactic for the Computer.
      It sends a bee from a Computer-owned hive to a non-Computer-owned hive under the following conditions:
      1: The start beehive has at least 8 bees in it.
      2: The start beehive has at least 10 more bees than the hive the Comp is considering to send to.
    */
    private void offense(World w){
        for(Beehive b1 : compHivesList){
            for(Beehive b2: b1.neighborHivesList){
                // 1st condition: Leave at least seven bees in the hive.
                // 2nd condition: has 10+ more bees than the neighbor.
                if(b1.getCount() > 7 && b1.getCount() > b2.getCount() + 10){
                    Bees.createBee(w, b1, b2, "COMP"); // Send a bee.
                }
            }
        }
    }

    //BREAK STALEMATE
    /*
    When the Computer has an absolute advantage in the number of bees compared to the Human player,
    invade all the Human player's beehives.
    */
    private void breakStalemate(World w){
        for(Beehive b1 : w.beehive){
            if(b1.getOwnership().equals("USER")){
                for(Beehive b2 : b1.neighborHivesList){
                    if(b2.getCount() > 2) { // Leave at least two bees in the hive.
                        Bees.createBee(w, b2, b1, "COMP");
                    }
                }
            }
        }
    }

    //INVADE ALL
    /*
    When the Human player has no beehives, invade all the beehives not occupied by the Computer.
    */

    private void invadeAll(World w){
        for(Beehive b1 : w.beehive){
            if(!b1.getOwnership().equals("COMP")){
                for(Beehive b2 : b1.neighborHivesList){
                    if(b2.getCount() > 1) { // Leave at least one bee in the hive.
                        Bees.createBee(w, b2, b1, "COMP");
                    }
                }
            }
        }
    }

    // Update the Computer (called in Main.java)
    public void update(World w){
        doStrategy(w);      //Choose from invadeAll(), breakStalemate() & defense(), and offense()
        addNewCompHives(w); //Add computer-owned beehives to the compHivesList
        removeACompHive(); //Remove hives that are in the compHivesList but is no longer owned by the Computer.
    }

    //Add new computer-owned hives into compHivesList
    public void addNewCompHives(World w) {
        for(Beehive b : w.beehive){
            if(b.getOwnership().equals("COMP")){
                compHivesList.add(b);
            }
        }
    }

    //Remove hives that are in the compHivesList but is no longer owned by the Computer.
    public void removeACompHive() {
        LinkedList<Beehive> removeFromList = new LinkedList<Beehive>();

        for(Beehive b : compHivesList){
            if(b.getCount() == 0 || !b.getOwnership().equals("COMP")){
                removeFromList.add(b);
            }
        }

        for(Beehive b : removeFromList){
            compHivesList.remove(b);
        }
    }

    public String getLevel(){
        return level;
    }

    public String toString() {
        return "Level: " + level + " || " + "Owned # of hives: " + compHivesList.size() + "\n Beehives:\n" + compHivesList.toString() + "\n";
    }
}


