import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class AlgorithmComparisonPage extends JPanel {

    private MainApp app;
    private JLabel primsTimeVal, kruskalsTimeVal, fasterVal;
    private JPanel primsBar, kruskalsBar;
    private JLabel primsBarPct, kruskalsBarPct;
    private JTextArea detailsArea;

    public AlgorithmComparisonPage(MainApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(MainApp.BG_COLOR);
        buildUI();
    }

    private void buildUI() {
        add(MainApp.buildSidebar(app, "ALGO"), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MainApp.BG_COLOR);
        p.add(buildTopBar(), BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setBackground(MainApp.BG_COLOR);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        main.add(buildTimingCards());
        main.add(Box.createVerticalStrut(24));
        main.add(buildPerformanceChart());
        main.add(Box.createVerticalStrut(24));
        main.add(buildAlgoDetails());

        JScrollPane scroll = new JScrollPane(main);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(MainApp.BG_COLOR);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainApp.PANEL_COLOR);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(14, 24, 14, 24)));
        JLabel t = new JLabel("≡  Algorithm Performance Comparison");
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));
        t.setForeground(MainApp.TEXT_COLOR);
        JLabel sub = new JLabel("Execution time comparison between Prim's and Kruskal's algorithms");
        sub.setFont(MainApp.FONT_SMALL);
        sub.setForeground(MainApp.MUTED_TEXT);
        JPanel tp = new JPanel(new BorderLayout(0, 3));
        tp.setOpaque(false);
        tp.add(t, BorderLayout.NORTH);
        tp.add(sub, BorderLayout.SOUTH);
        bar.add(tp, BorderLayout.WEST);
        return bar;
    }

    private JPanel buildTimingCards() {
        JPanel row = new JPanel(new GridLayout(1, 3, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Prim's card
        JPanel pCard = metricCard("Prim's Algorithm", "—", new Color(0x059669));
        primsTimeVal = findValueLabel(pCard);
        row.add(pCard);

        // Kruskal's card
        JPanel kCard = metricCard("Kruskal's Algorithm", "—", new Color(0x7C3AED));
        kruskalsTimeVal = findValueLabel(kCard);
        row.add(kCard);

        // Faster card
        JPanel fCard = metricCard("Faster Algorithm", "—", MainApp.HIGHLIGHT);
        fasterVal = findValueLabel(fCard);
        row.add(fCard);

        return row;
    }

    private JPanel metricCard(String title, String val, Color accent) {
        JPanel card = new JPanel(new BorderLayout(10, 6));
        card.setBackground(MainApp.PANEL_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent.darker()),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(MainApp.FONT_SMALL);
        tLbl.setForeground(MainApp.MUTED_TEXT);

        JLabel vLbl = new JLabel(val);
        vLbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        vLbl.setForeground(MainApp.TEXT_COLOR);
        vLbl.putClientProperty("VALUE_LABEL", Boolean.TRUE);

        JLabel accentBar = new JLabel("  ");
        accentBar.setBackground(accent);
        accentBar.setOpaque(true);
        accentBar.setPreferredSize(new Dimension(4, 60));

        JPanel textP = new JPanel(new GridLayout(2, 1, 0, 6));
        textP.setOpaque(false);
        textP.add(tLbl);
        textP.add(vLbl);

        card.add(accentBar, BorderLayout.WEST);
        card.add(textP, BorderLayout.CENTER);
        return card;
    }

    // Walk card components to get the value JLabel
    private JLabel findValueLabel(JPanel card) {
        for (Component c : card.getComponents()) {
            if (c instanceof JPanel) {
                for (Component cc : ((JPanel) c).getComponents()) {
                    if (cc instanceof JLabel && Boolean.TRUE.equals(((JLabel) cc).getClientProperty("VALUE_LABEL")))
                        return (JLabel) cc;
                }
            }
        }
        return new JLabel("—");
    }

    private JPanel buildPerformanceChart() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JLabel header = sectionLabel("Execution Time — Visual Comparison");
        wrapper.add(header, BorderLayout.NORTH);

        JPanel chartPanel = new JPanel();
        chartPanel.setBackground(MainApp.PANEL_COLOR);
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)));

        chartPanel.add(buildBarRow("Prim's", new Color(0x059669)));
        chartPanel.add(Box.createVerticalStrut(16));
        chartPanel.add(buildBarRow("Kruskal's", new Color(0x7C3AED)));

        wrapper.add(chartPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildBarRow(String label, Color color) {
        JPanel row = new JPanel(new BorderLayout(16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(MainApp.TEXT_COLOR);
        lbl.setPreferredSize(new Dimension(110, 30));

        JPanel barBg = new JPanel(new BorderLayout());
        barBg.setBackground(new Color(0x0F172A));
        barBg.setBorder(BorderFactory.createLineBorder(MainApp.BORDER_COLOR));
        barBg.setPreferredSize(new Dimension(0, 30));

        JPanel barFill = new JPanel();
        barFill.setBackground(color);
        barFill.setPreferredSize(new Dimension(0, 30));

        JLabel pct = new JLabel("0%");
        pct.setFont(new Font("Segoe UI", Font.BOLD, 11));
        pct.setForeground(Color.WHITE);
        pct.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));

        barFill.setLayout(new BorderLayout());
        barFill.add(pct, BorderLayout.WEST);
        barBg.add(barFill, BorderLayout.WEST);

        if ("Prim's".equals(label)) {
            primsBar = barFill;
            primsBarPct = pct;
        } else {
            kruskalsBar = barFill;
            kruskalsBarPct = pct;
        }

        row.add(lbl, BorderLayout.WEST);
        row.add(barBg, BorderLayout.CENTER);
        return row;
    }

    private JPanel buildAlgoDetails() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setOpaque(false);

        JLabel header = sectionLabel("Algorithm Characteristics");
        wrapper.add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 2, 16, 0));
        grid.setOpaque(false);
        grid.add(buildAlgoCard("Prim's Algorithm", new Color(0x059669),
            new String[][]{
                {"Type",            "Greedy / Vertex-based"},
                {"Time Complexity", "O(E log V) with PQ"},
                {"Space",           "O(V + E)"},
                {"Best For",        "Dense graphs"},
                {"Start",           "From any vertex"},
                {"Data Structure",  "Priority Queue"}
            },
            "Grows the MST one vertex at a time by always\n"
          + "picking the minimum weight edge that connects\n"
          + "the MST to a vertex not yet in the MST.\n"
          + "Efficient on dense, well-connected grids."));

        grid.add(buildAlgoCard("Kruskal's Algorithm", new Color(0x7C3AED),
            new String[][]{
                {"Type",            "Greedy / Edge-based"},
                {"Time Complexity", "O(E log E)"},
                {"Space",           "O(V + E)"},
                {"Best For",        "Sparse graphs"},
                {"Start",           "From sorted edges"},
                {"Data Structure",  "Union-Find (DSU)"}
            },
            "Sorts all edges by weight and adds them one\n"
          + "by one, skipping any edge that would form a\n"
          + "cycle. Uses Union-Find to detect cycles.\n"
          + "Efficient on sparse transmission networks."));

        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildAlgoCard(String title, Color accent, String[][] specs, String desc) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(MainApp.PANEL_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent.darker()),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tLbl.setForeground(accent);

        JPanel specGrid = new JPanel(new GridLayout(specs.length, 2, 8, 6));
        specGrid.setOpaque(false);
        for (String[] s : specs) {
            JLabel k = new JLabel(s[0] + ":");
            k.setFont(new Font("Segoe UI", Font.BOLD, 12));
            k.setForeground(MainApp.MUTED_TEXT);
            JLabel v = new JLabel(s[1]);
            v.setFont(MainApp.FONT_BODY);
            v.setForeground(MainApp.TEXT_COLOR);
            specGrid.add(k);
            specGrid.add(v);
        }

        JTextArea descArea = new JTextArea(desc);
        descArea.setFont(MainApp.FONT_BODY);
        descArea.setForeground(MainApp.MUTED_TEXT);
        descArea.setBackground(new Color(0x0F172A));
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        card.add(tLbl, BorderLayout.NORTH);
        card.add(specGrid, BorderLayout.CENTER);
        card.add(descArea, BorderLayout.SOUTH);
        return card;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(MainApp.MUTED_TEXT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return l;
    }

    public void refresh() {
        long pt = MainApp.primsTime;
        long kt = MainApp.kruskalsTime;

        primsTimeVal.setText(pt > 0 ? (pt / 1000) + " µs" : "—");
        kruskalsTimeVal.setText(kt > 0 ? (kt / 1000) + " µs" : "—");

        if (pt > 0 && kt > 0) {
            fasterVal.setText(pt <= kt ? "Prim's" : "Kruskal's");
            fasterVal.setForeground(pt <= kt ? new Color(0x059669) : new Color(0x7C3AED));

            long max = Math.max(pt, kt);
            int primsW   = (int)(((double) pt / max) * 100);
            int kruskalsW = (int)(((double) kt / max) * 100);

            primsBar.setPreferredSize(new Dimension(primsW * 6, 30));
            kruskalsBar.setPreferredSize(new Dimension(kruskalsW * 6, 30));
            primsBarPct.setText(primsW + "%");
            kruskalsBarPct.setText(kruskalsW + "%");
        } else {
            fasterVal.setText("—");
            fasterVal.setForeground(MainApp.MUTED_TEXT);
        }
        revalidate();
        repaint();
    }
}