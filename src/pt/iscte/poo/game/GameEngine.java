package pt.iscte.poo.game;

import pt.iscte.poo.utils.Point2D;
import objects.*;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class GameEngine implements Observer {

    private static GameEngine INSTANCE;
    private Room currentRoom;
    private int lastTickProcessed = 0;

    private GameEngine(String file) {
        currentRoom = new Room(file);
    }

    public static GameEngine getInstance(String file) {
        if (INSTANCE == null) {
            INSTANCE = new GameEngine(file);
        }
        return INSTANCE;
    }
    public int getLastTickProcessed() {
    	return lastTickProcessed;
    }

    public static ImageTile createGameObject(char symbol, Point2D position) {
        ImageGUI.getInstance().addImage(new Floor(position));
        switch (symbol) {
            case 'H': return new Player(position);
            case 'W': return new Wall(position);
            case 'S': return new Stairs(position);
            case 'G': return new Enemy(position);
            case '0': return new Door(position);
            case 't': return new Trap(position);
            case 's': return new Sword(position);
            case 'm': return new Meat(position);
            case 'P': return new Flag(position);
            case 'b': return new Bat(position);
            case 'E': return new HiddenTrap(position);
            case 'B': return new Bomb(position);
            case ' ': return new Floor(position);
          
            default: return new Floor(position);
            
        }
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    @Override
    public void update(Observed source) {
    	    	
        if (ImageGUI.getInstance().wasKeyPressed()) {
            int keyPressed = ImageGUI.getInstance().keyPressed();
            Player player = (Player) currentRoom.getPlayer(); 
            
            if (Direction.isDirection(keyPressed)) {
                Direction direction = Direction.directionFor(keyPressed);
                currentRoom.movePlayer(direction);
            }
            
            
            if (keyPressed == KeyEvent.VK_B) {
                if (player.hasBomb()) {
                	player.dropBomb(currentRoom); // Solta a bomba na sala atual
                } else {
                  setMensageScream("Nenhuma bomba para soltar.");
                  System.out.println(currentRoom.getinteractableObjects());
                }
    	    }


    	}
        
        int currentTicks = ImageGUI.getInstance().getTicks();
        
       
        while (lastTickProcessed < currentTicks) {
        	

            processTick();
            System.out.println(currentTicks);
            currentRoom.checkFallPlayer(false);
            setMensageScream(" ");
            if (currentRoom.getPlayer().wasDefeated()) {
                processGameOutcome(false);
            }
            Player player = currentRoom.getPlayer();
            if (player.getHasDropBomb()) {
            	player.incrementBombTicks();
            setMensageScream(" Tick da bomba: " + player.getBombTicks());

                if (player.getBombTicks() == 5) {
                	player.getCarriedBomb().explode(currentRoom);
                	player.setHasDroppedBomb(false);
                }
            }

            currentRoom.checkInteractionWithObjectsUnder();

            if ((int) (Math.random() * 3) == 1) {
                currentRoom.spawnBanana();
            }

            currentRoom.moveInteractiveObjects();
            
            for (TicksChangeble ticksC : currentRoom.getTicksChangeble()) {
            	if(ticksC != null)
            		ticksC.tick(currentRoom);
            }
            
            
        }

        if (shouldGorillaChasePlayer()) {
            currentRoom.moveGorilasRandomly();
        } else {
            currentRoom.moveGorilasTowardsPlayer();
        }
       
        ImageGUI.getInstance().update();
    }

    private boolean shouldGorillaChasePlayer() {
        int levelsCompleted = currentRoom.getLevelsCompleted();
        double chaseProbability = Math.min(0.5 + (levelsCompleted * 0.05), 1.0);
        return Math.random() < chaseProbability;
    }

    private void processTick() {
        lastTickProcessed++;
    }

    public void changeRoom(String newRoomFileName) {
        currentRoom = new Room(newRoomFileName);
    }

    public void processGameOutcome(boolean hasWon) {
        if (hasWon) {
            ImageGUI.getInstance().setStatusMessage("GANHOU");
            ImageGUI.getInstance().dispose();
        } else if (currentRoom.getPlayer().wasDefeated()) {
            ImageGUI.getInstance().setStatusMessage("Foi derrotado. Recomece o jogo.");
            restartRoom(currentRoom.firstRoomFile);
        } else if (currentRoom.getPlayer().hasLostALife()) {
            int currentLives = currentRoom.getPlayer().getCurrentLife() - 1;
            ImageGUI.getInstance().setStatusMessage("Perdeu uma vida. Recomece o nível.");
            restartRoom(currentRoom.currentRoomFile);
            currentRoom.getPlayer().setCurrentLife(currentLives);

        }
    }

    static class Highscore {
        String playerName;
        int timeInTicks;

        public Highscore(String playerName, int timeInTicks) {
            this.playerName = playerName;
            this.timeInTicks = timeInTicks;
        }
    }


    public void setFinalMensagem() {
        String playerName = generatePlayerName();
        int playerTime = GameEngine.getInstance("").getLastTickProcessed();
        
        File file = new File("highscores.txt");
        
        List<Highscore> highscores = new ArrayList<>();
        
        try {
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("Jogador")) {  // Ignorar o título
                        String[] parts = line.split(" - ");
                        String name = parts[0];
                        int time = Integer.parseInt(parts[1].replace(" ticks", ""));
                        highscores.add(new Highscore(name, time));
                    }
                }
                scanner.close();
            }

            highscores.add(new Highscore(playerName, playerTime));

            highscores.sort(Comparator.comparingInt(h -> h.timeInTicks));

            if (highscores.size() > 10) {
                highscores = highscores.subList(0, 10);
            }

            FileWriter writer = new FileWriter(file);
            writer.write("Tabela de Highscores:\n");
            for (Highscore highscore : highscores) {
                writer.write(highscore.playerName + " - " + highscore.timeInTicks + " ticks\n");
            }
            writer.close();

        } catch (IOException e) {
            System.err.println("Erro ao ler ou escrever no arquivo: " + e.getMessage());
        }
    }


    private String generatePlayerName() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String currentDateTime = sdf.format(new Date());
        return "Jogador_" + currentDateTime;  // Exemplo: "Jogador_08-12-2024_15-30-45"
    }
  //MENSAGENS 
  	public void setMensageScream(String mensage) {
  		ImageGUI.getInstance().setStatusMessage("Vidas: "+ currentRoom.getPlayer().getCurrentLife() + " HP: " + currentRoom.getPlayer().getCurrentHP() + " Ataque: " + currentRoom.getPlayer().getDamage() + mensage);
  	}
  	

  	
  	//NOVO MAPA 
  	public boolean restartRoom(File fileName) {
  	    if (fileName != null && fileName.exists()) {
  	        ImageGUI.getInstance().clearImages();
  	        currentRoom.removeAllObjects();
  	        currentRoom.loadMap(fileName.getPath());
  	        return true;
  	    } else {
  	        System.err.println("Ficheiro não encontrado");
  	        GameEngine.getInstance("").processGameOutcome(false);
  	        return false;
  	    }
  	}

  	public void loadNextRoom() {
  	    if (currentRoom.nextRoomFile != null && currentRoom.nextRoomFile.exists()) {
  	        ImageGUI.getInstance().clearImages();
  	        ImageGUI.getInstance().removeImage(currentRoom.getPlayer());
	  	     currentRoom.removeObject(currentRoom.getPlayer());
	  	    currentRoom.loadMap(currentRoom.nextRoomFile.getPath());
  	    } else {
  	        System.err.println("Erro: O arquivo do próximo nível não foi encontrado.");
  	        GameEngine.getInstance("").processGameOutcome(true);
  	    }
  	}

    
    
    
    
    

	
}
