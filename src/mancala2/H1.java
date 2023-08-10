package mancala2;

public class H1 implements Heuristic {
    @Override
    public int getValue(State state) {
        return (state.minScore - state.maxScore);   //(stones_in_my_storage – stones_in_opponents_storage)
    }
}
