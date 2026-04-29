import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class StatisticsPage extends JPanel {

    private MainApp app;
    private JLabel stationsLbl, connLbl, costSavedLbl, lossLbl, mstLbl, efficiencyLbl;

    public StatisticsPage(MainApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(MainApp.BG_COLOR);
        buildUI();
    }

    private void buildUI() {
        add(MainApp.buildSidebar(app, "STATS"), BorderLayout.WEST);
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

        main.add(buildKpiRow());
        main.add(Box.createVerticalStrut(24));
        main.add(buildSavingsPanel());
        main.add(Box.createVerticalStrut(24));
        main.add(buildProgressPanel());

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
        JLabel t = new JLabel("◑  Energy Statistics");
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));
        t.setForeground(MainApp.TEXT_COLOR);
        JButton refresh = MainApp.styledButton("↻  Refresh Stats", MainApp.BUTTON_COLOR);
        refresh.addActionListener(e -> refresh());
        bar.add(t, BorderLayout.WEST);
        bar.add(refresh, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildKpiRow() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        wrapper.add(sectionLabel("Key Performance Indicators"), BorderLayout.NORTH);

        JPanel row = new JPanel(new GridLayout(1, 3, 16, 0));
        row.setOpaque(false);

        stationsLbl = kpiLabel("0");
        connLbl     = kpiLabel("0");
        mstLbl      = kpiLabel("—");

        row.add(kpiCard("Power Stations Connected", stationsLbl, "⚡", MainApp.HIGHLIGHT));
        row.add(kpiCard("MST Connections (Edges)", connLbl, "⬡", new Color(0xA78BFA)));
        row.add(kpiCard("Minimum Cost Network", mstLbl, "◎", MainApp.SUCCESS));

        wrapper.add(row, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildSavingsPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        wrapper.add(sectionLabel("Cost Savings & Efficiency"), BorderLayout.NORTH);

        JPanel row = new JPanel(new GridLayout(1, 3, 16, 0));
        row.setOpaque(false);

        costSavedLbl = kpiLabel("—");
        lossLbl      = kpiLabel("—");
        efficiencyLbl = kpiLabel("—");

        row.add(kpiCard("Transmission Cost Saved", costSavedLbl, "💰", new Color(0xFBBF24)));
        row.add(kpiCard("Power Loss Reduction", lossLbl, "↓", new Color(0xF97316)));
        row.add(kpiCard("Grid Efficiency Score", efficiencyLbl, "★", MainApp.SUCCESS));

        wrapper.add(row, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildProgressPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setOpaque(false);
        wrapper.add(sectionLabel("Optimization Progress"), BorderLayout.NORTH);

        JPanel card = new JPanel();
        card.setBackground(MainApp.PANEL_COLOR);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)));

        card.add(progressRow("Grid Coverage",       getGridCoverage(), new Color(0x2563EB)));
        card.add(Box.createVerticalStrut(18));
        card.add(progressRow("Cost Optimization",   getCostOptPct(), new Color(0x059669)));
        card.add(Box.createVerticalStrut(18));
        card.add(progressRow("Power Efficiency",    getPowerEff(), new Color(0x7C3AED)));
        card.add(Box.createVerticalStrut(18));
        card.add(progressRow("Network Reliability", 92, new Color(0xD97706)));

        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel progressRow(String label, int pct, Color color) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(MainApp.TEXT_COLOR);
        lbl.setPreferredSize(new Dimension(180, 30));

        JPanel barBg = new JPanel(new BorderLayout());
        barBg.setBackground(new Color(0x0F172A));
        barBg.setBorder(BorderFactory.createLineBorder(MainApp.BORDER_COLOR));

        JPanel barFill = new JPanel(new BorderLayout());
        barFill.setBackground(color);
        int barW = Math.max(pct * 8, 0);
        barFill.setPreferredSize(new Dimension(barW, 28));

        JLabel pctLbl = new JLabel("  " + pct + "%");
        pctLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        pctLbl.setForeground(Color.WHITE);
        barFill.add(pctLbl, BorderLayout.WEST);
        barBg.add(barFill, BorderLayout.WEST);

        row.add(lbl, BorderLayout.WEST);
        row.add(barBg, BorderLayout.CENTER);
        return row;
    }

    private JPanel kpiCard(String title, JLabel valLbl, String icon, Color accent) {
        JPanel card = new JPanel(new BorderLayout(10, 4));
        card.setBackground(MainApp.PANEL_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent.darker()),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI", Font.BOLD, 28));
        ico.setForeground(accent);

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(MainApp.FONT_SMALL);
        tLbl.setForeground(MainApp.MUTED_TEXT);

        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valLbl.setForeground(MainApp.TEXT_COLOR);

        JPanel t = new JPanel(new GridLayout(2, 1, 0, 4));
        t.setOpaque(false);
        t.add(tLbl);
        t.add(valLbl);

        card.add(ico, BorderLayout.WEST);
        card.add(t, BorderLayout.CENTER);
        return card;
    }

    private JLabel kpiLabel(String v) {
        JLabel l = new JLabel(v);
        l.setFont(new Font("Segoe UI", Font.BOLD, 24));
        l.setForeground(MainApp.TEXT_COLOR);
        return l;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(MainApp.MUTED_TEXT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return l;
    }

    // ── Computed stats ────────────────────────────────────────────────────────

    private int getGridCoverage() {
        if (MainApp.nodeCount == 0 || MainApp.mstResult.isEmpty()) return 0;
        return Math.min(100, (int)(((double) MainApp.mstResult.size() / (MainApp.nodeCount - 1)) * 100));
    }

    private int getCostOptPct() {
        int total = MainApp.edges.stream().mapToInt(e -> e[2]).sum();
        if (total == 0 || MainApp.totalMSTCost == 0) return 0;
        return (int)(((double)(total - MainApp.totalMSTCost) / total) * 100);
    }

    private int getPowerEff() {
        if (MainApp.totalMSTCost == 0) return 0;
        return Math.min(95, 60 + getCostOptPct() / 3);
    }

    public void refresh() {
        int nodes = MainApp.nodeCount;
        int conns = MainApp.mstResult.size();
        int total = MainApp.edges.stream().mapToInt(e -> e[2]).sum();
        int mst   = MainApp.totalMSTCost;
        int saved = (mst > 0) ? (total - mst) : 0;
        double lossPct = (total > 0 && mst > 0)
            ? ((double) saved / total) * 100 * 0.35 : 0;
        int eff = getPowerEff();

        stationsLbl.setText(String.valueOf(nodes));
        connLbl.setText(String.valueOf(conns));
        mstLbl.setText(mst > 0 ? mst + " MW·km" : "—");
        costSavedLbl.setText(saved > 0 ? saved + " MW·km" : "—");
        lossLbl.setText(lossPct > 0 ? String.format("%.1f%%", lossPct) : "—");
        efficiencyLbl.setText(eff > 0 ? eff + "%" : "—");

        revalidate();
        repaint();
    }
}