package sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class SudokuGame extends JFrame
{
    private int puzzle[][] = new int[9][9];
    private int solution[][] = new int[9][9];
    private JTextField cells[][] = new JTextField[9][9];

    private static final Color BG = new Color(245, 245, 240);
    private static final Color CELL_FIXED = new Color(220, 220, 215);
    private static final Color CELL_USER = Color.WHITE;
    private static final Color TEXT_FIXED = new Color(40, 40, 40);
    private static final Color TEXT_USER = new Color(30, 80, 160);
    private static final Color TEXT_ERROR = new Color(200, 30, 30);
    private static final Color ACCENT = new Color(70, 130, 100);

    public SudokuGame()
    {
        setTitle("Sudoku");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(10, 10));

        add(buildGrid(),    BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        newGame();
        setVisible(true);
    }

    private JPanel buildGrid()
    {
        JPanel outer = new JPanel(new GridLayout(3, 3, 3, 3));
        outer.setBackground(new Color(80, 80, 80));
        outer.setBorder(new LineBorder(new Color(60, 60, 60), 3));
        outer.setOpaque(true);

        for(int br = 0; br < 3; br++)
        {
            for(int bc = 0; bc < 3; bc++)
            {
                JPanel box = new JPanel(new GridLayout(3, 3, 1, 1));
                box.setBackground(new Color(120, 120, 120));
                box.setOpaque(true);

                for(int r = 0; r < 3; r++)
                {
                    for(int c = 0; c < 3; c++)
                    {
                        int row = br * 3 + r;
                        int col = bc * 3 + c;
                        JTextField tf = new JTextField();
                        tf.setHorizontalAlignment(JTextField.CENTER);
                        tf.setFont(new Font("SansSerif", Font.BOLD, 20));
                        tf.setPreferredSize(new Dimension(52, 52));
                        tf.setBackground(CELL_USER);
                        tf.setForeground(TEXT_USER);
                        tf.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                        cells[row][col] = tf;
                        box.add(tf);
                    }
                }
                outer.add(box);
            }
        }

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        wrap.setBackground(BG);
        wrap.add(outer);
        return wrap;
    }

    private JPanel buildButtons()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        panel.setBackground(BG);

        JButton btnNew = makeBtn("New Game", ACCENT);
        JButton btnCheck = makeBtn("Check",    new Color(70, 110, 180));
        JButton btnSolve = makeBtn("Give Up",  new Color(180, 80, 60));

        btnNew.addActionListener(e -> newGame());
        btnCheck.addActionListener(e -> checkAnswers());
        btnSolve.addActionListener(e -> showSolution());

        panel.add(btnNew);
        panel.add(btnCheck);
        panel.add(btnSolve);
        return panel;
    }

    private JButton makeBtn(String text, Color bg)
    {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        b.setOpaque(true);
        return b;
    }

    private void newGame()
    {
        // 1. generate full board using SudokuGenerator (CLRS randomiser)
        SudokuGenerator gen = new SudokuGenerator();
        gen.fill(0, 0);

        // 2. copy full board to solution before removing numbers
        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                solution[i][j] = gen.board[i][j];
            }
        }

        // 3. remove 40 numbers to make the puzzle
        gen.removeNumbers();

        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                puzzle[i][j] = gen.board[i][j];
            }
        }

        // 4. render the puzzle on screen
        renderPuzzle();
    }

    private void renderPuzzle()
    {
        for(int r = 0; r < 9; r++)
        {
            for(int c = 0; c < 9; c++)
            {
                JTextField tf = cells[r][c];
                tf.setEditable(puzzle[r][c] == 0);
                if(puzzle[r][c] != 0)
                {
                    tf.setText(String.valueOf(puzzle[r][c]));
                    tf.setBackground(CELL_FIXED);
                    tf.setForeground(TEXT_FIXED);
                }
                else
                {
                    tf.setText("");
                    tf.setBackground(CELL_USER);
                    tf.setForeground(TEXT_USER);
                }
            }
        }
    }

    private void checkAnswers()
    {
        boolean allFilled = true;
        boolean allCorrect = true;

        for(int r = 0; r < 9; r++)
        {
            for(int c = 0; c < 9; c++)
            {
                if(puzzle[r][c] == 0)
                {
                    String txt = cells[r][c].getText().trim();
                    if(txt.isEmpty())
                    {
                        allFilled = false;
                        cells[r][c].setBackground(CELL_USER);
                    }
                    else
                    {
                        try
                        {
                            int val = Integer.parseInt(txt);
                            if(val == solution[r][c])
                            {
                                cells[r][c].setBackground(new Color(200, 240, 210));
                                cells[r][c].setForeground(TEXT_USER);
                            }
                            else
                            {
                                cells[r][c].setBackground(new Color(255, 210, 210));
                                cells[r][c].setForeground(TEXT_ERROR);
                                allCorrect = false;
                            }
                        }
                        catch(NumberFormatException ex)
                        {
                            cells[r][c].setBackground(new Color(255, 210, 210));
                            cells[r][c].setForeground(TEXT_ERROR);
                            allCorrect = false;
                        }
                    }
                }
            }
        }

        if(!allFilled)
        {
            JOptionPane.showMessageDialog(this, "Some cells are still empty!", "Keep going", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(allCorrect)
        {
            JOptionPane.showMessageDialog(this, "Solved correctly!", "Well done!", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Some cells are wrong - red cells need fixing.", "Not quite", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showSolution()
    {
        for(int r = 0; r < 9; r++)
        {
            for(int c = 0; c < 9; c++)
            {
                cells[r][c].setText(String.valueOf(solution[r][c]));
                cells[r][c].setEditable(false);
                if(puzzle[r][c] == 0)
                {
                    cells[r][c].setBackground(new Color(255, 245, 200));
                    cells[r][c].setForeground(new Color(150, 100, 0));
                }
            }
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new SudokuGame());
    }
}
