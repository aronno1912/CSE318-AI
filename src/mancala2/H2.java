package mancala2;

public class H2 implements Heuristic {
    int w1 = 1;
    int w2 = 1;
    @Override
    public int getValue(State state) {
        //W1 * (stones_in_my_storage – stones_in_opponents_storage) + W2 * (stones_on_my_side –
        //stones_on_opponents_side)
        int scoredif = state.minScore - state.maxScore;
        int blockcount = state.remainingStonesInBins(state.minArr) - state.remainingStonesInBins(state.maxArr);
        return (w1 * scoredif + w2 * blockcount);
    }
}
