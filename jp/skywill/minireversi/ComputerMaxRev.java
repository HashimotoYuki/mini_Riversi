package jp.skywill.minireversi;

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
            int reversed = board.calcRiversed(isBlack(), lsb);
            int numReversed = board.countBit(reversed);
            
            if (numReversed > maxReversed) {
                maxReversed = numReversed;
                position = lsb;
            }
            legalPosition ^= lsb;
        }
        return position;
    }
}
