package jp.skywill.minireversi;

import java.util.Scanner;

public class ExecuteGame {

    public static final boolean IS_PVP_MODE = false;

    public static void main(String[] args) throws Exception {
        Board board = new Board();
        board.printBoard();

        GameParticipant p1, p2;
        if (IS_PVP_MODE) {
            p1 = new Player(true);
            p2 = new Player(false);
        } else {
            if (decideIfPlayerIsBlack()) {
                p1 = new Player(true);
                p2 = new ComputerMaxRev(false);
            } else {
                p1 = new ComputerMaxRev(true);
                p2 = new Player(false);
            }
        } 

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

    private static boolean decideIfPlayerIsBlack() {
        while (true) {
            System.out.print("先手:1, 後手:2を入力してください:");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (input.length() == 1) {
                char c = input.charAt(0);
                if (c == '1') {
                    return true;
                } else if (c == '2') {
                    return false;
                }
            }
        }
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