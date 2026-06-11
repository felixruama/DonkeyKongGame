package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class InteractableObjects implements ImageTile, Interactable {

	protected Point2D position;
	
    public InteractableObjects(Point2D position) {
        this.position = position;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public  abstract int getLayer();

}
