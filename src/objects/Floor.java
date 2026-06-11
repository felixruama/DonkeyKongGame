package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Floor implements ImageTile,FallableTile {
	
	 protected Point2D position;
	 
//extends GameObject
	public Floor(Point2D position) {
		this.position = position;
	}

	@Override
	public String getName() {
		return "Floor";
	}

	@Override
	public Point2D getPosition() {
		return this.position;
	}
	
	@Override
	public int getLayer() {
		return 0;
	}
	@Override
    public boolean canFallThrough(MoveableObject movable, boolean vaiCair) {
        // O objeto pode cair através de um Floor
        return true;
    }
}
