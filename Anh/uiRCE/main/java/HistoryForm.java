import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class HistoryForm extends JFrame {

    // --- CÁC HÀM TRỢ GIÚP ---

    private Font getAppFont(float size, int style) {
        return new Font("Verdana", style, (int) size);
    }

    private final Color MAIN_BACKGROUND_COLOR = new Color(210, 210, 240); // Màu tím nhạt
    private final Color BORDER_COLOR = new Color(180, 180, 200);

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(getAppFont(14, Font.PLAIN));
        area.setBackground(Color.WHITE);
        return area;
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


    // --- HÀM TẠO FORM COMMAND HISTORY ---

    public HistoryForm() {
        setTitle("RCE Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(MAIN_BACKGROUND_COLOR);

        // ===== TIÊU ĐỀ VÀ ICON (RCE Server) =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);

        // LƯU Ý: Vẫn sử dụng file GIF và scale
        final String ICON_PATH = "/images/hehenho.gif"; 
        ImageIcon serverIcon = null;
        try {
            java.net.URL imageUrl = getClass().getResource(ICON_PATH); 
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image originalImage = originalIcon.getImage();
                
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

        // ===== KHU VỰC TRUNG TÂM (SEARCH VÀ HISTORY) =====
        
        // --- 1. Tạo thanh tìm kiếm ---
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Padding dưới

        JTextField searchField = new JTextField();
        searchField.setFont(getAppFont(14, Font.PLAIN));
        searchField.setText("Search Command History...");
        searchField.setForeground(Color.GRAY);

        // Đặt viền (Border) và FocusListener
        final Color ACTIVE_BORDER_COLOR = new Color(70, 70, 150); 
        final Color INACTIVE_BORDER_COLOR = new Color(180, 180, 200); 

        searchField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(INACTIVE_BORDER_COLOR, 1),
            new EmptyBorder(5, 5, 5, 5) 
        ));

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(ACTIVE_BORDER_COLOR, 2), 
                    new EmptyBorder(4, 4, 4, 4) 
                ));
                if (searchField.getText().equals("Search Command History...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(INACTIVE_BORDER_COLOR, 1),
                    new EmptyBorder(5, 5, 5, 5) 
                ));
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search Command History...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        
        searchPanel.add(searchField, BorderLayout.CENTER);


        // --- 2. Khu vực Command History ---
        JTextArea historyArea = createTextArea();
        historyArea.setEditable(true); 

        JScrollPane historyScroll = new JScrollPane(historyArea);
        historyScroll.setBorder(new LineBorder(BORDER_COLOR, 1)); 

        // Thêm tiêu đề Command History
        JPanel historyViewPanel = new JPanel(new BorderLayout());
        historyViewPanel.setOpaque(false);

        JLabel historyTitle = new JLabel("Command History:");
        historyTitle.setFont(getAppFont(16, Font.BOLD));
        historyTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); 

        historyViewPanel.add(historyTitle, BorderLayout.NORTH);
        historyViewPanel.add(historyScroll, BorderLayout.CENTER);


        // --- 3. Đặt tất cả vào Container CENTER ---
        JPanel centerContainer = new JPanel(new BorderLayout(0, 10)); // Khoảng cách 10px giữa Search và History
        centerContainer.setOpaque(false);

        centerContainer.add(searchPanel, BorderLayout.NORTH); // Thanh tìm kiếm ở trên
        centerContainer.add(historyViewPanel, BorderLayout.CENTER); // Lịch sử lệnh ở dưới

        mainPanel.add(centerContainer, BorderLayout.CENTER);


        // ===== KHU VỰC DƯỚI (4 NÚT CÙNG KÍCH THƯỚC VÀ CĂN GIỮA) =====
        
        // 1. Panel chứa 4 nút dùng GridLayout để buộc kích thước bằng nhau
        JPanel gridButtonPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        gridButtonPanel.setOpaque(false);

        Color buttonBaseColor = Color.WHITE;
        Color buttonBorderColor = new Color(50, 50, 50);

        // Khởi tạo các nút
        RoundedButton createButton = new RoundedButton("Create", buttonBaseColor, Color.BLACK, buttonBorderColor);
        RoundedButton editButton = new RoundedButton("Edit", buttonBaseColor, Color.BLACK, buttonBorderColor);
        RoundedButton deleteButton = new RoundedButton("Delete", buttonBaseColor, Color.BLACK, buttonBorderColor);
        RoundedButton reloadButton = new RoundedButton("Reload", buttonBaseColor, Color.BLACK, buttonBorderColor);
        
        // Đặt kích thước chiều cao cố định
        createButton.setPreferredSize(new Dimension(0, 40)); 
        
        gridButtonPanel.add(createButton);
        gridButtonPanel.add(editButton);
        gridButtonPanel.add(deleteButton);
        gridButtonPanel.add(reloadButton);

        // 2. Panel trung gian dùng FlowLayout để căn giữa gridButtonPanel
        JPanel southContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southContainer.setOpaque(false);
        
        // Giới hạn chiều rộng tối đa (Tùy chọn)
        gridButtonPanel.setPreferredSize(new Dimension(600, 40)); 

        southContainer.add(gridButtonPanel);

        mainPanel.add(southContainer, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new HistoryForm().setVisible(true));
    }
}