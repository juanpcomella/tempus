package MainWindow;

import javax.swing.*;
import javax.swing.border.LineBorder;

import BaseDeDatos.BDs;
import StartingWindows.Usuario;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.Timer;

public class RightSideBar extends JPanel {

    private ArrayList<String> habitosTotales = new ArrayList<>();
    private ArrayList<String> habitosDiarios = new ArrayList<>(); 
    private JPanel objetivosPanel;
    private JPanel habitosPanel;
    private ArrayList<Objetivo> listaObjetivos = new ArrayList<>();

    public RightSideBar(Usuario usuario) {
        setLayout(new GridLayout(3, 1, 5, 5));
        setBackground(new Color(50,70,90));
        setBorder(new LineBorder(new Color(173, 216, 230),10));

        // Panel de Objetivos
        JPanel objetivos = new JPanel();
        objetivos.setLayout(new BoxLayout(objetivos, BoxLayout.Y_AXIS));
        objetivos.setBackground(new Color(50,70,90));
        objetivos.setBorder(new LineBorder(new Color(50,70,90),5));

        JLabel objetivosLabel = new JLabel("Objetivos");
        objetivosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        objetivosLabel.setFont(new Font("Arial", Font.BOLD, 18));
        objetivosLabel.setForeground(Color.WHITE);
        objetivos.add(objetivosLabel);

        objetivosPanel = new JPanel();
        objetivosPanel.setLayout(new BoxLayout(objetivosPanel, BoxLayout.Y_AXIS));
        objetivosPanel.setBackground(Color.WHITE);

        JScrollPane objetivosScrollPane = new JScrollPane(objetivosPanel);
        objetivosScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        objetivosScrollPane.setPreferredSize(new Dimension(300, 200));
        objetivos.add(objetivosScrollPane);

        JButton añadirObjetivoButton = new JButton("Añadir Objetivo");
        añadirObjetivoButton.setForeground(new Color(50,70,90));
        añadirObjetivoButton.setBackground(Color.WHITE);
        añadirObjetivoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        BDs.crearTablaObjetivos();
        for(Objetivo objetivo: BDs.crearListaObjetivos(usuario.getNombreUsuario())) {
            añadirObjetivo(objetivo.getNombre(), objetivo.getFechaFin().toString(), usuario);
        }
        añadirObjetivoButton.addActionListener(e -> {
            mostrarDialogoAñadirObjetivo(usuario);
        });
        objetivos.add(Box.createVerticalStrut(20));
        objetivos.add(añadirObjetivoButton);
        
        add(objetivos);

        //HABITOS-----------------------------------------------------------------------
        //Panel habitos
        JPanel habitos = new JPanel();
        habitos.setBackground(new Color(50,70,90));
        habitos.setLayout(new BoxLayout(habitos, BoxLayout.Y_AXIS));
        habitos.setBackground(new Color(50,70,90));
        habitos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel habitosLabel = new JLabel("Hábitos");
        habitosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        habitosLabel.setFont(new Font("Arial", Font.BOLD, 18));
        habitosLabel.setForeground(Color.WHITE);
        habitos.add(habitosLabel);
        
        //PASO 1 -------------------------------------------------------------------------
        //lista de todos los habitos (solo el nombre del habito)
        BDs.crearTablaHabitosTemporales();
        habitosPanel = new JPanel();
        habitosPanel.setLayout(new GridLayout(4, 1, 5, 5));
        habitosPanel.setBackground(new Color(50,70,90));
        if(BDs.contarHabitos() == 0) {
            habitosTotales = cargarHabitosDesdeCSV("BaseDeDatos/objetivos_diarios.csv");
            habitosDiarios = generarHabitosDiarios(usuario);//lista de 4 habitos
            guardarHabitosDiariosEnBD(usuario);
            actualizarHabitosPanel(usuario);
        }else {
        if(checkearCambioHabitos(usuario)) {
            habitosTotales = cargarHabitosDesdeCSV("BaseDeDatos/objetivos_diarios.csv");
            habitosDiarios = generarHabitosDiarios(usuario);//lista de 4 habitos
            guardarHabitosDiariosEnBD(usuario);
            actualizarHabitosPanel(usuario);
        }else {
            String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    		habitosDiarios.clear();
        	for(Habito habito : BDs.crearListaHabitos(usuario.getNombreUsuario(), fechaHoy)) {
//        		System.out.println(habito.getNombre());
//        		System.out.println(habito.getFecha());
        		habitosDiarios.add(habito.getNombre());
        	}
            guardarHabitosDiariosEnBD(usuario);
            actualizarHabitosPanel(usuario);
        }
        }
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        for(Habito habito : BDs.crearListaHabitos(usuario.getNombreUsuario(), fechaHoy)) {
//        	System.out.println(habito.getNombre());
//        }
//        System.out.println(checkearCambioHabitos(usuario));
//        System.out.println(BDs.contarHabitos());

        //--------------------------------------------------------------------------------

        
        habitos.add(habitosPanel);
        add(habitos);

        JPanel vacio = new JPanel();
        vacio.setBackground(new Color(50,70,90));
        add(vacio);
    }
    
    //METODOS OBJETIVOS-----------------------------------------------------------------------------------
    private void mostrarDialogoAñadirObjetivo(Usuario usuario) {
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        JTextField nombreField = new JTextField();
        JTextField descripcionField = new JTextField();
        JTextField fechaField = new JTextField();

        dialogPanel.add(new JLabel("Nombre del objetivo:"));
        dialogPanel.add(nombreField);
        dialogPanel.add(new JLabel("Descripción del objetivo:"));
        dialogPanel.add(descripcionField);
        dialogPanel.add(new JLabel("Fecha de cumplimiento (YYYY-MM-DD):"));
        dialogPanel.add(fechaField);

        int option = JOptionPane.showConfirmDialog(this, dialogPanel, "Añadir Objetivo", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText().trim();
            String descripcion = descripcionField.getText().trim();
            String fecha = fechaField.getText().trim();

            if (!nombre.isEmpty() && !fecha.isEmpty()) {
                LocalDate fechaCumplimiento;
                try {
                    fechaCumplimiento = LocalDate.parse(fecha);
                    LocalDate fechaHoy = LocalDate.now();
                    if (fechaCumplimiento.isBefore(fechaHoy)) {
                        JOptionPane.showMessageDialog(this, "Fecha incorrecta, introduce una fecha a partir de la actual", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Fecha inválida. Formato esperado: YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; 
                }
                BDs.insertarObjetivos(usuario.getNombreUsuario(), nombre, descripcion, fecha, false);
                añadirObjetivo(nombre, fecha, usuario);
            } else {
                JOptionPane.showMessageDialog(this, "Todos los campos deben ser completados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

   
    private void añadirObjetivo(String nombre, String fechaCumplimiento, Usuario usuario) {
        Objetivo objetivo = new Objetivo(nombre, "Descripción del objetivo", LocalDate.parse(fechaCumplimiento), false);
        objetivo.setCuantoQueda(obtenerCuantoQueda(fechaCumplimiento)); 

        listaObjetivos.add(objetivo);

        // Usar el método recursivo para ordenar los objetivos
        ordenarObjetivosPorFechaRecursivo(listaObjetivos, 0);

        actualizarPanelObjetivos(usuario);
    }
    
    private void ordenarObjetivosPorFechaRecursivo(ArrayList<Objetivo> lista, int index) {
        // Caso base: si el índice alcanza el final de la lista, termina la recursión
        if (index == lista.size()) {
            return;
        }
        
        // Buscar el objetivo con el valor de 'cuantoQueda' mínimo desde el índice actual
        int minIndex = index;
        for (int i = index + 1; i < lista.size(); i++) {
            if (lista.get(i).getCuantoQueda() < lista.get(minIndex).getCuantoQueda()) {
                minIndex = i;
            }
        }
        
        // Intercambiar el objetivo en 'index' con el objetivo en 'minIndex'
        Objetivo temp = lista.get(index);
        lista.set(index, lista.get(minIndex));
        lista.set(minIndex, temp);

        // Llamada recursiva para ordenar el siguiente índice
        ordenarObjetivosPorFechaRecursivo(lista, index + 1);
    }

    private void ordenarObjetivosPorFecha() {
        listaObjetivos.sort(Comparator.comparingInt(Objetivo::getCuantoQueda));
    }

    private void actualizarPanelObjetivos(Usuario usuario) {
        objetivosPanel.removeAll();

        for (Objetivo objetivo : listaObjetivos) {
            JLabel objetivoLabel = new JLabel(objetivo.getNombre());

            objetivoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            objetivoLabel.setBackground(new Color(179, 229, 252));
            objetivoLabel.setForeground(new Color(30, 136, 229)); 
            objetivoLabel.setOpaque(true); 
            objetivoLabel.setHorizontalAlignment(SwingConstants.CENTER); 
            objetivoLabel.setPreferredSize(new Dimension(getWidth(), 40));
            objetivoLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
            objetivoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); 

            String mensajeConTiempoRestante = "Quedan " + objetivo.getCuantoQueda() + " días";
            objetivoLabel.setText(objetivo.getNombre() + " - " + mensajeConTiempoRestante);
            
            objetivoLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mostrarDetallesObjetivo(objetivo, usuario);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    objetivoLabel.setBackground(new Color(144, 202, 249));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    objetivoLabel.setBackground(new Color(179, 229, 252));
                }
            });
            
            if (objetivo.getCuantoQueda() == 0) {
                int opcion = JOptionPane.showConfirmDialog(
                    null, 
                    "¿Has completado el objetivo: " + objetivo.getNombre() + "?", 
                    "Confirmación de objetivo", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "¡Felicidades por completar tu objetivo!");
                } else if (opcion == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "¡Animo! ¡A la siguiente lo conseguiras!");
                }
            }
            objetivosPanel.add(objetivoLabel);
        }

        revalidate();
        repaint();
    }
    
    private void mostrarDetallesObjetivo(Objetivo objetivo, Usuario usuario) {
        JDialog dialog = new JDialog((Frame) null, "Detalles del Objetivo", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());

        JPanel contenidoPanel = new JPanel();
        contenidoPanel.setLayout(new BoxLayout(contenidoPanel, BoxLayout.Y_AXIS));
        contenidoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nombreLabel = new JLabel("Nombre: " + objetivo.getNombre());
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);      

        JLabel descripcionLabel = new JLabel("<html>Descripción: " + objetivo.getDescripcion() + "</html>");
        descripcionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descripcionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descripcionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel fechaFinLabel = new JLabel("Fecha Fin: " + objetivo.getFechaFin());
        fechaFinLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        fechaFinLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        contenidoPanel.add(nombreLabel);
        contenidoPanel.add(Box.createVerticalStrut(10));
        contenidoPanel.add(descripcionLabel);
        contenidoPanel.add(Box.createVerticalStrut(10));
        contenidoPanel.add(fechaFinLabel);

        JButton eliminarButton = new JButton("Eliminar objetivo");
        eliminarButton.setBackground(Color.RED); 
        eliminarButton.setForeground(Color.WHITE);
        eliminarButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                dialog,
                "¿Estás seguro de que deseas eliminar este objetivo?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
            	BDs.eliminarObjetivos(usuario.getNombreUsuario(), objetivo.getNombre());
                eliminarObjetivoDePantalla(objetivo, usuario);
                dialog.dispose();
            }
        });

        dialog.add(contenidoPanel, BorderLayout.CENTER);
        dialog.add(eliminarButton, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void eliminarObjetivoDePantalla(Objetivo objetivo, Usuario usuario) {
        listaObjetivos.removeIf(o -> o.getNombre().equals(objetivo.getNombre()));
        actualizarPanelObjetivos(usuario);
    }

    private int obtenerCuantoQueda(String fechaCumplimiento) {
        LocalDate fechaObjetivo = LocalDate.parse(fechaCumplimiento);
        LocalDate fechaHoy = LocalDate.now();
        return (int) java.time.temporal.ChronoUnit.DAYS.between(fechaHoy, fechaObjetivo);
    }

    private String calcularTiempoRestante(String fechaCumplimiento) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaObjetivo = formatoFecha.parse(fechaCumplimiento);
            Date fechaHoy = new Date();
            long diferenciaMillis = fechaObjetivo.getTime() - fechaHoy.getTime();
            long diasRestantes = diferenciaMillis / (1000 * 60 * 60 * 24);

            if (diasRestantes < 0) {
                return "¡El objetivo ya pasó!";
            } else if (diasRestantes == 0) {
                return "¡Hoy es el día!";
            } else {
                return diasRestantes + " días restantes";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Fecha inválida";
        }
    }
    //------------------------------------------------------------------------------------------------
  //METODOS HABITOS---------------------------------------------------------------------------------
    
    //PASO 1 --> CARGAR TODOS LOS HABITOS DEL CSV ----------------------------------------------------------
    //METE EN LA LISTA SOLO LOS NOMBRES DE LOS HABITOS (UNA COLUMNA)
    private ArrayList<String> cargarHabitosDesdeCSV(String archivo) {
        ArrayList<String> habitos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length > 0) {
                    habitos.add(partes[0].trim());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar hábitos desde el archivo: " + archivo,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return habitos;
    }
    //--------------------------------------------------------------------------------------------------------------
    //PASO 2 --> GENERAR UNA LISTA (habitosDiarios) CON SOLO 4 HABITOS (LOS QUE LUEGO SE PONDRAN EN EL PANEL)
    //METODO PARA CONSEGUIR 4 ELEMENTOS ALEATORIOS DE LA LISTA ORIGINAL
    public static ArrayList<String> obtenerElementosAleatorios(ArrayList<String> listaOriginal, int cantidad) {
        ArrayList<String> listaCopia = new ArrayList<>(listaOriginal);
        
        Collections.shuffle(listaCopia);
        
        ArrayList<String> listaAleatoria = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            listaAleatoria.add(listaCopia.get(i));
        }
        
        return listaAleatoria;
    }
    private ArrayList<String> generarHabitosDiarios(Usuario usuario) {
    	//habitosTotales = lista de todos los habitos cargados del csv
        habitosDiarios = obtenerElementosAleatorios(habitosTotales, 4);
        return habitosDiarios;
    }
    //-----------------------------------------------------------------------------------------------------------------
    
    //PASO 3 --> GUARDAMOS LOS 4 HABITOS DE habitosDiarios QUE USAREMOS MAS TARDE EN LA BASE DE DATOS CON LA FECHAS DE "HOY"
    private void guardarHabitosDiariosEnBD(Usuario usuario) {
    	BDs.eliminarTodosLosHabitos();
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            for (String habito : habitosDiarios) {
            	String nombre_habito;
                nombre_habito = habito;
                BDs.insertarHabitosTemporales(usuario.getNombreUsuario(), fechaHoy, nombre_habito);
            }
        }
    //---------------------------------------------------------------------------------------------------------------------------------------
    
    private boolean checkearCambioHabitos(Usuario usuario) {
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    	ArrayList<Habito> habitosActuales = BDs.crearListaHabitos(usuario.getNombreUsuario(), fechaHoy);
    	boolean cambio = false;
    	for(Habito habito : habitosActuales) {
    		if(habito.getFecha().equals(fechaHoy)) {
    			cambio = false;
    		}else {
    			cambio = true;
    		}
    	}
    	return cambio;
    }

    private void actualizarHabitosPanel(Usuario usuario) {
        habitosPanel.removeAll();
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
        for (String habito : habitosDiarios) {
    		System.out.println(BDs.seleccionarHabitoCompletado(usuario.getNombreUsuario(), "Jugar un juego de mesa con alguien"));
            JButton habitoButton = new JButton(habito);
            habitoButton.setFont(new Font("Arial", Font.PLAIN, 12));
            for(Habito habitoComprobarCompletado : BDs.crearListaHabitos(usuario.getNombreUsuario(), fechaHoy)) {
//            	System.out.println(habitoComprobarCompletado.isCompletado());
//        		System.out.println(BDs.seleccionarHabitoCompletado(usuario.getNombreUsuario(), habito)+"*****");
            	if(habitoComprobarCompletado.getNombre().equals(habito)) {
            		if(BDs.seleccionarHabitoCompletado(usuario.getNombreUsuario(), habito)){
            			habitoButton.setBackground(Color.GREEN);
                        habitoButton.setForeground(Color.WHITE);
            		}else {
            			System.out.println("si");
            	        habitoButton.setBackground(Color.RED);
            	        habitoButton.setForeground(Color.WHITE);
            	        habitoButton.addActionListener(e -> {
                            int respuesta = JOptionPane.showConfirmDialog(
                                    this,
                                    "¿Has completado el hábito: " + habito + "?",
                                    "Completar Hábito",
                                    JOptionPane.YES_NO_OPTION
                            );
                            if (respuesta == JOptionPane.YES_OPTION) {
                                habitoButton.setBackground(Color.GREEN);
                                BDs.updateCompletadoHabito(usuario.getNombreUsuario(), habito, true);
                                for(Habito habitoLista : BDs.crearListaHabitos(usuario.getNombreUsuario(), fechaHoy)) {
                                	System.out.println(habitoLista.getNombre());
//                            		System.out.println(habito);
                                	if(habitoLista.getNombre().equals(habito)) {
                                		System.out.println("igual");
                                		System.out.println(BDs.seleccionarHabitoCompletado(usuario.getNombreUsuario(), habito));
//                                		for(Habito h : BDs.crearListaHabitos(usuario.getNombreUsuario(), fechaHoy)) {
//                                			System.out.println(h.isCompletado());
//                                		}
                                		System.out.println(habito);
                                		habitoLista.setCompletado(true);
                                		BDs.updateCompletadoHabito(usuario.getNombreUsuario(), habito, true);
                                		System.out.println(BDs.seleccionarHabitoCompletado(usuario.getNombreUsuario(), "Jugar un juego de mesa con alguien"));
                                	}
                                }
                                //Suma 10 monedas por habito completado
                                usuario.setSaldo(usuario.getSaldo()+10);
                                BDs.updateSaldo(usuario.getNombreUsuario(), usuario.getSaldo());
                                Navbar.coinAmountLabel.setText(String.valueOf(usuario.getSaldo()));
                                
                                
                            } 
                        });
            		}
            	}
            }  
            habitosPanel.add(habitoButton);
        }
        revalidate();
        repaint();
    }
 
    //PASO 4 --> COGEMOS DE LA BASE DE DATOS LOS HABITOS CON LA FECHA DE HOY Y DEL USUARIO INDICADO Y METEMOS LOS NOMBRES EN LA LISTA habitos
//    private ArrayList<String> cargarHabitosDiarios(Usuario usuario) {
//    	ArrayList<Habito> habitosConFecha = new ArrayList<>();
//        ArrayList<String> habitos = new ArrayList<>();
////            String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//            habitosConFecha = BDs.crearListaHabitos(usuario.getNombreUsuario());
//            BDs.eliminarTodosLosHabitos();
//            
//            for(Habito habito: habitosConFecha) {
//            	habitos.add(habito.getNombre());
//            }
//
//        return habitos;
//    }

   
    
}
