package jp.skywill.minireversi;

import java.util.Scanner;

public class Player extends GameParticipant {
    public Player(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public int decidePosition(Board board) {
        int legalPosition = board.calcLegalPosition(isBlack());
        if (legalPosition == 0) return 0;

        while (true) {
            System.out.print("Position : ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            try {
                int position = 1 << hexStrToInt(input);
                if ((position & legalPosition) == position) {
                    return position;
                } else {
                    System.out.println("その場所は着手不可能です");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("0-fの値を入力してください");
            }
        }     
    }

    private int hexStrToInt(String hex) {
        if (hex.length() == 1) {
            char head = hex.charAt(0);
            if ('0' <= head && head <= '9') {
                return head - '0';
            }
            if ('a' <= head && head <= 'f') {
                return 10 + (head - 'a');
            }
        }
        throw new IllegalArgumentException();
    }
}   
