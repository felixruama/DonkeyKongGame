package pt.iscte.poo.game;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import objects.*;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Room {
	//ATRIBUTOS:
	//Matriz de objetos
	private ImageTile[][] gameObjects;  
	private int width = 10;
    private int height = 10;
    //Jogador 
    private Player player;
    //Ficheiros
    protected File firstRoomFile;
    protected File currentRoomFile;
    protected File nextRoomFile;
    //Booleans
    private boolean hasAnotherLevel; 
    private int levelsCompleted = 0;
    //Listas
    private List<InteractableObjects> interactiveObjects=new ArrayList<>();
    private List<MoveableObject> moveableObjects=new ArrayList<>();
    private List<InteractiveMovableObjects> interactiveMovableObjects = new ArrayList<>();
    		  
    public Room(String fileName) {
		firstRoomFile = new File(fileName);
		loadMap(fileName);
	}
 
    public void loadMap(String fileName) {
       
        currentRoomFile = new File(fileName);
        List<String> lines = new ArrayList<>();
       
        try (Scanner scanner = new Scanner(currentRoomFile)) {
            if (scanner.hasNextLine()) {
                String lineOne = scanner.nextLine();
                String[] splitedLineOne = lineOne.split(";");

                if (splitedLineOne.length > 1 && splitedLineOne[0].equals("#0")) {
                    nextRoomFile = new File(splitedLineOne[1]);
                    hasAnotherLevel = true;
                } else {
                    lines.add(lineOne);
                    nextRoomFile = null;
                    hasAnotherLevel = false;
                }
            }	
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            if (currentRoomFile.exists()) {
            	drawMap(lines);
            } else {
                System.err.println("Error: File not found. Please try again.");
            }
           
        } catch (FileNotFoundException e) {
            System.err.println("Error reading the file: " + fileName);
            requestFileAndReload();
        } catch (NoSuchElementException e) {
            System.err.println("Error: The file " + fileName + " is incomplete. The game will be terminated.");
            ImageGUI.getInstance().dispose();
        }
    }

    
    private void requestFileAndReload() {
        System.out.println("Please provide the name of the file to load:");
        try (Scanner inputScanner = new Scanner(System.in)) {
            String newFileName = inputScanner.nextLine();
            firstRoomFile = new File(newFileName);

            if (firstRoomFile.exists()) {
                loadMap(newFileName);
            } else {
                System.err.println("Error: File not found. Please try again.");
            }
        }
    }
    
	public void drawMap(List<String> lines) {	
		height = lines.size();
	    width = lines.get(1).length();
	    gameObjects = new ImageTile[height][width];

	    for (int y = 0; y < height; y++) {
	    	String line = lines.get(y);
	        for (int x = 0; x < width; x++) {
	        	char c = line.charAt(x);
	            Point2D position = new Point2D(x, y);
                ImageTile tile = GameEngine.createGameObject(c, position);
                gameObjects[y][x] = tile;      
                addObjectToInterface(tile);  
                if (c == 'H')
                	player = (Player) gameObjects[y][x];

            }
	    }
	}
  
 //RANDOM POSITION   
    public ImageTile[][] getGameObjects() {
        return gameObjects;
    }
    public Player getPlayer() {
    	return player;
    }
    public Enemy getEnemy() {
    	 for (MoveableObject movable : moveableObjects) { 
         	if(movable instanceof Enemys) {
		        if (movable != null && !moveableObjects.isEmpty()) {
		            return (Enemy) moveableObjects.get(0);
        }
         	}
    	 }
        return null;    	
    
    	 }
    	 
  //GETTERS 
    public File getCurrentRoomFile() {
    	return currentRoomFile;
    }
    public List<MoveableObject> getMovebleObjects(){
    	return moveableObjects;
    }
    public List<InteractiveMovableObjects> getiinteractiveMoveableObjects(){
    	return interactiveMovableObjects;
    }
    public List<InteractableObjects> getinteractableObjects(){
    	return interactiveObjects;
    }
    
    public File getNextRoom() {
        return nextRoomFile;
    }
    public int getLevelsCompleted() {
        return levelsCompleted;
    }
    private boolean getHasAnotherLevel() {
		return hasAnotherLevel;
	}
    public Door getRegularExit() {
        for (InteractableObjects obj : interactiveObjects) {
            if (obj instanceof Door && !((Exit) obj).isFinalExit()) {
                return (Door) obj;
            }
        }
        return null; 
    }

    public Flag getFinalExit() {
        for (InteractableObjects obj : interactiveObjects) {
            if (obj instanceof Flag && ((Exit) obj).isFinalExit()) {
                return (Flag) obj;
            }
        }
        return null; 
    }
    
    public List<TicksChangeble> getTicksChangeble() {
        List<TicksChangeble> ticksChangeble = new ArrayList<>();
        for (InteractableObjects obj : interactiveObjects) {
        	if(obj instanceof TicksChangeble )
            	ticksChangeble.add((TicksChangeble) obj);
            }
        
        return ticksChangeble;
    }
    //MOVIMENTO
    public void moveGorilasRandomly() {
        List<MoveableObject> movableCopy = new ArrayList<>(moveableObjects);

        for (MoveableObject movable : movableCopy) {
            if (movable instanceof Enemy) {
                ((Enemy) movable).moveRandomly(moveableObjects, player);
            }
        }
    }

    public void moveGorilasTowardsPlayer() {
        List<MoveableObject> movableCopy = new ArrayList<>(moveableObjects);

        for (MoveableObject movable : movableCopy) {
            if (movable instanceof Enemy) {
                ((Enemy) movable).moveTowardsPlayer(player, moveableObjects);
            }
        }
    }

    public void movePlayer(Direction direction) {
        Point2D nextPosition = player.getPosition().plus(direction.asVector());
        processPlayerAtack(nextPosition);
            	 if (isPositionValid(nextPosition)) {
            		 player.move(direction);
            }
        checkInteractionWithObjects();
        checkInteractionWithObjectsUnder();
        checkInteractionWithObjectsMoveable();
    }
    public void moveInteractiveObjects() {
        List<InteractiveMovableObjects> copy = new ArrayList<>(interactiveMovableObjects);

        for (InteractiveMovableObjects obj : copy) {
            if (obj != null) {
                obj.move(null);
                checkInteractionWithObjectsMoveable();

                // Remover objetos
                if (obj.getLastP()) {
                    interactiveMovableObjects.remove(obj);
                    removeObject(obj); 
                }
            }
        }
    }
    
    //VALIDAR POSIÇÕES 
    public boolean isPositionOccupiedByOtherMoveable(Point2D newPosition, List<MoveableObject> moveableObjects, MoveableObject currentObject) {
        for (MoveableObject moveable : moveableObjects) {
            if (!moveable.equals(currentObject) && moveable.getPosition().equals(newPosition)) {
                return true; // A posição está ocupada
            }
        }
        return false;
    }
    
    
    public boolean isPositionValid(Point2D position) {
        if (position.getX() < 0 || position.getX() >= width || position.getY() < 0 || position.getY() >= height) {
            return false;
        }
        ImageTile tile = gameObjects[position.getY()][position.getX()];
        if (tile == null) {
            return false;
        }
        if (tile instanceof FallableTile) {
            return ((FallableTile) tile).canFallThrough(null, false);
        }
        return true;
    }
   
    	//APOIO PARA OS PROCESS
	public ImageTile getTileUnder(MoveableObject movable) { 
    	Point2D positionBelow = movable.getPosition().plus(new Vector2D(0, 1)); 
    	if (positionBelow.getY() >= height) 
    		return null;  
    	return gameObjects[positionBelow.getY()][positionBelow.getX()]; 
	}
	//ADICIONAR A INTERFACES //addTile
	public void addObjectToInterface(ImageTile object) {
    	if(object instanceof Interactable && object instanceof Moveable) {
    		interactiveMovableObjects.add((InteractiveMovableObjects) object);
    		
    	}else if(object instanceof Interactable) {
    		interactiveObjects.add((InteractableObjects) object);
        
    	}else if(object instanceof Moveable) {
            moveableObjects.add((MoveableObject) object);
            
       }
    	ImageGUI.getInstance().addImage(object);
    	
    }
	public void addTile(ImageTile tile) {
        gameObjects[tile.getPosition().getY()][tile.getPosition().getX()] = tile;
        ImageGUI.getInstance().addImage(tile);
    }
    
	//REMOVER DE LISTAS/REMOVER TILE/ADD TILE
    public void removeObject(ImageTile object) {
    	if(object instanceof Interactable && object instanceof Moveable) {
        interactiveMovableObjects.remove(object);
    		
      }else if(object instanceof Interactable) {
        interactiveObjects.remove(object);
        
    	}else if(object instanceof Moveable) {
            moveableObjects.remove(object);
            
       }
    	
    	ImageGUI.getInstance().removeImage(object);
    	
    }
    public void removeAllObjects() {
		moveableObjects.removeAll(moveableObjects);
		interactiveObjects.removeAll(interactiveObjects);
		interactiveMovableObjects.removeAll(interactiveMovableObjects);
		
	}
    
    //PROCESS/HANDLE
    public void checkFall(MoveableObject movable, boolean vaiCair) {
        while (movable.getPosition().getY() + 1 < height) {
            ImageTile tileUnder = getTileUnder(movable);
            if (tileUnder == null || 
                (tileUnder instanceof FallableTile && 
                 ((FallableTile) tileUnder).canFallThrough(movable, vaiCair))) {
                movable.move(Direction.DOWN);
            } else {
                break; 
            }
        }
    }

    public void checkFallPlayer(boolean vaiCair) { 
        checkFall(player ,true);
    }
    
    public void checkInteractionWithObjects() {
        Point2D currentPosition = player.getPosition();

        for (ImageTile tile : interactiveObjects) {
            if (tile.getPosition().equals(currentPosition) && tile instanceof Interactable) {
                Interactable interactable = (Interactable) tile;
                interactable.interact(player, this);
                return;
                }
            }
        }

    public void checkInteractionWithObjectsMoveable() {
        Point2D currentPosition = player.getPosition();

        for (ImageTile tile : interactiveMovableObjects) {
            if (tile.getPosition().equals(currentPosition)) {
                
                if (tile instanceof Interactable) {
                Interactable interactable = (Interactable) tile;
                interactable.interact(player, this);
                removeObject(tile);
                return;
                   }
            }
        }
    }
    public void checkInteractionWithObjectsUnder() {
        ImageTile tileUnder = getTileUnder(player);
        if (tileUnder != null && tileUnder instanceof Interactable) {
            Interactable interactable = (Interactable) tileUnder;
            if(interactiveObjects.contains(tileUnder)) {
                interactable.interact(player, this);
                return;
            }
            }
        }

    public void processKongAtack(Point2D nextPosition) {
        if (!player.wasDefeated()) {
            List<MoveableObject> MovableCopy = new ArrayList<>(moveableObjects);
            for (MoveableObject movable : MovableCopy) {
            	if(movable instanceof Enemys)
            		movable.processAttacks(player, nextPosition);
            }
        } else {
            ImageGUI.getInstance().setStatusMessage("VOCÊ MORREU!!");
            GameEngine.getInstance("").processGameOutcome(false);
        }
    }
    
    public void processPlayerAtack(Point2D nextPosition) {
        if (!player.wasDefeated()) {
            List<MoveableObject> movableCopy = new ArrayList<>(moveableObjects);

            for (MoveableObject movable : movableCopy) {
                if (movable instanceof Enemys) {
                    player.processAttacks(movable, nextPosition);

                    if (movable.wasDefeated()) {
                        removeObject(movable); // Remove do mapa
                        moveableObjects.remove(movable); // Remove da lista original
                        ImageGUI.getInstance().removeImage(movable); // Remove da interface gráfica
                        ImageGUI.getInstance().setStatusMessage("KONG MORREU!!");
                    }
                }
            }
            boolean hasEnemiesLeft = moveableObjects.stream().anyMatch(obj -> obj instanceof Enemys);

            if (!hasEnemiesLeft) { 
                if (getHasAnotherLevel() && getRegularExit()!= null) {
                    getRegularExit().openExit(); 
                    levelsCompleted++;
                } else if(!getHasAnotherLevel() && getFinalExit()!= null) {
                    getFinalExit().openExit();
                }
            }
        }
    }
        
    //GERADOR DE OBJETOS(QUE NÃO ESTÃO NO FILE)
    public void spawnBanana() {
    	for (MoveableObject movable : moveableObjects) { 
        	if(movable instanceof Enemys) {
        	Point2D position= new Point2D (movable.getPosition().getX(), movable.getPosition().getY());
        	Banana novaBanana = new Banana (position);
            ImageGUI.getInstance().addImage(novaBanana);
            interactiveMovableObjects.add(novaBanana);  
            
    	}
    }
    }
      
//PARA A BOMBA
	public List<ImageTile> getObjectAt(Point2D position) {
		List<ImageTile> objetsAt=new ArrayList<>();
	    for (InteractableObjects obj : interactiveObjects) {
	        if (obj.getPosition().equals(position)) {
	             objetsAt.add(obj); // Retorna o objeto se encontrar a posição
	        }
	    }
	    for (MoveableObject obj : moveableObjects) {
	        if (obj.getPosition().equals(position)) {
	        	objetsAt.add(obj); // Retorna o objeto se encontrar a posição
	        }
	    }
	    for (InteractiveMovableObjects obj : interactiveMovableObjects) {
	        if (obj.getPosition().equals(position)) {
	        	objetsAt.add(obj); // Retorna o objeto se encontrar a posição
	            }
	        }
	        return objetsAt;
	}
	
}
