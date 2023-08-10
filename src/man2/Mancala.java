//package man2;
//
////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//
//
//
//import java.util.Random;
//
//
//public class Mancala {
//    public Mancala() {
//    }
//
//    public int Heuristic1(Board board, int id) {
//        return board.getScore(id) - board.getScore(1 - id);
//    }
//
//    public int Heuristic2(Board board, int W1, int W2, int id) {
//        int yy = board.getScore(id) - board.getScore(1 - id);
//        int xx = board.totStoneOnSide(id) - board.totStoneOnSide(1 - id);
//        return W1 * yy + W2 * xx;
//    }
//
//    public int Heuristic3(Board board, int W1, int W2, int W3, int id) {
//        int xx = board.getScore(id) - board.getScore(1 - id);
//        int yy = board.totStoneOnSide(id) - board.totStoneOnSide(1 - id);
//        int zz = board.additionalMovesEarned(id);
//        return W1 * xx + W2 * yy + W3 * zz;
//    }
//
//    public int Heuristic4(Board board, int W1, int W2, int W3, int W4, int id) {
//        int xx = board.getScore(id) - board.getScore(1 - id);
//        int yy = board.totStoneOnSide(id) - board.totStoneOnSide(1 - id);
//        int zz = board.additionalMovesEarned(id);
//        int hh = ((Storage)board.pots.elementAt(6)).numOfStones + ((Storage)board.pots.elementAt(13)).numOfStones;
//        return W1 * xx + W2 * yy + W3 * zz + W4 * hh;
//    }
//
//    public int HeuristicController(Board board, int PlayerID) {
//        int uu = 0;
//        Random rand = new Random();
//        int W1 = rand.nextInt(100) + 1;
//        int W2 = 100 - W1;
//        int W3 = rand.nextInt(50);
//        int W4 = rand.nextInt(80);
//        int rs = 0;
//        if (rs == 0) {
//            switch(PlayerID) {
//                case 0:
//                    this.Heuristic4(board, W1, W2, W3, W4, PlayerID);
//                case 1:
//                    uu = this.Heuristic2(board, W1, W2, PlayerID);
//            }
//        } else if (rs == 1) {
//            switch(PlayerID) {
//                case 1:
//                    this.Heuristic1(board, PlayerID);
//                case 0:
//                    uu = this.Heuristic3(board, W1, W2, W3, PlayerID);
//            }
//        } else if (rs == 2) {
//            switch(PlayerID) {
//                case 1:
//                    this.Heuristic2(board, W1, W2, PlayerID);
//                case 0:
//                    uu = this.Heuristic3(board, W1, W2, W3, PlayerID);
//            }
//        } else {
//            switch(PlayerID) {
//                case 1:
//                    this.Heuristic4(board, W1, W2, W3, W4, PlayerID);
//                case 0:
//                    uu = this.Heuristic3(board, W1, W2, W3, PlayerID);
//            }
//        }
//
//        return uu;
//    }
//
//    public int UTILITY(Board board, int PlayerID) {
//        return this.HeuristicController(board, PlayerID);
//    }
//
//    boolean Terminal_test(Board board, int depth, int PlayerID) {
//        if (depth == 1) {
//            return true;
//        } else {
//            int player = board.totStoneOnSide(PlayerID);
//            int Opponent = board.totStoneOnSide(1 - PlayerID);
//            return player == 0 || Opponent == 0;
//        }
//    }
//
//    public int max(int a, int b) {
//        return a > b ? a : b;
//    }
//
//    public int min(int a, int b) {
//        return a < b ? a : b;
//    }
//
//    Pair MAX(Board board, int alpha, int beta, int depth, int PlayerID) {
//        Pair<Integer, Integer> p = new Pair(-999999, -1);
//        if (this.Terminal_test(board, depth, PlayerID)) {
//            return new Pair(this.UTILITY(board, PlayerID), -1);
//        } else {
//            Board b = new Board();
//            int nxtPlayer = -1;
//
//            for(int i = 0; i < 6; ++i) {
//                if (((Storage)b.pots.elementAt(i)).numOfStones > 0) {
//                    if (PlayerID == 0) {
//                        nxtPlayer = b.makeAMove(i, PlayerID);
//                    } else {
//                        nxtPlayer = b.makeAMove(i, PlayerID);
//                    }
//                }
//
//                int val = false;
//                int val;
//                if (nxtPlayer == PlayerID) {
//                    val = (Integer)this.MAX(b, alpha, beta, depth - 1, PlayerID).getKey();
//                } else {
//                    val = (Integer)this.MIN(b, alpha, beta, depth - 1, 1 - PlayerID).getKey();
//                }
//
//                if ((Integer)p.getKey() < val) {
//                    p = new Pair(val, i);
//                }
//
//                if ((Integer)p.getKey() >= beta) {
//                    return p;
//                }
//
//                alpha = this.max(alpha, (Integer)p.getKey());
//            }
//
//            return p;
//        }
//    }
//
//    Pair MIN(Board board, int alpha, int beta, int depth, int PlayerID) {
//        Pair<Integer, Integer> p = new Pair(999999, -1);
//        if (this.Terminal_test(board, depth, PlayerID)) {
//            return new Pair(this.UTILITY(board, PlayerID), -1);
//        } else {
//            new Board();
//            int nxtPlayer = -1;
//
//            for(int i = 0; i < 6; ++i) {
//                Board b = board.copyBoard();
//                if (((Storage)b.pots.elementAt(i)).numOfStones > 0) {
//                    if (PlayerID == 0) {
//                        nxtPlayer = b.makeAMove(i, PlayerID);
//                    } else {
//                        nxtPlayer = b.makeAMove(i, PlayerID);
//                    }
//                }
//
//                int val = false;
//                int val;
//                if (nxtPlayer == PlayerID) {
//                    val = (Integer)this.MIN(b, alpha, beta, depth - 1, PlayerID).getKey();
//                } else {
//                    val = (Integer)this.MAX(b, alpha, beta, depth - 1, 1 - PlayerID).getKey();
//                }
//
//                if ((Integer)p.getKey() > val) {
//                    p = new Pair(val, i);
//                }
//
//                if ((Integer)p.getKey() <= alpha) {
//                    return p;
//                }
//
//                beta = this.min(beta, (Integer)p.getKey());
//            }
//
//            return p;
//        }
//    }
//
//    public int MIN_MAX(Board board, int PlayerID) {
//        int alpha = -999999;
//        int beta = 999999;
//        int depth = 12;
//        Pair<Integer, Integer> p = this.MAX(board, alpha, beta, depth, PlayerID);
//        return (Integer)p.getValue();
//    }
//
//    public static void main(String[] args) {
//        Mancala m = new Mancala();
//        Board b = new Board();
//        b.printBoard();
//        int playerId = 0;
//        int move = m.MIN_MAX(b, playerId);
//
//        while(true) {
//            int idx = 0;
//            if (((Storage)b.pots.elementAt(move)).numOfStones > 0) {
//                System.out.println("Player" + (playerId + 1) + " gives " + move + "th move");
//                playerId = b.makeAMove(move, playerId);
//                b.printBoard();
//            } else {
//                int i;
//                if (playerId == 0) {
//                    for(i = 0; i < 6; ++i) {
//                        if (((Storage)b.pots.elementAt(i)).numOfStones != 0) {
//                            idx = i;
//                        }
//                    }
//
//                    System.out.println("Player" + (playerId + 1) + " gives " + idx + "th move");
//                    playerId = b.makeAMove(idx, playerId);
//                    b.printBoard();
//                } else {
//                    for(i = 0; i < 6; ++i) {
//                        if (((Storage)b.pots.elementAt(i + 7)).numOfStones != 0) {
//                            idx = i;
//                        }
//                    }
//
//                    System.out.println("Player" + (playerId + 1) + " gives " + idx + "th move");
//                    playerId = b.makeAMove(idx, playerId);
//                    b.printBoard();
//                }
//            }
//
//            if (b.isGameOver()) {
//                b.gameOver();
//                System.out.println("Game Over");
//                b.printBoard();
//                System.out.println("Player 1 Score : " + b.getScore(0));
//                System.out.println("Player 2 Score : " + b.getScore(1));
//                if (b.getScore(0) > b.getScore(1)) {
//                    System.out.println("Player1 wins");
//                } else if (b.getScore(0) < b.getScore(1)) {
//                    System.out.println("Player2 wins");
//                } else {
//                    System.out.println("Draw");
//                }
//
//                return;
//            }
//
//            move = m.MIN_MAX(b, playerId);
//        }
//    }
//}
//
