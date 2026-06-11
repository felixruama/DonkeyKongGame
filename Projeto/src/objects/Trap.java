package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Trap extends InteractableObjects implements FallableTile {
	private Point2D position; 
	
	protected boolean wasExploted;
	
	public Trap(Point2D position) {
		super(position);
		this.position=position;
		wasExploted=false;
	}

	@Override
	public String getName() {
		if(wasExploted) {
			return "Floor";
		}else { 
			return "Trap";
		}
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public Point2D getPosition() {
		return this.position;
	}

	@Override
	public void interact(Player player, Room room) {
		player.wasAttacked();
	}
	
	public boolean getWasExplode() {
		return wasExploted;
	}
	
	@Override
    public void wasExplode() {
    	 wasExploted=true;
	}
	@Override
	public boolean canFallThrough(MoveableObject movable, boolean vaiCair) {
		if(wasExploted)
			return true;
		else 
			return false;
	}
	}



    	 
	


