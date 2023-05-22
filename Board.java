package jp.skywill.miniriversi;

import java.util.function.Function;

public class Board {
    public static final int HEIGHT = 4;
    public static final int WIDTH = 4;

    private int blackStone;
    private int whiteStone;

    public Board() {
        this.blackStone = 0x0240;
        this.whiteStone = 0x0420;
    }

    /**
     * 盤面の状態を表示する。
     * 黒石を'x'、白石を'o'で表す。
     */
    public void printBoard() {
        int mask = 0x0001;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if ((blackStone & mask) != 0x0000) {
                    System.out.print('x');
                } else if ((whiteStone & mask) != 0x0000) {
                    System.out.print('o');
                } else {
                    System.out.print('.');
                }
                mask <<= 1;
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * 石の数をカウントして返す。
     * @param stone : カウントの対象となる石のbb
     * @return 石の数
     */
    public int countStone(boolean isBlackTurn) {
        int stone = isBlackTurn ? blackStone : whiteStone;
        stone = (stone & 0x5555) + (stone >> 1 & 0x5555);
        stone = (stone & 0x3333) + (stone >> 2 & 0x3333);
        stone = (stone & 0x0f0f) + (stone >> 4 & 0x0f0f);
        stone = (stone & 0x00ff) + (stone >> 8 & 0x00ff);
        return stone;
    }

    /**
     * 合法手（置くと相手の石を返せる手）を表すbbを計算して返す。
     * @param own : 手番の側の石を表すbb
     * @param opponent : 相手の側の石を表すbb
     * @return 合法手を表すbb
     */
    public int calcLegalPosition(boolean isBlackTurn) {
        int own = isBlackTurn ? blackStone : whiteStone;
        int opponent = isBlackTurn ? whiteStone : blackStone;
        int lines = opponentLineFromPosition(own, opponent, 0x6666, s -> s >> 1) >> 1;
        lines |= opponentLineFromPosition(own, opponent, 0x6666, s -> s << 1) << 1;
        lines |= opponentLineFromPosition(own, opponent, 0x0ff0, s -> s >> 4) >> 4;
        lines |= opponentLineFromPosition(own, opponent, 0x0ff0, s -> s << 4) << 4;
        lines |= opponentLineFromPosition(own, opponent, 0x0660, s -> s >> 3) >> 3;
        lines |= opponentLineFromPosition(own, opponent, 0x0660, s -> s << 3) << 3;
        lines |= opponentLineFromPosition(own, opponent, 0x0660, s -> s >> 5) >> 5;
        lines |= opponentLineFromPosition(own, opponent, 0x0660, s -> s << 5) << 5;
        return lines & ~(own | opponent);
    }

    
    /**
     * 
     * @param isBlackTurn
     * @param position
     */
    public void riverse(boolean isBlackTurn, int position) {
        if (isBlackTurn) {
            int riversed = calcRiversed(blackStone, whiteStone, position);
            blackStone = blackStone ^ riversed ^ position;
            whiteStone = whiteStone ^ riversed;
        } else {
            int riversed = calcRiversed(whiteStone, blackStone, position);
            blackStone = blackStone ^ riversed;
            whiteStone = whiteStone ^ riversed ^ position;
        }
    }

    private int calcRiversed(int own, int opponent, int position) {
        int riversed = riversedLine(own, opponent, position, 0x6666, s -> s >> 1);
        riversed |= riversedLine(own, opponent, position, 0x6666, s -> s << 1);
        riversed |= riversedLine(own, opponent, position, 0x0ff0, s -> s >> 4);
        riversed |= riversedLine(own, opponent, position, 0x0ff0, s -> s << 4);
        riversed |= riversedLine(own, opponent, position, 0x0660, s -> s >> 3);
        riversed |= riversedLine(own, opponent, position, 0x0660, s -> s << 3);
        riversed |= riversedLine(own, opponent, position, 0x0660, s -> s >> 5);
        riversed |= riversedLine(own, opponent, position, 0x0660, s -> s << 5);
        return riversed;
    }

    private int riversedLine(
        int own, 
        int opponent, 
        int position,
        int mask,
        Function<Integer, Integer> directionShift) 
    {
        int opponentLine = opponentLineFromPosition(position, opponent, mask, directionShift);
        if ((directionShift.apply(opponentLine) & own) != 0) {
            return opponentLine;
        } else {
            return 0;
        }
    }

    private int opponentLineFromPosition(
        int position, 
        int opponent, 
        int mask, 
        Function<Integer, Integer> directionShift) 
    {
        int candidate = opponent & mask;
        int line = directionShift.apply(position) & candidate;
        line |= directionShift.apply(line) & candidate;
        return line;
    }


    /**
     * 【デバッグ用】ビットボードを表示する。
     * @param bb
     */
    public void printBB(int bb) {
        int mask = 0x0001;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if ((bb & mask) != 0x0000) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
                mask <<= 1;
            }
            System.out.println();
        }
        System.out.println();
    }

    public int getBlackStone() {
        return this.blackStone;
    }
    public int getWhiteStone() {
        return this.whiteStone;
    }
}
