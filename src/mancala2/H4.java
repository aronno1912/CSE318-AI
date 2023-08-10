package mancala2;

public class H4 implements Heuristic {
    int addmoves;
    int CapturedStone;
    int w1 = 4;
    int w2 = 7;
    int w3 = 6;
    int w4 = 9;
    public H4(int addmoves,int CapturedStone){
        this.addmoves = addmoves;
        this.CapturedStone = CapturedStone;
    }
    @Override
    public int getValue(State state) {

        /**
         *   W1 * (stones_in_my_storage – stones_in_opponents_storage) + W2 * (stones_on_my_side –
         * stones_on_opponents_side) + W3 * (additional_move_earned) + W4 * (stones_captured)
         * **/
        int scoredif = state.minScore - state.maxScore;
        int blockcount = state.remainingStonesInBins(state.minArr) - state.remainingStonesInBins(state.maxArr);
        return (w1 * scoredif + w2 * blockcount + w3 * addmoves + w4 * CapturedStone);
    }
}
