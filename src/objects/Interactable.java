package objects;

import pt.iscte.poo.game.Room;

public interface Interactable {
    void interact(Player player, Room room);
    void wasExplode();
    
   
}
