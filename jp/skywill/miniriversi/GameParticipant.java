package jp.skywill.miniriversi;

public abstract class GameParticipant {
    private boolean isBlack;

    public GameParticipant(boolean isBlack) {
        this.isBlack = isBlack;
    }

    public abstract int decidePosition(Board board);

    public boolean isBlack() {
        return this.isBlack;
    }
}
