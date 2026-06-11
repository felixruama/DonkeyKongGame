package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Meat extends InteractableObjects implements TicksChangeble{

	private boolean eaten; //indica se a carne foi comida
    protected boolean isGood;
    protected int ticks;
    protected int NUM_TICKS = 10;	//nº ticks até a carne se estragar
   
    public Meat(Point2D position) {
        super(position);
        isGood = true;
        eaten = false;
        ticks = NUM_TICKS;
    }

    @Override
    public String getName() {
        if (eaten) {
            return "Floor";
        } else {
            return isGood ? "GoodMeat" : "BadMeat";
        }
    }
    
    public int getLayer() {
		return 1;
	}
    
    public int getNumTicks() {
		return NUM_TICKS;
	}
    
    public void gotEaten() {
        this.eaten = true;
    }
    @Override
    public void tick(Room room) {
        if (ticks > 0) {
            ticks--;
            if (ticks == 0) {
                updateMeat();
            }
        }
    }
    
    public void updateMeat() {
        isGood = false; 
        ImageGUI.getInstance().update();
    }

    @Override
    public void interact(Player player, Room room) {
    	if (isGood) { 
    		player.hasEatenGoodMeat();
    	} else { 
    		player.hasEatenBadMeat();
    	}
    	gotEaten(); 
        room.removeObject(this); 
        ImageGUI.getInstance().removeImage(this);
    }
    public void wasExplode() {
		Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
		currentRoom.removeObject(this); // Remove do Room
    	 
    }

	

}
