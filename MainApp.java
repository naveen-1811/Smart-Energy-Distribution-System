import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {

    public static final Color BG_COLOR       = new Color(0x0F172A);
    public static final Color PANEL_COLOR    = new Color(0x1E293B);
    public static final Color BUTTON_COLOR   = new Color(0x2563EB);
    public static final Color HIGHLIGHT      = new Color(0x38BDF8);
    public static final Color ALERT          = new Color(0xEF4444);
    public static final Color SUCCESS        = new Color(0x22C55E);
    public static final Color TEXT_COLOR     = new Color(0xF1F5F9);
    public static final Color CARD_COLOR     = new Color(0x0F2744);
    public static final Color BORDER_COLOR   = new Color(0x334155);
    public static final Color MUTED_TEXT     = new Color(0x94A3B8);

    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO   = new Font("Consolas", Font.PLAIN, 12);

    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Shared data store
    public static java.util.List<int[]> edges = new java.util.ArrayList<>();
    public static int nodeCount = 0;
    public static java.util.List<int[]> mstResult = new java.util.ArrayList<>();
    public static int totalMSTCost = 0;
    public static long primsTime = 0;
    public static long kruskalsTime = 0;

    public MainApp() {
        setTitle("Smart Energy Distribution System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG_COLOR);

        LandingPage landing = new LandingPage(this);
        LoginPage login = new LoginPage(this);
        DashboardPage dashboard = new DashboardPage(this);
        SmartEnergyDistributionPage energyDist = new SmartEnergyDistributionPage(this);
        GridAnalysisPage gridAnalysis = new GridAnalysisPage(this);
        AlgorithmComparisonPage algoComp = new AlgorithmComparisonPage(this);
        StatisticsPage statistics = new StatisticsPage(this);
        AboutPage about = new AboutPage(this);

        cardPanel.add(landing, "LANDING");
        cardPanel.add(login, "LOGIN");
        cardPanel.add(dashboard, "DASHBOARD");
        cardPanel.add(energyDist, "ENERGY");
        cardPanel.add(gridAnalysis, "GRID");
        cardPanel.add(algoComp, "ALGO");
        cardPanel.add(statistics, "STATS");
        cardPanel.add(about, "ABOUT");

        add(cardPanel);
        showPage("LANDING");
        setVisible(true);
    }

    public void showPage(String name) {
        cardLayout.show(cardPanel, name);
        // Refresh pages that depend on shared data
        for (Component c : cardPanel.getComponents()) {
            if (c.isVisible()) {
                if (c instanceof GridAnalysisPage)    ((GridAnalysisPage) c).refresh();
                if (c instanceof AlgorithmComparisonPage) ((AlgorithmComparisonPage) c).refresh();
                if (c instanceof StatisticsPage)      ((StatisticsPage) c).refresh();
                if (c instanceof DashboardPage)       ((DashboardPage) c).refresh();
            }
        }
    }

    // ── Shared navigation helper used by all sidebar pages ──────────────────
    public static JPanel buildSidebar(MainApp app, String activePage) {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(0x0D1B2E));
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        // Logo area
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(new Color(0x0D1B2E));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(24, 16, 20, 16));
        JLabel logo = new JLabel("⚡ SEDS");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(HIGHLIGHT);
        JLabel logoSub = new JLabel("Smart Energy System");
        logoSub.setFont(FONT_SMALL);
        logoSub.setForeground(MUTED_TEXT);
        logoPanel.add(logo, BorderLayout.NORTH);
        logoPanel.add(logoSub, BorderLayout.SOUTH);
        sidebar.add(logoPanel, BorderLayout.NORTH);

        // Nav items
        String[][] items = {
            {"DASHBOARD", "⊞", "Dashboard"},
            {"ENERGY",    "⚡", "Energy Distribution"},
            {"GRID",      "⬡", "Grid Analysis"},
            {"ALGO",      "≡", "Algorithm Comparison"},
            {"STATS",     "◑", "Statistics"},
            {"ABOUT",     "ℹ", "About Project"},
        };

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(0x0D1B2E));
        navPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        for (String[] item : items) {
            boolean active = item[0].equals(activePage);
            JPanel navItem = createNavItem(item[1], item[2], active, () -> app.showPage(item[0]));
            navPanel.add(navItem);
        }
        sidebar.add(navPanel, BorderLayout.CENTER);

        // Logout button
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(new Color(0x0D1B2E));
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 12, 20, 12));
        JButton logout = createSidebarButton("⏻  Logout");
        logout.setForeground(ALERT);
        logout.addActionListener(e -> {
            MainApp.edges.clear();
            MainApp.mstResult.clear();
            MainApp.nodeCount = 0;
            MainApp.totalMSTCost = 0;
            app.showPage("LANDING");
        });
        bottom.add(logout);
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private static JPanel createNavItem(String icon, String label, boolean active, Runnable action) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.setBackground(active ? new Color(0x1D3461) : new Color(0x0D1B2E));
        item.setBorder(active
            ? BorderFactory.createMatteBorder(0, 3, 0, 0, HIGHLIGHT)
            : BorderFactory.createEmptyBorder(0, 3, 0, 0));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ico.setForeground(active ? HIGHLIGHT : MUTED_TEXT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(active ? new Font("Segoe UI", Font.BOLD, 13) : FONT_BODY);
        lbl.setForeground(active ? TEXT_COLOR : MUTED_TEXT);

        item.add(ico);
        item.add(lbl);

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { action.run(); }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!active) item.setBackground(new Color(0x162032));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!active) item.setBackground(new Color(0x0D1B2E));
            }
        });
        return item;
    }

    private static JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY);
        btn.setBackground(new Color(0x1E293B));
        btn.setForeground(MUTED_TEXT);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Shared styled button factory ──────────────────────────────────────────
    public static JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Shared card factory ───────────────────────────────────────────────────
    public static JPanel dashCard(String title, String value, String icon, Color accent) {
        JPanel card = new JPanel(new BorderLayout(8, 6));
        card.setBackground(PANEL_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent.darker(), 1),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI", Font.BOLD, 28));
        ico.setForeground(accent);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(FONT_SMALL);
        titleLbl.setForeground(MUTED_TEXT);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLbl.setForeground(TEXT_COLOR);

        JPanel text = new JPanel(new GridLayout(2, 1, 2, 2));
        text.setOpaque(false);
        text.add(titleLbl);
        text.add(valLbl);

        card.add(ico, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new MainApp();
        });
    }
}