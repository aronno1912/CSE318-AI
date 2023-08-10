package mancala2;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    public static int player = 0, computer = 1, heuristicType = 4;
    public static int turn, binCount = 6, flag = 0; //flag 1 mane next turn o same player eri
    public static int treeTurn, treeFlag = 0;
    public static int actuallyPlaying = 1, simulation = 2;
    public static int maxDepth = 9;
    public static State root = new State();
    public static State bestState;
    public static int stonesCapPlayer = 0, stonesCapCom = 0, additionalMovesPlayer = 0, addMovesCom = 0;
    public static ArrayList<State> gameState = new ArrayList<>();
    public static ArrayList<Integer> moves = new ArrayList<>();
    static Heuristic heuristic;

    //alpha= highestValue choice found at any choice point of path for MAX

    public static void setHeuristicType(int type,int stonesCap, int addMoves) {
        if(type == 1)
            heuristic = new H1();
        else if(type == 2)
            heuristic = new H2();
        else if(type == 3)
            heuristic = new H3(stonesCap);
        else if(type == 4)
            heuristic = new H4(stonesCap, addMoves);
        else if(type == 5)
            heuristic = new H5();
        else if(type == 6)
            heuristic = new H6();
    }

    //shey nije min  node, so shey tar child theke min value ta niye tar beta te rakhbe
    public static int minValue(State currState, int depth, int alpha, int beta,int mode)
    {

        if(currState.anysideAllEmpty(currState.maxArr) || currState.anysideAllEmpty(currState.minArr) || depth == maxDepth)
        {
            // game sesh
            bestState = currState;
            return currState.move;

        }

        //min valuer jonno first e ekta max number set kore nibo
        currState.value = Integer.MAX_VALUE;
        int i;
        State newState = null;

        for(i = 0; i < binCount; i++)
        {
             //just next iteration e chole jabe
            if(currState.minArr[i] == 0) continue;

            //kon bin choose korle ki state hobe
            newState = Match.makeState(2, currState, i, treeTurn, depth,mode);
            newState.move = i; //kon bin thrkr move korlo

            if(treeFlag == 0)
            {

                treeTurn = (treeTurn + 1) % 2;
                //nije min node. tai child er min val ta nibe
                newState.value = Math.min(newState.value, maxValue(newState, depth + 1, alpha, beta,mode));
            }

            if(treeFlag == 1) {
                newState.value = Math.min(newState.value, minValue(newState, depth + 1, alpha, beta,mode));
                treeFlag = 0;
            }

            if(newState.value <= alpha) {
                bestState = newState;
                return newState.value;
            }
            beta = Math.min(beta, newState.value);

        }
        bestState = newState;
        return newState.value;
    }


    public static int maxValue(State currState, int depth, int alpha, int beta,int mode)
    {

        if(currState.anysideAllEmpty(currState.maxArr) || currState.anysideAllEmpty(currState.minArr) || depth == maxDepth) {
            bestState = currState;
            return currState.value;

        }

        currState.value = Integer.MIN_VALUE;
        int i;
        State newState = null;
        for(i = 0; i < binCount; i++)
        {
            if(currState.maxArr[i] == 0) continue;

            newState = Match.makeState(2, currState, i, treeTurn, depth,mode);
            newState.move = i;
            if(treeFlag == 0) {

                treeTurn = (treeTurn + 1) % 2;
                newState.value = Math.max(newState.value, minValue(newState, depth + 1, alpha, beta,mode));

            }

            //computer or maximizing agent gets an extra turn
            if(treeFlag == 1) {

                newState.value = Math.max(newState.value, maxValue(newState, depth + 1, alpha, beta,mode));
                treeFlag = 0;

            }

            if(newState.value >= beta) {
                //System.out.println("maxValue: 2nd return, depth: " + depth);
                bestState = newState;
                return newState.value;

            }
            alpha = Math.max(alpha, newState.value);

        }
        //System.out.println("maxValue: 3rd return, depth: " + depth);
        bestState = newState;
        return newState.value;
    }


    //takes the current state as a parameter and returns the best possible move
    public static void miniMax(State currState,int mode)
    {

        maxValue(currState, 0, Integer.MIN_VALUE, Integer.MAX_VALUE,mode);
        //System.out.println("Inside miniMax: " + bestState.move);
    }

    public static void main(String[] args) {
        System.out.println("Enter choice:\n" +
                "1. Human vs AI\n" +
                "2. AI vs AI\n" );

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        if(choice == 1)
        {

                 //human vs ai
                Board board = new Board();
                root = board.initializeBoardPlay(root, heuristicType);
                gameState.add(root);
                Match.play(root,1);


        }



        //Ai vs Ai

        else
        {
            Board board = new Board();
            root = board.initializeBoardPlay(root, 3);
            gameState.add(root);
            Match.play(root,2);

        }

    }
}


//sobar bam side theke 1 count shuru