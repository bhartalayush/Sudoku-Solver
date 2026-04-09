public class SudokuBitMask {

    static int[][] board = {
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

    static int[] rowMask = new int[9];
    static int[] colMask = new int[9];
    static int[] boxMask = new int[9];

    static boolean solve(int row, int col) {

        if (row == 9)
            return true;

        int nextRow = (col == 8) ? row + 1 : row;
        int nextCol = (col + 1) % 9;

        if (board[row][col] != 0)
            return solve(nextRow, nextCol);

        int box = (row / 3) * 3 + (col / 3);

        for (int num = 1; num <= 9; num++) {
            int mask = 1 << num;

            if ((rowMask[row] & mask) == 0 &&
                (colMask[col] & mask) == 0 &&
                (boxMask[box] & mask) == 0) {

                board[row][col] = num;

                rowMask[row] |= mask;
                colMask[col] |= mask;
                boxMask[box] |= mask;

                if (solve(nextRow, nextCol))
                    return true;

                // backtrack
                board[row][col] = 0;
                rowMask[row] ^= mask;
                colMask[col] ^= mask;
                boxMask[box] ^= mask;
            }
        }

        return false;
    }

    static void initializeMasks() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int num = board[i][j];
                if (num != 0) {
                    int mask = 1 << num;
                    rowMask[i] |= mask;
                    colMask[j] |= mask;
                    boxMask[(i / 3) * 3 + (j / 3)] |= mask;
                }
            }
        }
    }

    static void printBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        initializeMasks();

        if (solve(0, 0)) {
            System.out.println("Solved Sudoku:");
            printBoard();
        } else {
            System.out.println("No solution found");
        }
    }
}
