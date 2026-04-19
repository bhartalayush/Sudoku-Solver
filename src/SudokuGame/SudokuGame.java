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

    // Timer
    private JLabel timerLabel;
    private Timer timer;
    private int seconds = 0;

    // Theme flag
    private boolean isDarkMode = false;

    // ── Light-mode palette ──────────────────────────────────────────────────
    private static final Color LIGHT_BG         = new Color(245, 245, 240);
    private static final Color LIGHT_CELL_FIXED = new Color(220, 220, 215);
    private static final Color LIGHT_CELL_USER  = Color.WHITE;
    private static final Color LIGHT_TEXT_FIXED = new Color(40,  40,  40);
    private static final Color LIGHT_TEXT_USER  = new Color(30,  80,  160);
    private static final Color LIGHT_TEXT_ERROR = new Color(200, 30,  30);
    private static final Color LIGHT_TIMER_FG   = new Color(50,  50,  50);

    // ── Dark-mode palette ───────────────────────────────────────────────────
    private static final Color DARK_BG          = new Color(18,  18,  18);
    private static final Color DARK_CELL_FIXED  = new Color(45,  45,  45);
    private static final Color DARK_CELL_USER   = new Color(30,  30,  30);
    private static final Color DARK_TEXT_FIXED  = new Color(200, 200, 200);
    private static final Color DARK_TEXT_USER   = new Color(100, 160, 255);
    private static final Color DARK_TEXT_ERROR  = new Color(255, 90,  90);
    private static final Color DARK_TIMER_FG    = new Color(210, 210, 210);

    // ── Button accent colors (unchanged in both modes) ──────────────────────
    private static final Color ACCENT           = new Color(70,  130, 100);
    private static final Color BTN_CHECK        = new Color(70,  110, 180);
    private static final Color BTN_SOLVE        = new Color(180, 80,  60);
    private static final Color BTN_HINT         = new Color(120, 100, 180);
    private static final Color BTN_THEME_LIGHT  = new Color(80,  80,  80);   // shown in light mode
    private static final Color BTN_THEME_DARK   = new Color(200, 180, 100);  // shown in dark mode

    // Panels & buttons that need repainting on theme change
    private JPanel topPanel;
    private JPanel gridWrap;
    private JPanel buttonsPanel;
    private JButton btnTheme;

    public SudokuGame()
    {
        setTitle("Sudoku");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(LIGHT_BG);
        setLayout(new BorderLayout(10, 10));

        topPanel     = buildTopPanel();
        gridWrap     = buildGrid();
        buttonsPanel = buildButtons();

        add(topPanel,     BorderLayout.NORTH);
        add(gridWrap,     BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        newGame();
        setVisible(true);
    }

    // ── helpers that return the right color for the current theme ────────────

    private Color bg()         { return isDarkMode ? DARK_BG         : LIGHT_BG;         }
    private Color cellFixed()  { return isDarkMode ? DARK_CELL_FIXED  : LIGHT_CELL_FIXED; }
    private Color cellUser()   { return isDarkMode ? DARK_CELL_USER   : LIGHT_CELL_USER;  }
    private Color textFixed()  { return isDarkMode ? DARK_TEXT_FIXED  : LIGHT_TEXT_FIXED; }
    private Color textUser()   { return isDarkMode ? DARK_TEXT_USER   : LIGHT_TEXT_USER;  }
    private Color textError()  { return isDarkMode ? DARK_TEXT_ERROR  : LIGHT_TEXT_ERROR; }
    private Color timerFg()    { return isDarkMode ? DARK_TIMER_FG    : LIGHT_TIMER_FG;   }

    // ── grid ─────────────────────────────────────────────────────────────────

    private JPanel buildGrid()
    {
        JPanel outer = new JPanel(new GridLayout(3, 3, 3, 3));
        outer.setBackground(new Color(80, 80, 80));
        outer.setBorder(new LineBorder(new Color(60, 60, 60), 3));
        outer.setOpaque(true);

        for (int br = 0; br < 3; br++)
        {
            for (int bc = 0; bc < 3; bc++)
            {
                JPanel box = new JPanel(new GridLayout(3, 3, 1, 1));
                box.setBackground(new Color(120, 120, 120));
                box.setOpaque(true);

                for (int r = 0; r < 3; r++)
                {
                    for (int c = 0; c < 3; c++)
                    {
                        int row = br * 3 + r;
                        int col = bc * 3 + c;
                        JTextField tf = new JTextField();
                        tf.setHorizontalAlignment(JTextField.CENTER);
                        tf.setFont(new Font("SansSerif", Font.BOLD, 20));
                        tf.setPreferredSize(new Dimension(52, 52));
                        tf.setBackground(LIGHT_CELL_USER);
                        tf.setForeground(LIGHT_TEXT_USER);
                        tf.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                        cells[row][col] = tf;
                        box.add(tf);
                    }
                }
                outer.add(box);
            }
        }

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        wrap.setBackground(LIGHT_BG);
        wrap.add(outer);
        return wrap;
    }

    // ── buttons panel ────────────────────────────────────────────────────────

    private JPanel buildButtons()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        panel.setBackground(bg());

        JButton btnNew   = makeBtn("New Game", ACCENT);
        JButton btnCheck = makeBtn("Check",    BTN_CHECK);
        JButton btnSolve = makeBtn("Give Up",  BTN_SOLVE);
        JButton btnHint  = makeBtn("Hint",     BTN_HINT);
        btnTheme         = makeBtn("🌙 Dark Mode", BTN_THEME_LIGHT);

        btnNew.addActionListener(e -> newGame());
        btnCheck.addActionListener(e -> checkAnswers());
        btnSolve.addActionListener(e -> showSolution());
        btnHint.addActionListener(e -> giveHint());
        btnTheme.addActionListener(e -> toggleTheme());

        panel.add(btnNew);
        panel.add(btnCheck);
        panel.add(btnSolve);
        panel.add(btnHint);
        panel.add(btnTheme);
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

    // ── top panel (timer) ────────────────────────────────────────────────────

    private JPanel buildTopPanel()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(LIGHT_BG);

        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        timerLabel.setForeground(LIGHT_TIMER_FG);

        panel.add(timerLabel);
        return panel;
    }

    // ── timer helpers ────────────────────────────────────────────────────────

    private void startTimer()
    {
        seconds = 0;
        if (timer != null) timer.stop();

        timer = new Timer(1000, e -> {
            seconds++;
            int mins = seconds / 60;
            int secs = seconds % 60;
            timerLabel.setText(String.format("Time: %02d:%02d", mins, secs));
        });
        timer.start();
    }

    private void stopTimer()
    {
        if (timer != null) timer.stop();
    }

    // ── theme toggle ─────────────────────────────────────────────────────────

    /**
     * Toggles between dark and light mode.
     * Repaints every component that has a theme-sensitive color.
     */
    private void toggleTheme()
    {
        isDarkMode = !isDarkMode;

        // ── frame background ─────────────────────────────────────────────────
        getContentPane().setBackground(bg());

        // ── top panel & timer label ──────────────────────────────────────────
        topPanel.setBackground(bg());
        timerLabel.setForeground(timerFg());

        // ── grid wrap background ─────────────────────────────────────────────
        gridWrap.setBackground(bg());

        // ── buttons panel background ─────────────────────────────────────────
        buttonsPanel.setBackground(bg());

        // ── theme button label & accent ───────────────────────────────────────
        if (isDarkMode)
        {
            btnTheme.setText("☀️ Light Mode");
            btnTheme.setBackground(BTN_THEME_DARK);
        }
        else
        {
            btnTheme.setText("🌙 Dark Mode");
            btnTheme.setBackground(BTN_THEME_LIGHT);
        }

        // ── repaint each cell with correct colors ─────────────────────────────
        for (int r = 0; r < 9; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                JTextField tf = cells[r][c];
                if (!tf.isEditable())
                {
                    // Fixed (pre-filled) cell
                    tf.setBackground(cellFixed());
                    tf.setForeground(textFixed());
                }
                else
                {
                    String txt = tf.getText().trim();
                    if (txt.isEmpty())
                    {
                        tf.setBackground(cellUser());
                        tf.setForeground(textUser());
                    }
                    else
                    {
                        // Preserve green hint color; only restyle plain user input
                        if (!tf.getForeground().equals(Color.GREEN))
                        {
                            // Keep feedback colors (green/red tinted bg) but update fg
                            Color currentBg = tf.getBackground();
                            boolean isCorrectHighlight = currentBg.equals(new Color(200, 240, 210));
                            boolean isWrongHighlight   = currentBg.equals(new Color(255, 210, 210));

                            if (!isCorrectHighlight && !isWrongHighlight)
                            {
                                tf.setBackground(cellUser());
                            }
                            tf.setForeground(textUser());
                        }
                    }
                }
            }
        }

        // ── refresh the whole window ──────────────────────────────────────────
        SwingUtilities.updateComponentTreeUI(this);
        repaint();
    }

    // ── game logic ────────────────────────────────────────────────────────────

    private void newGame()
    {
        SudokuGenerator gen = new SudokuGenerator();
        gen.fill(0, 0);

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                solution[i][j] = gen.board[i][j];

        gen.removeNumbers();

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                puzzle[i][j] = gen.board[i][j];

        renderPuzzle();
        startTimer();
    }

    private void renderPuzzle()
    {
        for (int r = 0; r < 9; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                JTextField tf = cells[r][c];
                tf.setEditable(puzzle[r][c] == 0);
                if (puzzle[r][c] != 0)
                {
                    tf.setText(String.valueOf(puzzle[r][c]));
                    tf.setBackground(cellFixed());
                    tf.setForeground(textFixed());
                }
                else
                {
                    tf.setText("");
                    tf.setBackground(cellUser());
                    tf.setForeground(textUser());
                }
            }
        }
    }

    private void giveHint()
    {
        for (int r = 0; r < 9; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                if (puzzle[r][c] == 0 && cells[r][c].getText().isEmpty())
                {
                    cells[r][c].setText(String.valueOf(solution[r][c]));
                    cells[r][c].setForeground(Color.GREEN);
                    return;
                }
            }
        }
    }

    private void checkAnswers()
    {
        boolean allFilled  = true;
        boolean allCorrect = true;

        for (int r = 0; r < 9; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                if (puzzle[r][c] == 0)
                {
                    String txt = cells[r][c].getText().trim();
                    if (txt.isEmpty())
                    {
                        allFilled = false;
                        cells[r][c].setBackground(cellUser());
                    }
                    else
                    {
                        try
                        {
                            int val = Integer.parseInt(txt);
                            if (val == solution[r][c])
                            {
                                cells[r][c].setBackground(new Color(200, 240, 210));
                                cells[r][c].setForeground(textUser());
                            }
                            else
                            {
                                cells[r][c].setBackground(new Color(255, 210, 210));
                                cells[r][c].setForeground(textError());
                                allCorrect = false;
                            }
                        }
                        catch (NumberFormatException ex)
                        {
                            cells[r][c].setBackground(new Color(255, 210, 210));
                            cells[r][c].setForeground(textError());
                            allCorrect = false;
                        }
                    }
                }
            }
        }

        if (!allFilled)
            JOptionPane.showMessageDialog(this, "Some cells are still empty!", "Keep going",  JOptionPane.INFORMATION_MESSAGE);
        else if (allCorrect)
        {
            stopTimer();
            JOptionPane.showMessageDialog(this, "Solved correctly!\n" + timerLabel.getText(), "Well done!", JOptionPane.INFORMATION_MESSAGE);
        }
        else
            JOptionPane.showMessageDialog(this, "Some cells are wrong - red cells need fixing.", "Not quite", JOptionPane.WARNING_MESSAGE);
    }

    private void showSolution()
    {
        stopTimer();
        for (int r = 0; r < 9; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                cells[r][c].setText(String.valueOf(solution[r][c]));
                cells[r][c].setEditable(false);
                if (puzzle[r][c] == 0)
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
