package mancala2;

public class H3 implements Heuristic {
    int addmoves;
    int w1 = 4;
    int w2 = 7;
    int w3 = 6;
    public H3(int addmoves){
        this.addmoves = addmoves;
    }
    @Override
    public int getValue(State state) {
        //W1 * (stones_in_my_storage – stones_in_opponents_storage) + W2 * (stones_on_my_side –
        //stones_on_opponents_side) + W3 * (additional_move_earned)

        int scoredif = state.minScore - state.maxScore;
        int blockcount = state.remainingStonesInBins(state.minArr) - state.remainingStonesInBins(state.maxArr);
        return (w1 * scoredif + w2 * blockcount + w3 * addmoves);
    }
}
