import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JPanel {

    private MainApp app;
    private JTextField userField;
    private JPasswordField passField;
    private JLabel msgLabel;

    private static final String VALID_USER = "admin";
    private static final String VALID_PASS = "1234";

    public LoginPage(MainApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(MainApp.BG_COLOR);
        buildUI();
    }

    private void buildUI() {
        // Split layout: left branding, right form
        JPanel left = buildLeftPanel();
        JPanel right = buildRightPanel();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(420);
        split.setDividerSize(0);
        split.setEnabled(false);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildLeftPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x0F172A),
                    getWidth(), getHeight(), new Color(0x1D3461));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // decorative circles
                g2.setColor(new Color(0x38BDF8, true));
                g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10, new float[]{6,6}, 0));
                g2.drawOval(40, 40, 200, 200);
                g2.drawOval(160, 300, 150, 150);
            }
        };
        p.setLayout(new GridBagLayout());
        p.setBackground(new Color(0x0F172A));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel icon = new JLabel("⚡");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        icon.setForeground(MainApp.HIGHLIGHT);
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("<html><div style='text-align:center;'>Smart Energy<br>Distribution System</div></html>");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(MainApp.TEXT_COLOR);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel sub = new JLabel("<html><div style='text-align:center;'>"
            + "Power Grid Optimization<br>using MST Algorithms</div></html>");
        sub.setFont(MainApp.FONT_BODY);
        sub.setForeground(MainApp.MUTED_TEXT);
        sub.setAlignmentX(CENTER_ALIGNMENT);
        sub.setHorizontalAlignment(SwingConstants.CENTER);

        JSeparator sep = new JSeparator();
        sep.setForeground(MainApp.BORDER_COLOR);
        sep.setMaximumSize(new Dimension(300, 2));
        sep.setAlignmentX(CENTER_ALIGNMENT);

        // Feature bullets
        String[] feats = {"⚡ Real-Time Grid Optimization", "⬡ Prim's & Kruskal's MST", "◑ Power Loss Reduction"};
        inner.add(icon);
        inner.add(Box.createVerticalStrut(20));
        inner.add(title);
        inner.add(Box.createVerticalStrut(12));
        inner.add(sub);
        inner.add(Box.createVerticalStrut(24));
        inner.add(sep);
        inner.add(Box.createVerticalStrut(24));
        for (String f : feats) {
            JLabel fl = new JLabel(f);
            fl.setFont(MainApp.FONT_BODY);
            fl.setForeground(MainApp.MUTED_TEXT);
            fl.setAlignmentX(CENTER_ALIGNMENT);
            inner.add(fl);
            inner.add(Box.createVerticalStrut(8));
        }

        p.add(inner);
        return p;
    }

    private JPanel buildRightPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(MainApp.BG_COLOR);

        JPanel card = new JPanel();
        card.setBackground(MainApp.PANEL_COLOR);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(40, 44, 40, 44)));

        JLabel loginTitle = new JLabel("Administrator Login");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        loginTitle.setForeground(MainApp.TEXT_COLOR);
        loginTitle.setAlignmentX(CENTER_ALIGNMENT);

        JLabel loginSub = new JLabel("Access the Smart Grid Control Panel");
        loginSub.setFont(MainApp.FONT_BODY);
        loginSub.setForeground(MainApp.MUTED_TEXT);
        loginSub.setAlignmentX(CENTER_ALIGNMENT);

        // Username
        JLabel userLbl = formLabel("Username");
        userField = styledField("admin");
        userField.setMaximumSize(new Dimension(360, 42));

        // Password
        JLabel passLbl = formLabel("Password");
        passField = new JPasswordField();
        passField.setFont(MainApp.FONT_BODY);
        passField.setBackground(new Color(0x0F172A));
        passField.setForeground(MainApp.TEXT_COLOR);
        passField.setCaretColor(MainApp.HIGHLIGHT);
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        passField.setMaximumSize(new Dimension(360, 42));

        // Hint
        JLabel hint = new JLabel("Default: admin / 1234");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(0x475569));
        hint.setAlignmentX(CENTER_ALIGNMENT);

        // Message
        msgLabel = new JLabel(" ");
        msgLabel.setFont(MainApp.FONT_SMALL);
        msgLabel.setForeground(MainApp.ALERT);
        msgLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Buttons
        JButton loginBtn = MainApp.styledButton("Sign In  →", MainApp.BUTTON_COLOR);
        loginBtn.setMaximumSize(new Dimension(360, 44));
        loginBtn.setAlignmentX(CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> doLogin());

        JButton resetBtn = MainApp.styledButton("Reset", new Color(0x1E293B));
        resetBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainApp.BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)));
        resetBtn.setMaximumSize(new Dimension(360, 44));
        resetBtn.setAlignmentX(CENTER_ALIGNMENT);
        resetBtn.addActionListener(e -> { userField.setText(""); passField.setText(""); msgLabel.setText(" "); });

        JButton backBtn = new JButton("← Back to Home");
        backBtn.setFont(MainApp.FONT_SMALL);
        backBtn.setForeground(MainApp.MUTED_TEXT);
        backBtn.setBackground(MainApp.PANEL_COLOR);
        backBtn.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        backBtn.setFocusPainted(false);
        backBtn.setAlignmentX(CENTER_ALIGNMENT);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> app.showPage("LANDING"));

        // Enter key triggers login
        ActionListener loginAction = e -> doLogin();
        passField.addActionListener(loginAction);
        userField.addActionListener(loginAction);

        card.add(loginTitle);
        card.add(Box.createVerticalStrut(6));
        card.add(loginSub);
        card.add(Box.createVerticalStrut(32));
        card.add(userLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(userField);
        card.add(Box.createVerticalStrut(18));
        card.add(passLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(passField);
        card.add(Box.createVerticalStrut(8));
        card.add(hint);
        card.add(Box.createVerticalStrut(6));
        card.add(msgLabel);
        card.add(Box.createVerticalStrut(24));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(resetBtn);
        card.add(Box.createVerticalStrut(20));
        card.add(backBtn);

        outer.add(card);
        return outer;
    }

    private void doLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword()).trim();
        if (user.equals(VALID_USER) && pass.equals(VALID_PASS)) {
            msgLabel.setForeground(MainApp.SUCCESS);
            msgLabel.setText("✓ Authentication successful. Loading...");
            Timer t = new Timer(600, e -> app.showPage("DASHBOARD"));
            t.setRepeats(false);
            t.start();
        } else {
            msgLabel.setForeground(MainApp.ALERT);
            msgLabel.setText("✗ Invalid credentials. Try admin / 1234");
        }
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
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        return tf;
    }
}