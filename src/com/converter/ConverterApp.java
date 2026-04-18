package com.converter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConverterApp extends JFrame {

    private JTextField inputField;
    private JLabel resultLabel;
    private JRadioButton binToDecRadio;
    private JRadioButton decToBinRadio;
    private JButton convertButton;
    private JButton clearButton;
    
    // Animation elements
    private List<FloatingParticle> particles;
    private Timer animationTimer;

    public ConverterApp() {
        setTitle("Number System Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Fullscreen friendly setup
        setMinimumSize(new Dimension(600, 500));
        setSize(700, 550);
        setLocationRelativeTo(null);
        // Allow resizing so it can be full-screened easily
        setResizable(true);
        
        // Initialize animation particles
        particles = new ArrayList<>();
        for (int i = 0; i < 75; i++) {
            particles.add(new FloatingParticle());
        }

        // Use a more stylish font for the title and increase the size
        Font titleFont = new Font("Century Gothic", Font.BOLD | Font.ITALIC, 46);
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 18);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 18);

        // 1. Animated Gradient Background Panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Draw Gradient Background
                Color color1 = new Color(15, 23, 42); // Very dark slate
                Color color2 = new Color(30, 41, 59); // Slightly lighter slate
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                
                // Draw Floating Particles
                for (FloatingParticle p : particles) {
                    p.draw(g2d, w, h);
                }
            }
        };
        // Use BorderLayout so we can center the content block dynamically
        mainPanel.setLayout(new BorderLayout());

        // Create a wrapper to hold the content perfectly in the center regardless of screen size
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        // The actual content panel that holds our UI vertically
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- Title ---
        JLabel titleLabel = new JLabel("Number Converter");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // --- Radio Buttons ---
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 0));
        radioPanel.setOpaque(false);
        
        binToDecRadio = createCustomRadioButton("Binary to Decimal", true, mainFont);
        decToBinRadio = createCustomRadioButton("Decimal to Binary", false, mainFont);

        ButtonGroup group = new ButtonGroup();
        group.add(binToDecRadio);
        group.add(decToBinRadio);
        
        radioPanel.add(binToDecRadio);
        radioPanel.add(decToBinRadio);
        contentPanel.add(radioPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- Input Field ---
        JPanel inputContainer = new JPanel(new BorderLayout());
        inputContainer.setOpaque(false);
        inputContainer.setMaximumSize(new Dimension(450, 55)); // Slightly larger input

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        inputField.setBackground(new Color(30, 41, 59, 200)); // Semi-transparent dark
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(56, 189, 248), 2, true), // Light Blue border
            new EmptyBorder(10, 20, 10, 20)
        ));
        inputContainer.add(inputField, BorderLayout.CENTER);
        contentPanel.add(inputContainer);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // --- Buttons Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setOpaque(false);

        convertButton = new RoundedButton("Convert", new Color(16, 185, 129), new Color(5, 150, 105)); // Emerald
        convertButton.setFont(boldFont);
        convertButton.setPreferredSize(new Dimension(160, 55));
        
        clearButton = new RoundedButton("Clear", new Color(239, 68, 68), new Color(220, 38, 38)); // Rose Red
        clearButton.setFont(boldFont);
        clearButton.setPreferredSize(new Dimension(160, 55));

        buttonPanel.add(convertButton);
        buttonPanel.add(clearButton);
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 45)));

        // --- Result Label ---
        resultLabel = new JLabel("Result will appear here");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        resultLabel.setForeground(new Color(148, 163, 184)); // Slate-400
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(resultLabel);

        // Add the content to the centering wrapper, then add to main panel
        centerWrapper.add(contentPanel);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        
        // --- Footer ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        footerPanel.setOpaque(false);
        JLabel footerLabel = new JLabel("Made with love by Aarushi, Ananya, Sushant, Mayank <3");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        footerLabel.setForeground(new Color(255, 105, 180)); // Hot Pink
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);

        // --- Setup Animation Timer ---
        animationTimer = new Timer(30, e -> {
            int w = mainPanel.getWidth();
            int h = mainPanel.getHeight();
            for (FloatingParticle p : particles) {
                p.update(w, h);
            }
            mainPanel.repaint();
        });
        animationTimer.start();

        // --- Actions ---
        convertButton.addActionListener(e -> performConversion());
        clearButton.addActionListener(e -> {
            inputField.setText("");
            resultLabel.setText("Result will appear here");
            resultLabel.setForeground(new Color(148, 163, 184));
            inputField.requestFocus();
        });
        
        // Allow pressing Enter key to trigger conversion
        inputField.addActionListener(e -> performConversion());
    }

    private JRadioButton createCustomRadioButton(String text, boolean selected, Font font) {
        JRadioButton radio = new JRadioButton(text, selected);
        radio.setFont(font);
        radio.setForeground(Color.WHITE);
        radio.setOpaque(false);
        radio.setFocusPainted(false);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return radio;
    }

    private void performConversion() {
        String input = inputField.getText().trim();
        if (input.isEmpty()) {
            showError("Please enter a number to convert.");
            return;
        }

        try {
            if (binToDecRadio.isSelected()) {
                String result = ConverterLogic.binaryToDecimal(input);
                showResult("Decimal: " + result);
            } else {
                String result = ConverterLogic.decimalToBinary(input);
                showResult("Binary: " + result);
            }
        } catch (NumberFormatException ex) {
            showError(ex.getMessage());
        } catch (Exception ex) {
            showError("Error: Value is likely out of supported bounds.");
        }
    }
    
    private void showResult(String text) {
        resultLabel.setText(text);
        resultLabel.setForeground(new Color(52, 211, 153)); // Emerald-400
    }
    
    private void showError(String text) {
        resultLabel.setText("Error occurred!");
        resultLabel.setForeground(new Color(248, 113, 113)); // Red-400
        
        UIManager.put("OptionPane.background", new Color(30, 41, 59));
        UIManager.put("Panel.background", new Color(30, 41, 59));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        
        JOptionPane.showMessageDialog(this, text, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new ConverterApp().setVisible(true);
        });
    }

    /** 
     * Internal class representing a single matrix-style floating character ('0' or '1').
     */
    class FloatingParticle {
        private float x, y, speed, alpha;
        private char text;
        private int size;
        private Random random = new Random();

        public FloatingParticle() {
            // Give them random initial values to distribute them across the screen immediately
            reset(true);
        }

        public void reset(boolean randomY) {
            // We use huge numbers for max width/height to ensure it spans full screen on setup
            x = random.nextInt(2500); 
            y = randomY ? random.nextInt(1500) : 1500 + random.nextInt(100);
            speed = 0.5f + random.nextFloat() * 1.5f; // Floating speed
            alpha = 0.05f + random.nextFloat() * 0.15f; // Very subtle opacity so it's not distracting
            size = 14 + random.nextInt(24);
            text = random.nextBoolean() ? '1' : '0';
        }

        public void update(int screenWidth, int screenHeight) {
            y -= speed; // Float upwards
            // If it floats off the top of the screen, reset it to the bottom
            if (y < -50) {
                x = random.nextInt(Math.max(screenWidth, 100));
                y = screenHeight + random.nextInt(50);
                speed = 0.5f + random.nextFloat() * 1.5f;
            }
        }

        public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
            // Only draw if within bounds to save performance
            if (x > screenWidth || y > screenHeight + 100 || y < -50) return;
            
            g2d.setFont(new Font("Consolas", Font.BOLD, size));
            // Color is light blue / cyan matching the theme
            g2d.setColor(new Color(56 / 255f, 189 / 255f, 248 / 255f, alpha));
            g2d.drawString(String.valueOf(text), x, y);
        }
    }

    /** 
     * Custom Rounded Button with Hover color states.
     */
    class RoundedButton extends JButton {
        private Color normalColor, hoverColor;
        private boolean isHovered = false;

        public RoundedButton(String text, Color normalColor, Color hoverColor) {
            super(text);
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;
            
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(hoverColor.darker());
            } else if (isHovered) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(normalColor);
            }
            
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25));
            
            g2.setColor(getForeground());
            FontMetrics metrics = g2.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(getText())) / 2;
            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            g2.drawString(getText(), x, y);
            
            g2.dispose();
        }
    }
}
