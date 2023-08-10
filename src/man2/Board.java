package man2;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import java.util.HashMap;
import java.util.Vector;

public class Board {
    public Vector<Storage> pots = new Vector();
    public HashMap<Storage, Storage> map;
    public boolean player1;
    public boolean player2;
    public int PLAY1Score;
    public int PLAY2Score;
    public int addMovesPlayer1 = 0;
    public int addMovesPlayer2 = 0;
    public int playerID;

    public Board() {
        int i;
        for(i = 0; i < 6; ++i) {
            this.pots.add(new Storage(4));
            ((Storage)this.pots.elementAt(i)).isPlayer1 = true;
        }

        this.pots.add(new Storage(0));
        ((Storage)this.pots.elementAt(6)).isMancala = true;
        ((Storage)this.pots.elementAt(6)).isPlayer1 = true;
        this.PLAY1Score = ((Storage)this.pots.elementAt(6)).numOfStones;

        for(i = 7; i < 13; ++i) {
            this.pots.add(new Storage(4));
            ((Storage)this.pots.elementAt(i)).isPlayer2 = true;
        }

        this.pots.add(new Storage(0));
        ((Storage)this.pots.elementAt(13)).isMancala = true;
        this.PLAY2Score = ((Storage)this.pots.elementAt(13)).numOfStones;
        this.map = new HashMap();

        for(i = 0; i < 6; ++i) {
            this.map.put((Storage)this.pots.elementAt(i), (Storage)this.pots.elementAt(12 - i));
        }

        for(i = 0; i < 6; ++i) {
            this.map.put((Storage)this.pots.elementAt(12 - i), (Storage)this.pots.elementAt(i));
        }

        this.player1 = true;
        this.player2 = false;
        this.playerID = 0;
    }

    public Board copyBoard() {
        Board newBoard = new Board();
        newBoard.pots = this.pots;
        newBoard.map = this.map;
        newBoard.PLAY1Score = this.PLAY1Score;
        newBoard.PLAY2Score = this.PLAY2Score;
        newBoard.addMovesPlayer1 = this.addMovesPlayer1;
        newBoard.addMovesPlayer2 = this.addMovesPlayer2;
        newBoard.player1 = this.player1;
        newBoard.player2 = this.player2;
        newBoard.playerID = this.playerID;
        return newBoard;
    }

    public boolean isGameOver() {
        if (!this.isPlayer1SideEmpty() && !this.isPlayer2SideEmpty()) {
            return false;
        } else {
            this.gameOver();
            return true;
        }
    }

    public int getScore(int id) {
        int score = 0;
        if (id == 0) {
            score = ((Storage)this.pots.elementAt(6)).numOfStones;
        } else if (id == 1) {
            score = ((Storage)this.pots.elementAt(13)).numOfStones;
        }

        return score;
    }

    public int totStoneOnSide(int id) {
        return id == 0 ? this.getTotalStonesAtPlayer1Side() : this.getTotalStonesAtPlayer2Side();
    }

    public int additionalMovesEarned(int id) {
        return id == 0 ? this.addMovesPlayer1 : this.addMovesPlayer2;
    }

    public void gameOver() {
        int i;
        if (this.isPlayer1SideEmpty()) {
            ((Storage)this.pots.elementAt(13)).numOfStones += this.getTotalStonesAtPlayer2Side();
            this.PLAY2Score = ((Storage)this.pots.elementAt(13)).numOfStones;

            for(i = 7; i < 13; ++i) {
                ((Storage)this.pots.elementAt(i)).clearPot();
            }
        } else if (this.isPlayer2SideEmpty()) {
            ((Storage)this.pots.elementAt(6)).numOfStones += this.getTotalStonesAtPlayer1Side();
            this.PLAY1Score = ((Storage)this.pots.elementAt(6)).numOfStones;

            for(i = 0; i < 6; ++i) {
                ((Storage)this.pots.elementAt(i)).clearPot();
            }
        }

    }

    public boolean isPlayer1SideEmpty() {
        int c = 0;

        for(int i = 0; i < 6; ++i) {
            if (((Storage)this.pots.elementAt(i)).numOfStones == 0) {
                ++c;
            }
        }

        if (c == 6) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPlayer2SideEmpty() {
        int c = 0;

        for(int i = 7; i < 13; ++i) {
            if (((Storage)this.pots.elementAt(i)).numOfStones == 0) {
                ++c;
            }
        }

        if (c == 6) {
            return true;
        } else {
            return false;
        }
    }

    public int getTotalStonesAtPlayer1Side() {
        int tot = 0;

        for(int i = 0; i < 6; ++i) {
            tot += ((Storage)this.pots.elementAt(i)).numOfStones;
        }

        return tot;
    }

    public int getTotalStonesAtPlayer2Side() {
        int tot = 0;

        for(int i = 7; i < 13; ++i) {
            tot += ((Storage)this.pots.elementAt(i)).numOfStones;
        }

        return tot;
    }

    public void togglePlayer() {
        if (this.player1 && !this.player2 && this.playerID == 0) {
            this.player1 = false;
            this.player2 = true;
            this.playerID = 1;
        } else if (!this.player1 && this.player2 && this.playerID == 1) {
            this.player2 = false;
            this.player1 = true;
            this.playerID = 0;
        }

    }

    public int makeAMove(int currentStorage, int player) {
        if (player == 1) {
            currentStorage += 7;
        }

        if ((!this.player1 || player != 0 || currentStorage < 6) && this.player2 && player == 1 && currentStorage < 6) {
        }

        int hh = currentStorage + ((Storage)this.pots.elementAt(currentStorage)).numOfStones;

        int m;
        for(m = currentStorage + 1; m <= hh; ++m) {
            int k = m % 14;
            if (this.player1 && k == 13 && player == 0) {
                ((Storage)this.pots.elementAt((hh + 1) % 14)).addStones();
            } else if (this.player2 && m % 14 == 6 && player == 1) {
                ((Storage)this.pots.elementAt((hh + 1) % 14)).addStones();
            } else {
                ((Storage)this.pots.elementAt(k)).addStones();
            }
        }

        ((Storage)this.pots.elementAt(currentStorage)).clearPot();
        m = hh % 14;
        if (((Storage)this.pots.elementAt(m)).numOfStones == 1) {
            if (this.player1 && !((Storage)this.pots.elementAt(m)).isMancala && ((Storage)this.pots.elementAt(m)).isPlayer1 && player == 0) {
                ((Storage)this.pots.elementAt(6)).numOfStones += ((Storage)this.map.get(this.pots.elementAt(m))).numOfStones;
                ((Storage)this.map.get(this.pots.elementAt(m))).clearPot();
            } else if (this.player2 && !((Storage)this.pots.elementAt(m)).isMancala && ((Storage)this.pots.elementAt(m)).isPlayer2 && player == 1) {
                ((Storage)this.pots.elementAt(13)).numOfStones += ((Storage)this.map.get(this.pots.elementAt(m))).numOfStones;
                ((Storage)this.map.get(this.pots.elementAt(m))).clearPot();
            }
        }

        if (m == 6 && this.player1 && player == 0) {
            ++this.addMovesPlayer1;
            return 0;
        } else if (m == 13 && this.player2 && player == 1) {
            ++this.addMovesPlayer2;
            return 1;
        } else {
            this.togglePlayer();
            return this.playerID;
        }
    }

    void printBoard() {
        System.out.print("  ");

        int i;
        for(i = 12; i >= 7; --i) {
            System.out.print(((Storage)this.pots.elementAt(i)).numOfStones + " ");
        }

        System.out.println();
        System.out.println(((Storage)this.pots.elementAt(13)).numOfStones + "\t      " + ((Storage)this.pots.elementAt(6)).numOfStones);
        System.out.print("  ");

        for(i = 0; i <= 5; ++i) {
            System.out.print(((Storage)this.pots.elementAt(i)).numOfStones + " ");
        }

        System.out.println();
    }
}

