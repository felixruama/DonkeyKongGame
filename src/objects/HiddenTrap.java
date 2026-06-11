
package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class HiddenTrap extends InteractableObjects implements FallableTile{
	  private Point2D position;
	    private boolean isUnderPlayer;
	    private boolean wasExploted;

	    public HiddenTrap(Point2D position) {
	        super(position);
	        this.position = position;
	        isUnderPlayer = false;
	        wasExploted = false;
	    }

	    @Override
	    public String getName() {
	        if (isUnderPlayer && wasExploted) {
	            return "Floor";
	        } else if (isUnderPlayer) {
	            return "Trap";  // Agora retorna "Floor" quando explodido
	        } else {
	            return "Wall";  // Retorna "Wall" se não tiver sido ativado ou explodido
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
			isUnderPlayer=true;
		}

	    @Override
	    public void wasExplode() {
	        wasExploted = true;
	        ImageGUI.getInstance().update();  // Atualiza a GUI após a explosão para refletir a mudança para "Floor"
	    }

	    @Override
	    public boolean canFallThrough(MoveableObject movable, boolean vaiCair) {
	        return wasExploted;  // Permite a queda se foi explodido
	    }

		
	}

