package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Flag extends InteractableObjects implements Exit  {
    private boolean taken; //indica se a bandeira foi apanhada
    private boolean isEnemyDead;

    public Flag(Point2D position) {
        super(position);
        this.isEnemyDead = false;
    }

    @Override
    public String getName() {
    	 return isEnemyDead ? "finalPrice" : "Flag"; 
    }

    public void setTaken() {
        this.taken = true;
    }
    
    public boolean isTaken() {
        return taken;
    }

    @Override
    public void interact(Player player, Room room) {
    	ImageGUI.getInstance().setStatusMessage("Ganhou");
    	GameEngine.getInstance("").setFinalMensagem();
    	GameEngine.getInstance("").processGameOutcome(true);
    	System.out.println("GANHOU EM "+ GameEngine.getInstance("").getLastTickProcessed());
    	
    }

	@Override
	public int getLayer() {
		return 1;
	}
	@Override
	public void wasExplode() {
		Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
		currentRoom.removeObject(this);
	}

	@Override
	public void openExit() {
		 this.isEnemyDead = true; 
	        ImageGUI.getInstance().update();
	}
	@Override
    public boolean isFinalExit() {
        return true; // Saída final
    }

		
	}

	
	


