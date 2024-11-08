import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrimeraVentana extends JFrame {

    public PrimeraVentana() {

        setTitle("WEDO");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear panel principal
        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        // Crear Titulo
        JLabel titulo = new JLabel("Bienvenido a WEDO", JLabel.CENTER);
        titulo.setFont(new Font("Monospaced", Font.PLAIN, 20));
        titulo.setBounds(496/2,60, 204, 100);

        panel.add(titulo);

        // Añadir Logo


        // Crear el enter button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Monospaced", Font.PLAIN, 16));
        registerButton.setSize(100, 50);
        registerButton.setLocation(330 - registerButton.getWidth(), 270);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterWindow registerWindow = new RegisterWindow();
                registerWindow.setLocationRelativeTo(null);
                registerWindow.setVisible(true);

                dispose();
            }
        });

        panel.add(registerButton);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Monospaced", Font.PLAIN, 16));
        loginButton.setSize(100, 50);
        loginButton.setLocation(370, 270);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setLocationRelativeTo(null);
                loginWindow.setVisible(true);

                dispose();
            }
        });

        panel.add(loginButton);


    }

    public static void main(String[] args) {
        PrimeraVentana window = new PrimeraVentana();

        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
