import Controllers.BoardGameConsoleController;
import Controllers.BoardGameUIController;
import UI.BoadGameApplication;
import javafx.application.Application;

public class main {
    /**
     * The main method of the application.
     * For console-view please delete the comment.
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        //BoardGameConsoleController boardGame = new BoardGameConsoleController();
        //boardGame.playGame();

        BoadGameApplication boardGameApp = new BoadGameApplication();
        Application.launch(boardGameApp.getClass(), args);
    }
}
