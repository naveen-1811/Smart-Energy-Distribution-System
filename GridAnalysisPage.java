import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

public class GridAnalysisPage extends JPanel {

    private MainApp app;
    private JTextArea mainArea;
    private JLabel totalEdgesLbl, totalNodesLbl, totalCostLbl, mstCostLbl;

    public GridAnalysisPage(MainApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(MainApp.BG_COLOR);
        buildUI();
    }

    private void buildUI() {
        add(MainApp.buildSidebar(app, "GRID"), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MainApp.BG_COLOR);
        p.add(buildTopBar(), BorderLayout.NORTH);
        p.add(buildMain(), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainApp.PANEL_COLOR);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(14, 24, 14, 24)));
        JLabel title = new JLabel("⬡  Power Grid Analysis");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(MainApp.TEXT_COLOR);
        JButton refresh = MainApp.styledButton("↻  Refresh", MainApp.BUTTON_COLOR);
        refresh.addActionListener(e -> refresh());
        bar.add(title, BorderLayout.WEST);
        bar.add(refresh, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildMain() {
        JPanel p = new JPanel(new BorderLayout(16, 16));
        p.setBackground(MainApp.BG_COLOR);
        p.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // Summary cards
        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);

        totalNodesLbl = cardVal("0");
        totalEdgesLbl = cardVal("0");
        totalCostLbl  = cardVal("0");
        mstCostLbl    = cardVal("—");

        cards.add(buildInfoCard("Power Stations", totalNodesLbl, "⚡", MainApp.HIGHLIGHT));
        cards.add(buildInfoCard("Transmission Lines", totalEdgesLbl, "⬡", new Color(0xA78BFA)));
        cards.add(buildInfoCard("Total Grid Cost", totalCostLbl, "◎", new Color(0xFBBF24)));
        cards.add(buildInfoCard("MST Cost (Optimized)", mstCostLbl, "✓", MainApp.SUCCESS));

        // Main text area
        mainArea = new JTextArea();
        mainArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        mainArea.setBackground(new Color(0x0A0F1E));
        mainArea.setForeground(new Color(0x38BDF8));
        mainArea.setEditable(false);
        mainArea.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JScrollPane scroll = new JScrollPane(mainArea);
        scroll.setBorder(BorderFactory.createLineBorder(MainApp.BORDER_COLOR));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // Adjacency matrix panel
        JPanel matrixPanel = buildMatrixPanel();

        JSplitPane hSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, matrixPanel);
        hSplit.setDividerLocation(500);
        hSplit.setDividerSize(4);
        hSplit.setBorder(null);
        hSplit.setBackground(MainApp.BORDER_COLOR);

        p.add(cards, BorderLayout.NORTH);
        p.add(hSplit, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildMatrixPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MainApp.PANEL_COLOR);
        p.setBorder(BorderFactory.createLineBorder(MainApp.BORDER_COLOR));

        JLabel header = new JLabel("  Connectivity Matrix", SwingConstants.LEFT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(MainApp.TEXT_COLOR);
        header.setBackground(new Color(0x0D1B2E));
        header.setOpaque(true);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        JTextArea matrix = new JTextArea();
        matrix.setFont(new Font("Consolas", Font.PLAIN, 11));
        matrix.setBackground(MainApp.PANEL_COLOR);
        matrix.setForeground(MainApp.TEXT_COLOR);
        matrix.setEditable(false);
        matrix.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        matrix.setName("MATRIX");

        JScrollPane scroll = new JScrollPane(matrix);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(MainApp.PANEL_COLOR);

        p.add(header, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildInfoCard(String title, JLabel valLbl, String icon, Color accent) {
        JPanel card = new JPanel(new BorderLayout(10, 4));
        card.setBackground(MainApp.PANEL_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent.darker()),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI", Font.BOLD, 26));
        ico.setForeground(accent);

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(MainApp.FONT_SMALL);
        tLbl.setForeground(MainApp.MUTED_TEXT);

        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLbl.setForeground(MainApp.TEXT_COLOR);

        JPanel t = new JPanel(new GridLayout(2, 1, 0, 2));
        t.setOpaque(false);
        t.add(tLbl);
        t.add(valLbl);

        card.add(ico, BorderLayout.WEST);
        card.add(t, BorderLayout.CENTER);
        return card;
    }

    private JLabel cardVal(String v) {
        JLabel l = new JLabel(v);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(MainApp.TEXT_COLOR);
        return l;
    }

    public void refresh() {
        int nodes = MainApp.nodeCount;
        int lines = MainApp.edges.size();
        int totalCost = MainApp.edges.stream().mapToInt(e -> e[2]).sum();

        totalNodesLbl.setText(String.valueOf(nodes));
        totalEdgesLbl.setText(String.valueOf(lines));
        totalCostLbl.setText(String.valueOf(totalCost));
        mstCostLbl.setText(MainApp.totalMSTCost > 0 ? String.valueOf(MainApp.totalMSTCost) : "—");

        // Build text report
        StringBuilder sb = new StringBuilder();
        sb.append("  ═══════════════════════════════════════════════════\n");
        sb.append("  POWER GRID ANALYSIS REPORT\n");
        sb.append("  ═══════════════════════════════════════════════════\n\n");
        sb.append(String.format("  Power Stations (Nodes)  : %d\n", nodes));
        sb.append(String.format("  Transmission Lines      : %d\n", lines));
        sb.append(String.format("  Total Grid Cost         : %d MW·km\n", totalCost));
        sb.append(String.format("  MST Optimized Cost      : %s MW·km\n\n",
            MainApp.totalMSTCost > 0 ? MainApp.totalMSTCost : "Not computed"));

        if (MainApp.totalMSTCost > 0 && totalCost > 0) {
            double savings = ((double)(totalCost - MainApp.totalMSTCost) / totalCost) * 100;
            sb.append(String.format("  Cost Savings            : %.1f%%\n\n", savings));
        }

        sb.append("  ── All Transmission Lines ─────────────────────────\n\n");
        sb.append("  No.   Src → Dst       Cost\n");
        sb.append("  ────────────────────────────\n");
        int i = 1;
        for (int[] e : MainApp.edges) {
            sb.append(String.format("  %-4d  PS-%d → PS-%-3d  %d MW·km\n", i++, e[0], e[1], e[2]));
        }

        if (!MainApp.mstResult.isEmpty()) {
            sb.append("\n  ── MST Optimal Connections ─────────────────────\n\n");
            sb.append("  No.   Src → Dst       Cost     Status\n");
            sb.append("  ─────────────────────────────────────\n");
            int j = 1;
            for (int[] e : MainApp.mstResult) {
                sb.append(String.format("  %-4d  PS-%d → PS-%-3d  %-8d ✓ INCLUDED\n",
                    j++, e[0], e[1], e[2]));
            }
            sb.append(String.format("\n  Total MST Cost: %d MW·km\n", MainApp.totalMSTCost));

            if (MainApp.primsTime > 0)
                sb.append(String.format("  Prim's Time:    %d µs\n", MainApp.primsTime / 1000));
            if (MainApp.kruskalsTime > 0)
                sb.append(String.format("  Kruskal's Time: %d µs\n", MainApp.kruskalsTime / 1000));
        }

        mainArea.setText(sb.toString());
        mainArea.setCaretPosition(0);

        // Build adjacency matrix
        updateMatrix(nodes);
    }

    private void updateMatrix(int n) {
        // find the matrix textarea inside the matrix panel
        for (Component comp : getComponents()) searchMatrix(comp, n);
        searchMatrix(this, n);
    }

    private void searchMatrix(Component root, int n) {
        if (root instanceof JTextArea && "MATRIX".equals(((JTextArea) root).getName())) {
            JTextArea ta = (JTextArea) root;
            if (n == 0 || MainApp.edges.isEmpty()) { ta.setText("  No data available."); return; }
            int[][] mat = new int[n][n];
            for (int[] e : MainApp.edges) { mat[e[0]][e[1]] = e[2]; mat[e[1]][e[0]] = e[2]; }
            StringBuilder sb = new StringBuilder();
            sb.append("   ");
            for (int i = 0; i < n; i++) sb.append(String.format("P%-3d", i));
            sb.append("\n");
            for (int i = 0; i < n; i++) {
                sb.append(String.format("P%-2d", i));
                for (int j = 0; j < n; j++) {
                    if (mat[i][j] == 0 && i != j) sb.append("  ─  ");
                    else if (i == j) sb.append("  ●  ");
                    else sb.append(String.format("%-5d", mat[i][j]));
                }
                sb.append("\n");
            }
            ta.setText(sb.toString());
        }
        if (root instanceof Container)
            for (Component c : ((Container) root).getComponents())
                searchMatrix(c, n);
    }
}