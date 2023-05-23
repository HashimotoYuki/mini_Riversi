package jp.skywill.miniriversi;

public class ComputerLSB extends GameParticipant {
    
    public ComputerLSB(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public int decidePosition(Board board) {
        int legalPosition = board.calcLegalPosition(isBlack());
        return legalPosition & -legalPosition;
    }
}
