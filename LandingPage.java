import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LandingPage extends JPanel {

    private MainApp app;

    public LandingPage(MainApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(MainApp.BG_COLOR);
        buildUI();
    }

    private void buildUI() {
        // Animated background grid panel
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dark base
                g2.setColor(MainApp.BG_COLOR);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Grid lines
                g2.setColor(new Color(0x1E293B));
                g2.setStroke(new BasicStroke(1f));
                for (int x = 0; x < getWidth(); x += 60)
                    g2.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 60)
                    g2.drawLine(0, y, getWidth(), y);

                // Glow circles
                float[][] glows = {{0.15f,0.25f,200},{0.8f,0.7f,250},{0.5f,0.1f,180}};
                Color[] cols = {new Color(0x2563EB,true), new Color(0x38BDF8,true), new Color(0x1D4ED8,true)};
                for (int i = 0; i < glows.length; i++) {
                    int cx = (int)(glows[i][0]*getWidth());
                    int cy = (int)(glows[i][1]*getHeight());
                    int r  = (int)glows[i][2];
                    RadialGradientPaint rg = new RadialGradientPaint(cx, cy, r,
                        new float[]{0f, 1f},
                        new Color[]{new Color(cols[i].getRed(), cols[i].getGreen(), cols[i].getBlue(), 60),
                                    new Color(cols[i].getRed(), cols[i].getGreen(), cols[i].getBlue(), 0)});
                    g2.setPaint(rg);
                    g2.fillOval(cx-r, cy-r, r*2, r*2);
                }
            }
        };
        bg.setLayout(new GridBagLayout());

        // Content panel
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Top badge
        JLabel badge = new JLabel("⚡  SMART GRID TECHNOLOGY");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(MainApp.HIGHLIGHT);
        badge.setAlignmentX(CENTER_ALIGNMENT);
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x38BDF8, true), 1),
            BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        badge.setOpaque(true);
        badge.setBackground(new Color(0x38BDF8, false));

        // Main title
        JLabel title = new JLabel("<html><div style='text-align:center;'>Smart Energy<br>Distribution System</div></html>");
        title.setFont(new Font("Segoe UI", Font.BOLD, 54));
        title.setForeground(MainApp.TEXT_COLOR);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Subtitle
        JLabel sub = new JLabel("<html><div style='text-align:center;'>"
            + "Power Grid Optimization using Prim's and Kruskal's Algorithms<br>"
            + "<span style='color:#38BDF8;'>Real-Time MST · Minimum Cost · Maximum Efficiency</span>"
            + "</div></html>");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sub.setForeground(MainApp.MUTED_TEXT);
        sub.setAlignmentX(CENTER_ALIGNMENT);
        sub.setHorizontalAlignment(SwingConstants.CENTER);

        // Divider line
        JSeparator sep = new JSeparator();
        sep.setForeground(MainApp.BORDER_COLOR);
        sep.setMaximumSize(new Dimension(500, 2));

        // Stats row
        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        statsRow.setOpaque(false);
        addStat(statsRow, "MST Algorithm", "Prim + Kruskal");
        addStatDivider(statsRow);
        addStat(statsRow, "Optimization", "Real-Time");
        addStatDivider(statsRow);
        addStat(statsRow, "Graph Theory", "Powered");

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        btnRow.setOpaque(false);

        JButton startBtn = MainApp.styledButton("⚡  Start System", MainApp.BUTTON_COLOR);
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        startBtn.setBorder(BorderFactory.createEmptyBorder(14, 32, 14, 32));
        startBtn.addMouseListener(hoverEffect(startBtn, MainApp.BUTTON_COLOR, new Color(0x1D4ED8)));
        startBtn.addActionListener(e -> app.showPage("LOGIN"));

        JButton aboutBtn = MainApp.styledButton("ℹ  About Project", new Color(0x1E293B));
        aboutBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        aboutBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(13, 32, 13, 32)));
        aboutBtn.addActionListener(e -> showAboutDialog());

        JButton exitBtn = MainApp.styledButton("✕  Exit", new Color(0x1E293B));
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        exitBtn.setForeground(MainApp.ALERT);
        exitBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x7F1D1D)),
            BorderFactory.createEmptyBorder(13, 32, 13, 32)));
        exitBtn.addActionListener(e -> System.exit(0));

        btnRow.add(startBtn);
        btnRow.add(aboutBtn);
        btnRow.add(exitBtn);

        // Version label
        JLabel version = new JLabel("v1.0.0  ·  Graph Theory Project  ·  Java Swing");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        version.setForeground(new Color(0x475569));
        version.setAlignmentX(CENTER_ALIGNMENT);

        content.add(Box.createVerticalStrut(20));
        content.add(badge);
        content.add(Box.createVerticalStrut(24));
        content.add(title);
        content.add(Box.createVerticalStrut(16));
        content.add(sub);
        content.add(Box.createVerticalStrut(28));
        content.add(sep);
        content.add(Box.createVerticalStrut(20));
        content.add(statsRow);
        content.add(Box.createVerticalStrut(36));
        content.add(btnRow);
        content.add(Box.createVerticalStrut(24));
        content.add(version);

        bg.add(content);
        add(bg, BorderLayout.CENTER);

        // Bottom status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        statusBar.setBackground(new Color(0x0D1B2E));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, MainApp.BORDER_COLOR));
        JLabel status = new JLabel("● System Ready");
        status.setFont(MainApp.FONT_SMALL);
        status.setForeground(MainApp.SUCCESS);
        JLabel java = new JLabel("Java Swing Application");
        java.setFont(MainApp.FONT_SMALL);
        java.setForeground(MainApp.MUTED_TEXT);
        statusBar.add(status);
        statusBar.add(new JSeparator(SwingConstants.VERTICAL));
        statusBar.add(java);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void addStat(JPanel p, String label, String value) {
        JPanel stat = new JPanel(new BorderLayout(2, 2));
        stat.setOpaque(false);
        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(MainApp.MUTED_TEXT);
        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("Segoe UI", Font.BOLD, 14));
        val.setForeground(MainApp.HIGHLIGHT);
        stat.add(lbl, BorderLayout.NORTH);
        stat.add(val, BorderLayout.CENTER);
        p.add(stat);
    }

    private void addStatDivider(JPanel p) {
        JLabel div = new JLabel("·");
        div.setFont(new Font("Segoe UI", Font.BOLD, 24));
        div.setForeground(MainApp.BORDER_COLOR);
        p.add(div);
    }

    private MouseAdapter hoverEffect(JButton btn, Color normal, Color hover) {
        return new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(normal); }
        };
    }

    private void showAboutDialog() {
        JDialog dlg = new JDialog((Frame)null, "About Project", true);
        dlg.setSize(480, 360);
        dlg.setLocationRelativeTo(this);
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(MainApp.PANEL_COLOR);
        p.setBorder(BorderFactory.createEmptyBorder(30, 32, 30, 32));
        JLabel t = new JLabel("Smart Energy Distribution System", SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));
        t.setForeground(MainApp.HIGHLIGHT);
        JTextArea ta = new JTextArea(
            "This project demonstrates real-time power grid optimization\n"
          + "using Minimum Spanning Tree algorithms.\n\n"
          + "Algorithms: Prim's Algorithm, Kruskal's Algorithm\n"
          + "Technology:  Java, Swing, Graph Theory\n"
          + "Purpose:     Minimize transmission cost and power loss\n"
          + "in electrical distribution networks.\n\n"
          + "Developer: Smart Grid Research Team");
        ta.setFont(MainApp.FONT_BODY);
        ta.setForeground(MainApp.TEXT_COLOR);
        ta.setBackground(MainApp.PANEL_COLOR);
        ta.setEditable(false);
        JButton close = MainApp.styledButton("Close", MainApp.BUTTON_COLOR);
        close.addActionListener(e -> dlg.dispose());
        JPanel btnP = new JPanel();
        btnP.setBackground(MainApp.PANEL_COLOR);
        btnP.add(close);
        p.add(t, BorderLayout.NORTH);
        p.add(ta, BorderLayout.CENTER);
        p.add(btnP, BorderLayout.SOUTH);
        dlg.add(p);
        dlg.setVisible(true);
    }
}