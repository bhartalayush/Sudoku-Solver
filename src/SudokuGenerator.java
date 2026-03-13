import java.util.Random;

public class SudokuGenerator
{
    int board[][] = new int[9][9];
    Random r = new Random();

    boolean check(int row, int col, int num)
    {
        for(int i = 0; i < 9; i++)
        {
            if(board[row][i] == num)
            {
                return false;
            }

            if(board[i][col] == num)
            {
                return false;
            }
        }

        int sr = row - row % 3;
        int sc = col - col % 3;

        for(int i = sr; i < sr + 3; i++)
        {
            for(int j = sc; j < sc + 3; j++)
            {
                if(board[i][j] == num)
                {
                    return false;
                }
            }
        }

        return true;
    }

    boolean fill(int row, int col)
    {
        if(row == 9)
        {
            return true;
        }

        int nextRow = row;
        int nextCol = col + 1;

        if(nextCol == 9)
        {
            nextRow = row + 1;
            nextCol = 0;
        }

        int nums[] = {1,2,3,4,5,6,7,8,9};

        for(int i = 0; i < 9; i++)
        {
            int j = r.nextInt(9);
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }

        for(int i = 0; i < 9; i++)
        {
            int num = nums[i];

            if(check(row, col, num))
            {
                board[row][col] = num;

                if(fill(nextRow, nextCol))
                {
                    return true;
                }

                board[row][col] = 0;
            }
        }

        return false;
    }

    void removeNumbers()
    {
        int remove = 40;

        while(remove > 0)
        {
            int row = r.nextInt(9);
            int col = r.nextInt(9);

            if(board[row][col] != 0)
            {
                board[row][col] = 0;
                remove--;
            }
        }
    }

    void printBoard()
    {
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                System.out.print(board[i][j] + " ");
            }

            System.out.println();
        }
    }

    public static void main(String args[])
    {
        SudokuRandomizer s = new SudokuRandomizer();

        s.fill(0,0);
        s.removeNumbers();

        System.out.println("Sudoku Puzzle:");
        s.printBoard();
    }
}
