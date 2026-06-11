package objects;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class MoveableObject implements ImageTile, Moveable {
	
	protected Point2D position;
    protected int damage;
	protected int currentLife;
	public Point2D newPosition;
    
    public MoveableObject(Point2D position) {
        this.position = position;
        newPosition = position;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public abstract String getName();
    
    @Override
    public abstract int getLayer();
    
    public void setPosition(Point2D position) {
        this.position = position;
    }
    
    public Point2D getNewPosition() {
    	return newPosition;
    }
    
    public void move(Direction direction) {
        Point2D nextPosition = position.plus(direction.asVector());
        Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
        
        if (currentRoom.isPositionOccupiedByOtherMoveable(nextPosition, currentRoom.getMovebleObjects(), this)) {
            return;
        }
        if ((direction == Direction.LEFT || direction == Direction.RIGHT) && canMoveH()) {
            position = nextPosition;  
        } 
        if ((direction == Direction.UP || direction == Direction.DOWN) && canMoveV()) {
            position = nextPosition;  
        }
    }

	public boolean isAttacking(Point2D targetPosition) {
		return this.getPosition().equals(targetPosition);
	}

	
    public void processAttacks(MoveableObject otherObject,Point2D nextPosition) {
        Room currentRoom = GameEngine.getInstance("").getCurrentRoom();

        List<MoveableObject> objectsToRemove = new ArrayList<>();
        
            if (!otherObject.equals(this) && otherObject.getPosition().equals(nextPosition)) {
                otherObject.wasAttacked();
               
                if (otherObject.wasDefeated()) {
                	currentRoom.removeObject(otherObject);
                    
                }
                return;
            }

        for (MoveableObject objectToRemove : objectsToRemove) {
            currentRoom.removeObject(objectToRemove);
        }
    }



 
    public abstract void wasAttacked();
    public abstract boolean wasDefeated();
    public abstract boolean canPassThrough();
	


}