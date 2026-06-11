package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Door extends InteractableObjects implements Exit {
    private Point2D position;
    private boolean isEnemyDead; //indica se o inimigo morreu
    
    public Door(Point2D position) {
        super(position);
        this.position = position;
        this.isEnemyDead = false;
    }

    @Override
    public String getName() { 
        return isEnemyDead ? "DoorOpen" : "DoorClosed"; //abre a porta se o inimigo tiver morrido
    }

    @Override
    public int getLayer() {
		return 1;
	}
    
    @Override
    public Point2D getPosition() {
        return this.position;
    }
    public void setKongState(boolean state) {	
    	isEnemyDead = state;
    }

    public void openExit() {
        this.isEnemyDead = true; 
        ImageGUI.getInstance().update();
    }

    @Override
    public void interact(Player player, Room room) {
        if(isEnemyDead) {
        	GameEngine.getInstance("").setMensageScream("A porta foi aberta! Indo para o próximo nível...");
	        GameEngine.getInstance("").loadNextRoom();
        }
    }

	@Override
	public void wasExplode() {
		Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
		currentRoom.removeObject(this); // Remove do Room
    	 
    }
	@Override
    public boolean isFinalExit() {
        return false;
    }
}
    

