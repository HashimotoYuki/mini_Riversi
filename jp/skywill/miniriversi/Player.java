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
            scanner.close();
            //scanner.nextLine();

            //int position = 1 << input;
            //if ((position & legalPosition) == position) {
                //scanner.close();
                //return position;
           // }
        } 

        
    }  
}
