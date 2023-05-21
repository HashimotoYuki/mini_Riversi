package jp.skywill.miniriversi;

public class ExecuteGame {
    public static void main(String[] args) throws Exception {
        Board board = new Board();

        board.printBoard();
        
        int black = board.getBlackStone();
        int white = board.getWhiteStone();
        board.printBB(board.calcLegalPosition(true));
        board.printBB(board.calcLegalPosition(false));
    }
}
