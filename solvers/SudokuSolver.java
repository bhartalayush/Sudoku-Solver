public class SudokuBruteForce {

    static int board[][] = {
        {5,3,0,0,7,0,0,0,0},
        {6,0,0,1,9,5,0,0,0},
        {0,9,8,0,0,0,0,6,0},
        {8,0,0,0,6,0,0,0,3},
        {4,0,0,8,0,3,0,0,1},
        {7,0,0,0,2,0,0,0,6},
        {0,6,0,0,0,0,2,8,0},
        {0,0,0,4,1,9,0,0,5},
        {0,0,0,0,8,0,0,7,9}
    };

    static boolean isCompleteValid()
    {
        // check rows
        for(int i = 0; i < 9; i++)
        {
            boolean seen[] = new boolean[10];
            for(int j = 0; j < 9; j++)
            {
                int num = board[i][j];
                if(num == 0 || seen[num]) return false;
                seen[num] = true;
            }
        }

        // check columns
        for(int j = 0; j < 9; j++)
        {
            boolean seen[] = new boolean[10];
            for(int i = 0; i < 9; i++)
            {
                int num = board[i][j];
                if(num == 0 || seen[num]) return false;
                seen[num] = true;
            }
        }

        // check 3x3 boxes
        for(int br = 0; br < 3; br++)
        {
            for(int bc = 0; bc < 3; bc++)
            {
                boolean seen[] = new boolean[10];
                for(int i = 0; i < 3; i++)
                {
                    for(int j = 0; j < 3; j++)
                    {
                        int num = board[br*3 + i][bc*3 + j];
                        if(num == 0 || seen[num]) return false;
                        seen[num] = true;
                    }
                }
            }
        }

        return true;
    }

    static boolean brute(int row, int col)
    {
        if(row == 9)
        {
            return isCompleteValid(); // only check at end
        }

        int nextRow = (col == 8) ? row + 1 : row;
        int nextCol = (col + 1) % 9;

        if(board[row][col] != 0)
        {
            return brute(nextRow, nextCol);
        }

        for(int num = 1; num <= 9; num++)
        {
            board[row][col] = num;

            if(brute(nextRow, nextCol))
                return true;
        }

        board[row][col] = 0;
        return false;
    }

    static void printBoard()
    {
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
                System.out.print(board[i][j] + " ");

            System.out.println();
        }
    }

    public static void main(String[] args)
    {
        if(brute(0,0))
        {
            System.out.println("Solved:");
            printBoard();
        }
        else
        {
            System.out.println("No solution");
        }
    }
}
