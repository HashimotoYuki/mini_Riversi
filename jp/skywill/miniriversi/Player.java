package jp.skywill.miniriversi;

import java.util.Scanner;

public class Player extends GameParticipant {
    public Player(boolean isBlack) {
        super(isBlack);
    }

    @Override
    public int decidePosition(Board board) {
        int legalPosition = board.calcLegalPosition(isBlack());

        while (true) {
            System.out.println("Position : ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            
            if (input.length() == 1) {
                char c = input.charAt(0);
                try {
                    int position = 1 << hexCharToInt(c);
                    if ((position & legalPosition) == position) {
                        return position;
                    } else {
                        System.out.println("その位置は着手不可能です");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("0-fの値を入力してください");
                }
            } else {
                System.out.println("0-fの値を入力してください");
            }
        }     
    }
    
    private int hexCharToInt(char hex) {
        if ('0' <= hex && hex <= '9') {
            return hex - '0';
        } else if ('a' <= hex && hex <= 'f') {
            return 10 + (hex - 'a');
        } else {
            throw new IllegalArgumentException();
        }
    }
}   
