package Main;

public class Engine {

    public Game game;


    public Engine(String input) {
        game = new Game(input);
    }


    public void printBoardState() {
        game.printBoardState();
    }
}
