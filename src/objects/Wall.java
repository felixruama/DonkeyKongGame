package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Wall implements ImageTile,FallableTile{
	private Point2D position; 
	
	public Wall(Point2D position) {
		this.position=position;
	}

	@Override
	public String getName() {
		return "Wall";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public Point2D getPosition() {
		return this.position;
	}
	@Override
    public boolean canFallThrough(MoveableObject movable, boolean vaiCair) {
        // O objeto pode cair através de um Floor
        return false;
    }
}
