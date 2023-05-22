package jp.skywill.miniriversi;

public class Computer extends GameParticipant {
    public Computer(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public int decidePosition(Board board) {
        int legalPosition = board.calcLegalPosition(isBlack());
        return legalPosition & -legalPosition;
    }
}
