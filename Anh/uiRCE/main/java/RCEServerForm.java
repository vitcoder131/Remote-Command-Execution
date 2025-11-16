import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class RCEServerForm extends JFrame {

    private Font getAppFont(float size, int style) {
        return new Font("Verdana", style, (int) size);
    }

    private final Color MAIN_BACKGROUND_COLOR = new Color(210, 210, 240); 
    private final Color BORDER_COLOR = new Color(180, 180, 200);

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(getAppFont(14, Font.PLAIN));
        area.setBackground(Color.WHITE);
        return area;
    }

    // Hàm tạo JScrollPane có TitledBorder
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
    
    // Lớp RoundedButton (Giữ nguyên)
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


    // --- HÀM TẠO FORM SERVER ---

    public RCEServerForm() {
        setTitle("RCE Server"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 650);  
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(MAIN_BACKGROUND_COLOR);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);

        final String ICON_PATH = "/images/hehenho.gif"; // Hoặc icon server khác

        ImageIcon serverIcon = null;
        try {
            java.net.URL imageUrl = getClass().getResource(ICON_PATH); 
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image originalImage = originalIcon.getImage();
                
                // Scale ảnh bằng BufferedImage
                int targetSize = 32;
                BufferedImage bufferedImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = bufferedImage.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(originalImage, 0, 0, targetSize, targetSize, null);
                g2.dispose();

                serverIcon = new ImageIcon(bufferedImage);
            }
        } catch (Exception e) {
            System.err.println("Lỗi tải Icon Server: " + e.getMessage());
        }

        if (serverIcon != null && serverIcon.getIconWidth() > 0) {
            JLabel iconLabel = new JLabel(serverIcon);
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); 
            topPanel.add(iconLabel); 
        }

        JLabel titleLabel = new JLabel(" RCE Server");
        titleLabel.setFont(getAppFont(24, Font.BOLD));
        topPanel.add(titleLabel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ===== KHU VỰC TRUNG TÂM (TEXT AREA DUY NHẤT) =====
        JTextArea resultsArea = createTextArea();
        JScrollPane resultsScroll = new JScrollPane(resultsArea);
        resultsScroll.setBorder(new LineBorder(BORDER_COLOR, 1)); // Chỉ dùng LineBorder đơn giản

        mainPanel.add(resultsScroll, BorderLayout.CENTER);


        // ===== KHU VỰC DƯỚI (NHẬP LỆNH VÀ CÁC NÚT) =====
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.setOpaque(false);
        JPanel commandPanel = new JPanel(new BorderLayout(10, 0));
        commandPanel.setOpaque(false);

        JTextField commandField = new JTextField();
        commandField.setFont(getAppFont(14, Font.PLAIN));
        commandField.setText("Type a command...");
        
        RoundedButton sendButton = new RoundedButton("Send", Color.WHITE, Color.BLACK, new Color(50, 50, 50));
        
        commandPanel.add(commandField, BorderLayout.CENTER);
        commandPanel.add(sendButton, BorderLayout.EAST);
        bottomPanel.add(commandPanel, BorderLayout.NORTH);

        // 2. Nút Command History (SOUTH của bottomPanel)
        JPanel historyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        historyPanel.setOpaque(false);
        
        RoundedButton historyButton = new RoundedButton("See Command History", Color.WHITE, Color.BLACK, new Color(50, 50, 50));
        historyPanel.add(historyButton);

        bottomPanel.add(historyPanel, BorderLayout.CENTER); // Đặt ở CENTER để nó nằm dưới commandPanel

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        // Đặt LookAndFeel để có giao diện đẹp hơn (như RCEClientForm)
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new RCEServerForm().setVisible(true));
    }
}