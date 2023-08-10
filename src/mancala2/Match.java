package mancala2;

import java.util.Scanner;

public class Match {

    int mode;
    //mode 1 means HumanvsAi
    //mode 2 means ai vs ai
    Match(int mode)
    {
        this.mode=mode;
    }


    public static State makeState(int id, State currState, int whichBin, int turn, int depth,int mode)
    {
           //num mane kon bin theke move korabe
        State newState = new State();
        newState.maxScore = currState.maxScore;
        newState.minScore = currState.minScore;
        newState.value = currState.value;
        newState.copy(currState.maxArr, "maxArr");
        newState.copy(currState.minArr, "minArr");
        int sum = 0;
        //For player's turn
        if((turn == Game.player && id == Game.actuallyPlaying) || (Game.treeTurn == Game.player && id == Game.simulation))
        {

            int stonesInThatBin = newState.minArr[whichBin];
            newState.minArr[whichBin] = 0;
            int j = 1;
            while(sum != stonesInThatBin)
            {

                //player's array

                //0 1 2 3 4 5
                //mane last stone ta ekhono nijer court ei ache
                while(sum < stonesInThatBin && j + whichBin < Game.binCount)
                {

                    newState.minArr[whichBin + j] += 1;
                    sum++;

                    //if the last block ends up in player's array's empty bin
                    //last stone ta kono empty bin e porle opor side er stone gulao niye nibe

                    /**
                     *
                     * One way to amass more stones is by strategically dropping your last stone into an empty hole on your side of the board.
                     * If you manage that,you're allowed to reach across the board and take your opponent's stones that are in the opposite hole.
                     *
                     **/
                    if(sum == stonesInThatBin && newState.minArr[whichBin + j] == 1 && newState.maxArr[whichBin + j] != 0)
                    {
                        newState.minScore += 1 + newState.maxArr[whichBin + j]; //add them also
                        Game.stonesCapPlayer += newState.maxArr[whichBin + j];
                        newState.minArr[whichBin + j] = 0; //coz oi gula dhoe storage e pathaye disi
                        newState.maxArr[whichBin + j] = 0;

                    }

                    j++;

                }

                //player's store
                //hoy storage e porse last stone ta
                if(sum < stonesInThatBin)
                {

                    newState.minScore++;
                    sum++;

                    //if the last block ends up in player's storage(big bin)
                    if(sum == stonesInThatBin)
                    {
                        /**????????????????????????????????????????????????????????????????????**/
                        if(turn == Game.player && id == Game.actuallyPlaying) Game.flag = 1;
                        if(Game.treeTurn == Game.player && id == Game.simulation)
                        {

                            Game.treeFlag = 1;
                            Game.additionalMovesPlayer += 1;

                        }
                    }

                }
                //opponent er  bin e porse
                //computer's array
                int k = Game.binCount - 1;
                while(sum < stonesInThatBin && k >= 0) //
                {

                    newState.maxArr[k] += 1;
                    k--;
                    sum++;

                }
                //erpor o jodi baki theke thake. 0th bin theke abar dewa start korbo
                whichBin = 0;
                j = 0;

            }
        } //player er turn sesh

        //For computer's turn
        else if((turn == Game.computer && id == Game.actuallyPlaying) || (Game.treeTurn == Game.computer && id == Game.simulation))

        {

            int stonesInSelectedBin = newState.maxArr[whichBin];
            newState.maxArr[whichBin] = 0;
            int j = 1;
            while(sum != stonesInSelectedBin)

            {

                //computer's array
                //mane nijer court ei ache
                while(sum < stonesInSelectedBin && whichBin - j >= 0)
                {

                    newState.maxArr[whichBin - j] += 1;
                    sum++;

                    //if the last block ends up in computer's array's empty bin
                    if(sum == stonesInSelectedBin && newState.maxArr[whichBin - j] == 1 && newState.minArr[whichBin - j] != 0) {

                        newState.maxScore += 1 + newState.minArr[whichBin - j];
                        Game.stonesCapCom += newState.minArr[whichBin - j];
                        newState.minArr[whichBin - j] = 0;
                        newState.maxArr[whichBin - j] = 0;

                    }
                    j++;

                }

                //computer's store
                if(sum < stonesInSelectedBin)
                {

                    newState.maxScore++;
                    sum++;

                    //if the last block ends up in computer's store
                    if(sum == stonesInSelectedBin) {

                        if(turn == Game.computer && id == Game.actuallyPlaying) Game.flag = 1;
                        if(Game.treeTurn == Game.computer && id == Game.simulation) {

                            Game.treeFlag = 1;
                            Game.addMovesCom += 1;
                        }
                    }

                }

                //player's array
                //opponent er bin e porse
                int k = 0;
                while(sum < stonesInSelectedBin && k < Game.binCount) {

                    newState.minArr[k] += 1;
                    k++;
                    sum++;

                }
                whichBin = Game.binCount;
                j = 1;

            }
        }


        if((Game.treeTurn == Game.player && id == 2 && depth == Game.maxDepth) || ((currState.anysideAllEmpty(currState.maxArr) || currState.anysideAllEmpty(currState.minArr)) && id == Game.simulation))  {
            {
//                if(mode==2)  Game.setHeuristicType(2, Game.stonesCapPlayer, Game.additionalMovesPlayer);
//                else
                Game.setHeuristicType(Game.heuristicType, Game.stonesCapPlayer, Game.additionalMovesPlayer);
                newState.value = Game.heuristic.getValue(newState);
            }
            //  System.out.println("Heuristic value is: " + newState.value);
        }
        if((Game.treeTurn == Game.computer && id == 2 && depth == Game.maxDepth) || ((currState.anysideAllEmpty(currState.maxArr) || currState.anysideAllEmpty(currState.minArr)) && id == Game.simulation)) {
            {
//                if(mode==2)  Game.setHeuristicType(2, Game.stonesCapCom, Game.addMovesCom);
//                else
                Game.setHeuristicType(Game.heuristicType, Game.stonesCapCom, Game.addMovesCom);
                newState.value = Game.heuristic.getValue(newState);
            }
            //System.out.println("Heuristic value is: " + newState.value);
        }
        //System.out.println("does it?");
        return newState;

    }

    public static void play(State currState, int mode)
    {
        if (mode == 2)  //ai vs ai
        {

            while (!isTheEnd(currState,2))
            {


                if (Game.turn == 0)
                    System.out.println("Computer1's turn");
                else if (Game.turn == 1)
                    System.out.print("Computer2's turn: ");


                int selectedBin = 0;
                //Computer1's turn  //min array

                if (Game.turn == 1) {

                    //make a copy of the current State and use it
                    State intelligentState = new State();
                    intelligentState.maxScore = currState.maxScore;
                    intelligentState.minScore = currState.minScore;
                    intelligentState.value = currState.value;
                    intelligentState.copy(currState.minArr, "minArr");
                    intelligentState.copy(currState.maxArr, "maxArr");
                    //Scanner in = new Scanner(System.in);
                    //num = in.nextInt() - 1;
                    Game.treeTurn = Game.computer;
                    Game.miniMax(intelligentState,2);
                    selectedBin = Game.bestState.move;
                    System.out.println(selectedBin + 1);
                }


                //computer2's turn
                if (Game.turn == 1) {

                    //make a copy of the current State and use it
                    State intelligentState = new State();
                    intelligentState.maxScore = currState.maxScore;
                    intelligentState.minScore = currState.minScore;
                    intelligentState.value = currState.value;
                    intelligentState.copy(currState.minArr, "minArr");
                    intelligentState.copy(currState.maxArr, "maxArr");
                    //Scanner in = new Scanner(System.in);
                    //num = in.nextInt() - 1;
                    Game.treeTurn = Game.computer;
                    Game.miniMax(intelligentState,2);
                    selectedBin = Game.bestState.move;
                    System.out.println(selectedBin + 1);
                }


                Game.moves.add(selectedBin);
                State newState;
                newState = makeState(1, currState, selectedBin, Game.turn, 0,2);
                currState.bestChild = newState;
                newState.move = selectedBin;
                newState.printState();
                Game.gameState.add(newState);
                currState = newState;

                //next turn opponent er
                if (Game.flag == 0)
                    Game.turn = (Game.turn + 1) % 2;
                Game.flag = 0;

            }

        }
        //Human vs AI
        else
        {

        while (!isTheEnd(currState,1))
        {


            if (Game.turn == 0)
                System.out.println("Your turn");
            else if (Game.turn == 1)
                System.out.print("Computer's turn: ");


            int selectedBin = 0;
            //player's turn
            while (Game.turn == 0) {

                Scanner in = new Scanner(System.in);
                selectedBin = in.nextInt() - 1;

                //check if the move is valid
                if (selectedBin < 0 || selectedBin >= Game.binCount)
                    continue;
                else {

                    //if the move is given in a bin containing zero blocks
                    if (Game.turn == 0 && currState.minArr[selectedBin] == 0) {
                        System.out.println("No stones in this bin....Please select another one");
                        continue;
                    } else if (Game.turn == 1 && currState.maxArr[selectedBin] == 0) {
                        System.out.println("No stones in this bin....Please select another one");
                        continue;
                    }
                }

                break;

            }

            //computer's turn
            if (Game.turn == 1) {

                //make a copy of the current State and use it
                State intelligentState = new State();
                intelligentState.maxScore = currState.maxScore;
                intelligentState.minScore = currState.minScore;
                intelligentState.value = currState.value;
                intelligentState.copy(currState.minArr, "minArr");
                intelligentState.copy(currState.maxArr, "maxArr");
                //Scanner in = new Scanner(System.in);
                //num = in.nextInt() - 1;
                Game.treeTurn = Game.computer;
                Game.miniMax(intelligentState,1);
                selectedBin = Game.bestState.move;
                System.out.println(selectedBin + 1);
            }


            Game.moves.add(selectedBin);
            State newState;
            newState = makeState(1, currState, selectedBin, Game.turn, 0,1);
            currState.bestChild = newState;
            newState.move = selectedBin;
            newState.printState();
            Game.gameState.add(newState);
            currState = newState;

            //next turn opponent er
            if (Game.flag == 0)
                Game.turn = (Game.turn + 1) % 2;
            Game.flag = 0;

        }
      }
    }

    public static boolean isTheEnd(State currState,int mode)
    {
        //The game ends when one player has emptied the six holes of stones on their side.
        // The other player then takes all the stones on their own side and places them into their store (or bowl).

        if (currState.anysideAllEmpty(currState.maxArr) || currState.anysideAllEmpty(currState.minArr)) {

            int count = currState.remainingStonesInBins(currState.maxArr);
            if (count != 0)
                currState.maxScore += count;

            count = currState.remainingStonesInBins(currState.minArr);
            if (count != 0)
                currState.minScore += count;
            if (mode == 2)
            {
                System.out.println("Computer1's final score: " + currState.minScore);
                System.out.println("Computer2's final score: " + currState.maxScore);
                if (currState.maxScore < currState.minScore)
                    System.out.println("Computer1 won");
                else if (currState.minScore < currState.maxScore)
                    System.out.println("Computer2 won");
                else
                    System.out.println("Game drawn");
            }
            else
            {
            System.out.println("Your final score: " + currState.minScore);
            System.out.println("Computer's final score: " + currState.maxScore);
            if (currState.maxScore < currState.minScore)
                System.out.println("Congratulations! You won");
            else if (currState.minScore < currState.maxScore)
                System.out.println("Sorry! You lost");
            else
                System.out.println("It is a Draw!!!!!!!!!!");
           }

            return true;

        }

        return false;
    }
}
