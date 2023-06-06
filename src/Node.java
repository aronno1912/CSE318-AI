import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;

public class Node {
     int n;
     int[][] board;
    int[][] goalBoard;

    int cost; // g(n)  ....cost to reach this current node
    Node parent;
    int blankRow;
    int blankCol;

    public Node(int n, int[][] board, int cost, Node parent) {
        this.n = n;
        this.board = board;
        this.goalBoard = new int[n][n];
        this.cost = cost;
        this.parent = parent;
       int a=1;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                goalBoard[i][j]=a++;
            }
        }

        goalBoard[n-1][n-1]=0;
    }

    int manhattanCost()
    {
        int manCost=0;

        // calculate  abs(x2-x1) + abs(y2-y1)  where (x2,y2) is the coordinate of goalBoard
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (board[i][j]!=0)
                    manCost+=abs(((board[i][j]-1)/n)-i)+abs(((board[i][j]-1)%n)-j);
            }
        }
        return manCost;
    }

    int hammingCost()
    {
        int hamCost=0;

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if ((board[i][j]!=0) && (board[i][j]!=goalBoard[i][j])) hamCost++;
            }
        }
        return hamCost;

    }


    void copyBoard(int[][] anotherB){
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                anotherB[i][j]=this.board[i][j];
            }
        }
    }

    void determineBlankPosition()
    {

        for (int j = 0; j < n; j++)
        {
            for (int l = 0; l < n; l++)
            {
                if(board[j][l]==0)
                {
                    blankRow=j;
                    blankCol=l;
                    break;
                }
            }
        }
    }

    //===========check if present board is the goal board==========================//
    boolean achievementUnlocked()
    {   boolean b=true;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (board[i][j]!=goalBoard[i][j]){b=false;break;}
            }
        }
        return b;
    }

    void swapPos(int r1, int c1, int r2, int c2)
    {
        int x = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = x;
    }

    ArrayList <Node> getChildrenNodes()
    {
      ArrayList<Node>children=new ArrayList <>();
        determineBlankPosition();

      /**===========MOVES===============**/

      // move blanks Down
        //System.out.println("Blank row  and col is "+blankRow+" "+blankCol);
        if(blankRow!=n-1)
        {
            int[][] anotherB= new int[n][n];
            copyBoard(anotherB);
            Node childNode= new Node(n, anotherB, this.cost+1,this);
            //move blank one row down
            childNode.swapPos(blankRow, blankCol, blankRow+1, blankCol);
            children.add(childNode);
        }

        // move blanks Up

        if(blankRow!=0)
        {
            int[][] anotherB= new int[n][n];
            copyBoard(anotherB);
            Node childNode= new Node(n, anotherB, this.cost+1,this);
            //move blank one row up
            childNode.swapPos(blankRow, blankCol, blankRow-1, blankCol);
            children.add(childNode);
        }


        // move blanks Right

        if(blankCol!=n-1)
        {
            int[][] anotherB= new int[n][n];
            copyBoard(anotherB);
            Node childNode= new Node(n, anotherB, this.cost+1,this);
            //move blank one column right
            childNode.swapPos(blankRow, blankCol, blankRow, blankCol+1);
            children.add(childNode);
        }

        // move blanks Left

        if(blankCol!=0)
        {
            int[][] anotherB= new int[n][n];
            copyBoard(anotherB);
            Node childNode= new Node(n, anotherB, this.cost+1,this);
            //move blank one column right
            childNode.swapPos(blankRow, blankCol, blankRow, blankCol-1);
            children.add(childNode);
        }

//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j< n; j++)
//            {
//
//                System.out.println( children.get(0).board[i][j]);
//            }
//        }

      return children;
    }
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Node that = (Node) o;
//        return Arrays.equals(board, that.board);
//    }
//
//    @Override
//    public int hashCode() {
//        return Arrays.deepHashCode(board);
//    }


}
