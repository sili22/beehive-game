How to compile, run, and use the program:

0. All the following files must be in the same folder:

Main.java
Beehive.java
Bees.java
Computer.java
Screen.java
Menu.java
Background.jpg
Hive.png
Bee.png
BumblebeeTrack.wav


1. >> Compile the following files by typing and entering javac *.java in Terminal

Main.java
Beehive.java
Bees.java
Computer.java
Screen.java
Menu.java

2. >> Run Main.java by typing and entering java Main in Terminal

3. >> On the first screen that appears, select level of difficulty and click "Start Game."

4. >> Play the game

----------------------

INSTRUCTIONS

----------------------

The goal of the game is to send a Bee from one Beehive to another and conquer all the beehives.

Each Beehive has a keyboard key associated with it.

   [W]		    [R]
[A] 	   [D]		 [G]
	  [X]		  [V]

To send a bee from one beehive to another, press and let go of the key you'd like the Bee to depart from.
Then, press and let go of the key you'd like the Bee to head toward.
For example, if I press and let go A and then do the same for W, a Bee will head from Beehive A to Beehive W.

Official rules are as follows:

In this game, you and the computer will each have a beehive to begin with (beehive A and beehive G respectively).
All the other beehives are unoccupied.
Your task is to annihilate the computer’s bees and occupy every beehive.
Each beehive that has bees in it will reproduce.
The number of bees in every beehive is displayed in a circle next to it.
You can only send your bees to adjacent beehives connected by roads.
You can select a beehive that you wish to send bees from by pressing the letter that corresponds to it on the keyboard,
and a label “SELECTED” will be displayed above that beehive.
Then you can press the letter that corresponds to the beehive you want to send bees to.
A bee will then be successfully sent.
You can deselect a selected beehive by pressing on any key that does not point to the selected beehive's neighbors.


----------------------

SOURCES

----------------------

>> Code Sources:

Main.java
 - sound() uses code based on https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
 - setBackgroundImage() uses code based on https://docs.oracle.com/javase/8/docs/technotes/guides/imageio/spec/apps.fm1.html
 - run(String love, String gameState) : the ScheduledExecutorService portion for this method was based on the code posted on https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ScheduledExecutorService.html

Bees.java
 - setBeeSprite() uses code based on https://docs.oracle.com/javase/8/docs/technotes/guides/imageio/spec/apps.fm1.html

Beehive.java
 - setHive() uses code based on https://docs.oracle.com/javase/8/docs/technotes/guides/imageio/spec/apps.fm1.html

Menu.java
 - Menu class uses code based on https://www.mainjava.com/swing/java-jbutton-tutorial-with-actionlistener-programming-examples/

>> File Sources:

1. Menu screen background image: https://unsplash.com/photos/2pl47VSTmEE (Creative Commons License)
2. Background image: https://unsplash.com/images/animals/bee (Creative Commons License)
3. Sprite for Bee: https://www.iconfinder.com/icons/5853921/bee_bug_fly_honey_insect_nectar_spring_icon (Creative Commons License)
4. Sprite for Hive: https://commons.wikimedia.org/wiki/File:DeseretBeehive.png (Creative Commons License)
5. Background music: https://www.youtube.com/watch?v=GWX22cTDvK8& (Obtained approval to use by creator (8 Bit Jazz) via e-mail)

----------------------

TEAM MEMBERS

----------------------

Tomajin Morikawa
Siyi Li
Lovemore Nyaumwe
