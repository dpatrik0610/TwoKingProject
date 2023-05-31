import Controllers.BoardGameConsoleController;
import Controllers.BoardGameUIController;
import UI.BoadGameApplication;
import javafx.application.Application;

public class main {
    public static void main(String[] args) {
        //BoardGameConsoleController boardGame = new BoardGameConsoleController();
        //boardGame.playGame();

        BoadGameApplication gameui = new BoadGameApplication();
        Application.launch(gameui.getClass(), args);
    }
}
