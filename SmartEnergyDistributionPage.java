import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class SmartEnergyDistributionPage extends JPanel {

    private MainApp app;
    private JTextField srcField, dstField, costField, nodeCountField;
    private JTextArea graphArea;
    private DefaultTableModel tableModel;
    private JLabel totalCostLabel, edgeCountLabel;

    public SmartEnergyDistributionPage(MainApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(MainApp.BG_COLOR);
        buildUI();
    }

    private void buildUI() {
        add(MainApp.buildSidebar(app, "ENERGY"), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MainApp.BG_COLOR);
        p.add(buildTopBar(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            buildLeftPanel(), buildRightPanel());
        split.setDividerLocation(340);
        split.setDividerSize(4);
        split.setBackground(MainApp.BORDER_COLOR);
        split.setBorder(null);
        p.add(split, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainApp.PANEL_COLOR);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(14, 24, 14, 24)));
        JLabel title = new JLabel("⚡  Smart Energy Distribution");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(MainApp.TEXT_COLOR);
        JLabel sub = new JLabel("Add power stations and transmission lines, then generate the optimal MST network");
        sub.setFont(MainApp.FONT_SMALL);
        sub.setForeground(MainApp.MUTED_TEXT);
        JPanel t = new JPanel(new BorderLayout(0, 3));
        t.setOpaque(false);
        t.add(title, BorderLayout.NORTH);
        t.add(sub, BorderLayout.SOUTH);
        bar.add(t, BorderLayout.WEST);

        JPanel badges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        badges.setOpaque(false);
        edgeCountLabel = badge("Lines: 0", MainApp.HIGHLIGHT);
        totalCostLabel = badge("MST Cost: —", MainApp.SUCCESS);
        badges.add(edgeCountLabel);
        badges.add(totalCostLabel);
        bar.add(badges, BorderLayout.EAST);
        return bar;
    }

    private JLabel badge(String text, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(c);
        l.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(c.darker()),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return l;
    }

    private JPanel buildLeftPanel() {
        JPanel p = new JPanel();
        p.setBackground(MainApp.PANEL_COLOR);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addSectionHeader(p, "Network Configuration");
        p.add(Box.createVerticalStrut(16));

        // Node count
        p.add(formLabel("Total Power Stations"));
        p.add(Box.createVerticalStrut(6));
        nodeCountField = styledField("e.g.  5");
        nodeCountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(nodeCountField);
        p.add(Box.createVerticalStrut(20));

        addSectionHeader(p, "Add Transmission Line");
        p.add(Box.createVerticalStrut(16));

        p.add(formLabel("Source Power Station  (0-indexed)"));
        p.add(Box.createVerticalStrut(6));
        srcField = styledField("e.g.  0");
        srcField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(srcField);
        p.add(Box.createVerticalStrut(14));

        p.add(formLabel("Destination Power Station"));
        p.add(Box.createVerticalStrut(6));
        dstField = styledField("e.g.  1");
        dstField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(dstField);
        p.add(Box.createVerticalStrut(14));

        p.add(formLabel("Transmission Cost (MW·km)"));
        p.add(Box.createVerticalStrut(6));
        costField = styledField("e.g.  10");
        costField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(costField);
        p.add(Box.createVerticalStrut(22));

        JButton addBtn = MainApp.styledButton("＋  Add Transmission Line", MainApp.BUTTON_COLOR);
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addBtn.setAlignmentX(LEFT_ALIGNMENT);
        addBtn.addActionListener(e -> addEdge());
        p.add(addBtn);
        p.add(Box.createVerticalStrut(10));

        JButton clearBtn = MainApp.styledButton("✕  Clear All Lines", new Color(0x1E293B));
        clearBtn.setForeground(MainApp.ALERT);
        clearBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x7F1D1D)),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)));
        clearBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        clearBtn.setAlignmentX(LEFT_ALIGNMENT);
        clearBtn.addActionListener(e -> clearAll());
        p.add(clearBtn);
        p.add(Box.createVerticalStrut(22));

        JSeparator sep = new JSeparator();
        sep.setForeground(MainApp.BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        p.add(sep);
        p.add(Box.createVerticalStrut(22));

        addSectionHeader(p, "Run Optimization");
        p.add(Box.createVerticalStrut(16));

        JButton primsBtn = MainApp.styledButton("⚡ Run Prim's Algorithm", new Color(0x059669));
        primsBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        primsBtn.setAlignmentX(LEFT_ALIGNMENT);
        primsBtn.addActionListener(e -> runPrims());
        p.add(primsBtn);
        p.add(Box.createVerticalStrut(10));

        JButton kruskalBtn = MainApp.styledButton("⚡ Run Kruskal's Algorithm", new Color(0x7C3AED));
        kruskalBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        kruskalBtn.setAlignmentX(LEFT_ALIGNMENT);
        kruskalBtn.addActionListener(e -> runKruskals());
        p.add(kruskalBtn);
        p.add(Box.createVerticalStrut(10));

        JButton loadSample = MainApp.styledButton("↻  Load Sample Grid", new Color(0xD97706));
        loadSample.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loadSample.setAlignmentX(LEFT_ALIGNMENT);
        loadSample.addActionListener(e -> loadSample());
        p.add(loadSample);

        return p;
    }

    private JPanel buildRightPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBackground(MainApp.BG_COLOR);

        // Graph text area
        graphArea = new JTextArea();
        graphArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        graphArea.setBackground(new Color(0x0A0F1E));
        graphArea.setForeground(new Color(0x38BDF8));
        graphArea.setEditable(false);
        graphArea.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        graphArea.setText(defaultGraphText());

        JScrollPane graphScroll = new JScrollPane(graphArea);
        graphScroll.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, MainApp.BORDER_COLOR));
        graphScroll.setPreferredSize(new Dimension(0, 220));

        // Results Table
        String[] cols = {"Source Node", "Destination Node", "Cost (MW·km)", "Algorithm"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        styleTable(table);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(null);
        tableScroll.getViewport().setBackground(MainApp.PANEL_COLOR);

        // Table header bar
        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(MainApp.PANEL_COLOR);
        tableHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        JLabel tLbl = new JLabel("⬡  Optimized MST Results");
        tLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tLbl.setForeground(MainApp.TEXT_COLOR);
        tableHeader.add(tLbl, BorderLayout.WEST);

        JPanel tableArea = new JPanel(new BorderLayout());
        tableArea.add(tableHeader, BorderLayout.NORTH);
        tableArea.add(tableScroll, BorderLayout.CENTER);

        JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, graphScroll, tableArea);
        vSplit.setDividerLocation(230);
        vSplit.setDividerSize(4);
        vSplit.setBorder(null);
        vSplit.setBackground(MainApp.BORDER_COLOR);

        p.add(vSplit, BorderLayout.CENTER);
        return p;
    }

    // ── Graph Display ─────────────────────────────────────────────────────────

    private String defaultGraphText() {
        return "  Smart Energy Distribution — Graph View\n"
             + "  ═══════════════════════════════════════\n\n"
             + "  Add transmission lines using the controls on the left.\n"
             + "  Then run Prim's or Kruskal's algorithm to compute\n"
             + "  the Minimum Spanning Tree (MST) for the power grid.\n\n"
             + "  Tip: Use 'Load Sample Grid' to try a pre-built example.\n";
    }

    private void renderGraph() {
        StringBuilder sb = new StringBuilder();
        sb.append("  Power Grid — Edge List View\n");
        sb.append("  ═══════════════════════════════════════\n\n");
        sb.append(String.format("  Stations: %d     Transmission Lines: %d\n\n",
            MainApp.nodeCount, MainApp.edges.size()));
        sb.append("  Src → Dst  │  Cost\n");
        sb.append("  ──────────────────\n");
        for (int[] e : MainApp.edges) {
            sb.append(String.format("  PS-%d → PS-%d  │  %d MW·km\n", e[0], e[1], e[2]));
        }
        if (!MainApp.mstResult.isEmpty()) {
            sb.append("\n  ── MST Result ──────────────────────\n");
            for (int[] e : MainApp.mstResult) {
                sb.append(String.format("  ✓ PS-%d → PS-%d  │  %d\n", e[0], e[1], e[2]));
            }
            sb.append(String.format("\n  Total MST Cost: %d MW·km\n", MainApp.totalMSTCost));
        }
        graphArea.setText(sb.toString());
    }

    // ── Add / Clear ───────────────────────────────────────────────────────────

    private void addEdge() {
        try {
            int nc = Integer.parseInt(nodeCountField.getText().trim());
            int src = Integer.parseInt(srcField.getText().trim());
            int dst = Integer.parseInt(dstField.getText().trim());
            int cost = Integer.parseInt(costField.getText().trim());
            if (src < 0 || dst < 0 || src >= nc || dst >= nc)
                throw new IllegalArgumentException("Node index out of range");
            if (cost <= 0)
                throw new IllegalArgumentException("Cost must be positive");
            MainApp.nodeCount = nc;
            MainApp.edges.add(new int[]{src, dst, cost});
            edgeCountLabel.setText("Lines: " + MainApp.edges.size());
            srcField.setText("");
            dstField.setText("");
            costField.setText("");
            renderGraph();
        } catch (NumberFormatException ex) {
            showError("Please enter valid integer values.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void clearAll() {
        MainApp.edges.clear();
        MainApp.mstResult.clear();
        MainApp.totalMSTCost = 0;
        MainApp.nodeCount = 0;
        tableModel.setRowCount(0);
        nodeCountField.setText("");
        edgeCountLabel.setText("Lines: 0");
        totalCostLabel.setText("MST Cost: —");
        graphArea.setText(defaultGraphText());
    }

    // ── Prim's Algorithm ──────────────────────────────────────────────────────

    private void runPrims() {
        if (!validateInputs()) return;
        long start = System.nanoTime();

        int n = MainApp.nodeCount;
        int[] key = new int[n];
        int[] parent = new int[n];
        boolean[] inMST = new boolean[n];
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        key[0] = 0;

        // Build adjacency list
        java.util.List<java.util.List<int[]>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : MainApp.edges) {
            adj.get(e[0]).add(new int[]{e[1], e[2]});
            adj.get(e[1]).add(new int[]{e[0], e[2]});
        }

        // Priority queue: {key, node}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.add(new int[]{0, 0});

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int u = top[1];
            if (inMST[u]) continue;
            inMST[u] = true;
            for (int[] nb : adj.get(u)) {
                int v = nb[0], w = nb[1];
                if (!inMST[v] && w < key[v]) {
                    key[v] = w;
                    parent[v] = u;
                    pq.add(new int[]{w, v});
                }
            }
        }

        long end = System.nanoTime();
        MainApp.primsTime = end - start;

        MainApp.mstResult.clear();
        int total = 0;
        tableModel.setRowCount(0);
        for (int v = 1; v < n; v++) {
            if (parent[v] != -1) {
                MainApp.mstResult.add(new int[]{parent[v], v, key[v]});
                tableModel.addRow(new Object[]{"PS-" + parent[v], "PS-" + v, key[v], "Prim's"});
                total += key[v];
            }
        }
        MainApp.totalMSTCost = total;
        totalCostLabel.setText("MST Cost: " + total);
        renderGraph();
        showInfo("Prim's Algorithm complete!\nMST Cost: " + total + " MW·km\nTime: " + (MainApp.primsTime/1000) + " µs");
    }

    // ── Kruskal's Algorithm ───────────────────────────────────────────────────

    private void runKruskals() {
        if (!validateInputs()) return;
        long start = System.nanoTime();

        int n = MainApp.nodeCount;
        // Sort edges by cost
        java.util.List<int[]> sorted = new ArrayList<>(MainApp.edges);
        sorted.sort(Comparator.comparingInt(a -> a[2]));

        int[] parent = new int[n];
        int[] rank   = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        MainApp.mstResult.clear();
        tableModel.setRowCount(0);
        int total = 0;

        for (int[] e : sorted) {
            int pu = find(parent, e[0]);
            int pv = find(parent, e[1]);
            if (pu != pv) {
                union(parent, rank, pu, pv);
                MainApp.mstResult.add(new int[]{e[0], e[1], e[2]});
                tableModel.addRow(new Object[]{"PS-" + e[0], "PS-" + e[1], e[2], "Kruskal's"});
                total += e[2];
            }
        }

        long end = System.nanoTime();
        MainApp.kruskalsTime = end - start;
        MainApp.totalMSTCost = total;
        totalCostLabel.setText("MST Cost: " + total);
        renderGraph();
        showInfo("Kruskal's Algorithm complete!\nMST Cost: " + total + " MW·km\nTime: " + (MainApp.kruskalsTime/1000) + " µs");
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]);
        return parent[x];
    }

    private void union(int[] parent, int[] rank, int x, int y) {
        if (rank[x] < rank[y]) { int t = x; x = y; y = t; }
        parent[y] = x;
        if (rank[x] == rank[y]) rank[x]++;
    }

    // ── Sample data ───────────────────────────────────────────────────────────

    private void loadSample() {
        clearAll();
        nodeCountField.setText("6");
        MainApp.nodeCount = 6;
        int[][] sample = {
            {0,1,4},{0,2,3},{1,2,1},{1,3,2},{2,4,6},
            {3,4,5},{3,5,7},{4,5,8},{1,4,3},{2,3,4}
        };
        for (int[] e : sample) MainApp.edges.add(e);
        edgeCountLabel.setText("Lines: " + MainApp.edges.size());
        renderGraph();
        showInfo("Sample grid loaded!\n6 Power Stations, 10 Transmission Lines.\nNow run Prim's or Kruskal's to compute MST.");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean validateInputs() {
        if (MainApp.nodeCount == 0 || MainApp.edges.isEmpty()) {
            showError("Please add power stations and at least one transmission line first.");
            return false;
        }
        return true;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Algorithm Result", JOptionPane.INFORMATION_MESSAGE);
    }

    private void styleTable(JTable t) {
        t.setBackground(MainApp.PANEL_COLOR);
        t.setForeground(MainApp.TEXT_COLOR);
        t.setFont(MainApp.FONT_BODY);
        t.setRowHeight(36);
        t.setGridColor(MainApp.BORDER_COLOR);
        t.setSelectionBackground(new Color(0x1D3461));
        t.setSelectionForeground(MainApp.TEXT_COLOR);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.getTableHeader().setBackground(new Color(0x0D1B2E));
        t.getTableHeader().setForeground(MainApp.MUTED_TEXT);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, MainApp.BORDER_COLOR));
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                c.setBackground(sel ? new Color(0x1D3461)
                    : (row % 2 == 0 ? MainApp.PANEL_COLOR : new Color(0x172032)));
                c.setForeground(col == 3 ? MainApp.HIGHLIGHT : MainApp.TEXT_COLOR);
                ((JLabel)c).setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        });
    }

    private void addSectionHeader(JPanel p, String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(new Color(0x475569));
        l.setAlignmentX(LEFT_ALIGNMENT);
        p.add(l);
    }

    private JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(MainApp.MUTED_TEXT);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JTextField styledField(String placeholder) {
        JTextField tf = new JTextField(placeholder);
        tf.setFont(MainApp.FONT_BODY);
        tf.setBackground(new Color(0x0F172A));
        tf.setForeground(MainApp.TEXT_COLOR);
        tf.setCaretColor(MainApp.HIGHLIGHT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return tf;
    }
}