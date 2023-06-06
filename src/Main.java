import java.util.Scanner;

public class Main {

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
        System.out.println("ic is  "+ic);
        //==============find the row distance of blank to goal position


        int blankPos=-1;
        for (int j = 0; j < n; j++)
        {
            for (int l = 0; l < n; l++)
            {
                if(board[j][l]==0)
                {
                    blankPos=j;
                    break;
                }
            }
        }
        int rowDistanceofBlank=n-blankPos;  //with respect to goal position of blank
        //System.out.println(rowDistanceofBlank);


        //if dimension n=odd
        if(n%2!=0)
        {

            if(ic%2==0)return true;
            else
                return false;
        }

        // when dimension is even,need to check blank's row distance also
        else

        {
           if((ic+rowDistanceofBlank)%2==0) return true;
           else return false;

        }



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

     if(!check(n,board)) System.out.println("not");

    }
}
