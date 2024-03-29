package Npuzzle;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;


 class ManhattanComp implements Comparator <Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return Integer.compare(o1.cost + o1.manhattanCost(), o2.cost + o2.manhattanCost());
    }


}

class HammingComp implements Comparator <Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return Integer.compare(o1.cost + o1.hammingCost(), o2.cost + o2.hammingCost());
    }
}
public class Main {

     private static int totalMoves;

     /*************  to check if the puzzle is unsolvable       *************/

    public static boolean check(int n,int[][]board)
    {
        int ic=0; //inversion count
        int arr[]=new int[n*n];
        int p=0;

        // converting 2d array to 1d array for inversion count
        for (int i = 0; i < n; i++) {
            for (int j = 0; j< n; j++) {
                arr[p++]=board[i][j];
            }
        }

        //============Inversion count==================

        for (int i = 0; i < n*n; i++)
        {
            if(arr[i]==0) continue;
            for (int j = i; j < n*n; j++)
            {
                if(arr[j]==0) continue;
                if(arr[j]<arr[i])
                    ic++;
            }
        }
       // System.out.println("ic is  "+ic);



        //==============find the row distance of blank to goal position

        int blankPos=-1;
        for (int j = 0; j < n; j++)
        {
            for (int l = 0; l < n; l++)
            {
                if(board[j][l]==0)
                {

                    blankPos=j;
                   // System.out.println("blank pos is "+blankPos);
                    break;
                }
            }
        }
        int rowDistanceofBlank=n-1-blankPos;  //with respect to goal position of blank
        //System.out.println(rowDistanceofBlank);


        /****************   if dimension n=odd  *************/
        /**      ic need to be even       **/


        if(n%2!=0)
        {

            if(ic%2==0)return true;
            else
                return false;
        }

        /****************   if dimension n=even  *************/

        /**        ic+row distance of blank from the goal blank position must be even                     **/

        // when dimension is even,need to check blank's row distance also
        else

        {
           // System.out.println("dfgh "+(ic+rowDistanceofBlank));
           if((ic+rowDistanceofBlank)%2==0) return true;
           else return false;

        }



    }

    private static void printSteps(Node currNode) {

        if (currNode == null) return;

        printSteps(currNode.parent);
        //System.out.println(currNode);
        for (int i = 0; i < currNode.n; i++) {
            for (int j = 0; j< currNode.n; j++)
            {
                System.out.print(" "+currNode.board[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }


    /****************  when the puzzle is solvable, solve it  *************/

    private static void doCalculation(int n,int[][]board,int choice)
    {


        Node root= new Node(n, board, 0, null);
        Node currNode=root;
        int expandedNodes=0; // those who have exited from the queue (that means their children are now pushed in the queue)
        int exploredNodes=1; // nodes those have entered in the queue at any time, considering the root node is in the queue already
        PriorityQueue<Node>queue;
        ManhattanComp mc=new ManhattanComp();
        HammingComp hc= new HammingComp();
        if (choice==0)
        {
            queue=new PriorityQueue <>(mc);  // queue is based on manhattan cost... jar cost kom shey age exit hobe
            System.out.println();

        }
        else
        queue=new PriorityQueue <>(hc);
        HashSet<Node> alreadyDoneNodes= new HashSet <>(); // closed list
        queue.add(root);
        while (!queue.isEmpty())
        {
            currNode=queue.poll();
            //currNode.determineBlankPosition();
            //System.out.println("the b position is "+currNode.blankRow+","+currNode.blankCol);

            //System.out.println("here  ");
            if(currNode.achievementUnlocked())
            {
                System.out.println("Puzzled solved!!!!");
                System.out.println("Total Moves= "+currNode.cost);
                totalMoves=currNode.cost;
                break;
            }

            alreadyDoneNodes.add(currNode);
            expandedNodes++;

            //check if children are already expanded or not...if not add them to the queue
            for (Node Childnode : currNode.getChildrenNodes())

            {
                // System.out.println(node.board);


                if(!alreadyDoneNodes.contains(Childnode))
                {
                    exploredNodes++;
                    queue.add(Childnode);
                }
            }
        }
        if (choice==0)
            System.out.println("Process Manhattan: ");
        else
            System.out.println("Process Hamming: ");

        System.out.println();
        printSteps(currNode);
        //System.out.println("Total Moves= "+currNode.cost);
       // System.out.println("Total Moves= "+totalMoves);
        if (choice==0)
            System.out.println("In Manhattan process");
        else
            System.out.println("In Hamming process");
            System.out.println("explored nodes "+exploredNodes);
        System.out.println("expanded nodes "+expandedNodes);


    }


        public static void main(String[] args) {

        Scanner scanner=new Scanner(System.in);
        int n= scanner.nextInt();
        int [][]board= new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j< n; j++)
            {
                int inp=scanner.nextInt();
                board[i][j]=inp;
            }
        }

//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j< n; j++)
//            {
//
//                System.out.println( board[i][j]);
//            }
//        }

     if(!check(n,board)) System.out.println("Unsolvable Case");
     else
     {



     doCalculation(n,board,0); //eta dile manhattan diye korbe
     doCalculation(n,board,1); //eta dile hamming diye


     }

    }
}
