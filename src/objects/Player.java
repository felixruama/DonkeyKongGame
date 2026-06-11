package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Player extends MoveableObject implements Attackable{

	//mudanças nos atributos
    protected final int MAX_LIFE = 3;
    protected final int MAX_HP = 200;
    protected final int POINTS = 10;
    protected int damage;
	protected int currentHP;
	protected final Point2D initialPosition;
	private Bomb carriedBomb; 
	private boolean hasDroppedBomb;
	private int bombTicks; 
	private boolean canMoveV;




    public Player(Point2D position) {
        super(position);
        currentLife = MAX_LIFE;
        currentHP = MAX_HP;
        damage = POINTS;
        initialPosition = position;
        canMoveV = false;

    }
    
    
    @Override
    public String getName() {
        return "Princess";
    }

    @Override
    public int getLayer() {
        return 2;
    }
    
    public void setPosition(Point2D newPosition) {
        this.position = newPosition;
    }
    
    public void setCurrentLife(int newLife) {
        currentLife = newLife;
    }

    @Override
    public boolean canMoveV() {
        return true; 
    }
    
    @Override    
    public boolean canMoveH() {
        return true; 
    }
    
    public boolean canPassThrough() {
        return false; 
    }
    
    public void move(Direction direction) {
    	Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
        super.move(direction); 
        currentRoom.checkInteractionWithObjects(); 
    
    }
     
    public void wasAttacked() {
    	Room currentRoom = GameEngine.getInstance("").getCurrentRoom();
    	if(currentHP == 0 && currentLife > 0) {
    		currentHP = MAX_HP - currentRoom.getEnemy().getDamage();
    		currentLife--;
    	} else {
    		currentHP -= POINTS;
    	}
    	if (this.wasDefeated()) {
            GameEngine.getInstance("").processGameOutcome(false);
        } else if (this.hasLostALife()) {
            GameEngine.getInstance("").processGameOutcome(false);
        }
    }
    
    
    

//mudanças em todos
	public boolean wasDefeated() {
		return currentLife <= 1 && currentHP <= 0;
	}

	public boolean hasLostALife() {
		return currentHP <= 0;
	}

	
	public void hasEatenGoodMeat() {
		if(currentHP == MAX_HP  && currentLife < MAX_LIFE) {
    		currentHP = POINTS;
    		currentLife++;
    	} else if(currentLife == MAX_LIFE && currentHP + POINTS >= 100) {
    		currentHP = MAX_HP;
    	} else {
    		currentHP += POINTS;
    	}	
		GameEngine.getInstance("").setMensageScream(" comeu uma boa carne!");
		GameEngine.getInstance("").setMensageScream(" comeu uma boa carne!");
	}
	
	public void hasEatenBadMeat() {
		if(currentHP == 0 &&  currentLife > 1  &&  currentLife < MAX_LIFE) {
    		currentHP = MAX_HP - POINTS;
    		currentLife--;
    	} else if(currentLife == 0 && currentHP - POINTS <= 0) {
    		GameEngine.getInstance("").processGameOutcome(false);
    	} else {
    		currentHP -= POINTS;
    	}	
    	GameEngine.getInstance("").setMensageScream(" comeu uma carne estragada!");	
    	GameEngine.getInstance("").setMensageScream(" comeu uma carne estragada!");	
	}
	
	public void hasSword(Boolean isGood) {
		if(isGood == true) {
			if((damage + POINTS) < 100)
				damage += POINTS;
			else
				damage = 100;
		} else {
			if((damage + POINTS/2) < 100)
				damage += POINTS/2;
			else
				damage = 100;
		}
		GameEngine.getInstance("").setMensageScream( " apanhou a espada! ");
		GameEngine.getInstance("").setMensageScream( " apanhou a espada! ");
	}

	
	
	public int getCurrentLife() {
        return currentLife;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getDamage() {
        return damage;
    }

    
    public void wasAttackedByBomb() {
        currentHP = 0;
        GameEngine.getInstance("").processGameOutcome(false);
    }
    
    public void addBomb(Bomb bomb) {
	    carriedBomb = bomb;
	}

	public void dropBomb(Room room) {
		if (carriedBomb != null && carriedBomb.getIsHeld()) {
	        carriedBomb.place(this.getPosition());
	        hasDroppedBomb = true;      
	    } else {
	        System.out.println("Nenhuma bomba para soltar.");
	    }
	}

	
	public boolean hasBomb() {
	    return carriedBomb != null;
	}
	public boolean getHasDropBomb() {
	    return hasDroppedBomb;
	}
	public Bomb getCarriedBomb() {
	    return carriedBomb;
	}
	public void setHasDroppedBomb(boolean b) {
	    this.hasDroppedBomb = b;
	}
	public void incrementBombTicks() {
        if (hasDroppedBomb) {
            bombTicks++;
        }
    }
	public int getBombTicks() {
		return bombTicks;
	}



}
