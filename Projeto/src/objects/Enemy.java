package objects;

import java.util.List;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Enemy extends MoveableObject implements Attackable,Enemys{

	protected final int MAX_LIFE = 100;
    protected final int KONGDAMEGE = 10;
   
    public Enemy(Point2D position) {
        super(position);
        currentLife = MAX_LIFE;
        damage = KONGDAMEGE;
        
    }
    public Point2D getPosition() {
    	return position;
    }

    @Override
    public String getName() {
        return "DonkeyKong";
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
		return false;
	}
    
    public int getCurrentLife() {
    	return currentLife;
    }
    
    public int getDamage() {
    	return damage;
    }
    
    
	public void move(Direction direction) {
        if (direction == Direction.UP||direction == Direction.DOWN) {
        	if(canMoveV())
        	position = position.plus(direction.asVector());
        
        } else if (direction == Direction.LEFT||direction == Direction.RIGHT) {
        	if(canMoveH())
            	position = position.plus(direction.asVector());
        }
    }
    
	public void moveRandomly(List<MoveableObject> moveableObjects, Player player) {
        if (player.wasDefeated() || player.hasLostALife()) {
            return;
        }

        Direction[] directions = Direction.values();
        Direction randomDirection = directions[(int) (Math.random() * directions.length)];
        Point2D newPosition = getPosition().plus(randomDirection.asVector());

        if (isPositionValid(newPosition, moveableObjects) && !isPositionOccupiedByOtherMoveable(newPosition, moveableObjects, this)) {
            move(randomDirection);
        }

        if (newPosition.equals(player.getPosition())) {
            player.wasAttacked();
        }
    }

    public void moveTowardsPlayer(Player player, List<MoveableObject> moveableObjects) {
        if (player.wasDefeated() || player.hasLostALife()) {
            return;
        }

        Point2D playerPosition = player.getPosition();
        Direction direction = calculateDirection(getPosition(), playerPosition);
        Point2D newPosition = getPosition().plus(direction.asVector());

        if (newPosition.equals(playerPosition)) {
            player.wasAttacked();
        }

        if (isPositionValid(newPosition, moveableObjects) && !isPositionOccupiedByOtherMoveable(newPosition, moveableObjects, this)) {
            move(direction);
        }
    }

    // Métodos auxiliares para validar posições:
    private boolean isPositionValid(Point2D position, List<MoveableObject> moveableObjects) {
    	Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
    	return currentRoom.isPositionValid(position);
    }

    private boolean isPositionOccupiedByOtherMoveable(Point2D position, List<MoveableObject> moveableObjects, MoveableObject current) {
        for (MoveableObject movable : moveableObjects) {
            if (!movable.equals(current) && movable.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    private Direction calculateDirection(Point2D current, Point2D target) {
        int dx = target.getX() - current.getX();
        int dy = target.getY() - current.getY();

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            return dy > 0 ? Direction.UP : Direction.DOWN;
        }
    }

    
    
 
    public void wasAttacked() {
       	if (wasDefeated()) {
       		return;
       	}
    	Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
       	GameEngine.getInstance("").setMensageScream( " Kong: " + this.currentLife);
    	currentLife -= currentRoom.getPlayer().getDamage();
    	
    }

    
	public boolean wasDefeated() {
		return currentLife <= 0;
	}
	
    @Override
    public boolean canPassThrough() {
        return false;
    }
    
        
    public void removeFromList() {
	    Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
	    currentRoom.removeAllObjects();
	    currentRoom.removeObject(this);   
    }
    
	
	@Override
	public void wasAttackedByBomb() {
    	Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
        currentLife = 0;
        if (wasDefeated()) {
        	currentRoom.processPlayerAtack(null);           
        }
    	
	}
	
	
}
