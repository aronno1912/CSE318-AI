package mancala2;

public class H6 implements Heuristic{
    @Override
    public int getValue(State state) {
        return -state.remainingStonesInBins(state.maxArr);
    }
}
