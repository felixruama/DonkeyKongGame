package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Sword extends InteractableObjects {
    private boolean taken; // Indica se a espada foi pega
    protected boolean isGood = true;
    protected int ticks;
    protected int NUM_TICKS = 10;
    protected boolean wasExploted;
    public Sword(Point2D position) {
        super(position);
    }

    @Override
    public String getName() {
    	if (taken) {
            return "Floor";
        } else {
            return isGood ? "Sword" : "Fire";
        }
    }

    public void gotTaken() {
        this.taken = true; // Marca a espada como pega
    }
    
    public void tick() {
        if (ticks > 0) {
            ticks--;
            if (ticks == 0) {
                isGood = false;
                ImageGUI.getInstance().update();
            }
        }
    }

    @Override

    public void interact(Player player, Room room) {
        ImageGUI.getInstance().setStatusMessage("manel pegou a espada" + player.getDamage());
        gotTaken(); 
        player.hasSword(isGood);
        room.removeObject(this); // Remove do Room     
    }
	@Override
	public int getLayer() {
		return 1;
	}
	@Override
    public void wasExplode() {
		Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
		currentRoom.removeObject(this); // Remove do Room
    }
}
