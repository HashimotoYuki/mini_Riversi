package jp.skywill.minireversi;

import java.util.function.Function;

/**
 * リバーシの盤面を表現したクラス。
 * 石の配置を管理し、反転処理や合法手（相手の石を反転できる手）の計算などを行える。
 */
public class Board {
    /**
     * 盤面の行数
     */
    public static final int HEIGHT = 4;
    /**
     * 盤面の列数
     */
    public static final int WIDTH = 4;

    private int blackStone;
    private int whiteStone;

    /**
     * 初期配置の盤面を生成する。
     */
    public Board() {
        this.blackStone = 0x0240;
        this.whiteStone = 0x0420;
    }

    /**
     * 既存の盤面を複製する。
     * @param board 複製元の盤面
     */
    public Board(Board board) {
        this.blackStone = board.getBlackStone();
        this.whiteStone = board.getWhiteStone();
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
                    System.out.print("x ");
                } else if ((whiteStone & mask) != 0x0000) {
                    System.out.print("o ");
                } else {
                    System.out.print(". ");
                }
                mask <<= 1;
            }

            System.out.print("  ");
            for (int j = 0; j < WIDTH; j++) {
                int index = i * HEIGHT + j;
                int c = index <= 9 ? '0'+index : 'a'+(index-10);
                System.out.printf("%c ", c);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * 黒石、または白石の数をカウントして返す。
     * @param isBlack true -> 黒石をカウント、false -> 白石をカウント
     * @return 石の数
     */
    public int countStone(boolean isBlack) {
        if (isBlack) {
            return countBit(blackStone);
        } else {
            return countBit(whiteStone);
        }
    }

    /**
     * 引数で与えられたbit列において、下位16bitに含まれる1の数をカウントして返す。
     * ただし、上位16bitがすべて0であることは保証されているものとする。
     * @param bit 対象となるbit列 
     * @return {@code bit}の下位16bitに含まれる1の数
     */
    public int countBit(int bit) {
        bit = (bit & 0x5555) + (bit >> 1 & 0x5555);
        bit = (bit & 0x3333) + (bit >> 2 & 0x3333);
        bit = (bit & 0x0f0f) + (bit >> 4 & 0x0f0f);
        bit = (bit & 0x00ff) + (bit >> 8 & 0x00ff);
        return bit;
    }

    /**
     * 合法手（置くと相手の石を返せる手）を表すビット列を計算して返す。
     * 合法手が複数ある場合も、それらすべてを含むビット列が返される。
     * @param isBlackTurn 現在の手番が黒かどうか
     * @return 合法手を表すbit列
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
     * 石を反転させる。
     * ただし、positionが合法手（置くと相手の石を返せる手）であることは保証されているものとする。
     * @param isBlackTurn 現在の手番が黒かどうか
     * @param position 石を置く場所を表すbit列
     */
    public void reverse(boolean isBlackTurn, int position) {
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

    /**
     * 石を置いたとき、反転する石を表すbit列を計算して返す。
     * @param isBlackTurn 現在の手番が黒かどうか
     * @param position 石を置く場所を表すbit列
     * @return : 反転する石を表すbit列
     */
    public int calcRiversed(boolean isBlackTurn, int position) {
        if (isBlackTurn) {
            return calcRiversed(blackStone, whiteStone, position);
        } else {
            return calcRiversed(whiteStone, blackStone, position);
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
     * ゲームが終了しているかどうかを判定する。
     * @return 黒と白の両方に置ける場所がない -> true
     */
    public boolean isFinishedGame() {
        return (calcLegalPosition(true) == 0 && calcLegalPosition(false) == 0);
    }

    /**
     * 【デバッグ用】
     * bit列を盤面の形式で表示する。
     * ビットが立っているマスを'#', そうでないマスを'.'で表す。
     * @param bb 表示させたいbit列
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

    /**
     * 黒石の配置を表すbit列を返す。
     * @return 黒石の配置を表すbit列
     */
    public int getBlackStone() {
        return this.blackStone;
    }

    /**
     * 白石の配置を表すbit列を返す。
     * @return 白石の配置を表すbit列
     */
    public int getWhiteStone() {
        return this.whiteStone;
    }
}
