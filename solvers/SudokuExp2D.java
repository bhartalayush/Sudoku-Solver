// Sudoku Solving Method - Explicit Stack + 2D Array Backtracking
//
// Mapping:
//   struct       -> class
//   2D int array -> board[9][9]
//   Stack (Ch.2) -> explicit Stack<StackFrame> with push/pop
//   Linear scan  -> isValid() checks (row, col, subgrid)

import java.util.Stack;

public class SudokuExp2D {
    // StackFrame 
    // Stores the state we push onto the stack:
    //   row, col  -> which cell we are filling
    //   val       -> which value (1-9) we last tried
    static class StackFrame {
        int row, col, val;

        StackFrame(int row, int col, int val) {
            this.row = row;
            this.col = col;
            this.val = val;
        }
    }
    // Board — 9x9 2D array
    // 0 means empty cell
    static int[][] board = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };
    // isValid() — Linear search on row, col, subgrid
    // Mirrors Tanenbaum's sequential search on arrays
    // Checks all 3 Sudoku constraints before placing a value
    static boolean isValid(int[][] board, int row, int col, int num) {

        // Check row — linear scan across 9 columns
        for (int c = 0; c < 9; c++) {
            if (board[row][c] == num)
                return false;
        }

        // Check column — linear scan across 9 rows
        for (int r = 0; r < 9; r++) {
            if (board[r][col] == num)
                return false;
        }

        // Check 3x3 subgrid — find top-left corner, then scan
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (board[r][c] == num)
                    return false;
            }
        }

        return true;
    }
    // findEmpty() — Scans board for next empty cell (value==0)
    // Returns {row, col} or null if board is complete
    // Mirrors Tanenbaum's linear array traversal
    static int[] findEmpty(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0)
                    return new int[]{r, c};
            }
        }
        return null; // no empty cell found — board is solved
    }
    // solve() — Explicit Stack-based Backtracking
    //
    //  Explicitly showing how recursive algorithms can be converted to iterative ones using an explicit stack.
    // Instead of recursion, we:
    //   PUSH  -> when we place a valid number in a cell
    //   POP   -> when no value 1-9 works (backtrack)
    static boolean solve(int[][] board) {

        // Stack stores StackFrame (struct equivalent)
        Stack<StackFrame> stack = new Stack<>();

        // Find the first empty cell to begin
        int[] empty = findEmpty(board);
        if (empty == null) return true; // already solved

        // Push the first frame with val=0 (not yet tried anything)
        stack.push(new StackFrame(empty[0], empty[1], 0));

        while (!stack.isEmpty()) {

            StackFrame frame = stack.peek(); // look at top, don't pop yet
            int row = frame.row;
            int col = frame.col;
            boolean placed = false;

            // Try values from (last tried + 1) up to 9
            for (int num = frame.val + 1; num <= 9; num++) {
                if (isValid(board, row, col, num)) {
                    // Valid number found — place it
                    board[row][col] = num;
                    frame.val = num; // record what we placed at this frame

                    // Find next empty cell
                    int[] next = findEmpty(board);
                    if (next == null) {
                        // No empty cell left — puzzle solved!
                        return true;
                    }

                    // Push next empty cell onto stack
                    stack.push(new StackFrame(next[0], next[1], 0));
                    placed = true;
                    break;
                }
            }

            // If no valid number was found for this cell — BACKTRACK
            if (!placed) {
                board[row][col] = 0;  // reset the cell
                stack.pop();          // pop this frame (Tanenbaum pop operation)
            }
        }

        return false; // no solution found
    }
    // printBoard() — Display the 9x9 array
    // Simple 2D array traversal
    static void printBoard(int[][] board) {
        System.out.println("+-------+-------+-------+");
        for (int r = 0; r < 9; r++) {
            System.out.print("| ");
            for (int c = 0; c < 9; c++) {
                System.out.print(board[r][c] + " ");
                if ((c + 1) % 3 == 0) System.out.print("| ");
            }
            System.out.println();
            if ((r + 1) % 3 == 0)
                System.out.println("+-------+-------+-------+");
        }
    }

    // main()
    public static void main(String[] args) {
        System.out.println("Initial Board:");
        printBoard(board);

        if (solve(board)) {
            System.out.println("\nSolved Board:");
            printBoard(board);
        } else {
            System.out.println("No solution exists.");
        }
    }
}