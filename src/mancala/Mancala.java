package mancala;

import java.util.Random;
import java.util.Scanner;

public class Mancala {

    public static void main(String[] args) {
        System.out.println("Starting Mancala....");

        int p1_win=0,p2_win=0,h1=1,h2=1;
        Random random = new Random();
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter number of iterations : ");
        int it = sc.nextInt();
        System.out.println("Choose Heuristic :\n" +
                "0 : Random\n" +
                "1 : Chosen heuristic\n-----------------\n");
        int choice = sc.nextInt();

        if(choice != 0){
            System.out.print("Enter Player 1 Heuristic : ");
            h1 = sc.nextInt();
            System.out.print("Enter Player 2 Heuristic : ");
            h2 = sc.nextInt();
        }
        System.out.println("Choose Player 1 :\n" +
                "1 : Human\n" +
                "2 : AI\n" +
                "3 : Random\n" +
                "------------------\n");
        int p1_choice = sc.nextInt();
        System.out.println("Choose Player 2 :\n" +
                "1 : Human\n" +
                "2 : AI\n" +
                "3 : Random\n" +
                "------------------\n");
        int p2_choice = sc.nextInt();

        for (int i = 0; i < it; i++) {
            Board b = new Board();
            b.printBoard();
            if(choice == 0){
                h1 = random.nextInt(3)+1;
                h2 = random.nextInt(3)+1;
            }

            boolean oppo = random.nextBoolean();

            Player p1 = new Player(b,oppo, h1);
            Player p2 = new Player(b,!oppo, h2);
            System.out.println("Init Player 1 | Heuristic :"+(h1)+" as "+(oppo?"Opponent":"player"));
            System.out.println("Init Player 2 | Heuristic :"+(h2)+" as "+(!oppo?"Opponent":"player"));

            while (!b.gameOver()){
                //System.out.println("\t\tPlayer 1 turn\n");
                p1.selectMove(p1_choice);
                //System.out.println("\t\tPlayer 2 turn\n");
                p2.selectMove(p2_choice);
            }
            if(p1.getMyStorage() > p2.getMyStorage()){
                System.out.println("Player 1 is winner");p1_win++;
            }
            else{
                System.out.println("Player 2 is winner");p2_win++;
            }
            //b.printBoard();
        }
        System.out.println("Win Count: player1 = "+p1_win+" player2 = "+p2_win);
        System.out.println("Win ratio : p1/p2 = "+ Double.toString(p1_win*1.0/p2_win));

    }
}
