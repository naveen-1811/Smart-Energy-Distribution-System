import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class AboutPage extends JPanel {

    private MainApp app;

    public AboutPage(MainApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(MainApp.BG_COLOR);
        buildUI();
    }

    private void buildUI() {
        add(MainApp.buildSidebar(app, "ABOUT"), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MainApp.BG_COLOR);
        p.add(buildTopBar(), BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setBackground(MainApp.BG_COLOR);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        // Hero
        main.add(buildHero());
        main.add(Box.createVerticalStrut(24));

        // Two column: Project info + Technologies
        JPanel cols = new JPanel(new GridLayout(1, 2, 20, 0));
        cols.setOpaque(false);
        cols.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        cols.add(buildProjectCard());
        cols.add(buildTechCard());
        main.add(cols);
        main.add(Box.createVerticalStrut(20));

        // Algorithms row
        JPanel algRow = new JPanel(new GridLayout(1, 2, 20, 0));
        algRow.setOpaque(false);
        algRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        algRow.add(buildAlgoCard("Prim's Algorithm", new Color(0x059669),
            "• Greedy vertex-based MST approach\n"
          + "• Starts from a source vertex\n"
          + "• Uses a min-heap priority queue\n"
          + "• O(E log V) time complexity\n"
          + "• Ideal for dense power grids\n"
          + "• Grows tree one vertex at a time"));
        algRow.add(buildAlgoCard("Kruskal's Algorithm", new Color(0x7C3AED),
            "• Greedy edge-based MST approach\n"
          + "• Sorts all edges by weight first\n"
          + "• Uses Union-Find (DSU) for cycles\n"
          + "• O(E log E) time complexity\n"
          + "• Ideal for sparse networks\n"
          + "• Processes cheapest edges first"));
        main.add(algRow);
        main.add(Box.createVerticalStrut(20));

        // Team card
        main.add(buildTeamCard());

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
        JLabel t = new JLabel("ℹ  About Project");
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));
        t.setForeground(MainApp.TEXT_COLOR);
        bar.add(t, BorderLayout.WEST);
        return bar;
    }

    private JPanel buildHero() {
        JPanel hero = new JPanel(new BorderLayout(20, 0));
        hero.setBackground(new Color(0x1D3461));
        hero.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainApp.HIGHLIGHT.darker()),
            BorderFactory.createEmptyBorder(28, 28, 28, 28)));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel icon = new JLabel("⚡");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 56));
        icon.setForeground(MainApp.HIGHLIGHT);

        JPanel text = new JPanel(new BorderLayout(0, 6));
        text.setOpaque(false);

        JLabel title = new JLabel("Smart Energy Distribution System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(MainApp.TEXT_COLOR);

        JLabel sub = new JLabel("Power Grid Optimization using Prim's and Kruskal's Minimum Spanning Tree Algorithms");
        sub.setFont(MainApp.FONT_BODY);
        sub.setForeground(MainApp.MUTED_TEXT);

        JPanel tags = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tags.setOpaque(false);
        for (String tag : new String[]{"Java", "Swing", "Graph Theory", "MST", "Real-Time"}) {
            JLabel t2 = new JLabel(tag);
            t2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            t2.setForeground(MainApp.HIGHLIGHT);
            t2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x38BDF8, true), 1),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)));
            tags.add(t2);
        }

        text.add(title, BorderLayout.NORTH);
        text.add(sub, BorderLayout.CENTER);
        text.add(tags, BorderLayout.SOUTH);
        hero.add(icon, BorderLayout.WEST);
        hero.add(text, BorderLayout.CENTER);
        return hero;
    }

    private JPanel buildProjectCard() {
        JPanel card = new JPanel(new BorderLayout(0, 14));
        card.setBackground(MainApp.PANEL_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel title = new JLabel("Project Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(MainApp.HIGHLIGHT);

        String[][] items = {
            {"Project Title",   "Smart Energy Distribution System"},
            {"Domain",          "Power Grid Optimization"},
            {"Problem",         "Minimize transmission cost & loss"},
            {"Approach",        "Minimum Spanning Tree (MST)"},
            {"Graph Model",     "Weighted Undirected Graph"},
            {"Input",           "Power stations + transmission lines"},
            {"Output",          "Optimal MST with minimum cost"},
            {"Platform",        "Java Desktop Application"},
        };

        JPanel grid = new JPanel(new GridLayout(items.length, 2, 8, 8));
        grid.setOpaque(false);
        for (String[] item : items) {
            JLabel k = new JLabel(item[0] + ":");
            k.setFont(new Font("Segoe UI", Font.BOLD, 12));
            k.setForeground(MainApp.MUTED_TEXT);
            JLabel v = new JLabel(item[1]);
            v.setFont(MainApp.FONT_BODY);
            v.setForeground(MainApp.TEXT_COLOR);
            grid.add(k);
            grid.add(v);
        }

        card.add(title, BorderLayout.NORTH);
        card.add(grid, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildTechCard() {
        JPanel card = new JPanel(new BorderLayout(0, 14));
        card.setBackground(MainApp.PANEL_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel title = new JLabel("Technologies Used");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(MainApp.HIGHLIGHT);

        String[][] techs = {
            {"☕", "Java SE",          "Core programming language"},
            {"🖥", "Java Swing",       "Desktop UI framework"},
            {"⬡", "Graph Theory",     "Weighted undirected graphs"},
            {"⚡", "Prim's Algorithm", "MST via priority queue"},
            {"≡", "Kruskal's Algo",   "MST via Union-Find DSU"},
            {"◎", "CardLayout",       "Multi-page navigation"},
            {"⊞", "JTable",           "Result display component"},
            {"○", "Java Collections", "List, PriorityQueue, Arrays"},
        };

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        for (String[] t : techs) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

            JLabel ico = new JLabel(t[0]);
            ico.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            ico.setForeground(MainApp.HIGHLIGHT);
            ico.setPreferredSize(new Dimension(24, 28));

            JLabel name = new JLabel(t[1]);
            name.setFont(new Font("Segoe UI", Font.BOLD, 12));
            name.setForeground(MainApp.TEXT_COLOR);
            name.setPreferredSize(new Dimension(140, 28));

            JLabel desc = new JLabel(t[2]);
            desc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            desc.setForeground(MainApp.MUTED_TEXT);

            row.add(ico, BorderLayout.WEST);
            row.add(name, BorderLayout.CENTER);
            row.add(desc, BorderLayout.EAST);

            list.add(row);
            list.add(Box.createVerticalStrut(4));
        }

        card.add(title, BorderLayout.NORTH);
        card.add(list, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildAlgoCard(String title, Color accent, String content) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(MainApp.PANEL_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent.darker()),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tLbl.setForeground(accent);

        JTextArea ta = new JTextArea(content);
        ta.setFont(MainApp.FONT_BODY);
        ta.setForeground(MainApp.TEXT_COLOR);
        ta.setBackground(new Color(0x0F172A));
        ta.setEditable(false);
        ta.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);

        card.add(tLbl, BorderLayout.NORTH);
        card.add(ta, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildTeamCard() {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(MainApp.PANEL_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xFBBF24).darker()),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel icon = new JLabel("👤");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 40));

        JPanel text = new JPanel(new GridLayout(3, 1, 0, 4));
        text.setOpaque(false);

        JLabel name = new JLabel("Developer / Project Team");
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        name.setForeground(MainApp.TEXT_COLOR);

        JLabel role = new JLabel("Smart Grid Research & Algorithm Implementation");
        role.setFont(MainApp.FONT_BODY);
        role.setForeground(MainApp.MUTED_TEXT);

        JLabel tech = new JLabel("Java · Swing · Graph Theory · MST Algorithms · Data Structures");
        tech.setFont(MainApp.FONT_SMALL);
        tech.setForeground(new Color(0xFBBF24));

        text.add(name);
        text.add(role);
        text.add(tech);

        card.add(icon, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }
}