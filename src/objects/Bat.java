package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Bat extends InteractiveMovableObjects{
    private boolean lastP = false;
    protected boolean wasExploted;

    public Bat(Point2D initialPosition) {
        super(initialPosition);
    }

    @Override
    public String getName() {
        return "Bat";
    }

    @Override
    public int getLayer() {
        return 2;
    }

    @Override
    public boolean canMoveH() {
        return true; 
    }

    @Override
    public boolean canMoveV() {
        return true; 
    }

    public boolean getLastP() {
        return lastP;
    }
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public void move(Direction direction) {
    	Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
    	Direction randomDirection = Direction.random(); 
    	Point2D newPosition = this.getPosition().plus(randomDirection.asVector());
    	if(currentRoom.isPositionValid(newPosition)){
	        super.move(randomDirection);
	        if (position.getY() == 10) {
	            lastP = true;
	        }
    	}
    }
    @Override
	public void wasExplode() {
		 wasExploted=true;
	}


}
