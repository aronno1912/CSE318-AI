package man2;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



public class Storage {
    public int numOfStones;
    public boolean isMancala;
    public boolean isPlayer1;
    public boolean isPlayer2;

    public Storage(int numOfStones) {
        this.numOfStones = numOfStones;
        this.isMancala = false;
        this.isPlayer1 = false;
        this.isPlayer2 = false;
    }

    public void isStorageMancala() {
        this.isMancala = true;
    }

    public int getNumOfStones() {
        return this.numOfStones;
    }

    public void clearPot() {
        this.numOfStones = 0;
    }

    public void addStones() {
        ++this.numOfStones;
    }

    public void removeStones() {
        --this.numOfStones;
    }
}

