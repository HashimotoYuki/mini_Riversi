package jp.skywill.minireversi;

public class ComputerMiniMax extends GameParticipant {

    private static final int INF = 1024;

    public ComputerMiniMax(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public int decidePosition(Board board) {
        int legalPosition = board.calcLegalPosition(isBlack());
        int maxValue = -INF;
        int position = 0;

        while (legalPosition != 0) {
            int lsb = legalPosition & -legalPosition;
            Board nextBoard = new Board(board);
            nextBoard.reverse(isBlack(), lsb);
            int value = miniMax(nextBoard, !isBlack());

            System.out.println(value);

            if (value > maxValue) {
                maxValue = value;
                position = lsb;
            }
            legalPosition ^= lsb;
        }
        return position;
    }

    private int miniMax(Board board, boolean isBlackTurn) {
        if (board.isFinishedGame()) {
            return board.countStone(isBlack()) - board.countStone(!isBlack());
        }

        int legalPosition = board.calcLegalPosition(isBlackTurn);
        if (legalPosition == 0) {
            return miniMax(board, !isBlackTurn);
        }

        if (isBlackTurn ^ this.isBlack()) {
            // 現在は相手のターン
            int minValue = INF;
            while (legalPosition != 0) {
                int lsb = legalPosition & -legalPosition;
                Board nextBoard = new Board(board);
                nextBoard.reverse(isBlackTurn, lsb);
                int value = miniMax(nextBoard, !isBlackTurn);
                if (value < minValue) minValue = value;
                legalPosition ^= lsb;
            }
            return minValue;

        } else {
            // 現在は自分のターン
            int maxValue = -INF;
            while (legalPosition != 0) {
                int lsb = legalPosition & -legalPosition;
                Board nextBoard = new Board(board);
                nextBoard.reverse(isBlackTurn, lsb);
                int value = miniMax(nextBoard, !isBlackTurn);
                if (value > maxValue) maxValue = value;
                legalPosition ^= lsb;
            }
            return maxValue;
        }
    }
}
