package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class InteractiveMovableObjects implements Moveable,Interactable,ImageTile{
	
	protected Point2D position;
	
	public InteractiveMovableObjects(Point2D position) {
        this.position = position;
    }
	
	public Point2D getPosition() {
		return position;
	}

	@Override
	public abstract boolean canMoveH();
	
	@Override
	public abstract boolean canMoveV();
	
	@Override
	public void interact(Player player, Room room) {
		Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
		if(this.getPosition().equals(player.getPosition())) {
			currentRoom.removeObject(this);
			player.wasAttacked();
			if(player.wasDefeated())
				GameEngine.getInstance("").processGameOutcome(false);
		}
    }
	
	public void move(Direction direction) {
        Point2D nextPosition = position.plus(direction.asVector());
        Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
        interact(currentRoom.getPlayer(),currentRoom);
        
        if ((direction == Direction.LEFT || direction == Direction.RIGHT) && canMoveH()) {
            position = nextPosition;  
        }  
        
        if ((direction == Direction.UP || direction == Direction.DOWN) && canMoveV()) {
            position = nextPosition; 
        }
    }
	
	
	public abstract boolean canPassThrough();
	public abstract boolean	getLastP();
}
