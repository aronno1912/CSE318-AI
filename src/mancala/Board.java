package mancala;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Board {

    private static final int TOTAL_BINS = 6;
    private static final int DEFAULT_STONES = 4;
    private static final String OUT_FILE = "output.txt";

    private List<Integer> lowerBin;
    private List<Integer> upperBin;
    private int upperBinStorage;
    private int lowerBinStorage;
    private File outfile;

    public Board() {
        this.lowerBin = new ArrayList<>();
        this.upperBin = new ArrayList<>();
        this.upperBinStorage = 0;
        this.lowerBinStorage = 0;
        this.outfile = new File(OUT_FILE);
        initBoard();
    }

    public Board(Board b) {
        this.lowerBin = new ArrayList<>();
        this.upperBin = new ArrayList<>();
        for (int i = 0; i < b.getUpperBin().size(); i++) {
            this.upperBin.add(b.getUpperBin().get(i));
            this.lowerBin.add(b.getLowerBin().get(i));
        }
        this.upperBinStorage = b.getUpperBinStorage();
        this.lowerBinStorage = b.getLowerBinStorage();
    }

    private void initBoard(){
        for (int i=0; i< TOTAL_BINS ; i++){
            lowerBin.add(4);
            upperBin.add(4);
        }
    }

    public void printBoard(){
        System.out.println("------------------------------\n");
        for (int i = 0; i < TOTAL_BINS; i++) {
            System.out.print("( "+ upperBin.get(i)+" ) ");
        }
        System.out.println("\n------------------------------\n");

        for (int i = 0; i < TOTAL_BINS; i++) {
            System.out.print("( "+ lowerBin.get(i)+" ) ");
        }
        System.out.println("\n------------------------------\nUpper Bin : "+ upperBinStorage +"  Lower Bin : "+ lowerBinStorage);


    }

    public void printtoFile() throws IOException {
        FileWriter fileWriter = new FileWriter(outfile);
        StringBuilder fileContent = new StringBuilder();

        System.out.println("---------------------------\n");
        fileContent.append("---------------------------\n");
        for (int i = 0; i < TOTAL_BINS; i++) {
            System.out.print(lowerBin.get(i)+" - ");
            fileContent.append(lowerBin.get(i)).append(" - ");
        }
        System.out.println("\n---------------------------\n");
        fileContent.append("\n---------------------------\n");
        for (int i = 0; i < TOTAL_BINS; i++) {
            System.out.print(upperBin.get(i)+" - ");
            fileContent.append(upperBin.get(i)).append(" - ");
        }
        System.out.println("\n---------------------------\nStorage : "+ upperBinStorage +"  Opponent Store : "+ lowerBinStorage);
        fileContent.append("\n---------------------------\nStorage : ").append(upperBinStorage).append("  Opponent Store : ").append(lowerBinStorage);
        fileWriter.write(fileContent.toString());
        //fileWriter.close();

    }

    public static int getTotalBins() {
        return TOTAL_BINS;
    }

    public static int getDefaultStones() {
        return DEFAULT_STONES;
    }

    public List<Integer> getLowerBin() {
        return lowerBin;
    }

    public void setLowerBin(List<Integer> lowerBin) {
        this.lowerBin = lowerBin;
    }

    public List<Integer> getUpperBin() {
        return upperBin;
    }

    public void setUpperBin(List<Integer> upperBin) {
        this.upperBin = upperBin;
    }

    public int getUpperBinStorage() {
        return upperBinStorage;
    }

    public void setUpperBinStorage(int upperBinStorage) {
        this.upperBinStorage = upperBinStorage;
    }

    public int getLowerBinStorage() {
        return lowerBinStorage;
    }

    public void setLowerBinStorage(int lowerBinStorage) {
        this.lowerBinStorage = lowerBinStorage;
    }

    boolean gameOver(){
        boolean over_upper = true, over_lower = true;
        for (int i = 0; i < TOTAL_BINS; i++) {
            if(upperBin.get(i) != 0 )
                over_upper = false;
        }

        for (int i = 0; i < TOTAL_BINS; i++) {
            if(lowerBin.get(i) != 0 )
                over_lower = false;
        }
        if(over_lower && over_upper){
            if(over_upper)
                collectLowerStones();
            else
                collectUpperStones();

            return true;
        }
        else {
            return false;
        }
    }

    private void collectLowerStones() {
        for (int i = 0; i < TOTAL_BINS; i++) {
            lowerBinStorage += lowerBin.get(i);
        }

    }

    private void collectUpperStones() {
        for (int i = 0; i < TOTAL_BINS; i++) {
            upperBinStorage += upperBin.get(i);
        }
    }

    public int getLowerBinStones(){
        int sum = 0;
        for (int i = 0; i < TOTAL_BINS; i++) {
            sum += lowerBin.get(i);
        }
        return sum;
    }

    public int getUpperBinStones(){
        int sum = 0;
        for (int i = 0; i < TOTAL_BINS; i++) {
            sum += upperBin.get(i);
        }
        return sum;
    }

    public boolean generateMove(int pos, boolean isOpponent){
        boolean freeturn = false;
        int myStorage,opponentStorage;
        List<Integer> playerBin,opponentBin;
        if(isOpponent){
            myStorage = getUpperBinStorage();
            opponentStorage = getLowerBinStorage();
            playerBin = this.getUpperBin();
            opponentBin = this.getLowerBin();
        }
        else{
            myStorage = this.getLowerBinStorage();
            opponentStorage = this.getUpperBinStorage();
            playerBin = this.getLowerBin();
            opponentBin = this.getUpperBin();
        }
        if(playerBin.get(pos) == 0){
            System.out.println("DEBUG : Gen Invalid move");
            return false;
        }

        int stones = playerBin.get(pos);
        playerBin.set(pos,0);
        int newPos = pos;
        List<Integer> curr_Bin = playerBin;
        for (int i = 0; i < stones; i++) {

            if(curr_Bin.equals(playerBin))
            {
                if(isOpponent)
                {
                    newPos--;
                    if(newPos == -1){
                        newPos = 0;myStorage++;i++;
                        curr_Bin = opponentBin;
                    }
                }
                else
                {
                    newPos++;
                    if(newPos == 6)
                    {
                        newPos = 5;myStorage++;i++;
                        curr_Bin = opponentBin;
                    }
                }
                if(curr_Bin.get(newPos) == 0 && i == stones -1){
                    //capture all stones from opponent
                    //System.out.println("Captured stone ---------");
                    myStorage += opponentBin.get(newPos) + 1;
                    opponentBin.set(newPos , 0);
                    continue;
                }

            }
            else
            {
                if(isOpponent)
                {
                    newPos++;
                    if(newPos == 6)
                    {
                        newPos = 5;//skiping opponent storage
                        curr_Bin = playerBin;
                    }
                }
                else
                {
                    newPos--;
                    if(newPos == -1){
                        newPos = 0;//skipping opponent storage
                        curr_Bin = playerBin;
                    }
                }
            }

            if(i==stones)
            {
                freeturn = true;
                //System.out.println("Free Turn ------------------------");
                //makemove(new Random().nextInt(5));
                return freeturn;
                //break;
            }

            int val = curr_Bin.get(newPos);
            curr_Bin.set(newPos,val+1);

        }
        if(isOpponent){
            this.setLowerBinStorage(myStorage);
            this.setUpperBinStorage(opponentStorage);
        }
        else{
            this.setUpperBinStorage(myStorage);
            this.setLowerBinStorage(opponentStorage);
        }
        //myboard.printBoard();
        return false;
    }

    public boolean generateNextMove(int pos , boolean isOpponent){
        boolean freeturn = false;
        int myStorage,opponentStorage;
        List<Integer> playerBin,opponentBin;
        if(isOpponent){
            myStorage = getUpperBinStorage();
            opponentStorage = getLowerBinStorage();
            playerBin = this.getUpperBin();
            opponentBin = this.getLowerBin();
        }
        else{
            myStorage = this.getLowerBinStorage();
            opponentStorage = this.getUpperBinStorage();
            playerBin = this.getLowerBin();
            opponentBin = this.getUpperBin();
        }
        if(playerBin.get(pos) == 0){
            //System.out.println("DEBUG : Gen Invalid move");
            return false;
        }

        int stones = playerBin.get(pos);
        playerBin.set(pos,0);
        int newPos = pos;
        List<Integer> curr_Bin = playerBin;
        for (int i = 0; i < stones; i++) {

            if(curr_Bin.equals(playerBin))
            {
                //System.out.println("DEBUG: playrBin "+i);
                if(isOpponent)
                {
                    newPos--;
                    if(newPos == -1){
                        newPos = 0;myStorage++;i++;
                        //i++;opponentStorage++;
                        curr_Bin = opponentBin;
                    }
                }
                else
                {
                    newPos++;
                    if(newPos == 6)
                    {
                        newPos = 5;myStorage++;i++;
                        curr_Bin = opponentBin;
                    }
                }
                if(curr_Bin.get(newPos) == 0 && i == stones -1){
                    //capture all stones from opponent
                    System.out.println("Captured stone ---------");
                    //captured_stones += opponentBin.get(newPos) + 1;
                    myStorage += opponentBin.get(newPos) + 1;
                    opponentBin.set(newPos , 0);
                    //myboard.printBoard();
                    continue;
                }

            }
            else
            {
                //System.out.println("DEBUG: opponentBin "+i);
                if(isOpponent)
                {
                    newPos++;
                    if(newPos == 6)
                    {
                        newPos = 5;//skiping opponent storage
                        curr_Bin = playerBin;
                        //opponentStorage++;i++;
                    }
                }
                else
                {
                    newPos--;
                    if(newPos == -1){
                        newPos = 0;//skipping opponent storage
                        curr_Bin = playerBin;
                        //opponentStorage++;i++;
                    }
                }
            }

            if(i==stones)
            {
                //freeturn++;
                System.out.println("Free Turn ------------------------");
                if(isOpponent){
                    this.setUpperBinStorage(myStorage);
                    this.setLowerBinStorage(opponentStorage);
                }
                else{
                    this.setLowerBinStorage(myStorage);
                    this.setUpperBinStorage(opponentStorage);
                }
                //myboard.printBoard();
                return true;
                //break;
            }

            int val = curr_Bin.get(newPos);
            curr_Bin.set(newPos,val+1);

        }
        if(isOpponent){
            this.setUpperBinStorage(myStorage);
            this.setLowerBinStorage(opponentStorage);
        }
        else{
            this.setLowerBinStorage(myStorage);
            this.setUpperBinStorage(opponentStorage);
        }
        //myboard.printBoard();

        return false;
    }
}
