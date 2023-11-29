//i guess the 3rd heuristic(select option 4 is the best so far) for depth 8
#include <bits/stdc++.h>
using namespace std;
#define INF 999999
#define DEPTH 8
// jar jar dik theke tar leftmost ta 1
int W1, W2, W3, W4;

/****  Rules
 *
 * Get a free turn: When dropping stones into holes, you can drop one into your own mancala store (bowl). If it's your last stone in your hand,
 *  you get another turn.
 *
Gather more stones: One way to amass more stones is by strategically dropping your last stone into an empty hole on your side of the board.
If you manage that, you're allowed to reach across the board and take your opponent's stones that are in the opposite hole.
 *
 *
*/

struct BoardState
{
  // player 1 is the top player. his 1st bin is top right small bin, storage is the left one i.e bins[6]
  // player 2 is the bottom player. his 1st bin is bottom left small bin, storage is the right one i.e bins[13]
  int bins[14];
  int presentTurn; // 1 means player1's turn. 2 means player2's turn
  int earnedAdditionalMoves;
  int acquiredStones; // by depositiong last stone on my own side,i can capture opponent's stones of the opposite bin
  int heuristicValue;
  int stonesInOwnMancala1, stonesInOwnMancala2; // if player1 his own mancala is mancala1,if player2 then mancala2
  int stcap;
  int chosenBin;

  BoardState()
  {
    presentTurn = 1;
    earnedAdditionalMoves = 0;
    stcap = 0;
    acquiredStones = 0;
    heuristicValue = 0;
    bins[6] = bins[13] = 0;
    stonesInOwnMancala1 = bins[6], stonesInOwnMancala2 = bins[13];
    chosenBin=0;
    // initializing
    for (int i = 0; i <= 5; i++)
    {

      bins[i] = 4;     // player 1's side
      bins[i + 7] = 4; // player 2's side
    }
  }

  // if one side's all bins are empty
  bool checkZero()
  {
    int sum = 0;
    for (int i = 0; i <= 5; i++)
    {
      sum += bins[i];
    }
    if (sum == 0)
      return true;

    sum = 0;
    for (int i = 7; i <= 12; i++)
    {
      sum += bins[i];
    }
    if (sum == 0)
      return true;
    return false;
  }

  void showGameState()
  {

    // player 1 upper
    // player 2 lower
    cout << "================Player 1==================" << endl;
    printf("\t");
    for (int i = 5; i >= 0; i--)
      {
        printf("%4d", bins[i]);
      }
    printf("\t\n");
    printf("(%4d)", bins[6]); // player 1's mancala
    for (int i = 4; i >= 0; i--)
     {
       printf("\t");
     }
    printf("(%4d)\n", bins[13]); // player 2's mancala
    printf("\t");
    for (int i = 7; i <= 12; i++)
      {
        printf("%4d", bins[i]);
      }
    printf("\t\n");
    cout << "================Player 2==================" << endl;

    cout << "Next turn : Player " << presentTurn << endl;
  }

  int getHeuristicValue(int heuType)
  {
    stonesInOwnMancala1 = bins[6], stonesInOwnMancala2 = bins[13];

    if (heuType == 1)

    {
      return stonesInOwnMancala1 - stonesInOwnMancala2;
    }

    int remainingStonesOnSide1 = 0, remainingStonesOnSide2 = 0;
    // calculate each side's leftover stones on 6 bins
    for (int i = 0; i < 6; i++)
      remainingStonesOnSide1 += bins[i];
    for (int i = 0; i < 6; i++)
      remainingStonesOnSide2 += bins[i + 7];

    if (heuType == 2)
    {
      // W1 = 16, W2 = 1;
      W1 = 20, W2 = 30;
      int val = W1 * (stonesInOwnMancala1 - stonesInOwnMancala2) + W2 * (remainingStonesOnSide1 - remainingStonesOnSide2);
      return val;
    }

    if (heuType == 3)
    {
      // W1 = 16, W2 = 1, W3 = 4, W4 = 0;
      W1 = 30, W2 = 20, W3 = 90;
      return W1 * (stonesInOwnMancala1 - stonesInOwnMancala2) + W2 * (remainingStonesOnSide1 - remainingStonesOnSide2) + W3 * earnedAdditionalMoves;
    }

    if (heuType == 4)
    {
      
      W1 = 40, W2 = 30, W3 = 20, W4 = 90;
   
      // cout<<endl<<earnedAdditionalMoves<<endl;
      // cout<<endl<<acquiredStones<<endl;
      return W1 * (stonesInOwnMancala1 - stonesInOwnMancala2) + W2 * (remainingStonesOnSide1 - remainingStonesOnSide2) + W3 * earnedAdditionalMoves + W4 * acquiredStones;
    }
  }

} typedef board;

// NEED FOR SORTING
struct comparator
{
  inline bool operator()(const board &b1, const board &b2)
  {
    return (b1.heuristicValue < b2.heuristicValue);
  }
};

// human decides and based on that a state is returned
board inActionHuman(board current)
{
  int side = current.presentTurn;

  cout << "Choose your bin : ";
  int choice;
  cin >> choice;
  while(choice<1 || choice> 6)
  {
    cout<<endl<<"Incorrect Bin number !!!"<<endl;
     cout<<"Please choose again between 1 to 6 ..."<<endl;
    cin>>choice;
  }
  int currPos = (side - 1) * 7 + choice; // so if i'm player 2 and make choice 4, now_at is (2-1)*7+4 = 11, just where i should start dropping bodies
  int stonesInSelectedBin = current.bins[currPos - 1];
  current.bins[currPos - 1] = 0;
  while (stonesInSelectedBin)
  {
    if ((side == 1 && currPos == 13) || (side == 2 && currPos == 6))

    {
      // opponent mancala - so do nothing
    }
    else
    {
      current.bins[currPos]++;
      stonesInSelectedBin--;
      if (stonesInSelectedBin == 0)
      {
        // When dropping stones into holes, you can drop one into your own mancala store (bowl).
        // If it's your last stone in your hand, you get another turn.

        if (currPos != ((side - 1) * 7 + 6)) // not my mancala, so no free turn
        {
          current.presentTurn = 3 - side; // opponent's turn
        }
        else
        {
          current.earnedAdditionalMoves++;
        }
        // check if last slot is of own and was empty
        int bin1st = (side - 1) * 7; // own first bin
        int binLast = bin1st + 5;    // own last bin

        if (current.bins[currPos] == 1 && currPos >= bin1st && currPos <= binLast && current.bins[12 - currPos] != 0) // and opponent er extact ulta dik er bin a stone ache
        {
          current.bins[(side - 1) * 7 + 6] += (current.bins[currPos] + current.bins[12 - currPos]); // collect stones of opponnent on the opposite bin and put them in own mancala
          current.acquiredStones += current.bins[12 - currPos];
          current.bins[currPos] = 0;
          current.bins[12 - currPos] = 0; // opponent side er
        }
      }
    }
    currPos = (currPos + 1) % 14;
  }
  return current;
}

vector<board> getChildNodes(board b, int heuType)
{
  // returns an sorted array of children
  int start, myStorage, opponentStorage;

  vector<board> children;
  // player 1 is max player
  if (b.presentTurn == 1)
  { // player 1
    start = 0;
    myStorage = 6;
    opponentStorage = 13;
  }
  else
  { // player 2
    start = 7;
    myStorage = 13;
    opponentStorage = 6; // coz count always 0 to 6 korbo tai
  }

  // start calculating for each bin choice =============================
  for (int i = 0; i < 6; i++)
  {
    int whichBin = start + i;
    int stonesInThatBin = b.bins[whichBin];
    if (stonesInThatBin < 0)
    {
    }

    if (stonesInThatBin > 0)

    {
      board child = b; // valid choice
      child.bins[whichBin] = 0;
      child.chosenBin=i+1;
      child.stcap = 0;
      child.acquiredStones = 0;
      child.earnedAdditionalMoves = 0;
      for (int j = 1;; j++) // as long as stones in that bin are not distributed
      {
        int latestDropBin = (whichBin + j) % 14;
        if (latestDropBin != opponentStorage) // opponent er mancala te kono stone drop hobe na
        {
          stonesInThatBin--;
          child.bins[latestDropBin]++;
        }

        // the last drop
        if (stonesInThatBin == 0)
        {
          // If the last piece you drop is in your own mancala, you get a free turn.
          if (latestDropBin == myStorage)
          {
            child.presentTurn = b.presentTurn;
            child.earnedAdditionalMoves += 1; 
          }

          else
            child.presentTurn = 3 - b.presentTurn; // opponent will get turn now
          // If the last piece you drop is in an empty hole on your side, you capture that piece and any pieces in the hole
          //  directly opposite.
          if (latestDropBin >= start && latestDropBin < myStorage && child.bins[latestDropBin] == 1 && child.bins[12 - latestDropBin] > 0)
          {
            int parallelBinOnopponentSide = 12 - latestDropBin;
            child.stcap += (child.bins[latestDropBin] + child.bins[parallelBinOnopponentSide]);
            child.acquiredStones += child.bins[parallelBinOnopponentSide]; ////////////////////////////////////////////////////
            child.bins[myStorage] += (child.bins[latestDropBin] + child.bins[parallelBinOnopponentSide]);
            child.bins[latestDropBin] = 0;
            child.bins[parallelBinOnopponentSide] = 0;
          }
          break;
        }
      }
      // for min node, just put a minus
      if (b.presentTurn == 2)
      {
        child.stcap = (-1) * child.stcap;
        child.acquiredStones = (-1) * child.acquiredStones;
        child.earnedAdditionalMoves = (-1) * child.earnedAdditionalMoves;
      }
      if (child.checkZero())
      {
        // game over - need to capture all pieces to mancala
        int stones = 0;
        for (int i = 0; i <= 5; i++)
        {
          stones += child.bins[i];
          child.bins[i] = 0;
        }
        child.bins[6] += stones;

        child.stcap += stones; // good for player 1, max node, so add
        child.acquiredStones+=stones; 

        stones = 0;
        for (int i = 7; i <= 12; i++)
        {
          stones += child.bins[i];
          child.bins[i] = 0;
        }
        child.bins[13] += stones;

        child.stcap -= stones;          // good for player 2, min node, so neg
        child.acquiredStones -= stones; 
      }

      child.heuristicValue = child.getHeuristicValue(heuType);
      children.push_back(child);
    }
  }
   //just some optimization for minimax......
  sort(children.begin(), children.end(), comparator());
  if (b.presentTurn == 1)
  {
    // bring best child first for max node
    reverse(children.begin(), children.end());
  }

  return children;
}

//basically a dfs with a return
// need to check two case..if i am min or if i am max
int miniMax(board currState, int depth, int alpha, int beta, int heuType)
{

  if (currState.checkZero() || depth == 0) 
  {
      //cout<<endl<<currState.earnedAdditionalMoves<<endl;
      //cout<<endl<<currState.acquiredStones<<endl;
    return currState.getHeuristicValue(heuType);
  }
  vector<board> childNodes = getChildNodes(currState, heuType);
  int countChild = childNodes.size();

  // if he is a max node
  if (currState.presentTurn == 1)
  {
    int val = alpha;
    for (int i = 0; i < countChild; i++)
    {
      int v1 = miniMax(childNodes[i], depth - 1, val, beta, heuType);
      if (v1 > val)
        val = v1;
      if (val >= beta)
        // return beta;  
        return val; //alpha>=beta hoye gele ar explore korar dorkar nai  (alpha-beta prunning)
    }

    return val;
  }
  // if he is a min node

  if (currState.presentTurn == 2)
  {
    int val = beta;
    for (int i = 0; i < countChild; i++)
    {
      int v1 = miniMax(childNodes[i], depth - 1, alpha, val, heuType);
      if (v1 < val)
        val = v1;
      if (val <= alpha)
        // return alpha;  
        return val;  // //alpha>=beta hoye gele ar explore korar dorkar nai    (alpha-beta prunning)
    }

    return val;
  }
}

int main()
{

  int player1, player2;
  cout << "Welcome to Mancala Game.............!!!!!!!! " << endl;
  cout << "Select Players : " << endl;
  cout << "1. Human" << endl;
  cout << "2. AI with heuristic1" << endl;
  cout << "3. AI with heuristic2" << endl;
  cout << "4. AI with heuristic3" << endl;
  cout << "5. AI with heuristic4" << endl;
  cout << "\nPlayer-1 : ";
  cin >> player1;
  while (player1 < 1 || player1 > 5)
  {
    cout << "Invalid choice....Please choose between 1 to 5" << endl;
    cin >> player1;
  }
  player1--; // to get match with heuristic type
  cout << "\nPlayer-2 : ";
  cin >> player2;
  while (player2 < 1 || player2 > 5)
  {
    cout << "Invalid choice....Please choose between 1 to 5" << endl;
    cin >> player2;
  }
  player2--;

  board state;
  while (true)
  {
    state.showGameState();

    //. The game ends when one player has emptied the six holes of stones on
    // their side. The other player then takes all the stones on their own side and places them into their store (or bowl)

    if (state.checkZero())
    {
      cout << "\n---GAME OVER---\n";

      int score1 = 0, score2 = 0;

      for (int i = 0; i <= 6; i++)
      {
        score1 += state.bins[i];
      }

      for (int i = 7; i <= 13; i++)
      {
        score2 += state.bins[i];
      }

      int difference = score1 - score2;
      if (difference > 0)
      {
        cout << "Player 1 wins!!!" << endl;
        cout << "=======Final score======" << endl;
        cout << "Player1: " << score1 << endl;
        cout << "Player2: " << score2 << endl;
      }
      else if (difference < 0)
      {
        cout << "Player 2 wins!!!" << endl;
        cout << "=======Final score======" << endl;
        cout << "Player1: " << score1 << endl;
        cout << "Player2: " << score2 << endl;
      }
      else
      {
        cout << "Match Draw!" << endl;
        cout << "=======Final score======" << endl;
        cout << "Player1: " << score1 << endl;
        cout << "Player2: " << score2 << endl;
      }
      break;
    }

    int playingPlayer;
    if (state.presentTurn == 1)
      playingPlayer = player1;
    else
      playingPlayer = player2;

    // human is playing
    if (playingPlayer == 0)
    {
      state = inActionHuman(state);
    }

    // ai is playing
    else
    {

      cout << "AI in action ... " << endl;

      vector<board> children = getChildNodes(state, playingPlayer);
      int sz = children.size();
      board nextState;
      if (state.presentTurn == 1)
      {
        // player1 is max player
        int bestValue = -INF;
        for (int i = 0; i < sz; i++)
        {
          int gotcha = miniMax(children[i], DEPTH, -INF, +INF, playingPlayer); // here playing player is the heuType also
          if (gotcha > bestValue)
          {
            bestValue = gotcha;
            nextState = children[i];
          }
        }
      }
      else if (state.presentTurn == 2)
      {
        // min node
        int bestValue = INF;
        for (int i = 0; i < sz; i++)
        {
          int gotcha = miniMax(children[i], DEPTH, -INF, +INF, playingPlayer); // here playing player is the heuType also
          if (gotcha < bestValue)
          {
            bestValue = gotcha;
            nextState = children[i];
          }
        }
      }
      state = nextState;
      cout<<"Chosen bin by AI is "<<state.chosenBin<<endl;
      //  cout<<"moves "<<endl<<state.earnedAdditionalMoves<<endl;
      // cout<<"captured stones "<<endl<<state.acquiredStones<<endl;
    }
  }
  return 0;
}
