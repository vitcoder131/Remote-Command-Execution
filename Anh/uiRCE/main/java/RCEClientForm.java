import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RCEClientForm extends JFrame {

    private Font getAppFont(float size, int style) {
        return new Font("Verdana", style, (int) size);
    }

    private final Color MAIN_BACKGROUND_COLOR = new Color(210, 210, 240);  // Màu tím nhạt ban đầu
    private final Color BORDER_COLOR = new Color(180, 180, 200);

    public RCEClientForm() {
        setTitle("RCE Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(MAIN_BACKGROUND_COLOR);

        // ===== TIÊU ĐỀ =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(" RCE Client");
        titleLabel.setFont(getAppFont(24, Font.BOLD));
        topPanel.add(titleLabel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ===== TEXT AREAS =====
        JTextArea onlineUsersArea = createTextArea();
        JScrollPane onlineUsersScroll = createTitledScrollPane(onlineUsersArea, "Online users:", getAppFont(14, Font.BOLD));

        JTextArea resultsArea = createTextArea();
        JScrollPane resultsScroll = createTitledScrollPane(resultsArea, "Results:", getAppFont(14, Font.BOLD));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, onlineUsersScroll, resultsScroll);
        splitPane.setResizeWeight(0.35);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // ===== BUTTONS – CĂN PHẢI =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        buttonPanel.setOpaque(false);

        Color buttonBaseColor = Color.WHITE;
        Color buttonBorderColor = new Color(50, 50, 50);

        RoundedButton cpuButton = new RoundedButton("Get CPU Usage", buttonBaseColor, Color.BLACK, buttonBorderColor);
        RoundedButton diskButton = new RoundedButton("Get Disk Space", buttonBaseColor, Color.BLACK, buttonBorderColor);

        buttonPanel.add(cpuButton);
        buttonPanel.add(diskButton);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(getAppFont(14, Font.PLAIN));
        area.setBackground(Color.WHITE);
        return area;
    }

private JScrollPane createTitledScrollPane(Component view, String title, Font titleFont) {
    JScrollPane scroll = new JScrollPane(view);

    TitledBorder tb = BorderFactory.createTitledBorder(
            new LineBorder(BORDER_COLOR, 1),
            title,
            TitledBorder.LEFT, TitledBorder.TOP
    );
    tb.setTitleFont(new Font("Verdana", Font.BOLD, 14));

    scroll.setBorder(tb);
    return scroll;
}


    // ===== NÚT BO TRÒN =====
    class RoundedButton extends JButton {
        private Color baseColor;
        private Color hoverColor;
        private Color pressedColor;
        private Color currentColor;
        private Color borderColor;
        private final int arcWidth = 10;
        private final int borderThickness = 1;

        public RoundedButton(String text, Color base, Color fg, Color borderC) {
            super(text);
            this.baseColor = base;
            this.setForeground(fg);
            this.borderColor = borderC;

            this.hoverColor = new Color(240, 240, 240);
            this.pressedColor = new Color(220, 220, 220);

            this.currentColor = baseColor;
            this.setFont(new Font("Verdana", Font.BOLD, 13));


            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { currentColor = hoverColor; repaint(); }
                @Override public void mouseExited(MouseEvent e) { currentColor = baseColor; repaint(); }
                @Override public void mousePressed(MouseEvent e) { currentColor = pressedColor; repaint(); }
                @Override public void mouseReleased(MouseEvent e) {
                    currentColor = getBounds().contains(e.getPoint()) ? hoverColor : baseColor;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(currentColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arcWidth, arcWidth));

            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            g2.draw(new RoundRectangle2D.Double(
                    borderThickness / 2.0, borderThickness / 2.0,
                    getWidth() - borderThickness, getHeight() - borderThickness,
                    arcWidth, arcWidth
            ));

            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new RCEClientForm().setVisible(true));
    }
}
