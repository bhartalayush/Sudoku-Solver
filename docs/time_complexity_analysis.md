## Time Complexity Analysis

---

### SudokuSolverCLRS ~ Ayush

isSafe()  
Time Complexity: O(1)  
Space Complexity: O(1)

solve()  
Time Complexity: O(9^n) ≈ O(9^81)  
Space Complexity: O(n)

printBoard()  
Time Complexity: O(1)  
Space Complexity: O(1)

Overall  
Big O: O(9^n) ≈ O(9^81)  
Omega: Ω(n)  
Space Complexity: O(n)

---

### SudokuBitMask ~ Varsha

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

### SudokuBruteForce


---

### SudokuCSP


---

### SudokuExp2D


---
