## Time Complexity Analysis

---

### SudokuSolverCLRS ~ Ayush

isSafe()  
Time Complexity: O(1)  
Checks row, column and box with fixed size loops.

solve()  
Time Complexity: O(9^n) ≈ O(9^81)  
Backtracking, tries all possibilities in worst case.

printBoard()  
Time Complexity: O(1)  
Fixed 9x9 traversal.

Overall Time Complexity: O(9^n) ≈ O(9^81)

---

### SudokuGenerator ~ Varsha

check()  
Time Complexity: O(1)  
Fixed loops of size 9.

fill()  
Time Complexity: O(9^n) ≈ O(9^81)  
Backtracking over all cells.

removeNumbers()  
Time Complexity: O(1)  
Removes fixed 40 elements.

printBoard()  
Time Complexity: O(1)  
Fixed 9x9 traversal.

Overall Time Complexity: O(9^n) ≈ O(9^81)

---

### SudokuSolver


---

### SudokuCSP


---

### SudokuExp2D


---
