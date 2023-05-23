package jp.skywill.miniriversi;

public class ComputerMaxRev extends GameParticipant {

    public ComputerMaxRev(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public int decidePosition(Board board) {
        int maxReversed = 0;
        int position = 0;
        int legalPosition = board.calcLegalPosition(isBlack());
        while (legalPosition != 0) {
            int lsb = legalPosition & -legalPosition;
            int numRiversed = board.calcRiversed(isBlack(), lsb);
            if (board.countBit(numRiversed) > maxReversed) {
                position = lsb;
            }
            legalPosition ^= lsb;
        }
        return position;
    }
    
}
