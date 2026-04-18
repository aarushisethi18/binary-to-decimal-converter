package com.converter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setSize(750, 700);
        setMinimumSize(new Dimension(650, 650));
        setLocationRelativeTo(null);

        // --- Custom Fonts ---
        Font titleFont = new Font("Century Gothic", Font.BOLD, 48);
        Font sectionFont = new Font("Segoe UI", Font.BOLD, 22);
        Font inputFont = new Font("Segoe UI", Font.BOLD, 26);
        Font btnFont = new Font("Segoe UI", Font.BOLD, 20);
        Font resultFont = new Font("Segoe UI", Font.BOLD, 32);

        // Initialize animation particles
        particles = new ArrayList<>();
        for (int i = 0; i < 75; i++) {
            particles.add(new FloatingParticle());
        }

        // 1. Full-Screen Gradient Background with Floating Numbers
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color c1 = new Color(0x3ec6a8); // Teal
                Color c2 = new Color(0x2f80ed); // Blue
                Color c3 = new Color(0x1b2a49); // Dark Navy

                LinearGradientPaint lgp = new LinearGradientPaint(
                    0, 0, getWidth(), getHeight(),
                    new float[]{0.0f, 0.5f, 1.0f},
                    new Color[]{c1, c2, c3}
                );
                
                g2d.setPaint(lgp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw Floating Particles behind the glass
                for (FloatingParticle p : particles) {
                    p.draw(g2d, getWidth(), getHeight());
                }
                
                g2d.dispose();
            }
        };
        backgroundPanel.setLayout(new GridBagLayout()); // To center the glass container

        // 2. Glassmorphism Container Box
        JPanel glassContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int arc = 35; // Rounded corners
                
                // Drop shadow
                g2.setColor(new Color(0, 0, 0, 60));
                g2.fillRoundRect(8, 8, getWidth() - 16, getHeight() - 16, arc, arc);
                
                // Semi-transparent glass background
                g2.setColor(new Color(25, 35, 60, 160)); // Dark tinted glass
                g2.fillRoundRect(0, 0, getWidth() - 16, getHeight() - 16, arc, arc);
                
                // Thin white border for glass effect
                g2.setColor(new Color(255, 255, 255, 50));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 16, getHeight() - 16, arc, arc);
                
                g2.dispose();
            }
        };
        glassContainer.setOpaque(false);
        glassContainer.setLayout(new BoxLayout(glassContainer, BoxLayout.Y_AXIS));
        glassContainer.setBorder(new EmptyBorder(40, 50, 40, 50));
        glassContainer.setPreferredSize(new Dimension(600, 550));

        // --- Title ---
        JLabel titleLabel = new JLabel("Number Converter");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassContainer.add(titleLabel);
        glassContainer.add(Box.createRigidArea(new Dimension(0, 40)));

        // --- Conversion Sections (Standard Radio Buttons) ---
        // Stacking them visibly so BOTH are clearly shown
        JPanel radioPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        radioPanel.setOpaque(false);
        radioPanel.setMaximumSize(new Dimension(450, 100));
        
        binToDecRadio = new JRadioButton("Binary to Decimal");
        binToDecRadio.setFont(sectionFont);
        binToDecRadio.setForeground(Color.WHITE);
        binToDecRadio.setOpaque(false);
        binToDecRadio.setFocusPainted(false);
        binToDecRadio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        binToDecRadio.setSelected(true); // Default
        
        decToBinRadio = new JRadioButton("Decimal to Binary");
        decToBinRadio.setFont(sectionFont);
        decToBinRadio.setForeground(Color.WHITE);
        decToBinRadio.setOpaque(false);
        decToBinRadio.setFocusPainted(false);
        decToBinRadio.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ButtonGroup group = new ButtonGroup();
        group.add(binToDecRadio);
        group.add(decToBinRadio);

        radioPanel.add(binToDecRadio);
        radioPanel.add(decToBinRadio);
        glassContainer.add(radioPanel);
        glassContainer.add(Box.createRigidArea(new Dimension(0, 40)));

        // --- Input Field ---
        inputField = new RoundedTextField(20);
        inputField.setFont(inputFont);
        inputField.setBackground(new Color(255, 255, 255, 25)); // Dark semi-transparent
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setMaximumSize(new Dimension(450, 60));
        
        glassContainer.add(inputField);
        glassContainer.add(Box.createRigidArea(new Dimension(0, 40)));

        // --- Buttons Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setOpaque(false);

        convertButton = new HoverButton("Convert", new Color(0x3ec6a8), new Color(0x2da88d));
        convertButton.setFont(btnFont);
        convertButton.setPreferredSize(new Dimension(180, 55));
        
        clearButton = new HoverButton("Clear", new Color(0xef4444), new Color(0xdc2626));
        clearButton.setFont(btnFont);
        clearButton.setPreferredSize(new Dimension(180, 55));

        buttonPanel.add(convertButton);
        buttonPanel.add(clearButton);
        glassContainer.add(buttonPanel);
        glassContainer.add(Box.createRigidArea(new Dimension(0, 40)));

        // --- Result Label ---
        resultLabel = new JLabel("Result");
        resultLabel.setFont(resultFont);
        resultLabel.setForeground(new Color(255, 255, 255, 150));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassContainer.add(resultLabel);

        // Add the glass container to the background
        backgroundPanel.add(glassContainer);
        
        // --- Footer ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        footerPanel.setOpaque(false);
        JLabel footerLabel = new JLabel("Made with love by Aarushi, Ananya, Sushant, Mayank <3");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        footerLabel.setForeground(new Color(255, 105, 180)); // Hot Pink
        footerPanel.add(footerLabel);
        
        // Add elements to frame
        setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // --- Setup Animation Timer ---
        animationTimer = new Timer(30, e -> {
            int w = backgroundPanel.getWidth();
            int h = backgroundPanel.getHeight();
            for (FloatingParticle p : particles) {
                p.update(w, h);
            }
            backgroundPanel.repaint();
        });
        animationTimer.start();

        // --- Actions ---
        convertButton.addActionListener(e -> performConversion());
        clearButton.addActionListener(e -> {
            inputField.setText("");
            resultLabel.setText("Result");
            resultLabel.setForeground(new Color(255, 255, 255, 150));
            inputField.requestFocus();
        });
        
        inputField.addActionListener(e -> performConversion());
    }

    private void performConversion() {
        String input = inputField.getText().trim();
        if (input.isEmpty()) {
            showError("Please enter a number.");
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
            showError("Invalid Input!");
        } catch (Exception ex) {
            showError("Error: Out of bounds.");
        }
    }
    
    private void showResult(String text) {
        resultLabel.setText(text);
        resultLabel.setForeground(new Color(0x3ec6a8)); // Teal Accent
    }
    
    private void showError(String text) {
        resultLabel.setText(text);
        resultLabel.setForeground(new Color(0xef4444)); // Bright Red
    }

    public static void main(String[] args) {
        // Use system look and feel so standard radio buttons render nicely
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new ConverterApp().setVisible(true);
        });
    }

    // --- Custom UI Components ---

    /** 
     * Internal class representing a single matrix-style floating character ('0' or '1').
     */
    class FloatingParticle {
        private float x, y, speed, alpha;
        private char text;
        private int size;
        private Random random = new Random();

        public FloatingParticle() {
            reset(true);
        }

        public void reset(boolean randomY) {
            x = random.nextInt(2500); 
            y = randomY ? random.nextInt(1500) : 1500 + random.nextInt(100);
            speed = 0.5f + random.nextFloat() * 1.5f; 
            alpha = 0.05f + random.nextFloat() * 0.15f; 
            size = 16 + random.nextInt(24);
            text = random.nextBoolean() ? '1' : '0';
        }

        public void update(int screenWidth, int screenHeight) {
            y -= speed; 
            if (y < -50) {
                x = random.nextInt(Math.max(screenWidth, 100));
                y = screenHeight + random.nextInt(50);
                speed = 0.5f + random.nextFloat() * 1.5f;
            }
        }

        public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
            if (x > screenWidth || y > screenHeight + 100 || y < -50) return;
            g2d.setFont(new Font("Consolas", Font.BOLD, size));
            g2d.setColor(new Color(255, 255, 255, (int)(alpha * 255))); // White floating numbers look great on the gradient
            g2d.drawString(String.valueOf(text), x, y);
        }
    }

    class RoundedTextField extends JTextField {
        private int arc;
        public RoundedTextField(int columns) {
            super(columns);
            this.arc = 25;
            setOpaque(false);
            setBorder(new EmptyBorder(10, 20, 10, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 100)); // Light border
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, arc, arc);
            g2.dispose();
        }
    }

    class HoverButton extends JButton {
        private Color normalColor, hoverColor;
        private float scale = 1.0f;

        public HoverButton(String text, Color normalColor, Color hoverColor) {
            super(text);
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { scale = 1.05f; repaint(); }
                @Override public void mouseExited(MouseEvent e) { scale = 1.0f; repaint(); }
                @Override public void mousePressed(MouseEvent e) { scale = 0.95f; repaint(); }
                @Override public void mouseReleased(MouseEvent e) { scale = 1.05f; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Handle scaling effect for hover/click
            int w = (int)(getWidth() * scale);
            int h = (int)(getHeight() * scale);
            int x = (getWidth() - w) / 2;
            int y = (getHeight() - h) / 2;

            if (getModel().isPressed()) {
                g2.setColor(hoverColor.darker());
            } else if (scale > 1.0f) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(normalColor);
            }
            
            // Soft glow on hover
            if (scale > 1.0f) {
                setShadow(g2, hoverColor);
            }
            
            g2.fill(new RoundRectangle2D.Float(x, y, w, h, 25, 25));
            
            // Text drawing
            g2.setColor(getForeground());
            FontMetrics metrics = g2.getFontMetrics();
            int tx = (getWidth() - metrics.stringWidth(getText())) / 2;
            int ty = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            g2.drawString(getText(), tx, ty);
            
            g2.dispose();
        }
        
        private void setShadow(Graphics2D g2, Color color) {
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
            g2.fillRoundRect(2, 2, getWidth(), getHeight(), 25, 25);
        }
    }
}
