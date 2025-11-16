import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignInForm extends JFrame {

    private Font getAppFont(float size, int style) {
    return new Font("Verdana", style, (int) size);  
    }
 
    //bo tròn - Tăng độ tương phản của viền
    private Border createRoundedLineBorder() {
    return new LineBorder(new Color(150, 150, 170), 2, true); 
    }

    public SignInForm() {
        setTitle("Remote Command Execution - Team 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 650);  
        setLocationRelativeTo(null);
        setResizable(true);
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        add(mainPanel);
        
        // ==================== BÊN TRÁI (LEFT PANEL) ====================
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(218, 222, 245)); 
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(80, 50, 80, 50));
        leftPanel.add(Box.createVerticalGlue());
        
        JPanel textWrapper = new JPanel();
        textWrapper.setOpaque(false);
        textWrapper.setLayout(new BoxLayout(textWrapper, BoxLayout.Y_AXIS));
        textWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel teamLabel = new JLabel("Team 1");
        teamLabel.setFont(getAppFont(18, Font.PLAIN));
        teamLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("Remote Command Execution");
        titleLabel.setFont(getAppFont(24, Font.BOLD));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        textWrapper.add(teamLabel);
        textWrapper.add(Box.createVerticalStrut(10));
        textWrapper.add(titleLabel);

        //insert ảnh
        JLabel imageLabel;
        try {
            java.net.URL imageUrl = getClass().getResource("/images/hehenho.gif");
            
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                imageLabel = new JLabel(icon);
            } else {
                System.err.println("Lỗi: Không tìm thấy file ảnh. Vui lòng kiểm tra đường dẫn.");
                imageLabel = new JLabel("Image Not Found");
                imageLabel.setFont(getAppFont(14, Font.BOLD));
                imageLabel.setForeground(Color.RED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel = new JLabel("Error loading image");
        }

        // DỊCH CHUYỂN SANG TRÁI 20PX
        JPanel imageWrapper = new JPanel();
        imageWrapper.setOpaque(false);
        imageWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
        final int SHIFT_LEFT_AMOUNT = 20; 
        imageWrapper.setBorder(new EmptyBorder(0, SHIFT_LEFT_AMOUNT, 0, 0)); 
        imageWrapper.add(imageLabel);
        imageWrapper.setAlignmentX(Component.CENTER_ALIGNMENT); 

        leftPanel.add(textWrapper); 
        leftPanel.add(Box.createVerticalStrut(60)); 
        leftPanel.add(imageWrapper); 
        leftPanel.add(Box.createVerticalStrut(30)); 

        leftPanel.add(Box.createVerticalGlue());
        mainPanel.add(leftPanel);

        // ==================== BÊN PHẢI (RIGHT PANEL) ====================
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(70, 80, 70, 80));  
        rightPanel.setBackground(Color.WHITE);

        JLabel loginLabel = new JLabel("SIGN IN");
        loginLabel.setFont(getAppFont(22, Font.BOLD));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  

        // Áp dụng style và bo tròn cho Text Field
        JTextField fullnameField = new JTextField();
        styleRoundedField(fullnameField, "Full name");
        JTextField emailField = new JTextField();
        styleRoundedField(emailField, "Email");
        JTextField phoneField = new JTextField();
        styleRoundedField(phoneField, "Phone");
        JTextField usernameField = new JTextField();
        styleRoundedField(usernameField, "User name");
        
        //MK
        JPanel passwordPanel = createPasswordFieldPanel("Password");
        JPanel comfirmpasswordPanel = createPasswordFieldPanel(" Comfirm password");

        // Nút NEXT 
        Color nextBaseColor = Color.decode("#0B0B45");
        RoundedButton nextButton = new RoundedButton("NEXT", nextBaseColor, Color.WHITE);
        nextButton.setFont(getAppFont(16, Font.BOLD));
        nextButton.setMaximumSize(new Dimension(280, 48));  
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);  

        // Thêm các thành phần vào panel phải
        rightPanel.add(loginLabel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(fullnameField);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(emailField);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(phoneField);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(usernameField);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(comfirmpasswordPanel);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(passwordPanel);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(nextButton);
        rightPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(rightPanel);
    }
    
    //HÀM STYLE CHO TEXTFIELD
    private void styleRoundedField(JTextField field, String placeholder) {
        field.setFont(getAppFont(15, Font.PLAIN));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        field.setBorder(BorderFactory.createTitledBorder(
                createRoundedLineBorder(),  
                placeholder,
                TitledBorder.LEFT, TitledBorder.TOP,  
                getAppFont(13, Font.PLAIN),  
                new Color(100, 100, 100))); // Màu chữ placeholder tối hơn
        field.setBackground(Color.WHITE); // Nền trắng tinh
    }

    // ======= HÀM TẠO PANEL CHO PASSWORD FIELD CÓ ICON XEM/ẨN =======
private JPanel createPasswordFieldPanel(String placeholder) {
    JPanel fieldPanel = new JPanel(new BorderLayout());
    fieldPanel.setOpaque(false);
    fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); 

    // Thiết lập viền TitledBorder như các JTextField khác
    TitledBorder titledBorder = BorderFactory.createTitledBorder(
            createRoundedLineBorder(), 
            placeholder,
            TitledBorder.LEFT, TitledBorder.TOP, 
            getAppFont(13, Font.PLAIN), 
            new Color(100, 100, 100));
    fieldPanel.setBorder(titledBorder);

    // Trường mật khẩu
    JPasswordField inputField = new JPasswordField();
    inputField.setFont(getAppFont(15, Font.PLAIN));
    inputField.setBackground(Color.WHITE);
    inputField.setBorder(new EmptyBorder(10, 5, 5, 5)); 
    
    // Lưu trữ ký tự echo mặc định ('•')
    final char defaultEchoChar = inputField.getEchoChar();

    JLabel eyeIcon = new JLabel("\uD83D\uDDA5 ");
    eyeIcon.setFont(getAppFont(20, Font.PLAIN));
    eyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
    eyeIcon.setForeground(new Color(50, 50, 50)); 
    
    // Thêm chức năng xem/ẩn khi click
    eyeIcon.addMouseListener(new MouseAdapter() {
        boolean isShowing = false;
        @Override
        public void mouseClicked(MouseEvent e) {
            isShowing = !isShowing;
            if (isShowing) {
                inputField.setEchoChar((char) 0); // Hiển thị mật khẩu
                eyeIcon.setText(" \uD83D\uDDA5 "); // Đổi icon thành mắt mở
            } else {
                inputField.setEchoChar(defaultEchoChar); // Ẩn mật khẩu (dấu chấm)
                eyeIcon.setText(" \uD83D\uDDA5 "); // Đổi icon thành mắt đóng (hoặc giữ nguyên)
            }
        }
    });

    JPanel eyePanel = new JPanel(new BorderLayout());
    eyePanel.setOpaque(false);
    eyePanel.add(eyeIcon, BorderLayout.CENTER);
    
    fieldPanel.add(inputField, BorderLayout.CENTER);
    fieldPanel.add(eyePanel, BorderLayout.EAST);

    return fieldPanel;
}
    // ======= LỚP NỘI BỘ TẠO NÚT BO TRÒN VÀ HIỆU ỨNG HOVER (Không đổi) =======
    class RoundedButton extends JButton {
        private Color baseColor;
        private Color hoverColor;
        private Color pressedColor;
        private Color currentColor;
        private final int arcWidth = 30;  

        public RoundedButton(String text, Color base, Color fg) {
            super(text);
            this.baseColor = base;
            this.setForeground(fg);
            
            // Xử lý màu hover/pressed cho nút trắng (SIGN IN)
            if (base == Color.WHITE) {
                 this.hoverColor = new Color(240, 240, 240); // Hover nhạt
                 this.pressedColor = new Color(220, 220, 220); // Pressed đậm hơn
            } else {
                 this.hoverColor = base.darker().darker();  
                 this.pressedColor = base.brighter();
            }
            
            this.currentColor = baseColor;
            
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

            // VẼ VIỀN NẾU CÓ (Quan trọng cho nút SIGN IN kiểu Outlined)
            Border border = getBorder();
            if (border instanceof LineBorder) {
                LineBorder lb = (LineBorder) border;
                // Chỉ vẽ nếu màu nền không phải là màu cơ bản (tránh double border)
                if (currentColor.equals(baseColor)) { 
                    g2.setColor(lb.getLineColor());
                    g2.setStroke(new BasicStroke(lb.getThickness()));  
                    g2.draw(new RoundRectangle2D.Double(
                        lb.getThickness() / 2.0, lb.getThickness() / 2.0,  
                        getWidth() - lb.getThickness(), getHeight() - lb.getThickness(),  
                        arcWidth, arcWidth
                    ));
                }
            }

            super.paintComponent(g2);
            g2.dispose();
        }
    }


    public static void main(String[] args) {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); }  
        catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> new SignInForm().setVisible(true));
    }
}