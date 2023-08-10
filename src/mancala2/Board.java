package mancala2;

public class Board {
    public static State initializeBoardPlay(State root,int type)
    {
       // Game.turn = turn;

        //initialize each players bins with 4 stones
        for(int i = 0; i < 6; i++)
        {
            root.maxArr[i] = 4;
            root.minArr[i] = 4;
        }

        Heuristic heuristic = null;
        if(type == 1)
            heuristic = new H1();
        else if(type == 2)
            heuristic = new H2();
        else if(type == 3)
            heuristic = new H3(0);
        else if(type == 4)
            heuristic = new H4(0, 0);


        root.value = heuristic.getValue(root);
        root.printState();
        return root;
    }


}
