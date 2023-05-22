package jp.skywill.miniriversi;

public class ExecuteGame {
    public static void main(String[] args) throws Exception {
        Board board = new Board();

        board.printBoard();

        int legalPosition = board.calcLegalPosition(true);
        int position = legalPosition & -legalPosition;
        board.riverse(true, position);
        board.printBoard();
    }
}