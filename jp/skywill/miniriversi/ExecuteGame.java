package jp.skywill.miniriversi;

public class ExecuteGame {
    public static void main(String[] args) throws Exception {
        Board board = new Board();
        board.printBoard();

        GameParticipant p1 = new Computer(true);
        GameParticipant p2 = new Player(false);

        boolean isBlackTurn = true;
        while (!board.isFinishedGame()) {
            int position = isBlackTurn ? p1.decidePosition(board) : p2.decidePosition(board);
            if (position != 0) {
                board.riverse(isBlackTurn, position);
                board.printBoard();
            }
            isBlackTurn = !isBlackTurn;
        }
    }
}