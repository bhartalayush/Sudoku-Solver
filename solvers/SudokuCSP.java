import java.util.Random;

public class SudokuCSP {

    int board[][] = new int[9][9];
    Random rand = new Random();

    boolean isSafe(int row, int col, int num) {

        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num)
                return false;

            if (board[i][col] == num)
                return false;
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {

                if (board[i][j] == num)
                    return false;
            }
        }

        return true;
    }

    boolean fillBoard(int row, int col) {

        if (row == 9)
            return true;

        int nextRow = row;
        int nextCol = col + 1;

        if (nextCol == 9) {
            nextRow = row + 1;
            nextCol = 0;
        }

        int nums[] = {1,2,3,4,5,6,7,8,9};

        for (int i = 0; i < 9; i++) {

            int j = rand.nextInt(9);
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }

        for (int i = 0; i < 9; i++) {

            int num = nums[i];

            if (isSafe(row, col, num)) {

                board[row][col] = num;

                if (fillBoard(nextRow, nextCol))
                    return true;

                board[row][col] = 0;
            }
        }

        return false;
    }

    void removeNumbers() {

        int count = 40;

        while (count > 0) {

            int row = rand.nextInt(9);
            int col = rand.nextInt(9);

            if (board[row][col] != 0) {

                board[row][col] = 0;
                count--;
            }
        }
    }

    void printBoard() {

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {

                System.out.print(board[i][j] + " ");
            }

            System.out.println();
        }
    }

    public static void main(String args[]) {

        SudokuCSP s = new SudokuCSP();

        s.fillBoard(0,0);
        s.removeNumbers();

        System.out.println("Sudoku Puzzle:");
        s.printBoard();
    }
}