package jp.skywill.miniriversi;

public class ExecuteGame {
    public static void main(String[] args) throws Exception {
        Board board = new Board();
        board.printBoard();

        GameParticipant p1 = new ComputerMaxRev(true);
        GameParticipant p2 = new ComputerLSB(false);

        boolean isBlackTurn = true;
        while (!board.isFinishedGame()) {
            int position = isBlackTurn ? p1.decidePosition(board) : p2.decidePosition(board);
            if (position != 0) {
                board.reverse(isBlackTurn, position);
                board.printBoard();
            }
            isBlackTurn = !isBlackTurn;
        }
        printResult(board);
    }

    private static void printResult(Board board) {
        int numBlack = board.countStone(true);
        int numWhite = board.countStone(false);

        System.out.println("----");
        System.out.println("Black : " + numBlack);
        System.out.println("White : " + numWhite);

        if (numBlack > numWhite) {
            System.out.println("Black Wins!");
        } else if (numBlack < numWhite) {
            System.out.println("White Wins!");
        } else {
            System.out.println("Draw");
        }
        System.out.println("----");
    }
}