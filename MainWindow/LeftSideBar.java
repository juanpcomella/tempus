package MainWindow;

import BaseDeDatos.BDs;
import ProfileWindow.ProfileWindowOther;
import ProfileWindow.ProfileWindowSelf;
import StartingWindows.Usuario;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeftSideBar extends JPanel {
    Notas nota;

    public LeftSideBar(Usuario usuario) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(173, 216, 230));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Usar BoxLayout para apilar los botones verticalmente
        panel.setBackground(new Color(50, 70, 90));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBorder(new LineBorder(new Color(173, 216, 230), 10));

        // Botón "+" en la parte superior
        JButton button1 = new JButton("+");
        button1.setBackground(new Color(173, 216, 230));
        button1.setForeground(new Color(50, 70, 90));
        button1.setPreferredSize(new Dimension(100, 30));
        button1.setMaximumSize(new Dimension(100, 30));
        button1.setMinimumSize(new Dimension(100, 30));
        button1.setAlignmentX(Component.CENTER_ALIGNMENT); // Asegura que el botón "+" esté centrado
        
        // Añadir el botón "+" al panel
        panel.add(button1);

        // Espacio entre el button1 y el primer botonPagina
        panel.add(Box.createVerticalStrut(10)); // Añadir espacio vertical de 10 píxeles

        Color colorTurquesa = new Color(173, 216, 230);
        Color azulOscuro = new Color(50, 70, 90);

        // Crear un mapa de notas
        Map<JButton, Notas> notasMap = new HashMap<>();
        

        // Acción para crear nuevas páginas
        button1.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(null, "Escribe el título de la página:", "Crear Página", JOptionPane.QUESTION_MESSAGE);

            if (input != null && !input.trim().isEmpty()) {
                JButton nuevoBoton = new JButton(input);
                nuevoBoton.setBackground(azulOscuro);
                nuevoBoton.setForeground(colorTurquesa);
                Border bordeBton = BorderFactory.createLineBorder(colorTurquesa,5);
                nuevoBoton.setBorder(bordeBton);
                String contenidoVacio = "";

                nuevoBoton.addActionListener(a -> {
                    Notas nuevaNota = notasMap.get(nuevoBoton);

                    if (nuevaNota == null) {
                        nuevaNota = new Notas(input, contenidoVacio);
                        notasMap.put(nuevoBoton, nuevaNota);
                        nuevaNota.botonPagina = nuevoBoton; // Vincular el boton con la nota actualizada
                    }

                    nuevaNota.setVisible(true);
                });

                // Click derecho para eliminar
                nuevoBoton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            int respuesta = JOptionPane.showConfirmDialog(null,
                                    "¿Desea eliminar la nota seleccionada?", "Eliminar nota",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (respuesta == JOptionPane.YES_OPTION) {
                                panel.remove(nuevoBoton);
                                panel.repaint();
                                notasMap.remove(nuevoBoton); // Eliminar del mapa
                            }
                        }
                    }
                });

                // Añadir al panel y actualizar diseño
                nuevoBoton.setPreferredSize(new Dimension(80, 30));
                nuevoBoton.setMaximumSize(new Dimension(80, 30));
                nuevoBoton.setMinimumSize(new Dimension(80, 30));
                nuevoBoton.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(nuevoBoton);
                panel.add(Box.createVerticalStrut(10));
                panel.revalidate();
                panel.repaint();

                // Asocia el nuevo botón con la nueva nota en el mapa
                Notas nuevaNota = new Notas(input, contenidoVacio);
                nuevaNota.botonPagina = nuevoBoton;
                notasMap.put(nuevoBoton, nuevaNota);
            }
        });
        
        
        
        

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Desactivar la barra de desplazamiento horizontal
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Asegurar que la barra de desplazamiento vertical siempre esté visible

        add(scrollPane); // Añadir el panel a la barra lateral

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel2.setBackground(new Color(50, 70, 90));
        panel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel2.setBorder(new LineBorder(new Color(173, 216, 230), 5));

        String usuarioActual = usuario.getNombreUsuario();
        cargarSeguimientosEnPanel(usuarioActual, panel2);

        add(new JScrollPane(panel2));
    }

    public void cargarSeguimientosEnPanel(String usuarioActual, JPanel panel2) {
        panel2.removeAll();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        Usuario user = BDs.obtenerUsuario(usuarioActual);

        // Añadir un título "Siguiendo"
        JLabel titulo = new JLabel("Siguiendo", SwingConstants.CENTER);
        titulo.setFont(new Font("Tahoma", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel2.add(Box.createVerticalStrut(10));
        panel2.add(titulo);
        panel2.add(Box.createVerticalStrut(20));

        ArrayList<Usuario> listaSeguimientos = BDs.crearListaSeguimientosPorUsuario(usuarioActual);

        for (Usuario seguido : listaSeguimientos) {
            JButton botonSeguido = new JButton(seguido.getNombreUsuario());
            botonSeguido.setFont(new Font("Arial", Font.PLAIN, 18));
            botonSeguido.setBackground(new Color(173, 216, 230));
            botonSeguido.setForeground(Color.BLACK);
            botonSeguido.setPreferredSize(new Dimension(175, 40));
            botonSeguido.setMaximumSize(new Dimension(175, 40));
            botonSeguido.setAlignmentX(Component.CENTER_ALIGNMENT);
            botonSeguido.addActionListener(e -> {
                SwingUtilities.invokeLater(() -> new ProfileWindowOther(user, seguido).setVisible(true));
                SwingUtilities.getWindowAncestor(LeftSideBar.this).dispose();
            });

            panel2.add(botonSeguido);
            panel2.add(Box.createVerticalStrut(7));
        }

        panel2.revalidate();
        panel2.repaint();
    }
}
