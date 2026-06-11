package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Stairs implements ImageTile{
    private Point2D position;

    public Stairs(Point2D position) {
        this.position = position;
    }
    @Override
    public Point2D getPosition() {
        return position;
    }

	@Override
	public String getName() {
		return "Stairs";
		
	}

	@Override
	public int getLayer() {
		return 1;
	}


}
