import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class DashboardPage extends JPanel {

    private MainApp app;
    private JLabel stationsVal, linesVal, costVal, statusVal;

    public DashboardPage(MainApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(MainApp.BG_COLOR);
        buildUI();
    }

    private void buildUI() {
        add(MainApp.buildSidebar(app, "DASHBOARD"), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MainApp.BG_COLOR);

        // Top bar
        p.add(buildTopBar(), BorderLayout.NORTH);

        // Scrollable main area
        JPanel main = new JPanel();
        main.setBackground(MainApp.BG_COLOR);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Section: Summary Cards
        JLabel sec1 = sectionLabel("System Overview");
        main.add(sec1);
        main.add(Box.createVerticalStrut(14));
        main.add(buildCardsRow());
        main.add(Box.createVerticalStrut(28));

        // Section: Quick Actions
        JLabel sec2 = sectionLabel("Quick Actions");
        main.add(sec2);
        main.add(Box.createVerticalStrut(14));
        main.add(buildQuickActions());
        main.add(Box.createVerticalStrut(28));

        // Section: System Info
        JLabel sec3 = sectionLabel("System Information");
        main.add(sec3);
        main.add(Box.createVerticalStrut(14));
        main.add(buildInfoPanel());

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

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(MainApp.TEXT_COLOR);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        right.setOpaque(false);
        JLabel dot = new JLabel("● LIVE");
        dot.setFont(new Font("Segoe UI", Font.BOLD, 11));
        dot.setForeground(MainApp.SUCCESS);
        JLabel time = new JLabel("Smart Grid Control Center");
        time.setFont(MainApp.FONT_SMALL);
        time.setForeground(MainApp.MUTED_TEXT);
        right.add(dot);
        right.add(time);

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildCardsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        // We'll store refs to the value labels for refresh()
        JPanel c1 = buildCard("Total Power Stations", "0", "⚡", MainApp.HIGHLIGHT, true);
        JPanel c2 = buildCard("Transmission Lines", "0", "⬡", new Color(0xA78BFA), false);
        JPanel c3 = buildCard("Minimum Cost (MST)", "0", "◎", MainApp.SUCCESS, false);
        JPanel c4 = buildStatusCard();

        row.add(c1);
        row.add(c2);
        row.add(c3);
        row.add(c4);
        return row;
    }

    private JPanel buildCard(String title, String val, String icon, Color accent, boolean primary) {
        JPanel card = new JPanel(new BorderLayout(10, 4));
        card.setBackground(MainApp.PANEL_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent.darker(), 1),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI", Font.BOLD, 32));
        ico.setForeground(accent);
        ico.setVerticalAlignment(SwingConstants.CENTER);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(MainApp.FONT_SMALL);
        titleLbl.setForeground(MainApp.MUTED_TEXT);

        JLabel valLbl = new JLabel(val);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valLbl.setForeground(MainApp.TEXT_COLOR);

        // Tag these labels by name so refresh() can find them
        if (title.contains("Stations"))    valLbl.setName("STATIONS");
        else if (title.contains("Lines"))  valLbl.setName("LINES");
        else if (title.contains("Cost"))   valLbl.setName("COST");

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 4));
        text.setOpaque(false);
        text.add(titleLbl);
        text.add(valLbl);

        card.add(ico, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildStatusCard() {
        JPanel card = new JPanel(new BorderLayout(10, 4));
        card.setBackground(MainApp.PANEL_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainApp.SUCCESS.darker(), 1),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JLabel ico = new JLabel("◑");
        ico.setFont(new Font("Segoe UI", Font.BOLD, 32));
        ico.setForeground(MainApp.SUCCESS);

        JLabel titleLbl = new JLabel("System Status");
        titleLbl.setFont(MainApp.FONT_SMALL);
        titleLbl.setForeground(MainApp.MUTED_TEXT);

        statusVal = new JLabel("Optimized / Active");
        statusVal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statusVal.setForeground(MainApp.SUCCESS);

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 4));
        text.setOpaque(false);
        text.add(titleLbl);
        text.add(statusVal);

        card.add(ico, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildQuickActions() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        String[][] actions = {
            {"⚡ Add Transmission Line", "ENERGY"},
            {"⬡ Run MST Algorithm",      "ENERGY"},
            {"≡ Compare Algorithms",     "ALGO"},
            {"◑ View Statistics",        "STATS"}
        };
        Color[] colors = {MainApp.BUTTON_COLOR, new Color(0x059669), new Color(0x7C3AED), new Color(0xD97706)};

        for (int i = 0; i < actions.length; i++) {
            final String target = actions[i][1];
            JButton btn = MainApp.styledButton(actions[i][0], colors[i]);
            btn.addActionListener(e -> app.showPage(target));
            row.add(btn);
        }
        return row;
    }

    private JPanel buildInfoPanel() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        String[][] info = {
            {"Algorithm Type", "Minimum Spanning Tree"},
            {"Supported Algorithms", "Prim's + Kruskal's"},
            {"Graph Type", "Weighted Undirected"},
            {"Optimization Goal", "Minimum Total Cost"},
            {"Application Domain", "Power Grid Networks"},
            {"Time Complexity", "O(E log V)"}
        };

        for (String[] item : info) {
            JPanel cell = new JPanel(new BorderLayout(4, 4));
            cell.setBackground(MainApp.PANEL_COLOR);
            cell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainApp.BORDER_COLOR),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
            JLabel lbl = new JLabel(item[0]);
            lbl.setFont(MainApp.FONT_SMALL);
            lbl.setForeground(MainApp.MUTED_TEXT);
            JLabel val = new JLabel(item[1]);
            val.setFont(new Font("Segoe UI", Font.BOLD, 13));
            val.setForeground(MainApp.TEXT_COLOR);
            cell.add(lbl, BorderLayout.NORTH);
            cell.add(val, BorderLayout.CENTER);
            grid.add(cell);
        }
        return grid;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(MainApp.MUTED_TEXT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return l;
    }

    public void refresh() {
        // Walk the component tree to update card values
        updateCardLabels(this);
    }

    private void updateCardLabels(Container c) {
        for (Component comp : c.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if ("STATIONS".equals(lbl.getName()))
                    lbl.setText(String.valueOf(MainApp.nodeCount));
                else if ("LINES".equals(lbl.getName()))
                    lbl.setText(String.valueOf(MainApp.edges.size()));
                else if ("COST".equals(lbl.getName()))
                    lbl.setText(MainApp.totalMSTCost > 0 ? String.valueOf(MainApp.totalMSTCost) : "—");
            }
            if (comp instanceof Container)
                updateCardLabels((Container) comp);
        }
    }
}