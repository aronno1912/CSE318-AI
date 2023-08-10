//package mancala2;
//
//public class AIvsAI {
//
//    public static State makeState(int id, State currState, int num, int turn, int depth) {
//
//        State newState = new State();
//        newState.maxScore = currState.maxScore;
//        newState.minScore = currState.minScore;
//        newState.value = currState.value;
//        newState.copy(currState.maxArr, "maxArr");
//        newState.copy(currState.minArr, "minArr");
//        int sum = 0;
//        //For player's turn
//        if((turn == Game.player && id == Game.actuallyPlaying) || (Game.treeTurn == Game.player && id == Game.simulation)) {
//
//            int count = newState.minArr[num];
//            newState.minArr[num] = 0;
//            int j = 1;
//            while(sum != count) {
//
//                //player's array
//                while(sum < count && j + num < Game.binCount) {
//
//                    newState.minArr[num + j] += 1;
//                    sum++;
//
//                    //if the last block ends up in player's array's empty bin
//                    if(sum == count && newState.minArr[num + j] == 1) {
//
//                        newState.minScore += 1 + newState.maxArr[num + j];
//                        Game.stonesCapPlayer += newState.maxArr[num + j];
//                        newState.minArr[num + j] = 0;
//                        newState.maxArr[num + j] = 0;
//
//                    }
//
//                    j++;
//
//                }
//
//                //player's store
//                if(sum < count) {
//
//                    newState.minScore++;
//                    sum++;
//
//                    //if the last block ends up in player's store
//                    if(sum == count) {
//
//                        if(turn == Game.player && id == Game.actuallyPlaying) Game.flag = 1;
//                        if(Game.treeTurn == Game.player && id == Game.simulation) {
//
//                            Game.treeFlag = 1;
//                            Game.addMovesCom += 1;
//
//                        }
//                    }
//
//                }
//
//                //computer's array
//                int k = Game.binCount - 1;
//                while(sum < count && k >= 0) {
//
//                    newState.maxArr[k] += 1;
//                    k--;
//                    sum++;
//
//                }
//                num = 0;
//                j = 0;
//
//            }
//        }
//
//        //For computer's turn
//        else if((turn == Game.computer && id == Game.actuallyPlaying) || (Game.treeTurn == Game.computer && id == Game.simulation)) {
//
//            int count = newState.maxArr[num];
//            newState.maxArr[num] = 0;
//            int j = 1;
//            while(sum != count) {
//
//                //computer's array
//                while(sum < count && num - j >= 0) {
//
//                    newState.maxArr[num - j] += 1;
//                    sum++;
//
//                    //if the last block ends up in computer's array's empty bin
//                    if(sum == count && newState.maxArr[num - j] == 1) {
//
//                        newState.maxScore += 1 + newState.minArr[num - j];
//                        Game.stonesCapCom += newState.minArr[num - j];
//                        newState.minArr[num - j] = 0;
//                        newState.maxArr[num - j] = 0;
//
//                    }
//                    j++;
//
//                }
//
//                //computer's store
//                if(sum < count) {
//
//                    newState.maxScore++;
//                    sum++;
//
//                    //if the last block ends up in computer's store
//                    if(sum == count) {
//
//                        if(turn == Game.computer && id == Game.actuallyPlaying) Game.flag = 1;
//                        if(Game.treeTurn == Game.computer && id == Game.simulation) {
//
//                            Game.treeFlag = 1;
//                            Game.addMovesCom += 1;
//                        }
//                    }
//
//                }
//
//                //player's array
//                int k = 0;
//                while(sum < count && k < Game.binCount) {
//
//                    newState.minArr[k] += 1;
//                    k++;
//                    sum++;
//
//                }
//                num = Game.binCount;
//                j = 1;
//
//            }
//        }
//
//        //System.out.println("Treeturn: " + treeTurn + ", depth: " + depth);
//        if(Game.treeTurn == Game.player)  {
//            //newState.value = Game.heuristic(newState, Game.heuristicType1, Game.stonesCapPlayer, Game.addMovesPlayer);
//            Game.setHeuristicType(Game.heuristicType1, Game.stonesCapPlayer, Game.additionalMovesPlayer);
//            newState.value = Game.heuristic.getValue(newState);
//            //  System.out.println("Heuristic value is: " + newState.value);
//        }
//        if(Game.treeTurn == Game.computer) {
//            //newState.value = Game.heuristic(newState, Game.heuristicType2, Game.stonesCapCom, Game.addMovesCom);
//            Game.setHeuristicType(Game.heuristicType2, Game.stonesCapCom, Game.additionalMovesPlayer);
//            newState.value = Game.heuristic.getValue(newState);
//            //System.out.println("Heuristic value is: " + newState.value);
//        }
//        //System.out.println("does it?");
//        return newState;
//
//    }
//
//    public static boolean gameOver(State currState) {
//
//        if (currState.anysideAllEmpty(currState.maxArr) || currState.anysideAllEmpty(currState.minArr)) {
//
//            int count = currState.remainingStonesInBins(currState.maxArr);
//            if (count != 0)
//                currState.maxScore += count;
//
//            count = currState.remainingStonesInBins(currState.minArr);
//            if (count != 0)
//                currState.minScore += count;
//
//            //System.out.println("Your final score: " + currState.minScore);
//            //System.out.println("Computer's final score: " + currState.maxScore);
//            if (currState.maxScore < currState.minScore) {
//                Game.winner = Game.player;
//                //System.out.println("Congratulations! You won");
//            }
//            else if (currState.minScore < currState.maxScore) {
//                Game.winner = Game.computer;
//                //System.out.println("Sorry! You lost");
//            }
//            else {
//                Game.winner = -1;
//                //System.out.println("Game drawn");
//            }
//
//            return true;
//
//        }
//
//        return false;
//    }
//
//    public static void gameAlgo(State currState) {
//
//        while (!gameOver(currState)) {
//
//            /*if(turn == 0)
//                System.out.println("Your turn");
//            else if(turn == 1)
//                System.out.print("Computer's turn: ");
//            */
//
//            int num = 0;
//
//            //1st AI's turn
//            if(Game.turn == Game.player) {
//
//                //make a copy of the current State and use it
//                State intelligentState = new State();
//                intelligentState.maxScore = currState.maxScore;
//                intelligentState.minScore = currState.minScore;
//                intelligentState.value = currState.value;
//                intelligentState.copy(currState.minArr, "minArr");
//                intelligentState.copy(currState.maxArr, "maxArr");
//                //Scanner in = new Scanner(System.in);
//                //num = in.nextInt() - 1;
//                Game.treeTurn = Game.player;
//                Game.miniMax(intelligentState);
//                num = Game.bestState.move;
//                //System.out.println(num + 1);
//
//            }
//
//            //2nd AI's turn
//            if(Game.turn == Game.computer) {
//
//                //make a copy of the current State and use it
//                State intelligentState = new State();
//                intelligentState.maxScore = currState.maxScore;
//                intelligentState.minScore = currState.minScore;
//                intelligentState.value = currState.value;
//                intelligentState.copy(currState.minArr, "minArr");
//                intelligentState.copy(currState.maxArr, "maxArr");
//                //Scanner in = new Scanner(System.in);
//                //num = in.nextInt() - 1;
//                Game.treeTurn = Game.computer;
//                Game.miniMax(intelligentState);
//                num = Game.bestState.move;
//                //System.out.println(num + 1);
//
//            }
//
//
//            //moves.add(num);
//            State newState;
//            newState = makeState(1, currState, num, Game.turn, 0);
//            currState.bestChild = newState;
//            newState.move = num;
//            //newState.printState();
//            //gameState.add(newState);
//            currState = newState;
//
//            //System.out.println("Before Flag: " + flag);
//            if(Game.flag == 0)
//                Game.turn = (Game.turn + 1) % 2;
//            Game.flag = 0;
//            //System.out.println("After Flag: " + flag);
//            //System.out.println("Turn: " + turn);
//        }
//    }
//
//    public static void match(int p1, int p2) {
//
//        int i;
//        for(i = 1; i <= Game.numberOfGamesPerPair; i++) {
//
//            //heuristicType 1(p1) is giving the first move
//            if(i >= 1 && i <= Game.numberOfGamesPerPair/2) Game.turn = Game.player;
//
//            //heuristicType 2(p2) is giving the first move
//            if(i >= Game.numberOfGamesPerPair/2 && i <= Game.numberOfGamesPerPair) Game.turn = Game.computer;
//
//            Board board = new Board();
//            Game.root = board.initializeBoardSim(Game.root);
//            gameAlgo(Game.root);
//
//            //First move by player & won by player
//            if(i >= 1 && i <= Game.numberOfGamesPerPair/2 && Game.winner == Game.player)
//                Game.stat[p1][p2]++;
//
//            //First move by player, won by computer
//            if(i >= Game.numberOfGamesPerPair/2 && i <= Game.numberOfGamesPerPair && Game.winner == Game.computer)
//                Game.reverseStat[p1][p2]++;
//
//            //First move by computer, won by computer
//            if(i >= Game.numberOfGamesPerPair/2 && i <= Game.numberOfGamesPerPair && Game.winner == Game.computer)
//                Game.stat[p2][p1]++;
//
//            //First move by computer, won by player
//            if(i >= Game.numberOfGamesPerPair/2 && i <= Game.numberOfGamesPerPair && Game.winner == Game.player)
//                Game.reverseStat[p2][p1]++;
//        }
//
//        //if(Game.stat[p1][p2] != 0) Game.stat[p1][p2]--;
//        //if(Game.stat[p2][p1] != 0) Game.stat[p2][p1]--;
//        if(Game.reverseStat[p1][p2] != 0) Game.reverseStat[p1][p2]--;
//        if(Game.reverseStat[p2][p1] != 0) Game.reverseStat[p2][p1]--;
//
//
//    }
//
//}
//
//
