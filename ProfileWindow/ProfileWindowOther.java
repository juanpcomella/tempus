package ProfileWindow;

import MainWindow.MainWindow;
import StartingWindows.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;
import java.io.*;



import java.awt.geom.Ellipse2D;

public class ProfileWindowOther extends JFrame {

    public ProfileWindowOther(Usuario usuarioActual, Usuario usuarioBusqueda) {
        setTitle("WEDO - " + usuarioBusqueda.getNombreUsuario());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Left Panel Configuration
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        GridBagConstraints leftGBC = new GridBagConstraints();
        leftGBC.insets = new Insets(5, 5, 5, 5);
        leftGBC.fill = GridBagConstraints.NONE;
        leftGBC.anchor = GridBagConstraints.NORTHWEST;


        // Left Panel - Back Button
        leftGBC.gridx = 0;
        leftGBC.gridy = 0;
        leftGBC.weightx = 0;
        leftGBC.weighty = 0;
        JButton backButton = new JButton("<<");
        backButton.setBackground(new Color(0,0,0,0));
        backButton.setFocusPainted(false);
        backButton.setFocusable(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainWindow mainWindow = new MainWindow(usuarioActual);
                mainWindow.setVisible(true);
                dispose();
            }
        });

        leftPanel.add(backButton, leftGBC);

        // Left Panel - Profile Picture
        leftGBC.gridy = 1;
        leftGBC.weightx = 1.0;
        leftGBC.weighty = 0.40;
        leftGBC.fill = GridBagConstraints.BOTH;
        leftGBC.anchor = GridBagConstraints.CENTER;

        JPanel profilePicturePanel = new JPanel(new BorderLayout());
        profilePicturePanel.setBackground(Color.WHITE);

        try {
            BufferedImage profileImage = ImageIO.read(new File("imagenes/PERFIL.png"));
            JLabel profilePictureLabel = new JLabel(new ImageIcon(getCircularImage(profileImage, 200)));
            profilePicturePanel.add(profilePictureLabel, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
            profilePicturePanel.add(new JLabel("Image not found"), BorderLayout.CENTER);
        }

        leftPanel.add(profilePicturePanel, leftGBC);


        // Left Panel - Username and Button Row
        leftGBC.gridy = 3;
        leftGBC.weighty = 0.2;

        JPanel usernamePanel = new JPanel(new GridBagLayout());
        usernamePanel.setBackground(Color.WHITE);
        JLabel usernameLabel = new JLabel(""+usuarioBusqueda.getNombreUsuario(), SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 48));
        usernamePanel.add(usernameLabel);
        leftPanel.add(usernamePanel, leftGBC);

        // Left Panel - Description Row
        leftGBC.gridy = 4;
        leftGBC.weighty = 0.4;

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setBackground(Color.WHITE);
        JLabel descriptionLabel = new JLabel("Alguna descripción o algún dato noc");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        descriptionPanel.add(descriptionLabel);
        leftPanel.add(descriptionPanel, leftGBC);

        // Follow Button
        leftGBC.fill = GridBagConstraints.NONE;
        leftGBC.gridy = 5;
        leftGBC.insets = new Insets(5, 0, 0, 15);
        JButton editButton = new JButton("Seguir");
        editButton.setFont(new Font("Tahoma", Font.BOLD, 24));
        editButton.setBackground(new Color(0,0,0,0));
        editButton.setFocusPainted(false);
        editButton.setFocusable(false);
        editButton.setContentAreaFilled(false);

        leftPanel.add(editButton, leftGBC);

        mainPanel.add(leftPanel, gbc);

        // Right Panel Configuration
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        GridBagConstraints rightGBC = new GridBagConstraints();
        rightGBC.insets = new Insets(5, 5, 5, 5);
        rightGBC.fill = GridBagConstraints.BOTH;
        rightGBC.gridx = 0;
        rightGBC.weightx = 1.0;

        // Right Panel - Daily Streaks Panel
        rightGBC.gridy = 0;
        rightGBC.weighty = 0.333;
        JPanel dailyStreakPanel = new JPanel(new BorderLayout());
        dailyStreakPanel.setBackground(new Color(58, 92, 181));

        JLabel streakTitleLabel = new JLabel("Racha de Objetivos Diarios", SwingConstants.CENTER);
        streakTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        streakTitleLabel.setForeground(Color.WHITE);
        streakTitleLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        dailyStreakPanel.add(streakTitleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(58, 92, 181));
        GridBagConstraints streakGBC = new GridBagConstraints();
        streakGBC.insets = new Insets(5, 5, 5, 5);

        streakGBC.gridx = 0;
        streakGBC.gridy = 0;
        streakGBC.anchor = GridBagConstraints.CENTER;
        JLabel flameIcon = new JLabel("\uD83D\uDD25");
        flameIcon.setFont(new Font("Arial", Font.PLAIN, 48));
        contentPanel.add(flameIcon, streakGBC);

        streakGBC.gridx = 1;
        streakGBC.gridy = 0;
        streakGBC.anchor = GridBagConstraints.WEST;
        int streakCount = 0;
        JLabel streakCountLabel = new JLabel(streakCount + "");
        streakCountLabel.setFont(new Font("Arial", Font.BOLD, 48));
        streakCountLabel.setForeground(new Color(255, 165, 0));
        contentPanel.add(streakCountLabel, streakGBC);

        streakGBC.gridx = 2;
        streakGBC.gridy = 0;

        contentPanel.add(Box.createHorizontalStrut(20), streakGBC);

        streakGBC.gridx = 3;
        streakGBC.gridy = 0;

        streakGBC.anchor = GridBagConstraints.CENTER;
        JPanel gridPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        gridPanel.setBackground(new Color(58, 92, 181));

        int cantObjetivosDiarios = 4;

        for (int i = 0; i < cantObjetivosDiarios; i++) {
            JPanel box = new JPanel();
            box.setPreferredSize(new Dimension(60, 60));
            box.setBackground(new Color(173, 216, 230));
            gridPanel.add(box);
        }

        contentPanel.add(gridPanel, streakGBC);

        dailyStreakPanel.add(contentPanel, BorderLayout.CENTER);

        rightPanel.add(dailyStreakPanel, rightGBC);

        // Right Panel - Progress Panel
        rightGBC.gridy = 1;
        rightGBC.weighty = 0.333;

        JPanel progressPanel = new JPanel();
        progressPanel.setBackground(new Color(58, 92, 181));
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));

        progressPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel progressLabel = new JLabel("Titulo del objetivo", SwingConstants.CENTER);
        progressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressLabel.setForeground(Color.WHITE);
        progressLabel.setFont(new Font("Arial", Font.BOLD, 24));
        progressPanel.add(progressLabel);

        progressPanel.add(Box.createVerticalGlue());

        JLabel percentageLabel = new JLabel("0%", SwingConstants.CENTER);
        percentageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        percentageLabel.setForeground(Color.WHITE);
        percentageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        progressPanel.add(percentageLabel);

        progressPanel.add(Box.createVerticalStrut(5));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(15);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(150, 10));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressPanel.add(progressBar);

        percentageLabel.setText(progressBar.getValue() + "%");

        progressPanel.add(Box.createVerticalGlue());

        rightPanel.add(progressPanel, rightGBC);

        // Right Panel - Calendar Panel
        rightGBC.gridy = 2;
        rightGBC.weighty = 0.333;
        JPanel activityPanel = new JPanel();
        activityPanel.setBackground(new Color(58, 92, 181));
        activityPanel.setLayout(new BorderLayout());

        JLabel activityLabel = new JLabel("Próximas actividades de {USER}", SwingConstants.CENTER);
        activityLabel.setForeground(Color.WHITE);
        activityLabel.setFont(new Font("Arial", Font.BOLD, 24));
        activityLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        activityPanel.add(activityLabel, BorderLayout.NORTH);

        String[] activityTableParams = {"Activity", "Date"};

        DefaultTableModel tableModel = new DefaultTableModel(activityTableParams, 0);
        JTable activityJTable = new JTable(tableModel);
        activityJTable.setOpaque(false);
        activityJTable.setBackground(new Color(0,0,0,0));

        JScrollPane jtableScroll = new JScrollPane(activityJTable);
        jtableScroll.getViewport().setOpaque(false);
        jtableScroll.setOpaque(false);

        activityPanel.add(jtableScroll, BorderLayout.CENTER);

        rightPanel.add(activityPanel, rightGBC);

        mainPanel.add(rightPanel, gbc);

        // Add main panel to frame
        add(mainPanel);
    }

    private BufferedImage getCircularImage(BufferedImage image, int diameter) {
        BufferedImage output = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = output.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, diameter, diameter);
        g2d.setClip(circle);
        g2d.drawImage(image, 0, 0, diameter, diameter, null);

        g2d.dispose();
        return output;
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear la ventana principal
        SwingUtilities.invokeLater(() -> {
            Usuario user = new Usuario(null, null, null);
            ProfileWindowOther window = new ProfileWindowOther(user, user);
            window.setVisible(true);

        });

    }

}
