package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bomb extends InteractableObjects {
    private boolean isHeld; //Indica se a bomba está com o jogador
    private boolean wasPlaced; //Indica se a bomba foi colocada no mapa
    private int ticksToExplode; //Contador para explosão 
    protected int NUM_TICKS = 5; 
    protected int RADIUS = 1; // Raio da explosão
    protected boolean wasExploted;

    public Bomb(Point2D position) {
        super(position);
        this.isHeld = false;
        this.wasPlaced = false;
        this.ticksToExplode = 0;
    }

    @Override
    public String getName() {
        return "Bomb";
    }

    public void pickUp() {
        this.isHeld = true; // Marca como apanhada
    }
    
    public boolean getIsHeld() {
    	return isHeld;
    }

    public void place(Point2D position) {
        this.isHeld = false;
        this.wasPlaced = true;
        this.setPosition(position);
        this.ticksToExplode = NUM_TICKS;
        ImageGUI.getInstance().addImage(this);// Adiciona a bomba ao mapa
    }

    private void setPosition(Point2D position) {
        this.position = position;
    }
    
    @Override
    public int getLayer() {
        return 3;
    }
    
    public void tick(Room room) {
    	
        if (wasPlaced && ticksToExplode > 0) {
            ticksToExplode--;
            if (ticksToExplode == 0) {
                explode(room);
            }
        }
    }

    public void explode(Room room) {
        Point2D position = getPosition();

        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dy = -RADIUS; dy <= RADIUS; dy++) {
                Point2D affectedPoint = position.plus(new Vector2D(dx, dy));

                //verificar se há objetos para interagir na posição afetada
                room.getObjectAt(affectedPoint).forEach(obj -> {
                    if (obj instanceof Interactable || obj instanceof Moveable) {
                    	if (obj instanceof Attackable) {
                            ((Attackable) obj).wasAttackedByBomb();
                        }
                           if (obj instanceof Interactable) {
                            	((Interactable)obj).wasExplode();
                          
                           }
                        } else {
                           
                            room.removeObject(obj); // Remove objeto da sala
                            ImageGUI.getInstance().removeImage(obj); // Remove imagem da GUI
                        }
                });
            }
        }
        room.removeObject(this);
    }

    @Override
    public void interact(Player player, Room room) {	
            pickUp();
            player.addBomb(this); 
            room.removeObject(this); 
            ImageGUI.getInstance().removeImage(this); 
        }
    
    @Override
    public void wasExplode() {
    	 wasExploted=true;
    }

}