package pt.iscte.poo.game;
import pt.iscte.poo.gui.ImageGUI;

public class Main {
	public static void main(String[] args) {
		GameEngine engine = GameEngine.getInstance("room0.txt");
		ImageGUI gui = ImageGUI.getInstance();
		gui.setStatusMessage("Good Luck");
		gui.registerObserver(engine);
		gui.go();
	}
}
