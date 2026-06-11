package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Banana extends InteractiveMovableObjects{
	

	    private boolean lastP = false;
	    
	    public Banana(Point2D initialPosition) {
	        super(initialPosition);
	       
	    }

	    @Override
	    public String getName() {
	        return "Banana";
	    }

	    @Override
	    public int getLayer() {
	        return 2;
	    }

	    @Override
	    public boolean canMoveH() {
	        return false; // Não se move horizontalmente
	    }

	    @Override
	    public boolean canMoveV() {
	        return true; // Apenas se move verticalmente
	    }

	    public boolean getLastP() {
	        return lastP;
	    }
	    public boolean canPassThrough() {
	        return true; // Permite atravessar bananas
	    }

	    @Override
	    public void move(Direction direction) {
	        super.move(Direction.DOWN);
	        if (position.getY() == 10) {
	            lastP = true;
	        }
	    }
	    @Override
	    public void wasExplode() {
			Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
			currentRoom.removeObject(this); 
	    	 
	    }

		
	}

