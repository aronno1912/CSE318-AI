package mancala2;

public class State {

    //binCount, blocks can be changed
    //value is the heuristic value of the state
    //move is the move that will generate this state from its parent state
    public int binCount = 6;
    public int  maxScore = 0; //number of stones in storage bin of one player
    public int minScore = 0;
    public int value, move = 0;
    int[] maxArr = new int[binCount];
    int[] minArr = new int[binCount];
    public State bestChild;


    public State() {

    }


    //copy the contents of a parent state's array into a child states's array
    public void copy(int[] arr, String str)
    {

        int i;
        if(str == "minArr") {

            for(i = 0; i < binCount; i++)
                minArr[i] = arr[i];
        }
        else {

            for(i = 0; i < binCount; i++)
                maxArr[i] = arr[i];

        }

    }


    //initialize only the root state
    /*public void initializeBoard() {

        int i;
        for(i = 0; i < binCount; i++) {

            maxArr[i] = blocks;
            minArr[i] = blocks;
        }
    }*/


    //count the total number of blocks in all the bins of a player
    //Koyta stone ache
    public int remainingStonesInBins(int[] arr) {

        int i, count = 0;
        for(i = 0; i < binCount; i++)
            count += arr[i];
        return count;
    }


    //check whether an array has all zero elements
    //return true if any array has all zero elements, else return false
    public boolean anysideAllEmpty(int[] arr)
    {
        for( int i = 0; i < binCount; i++) {

            if(arr[i]!= 0)
               return false;
        }

            return true;


    }


    public void printState() {

        int i;
        //System.out.print("MaxArray: ");
        System.out.println("-------------------------------");
        System.out.print("|  ");
        //maxarr ta hocche uporer ta
        for(i = 0; i < binCount; i++) {
            System.out.print("| ");
            System.out.print(maxArr[i] + " ");
        }
        //for (int i = board.length - 2; i >= board.length / 2; i--) {
        //  System.out.print("| ");
        //System.out.printf("%-2s",board[i]);
        // }
        System.out.print("|  |\n|");
        System.out.printf("%-2d|-----------------------|%2d|\n", maxScore, minScore);
        System.out.print("|  ");
        for(i = 0; i < binCount; i++) {
            System.out.print("| ");
            System.out.print(minArr[i] + " ");
        }
        System.out.println("|  |");
        System.out.println("-------------------------------");

    }

}
