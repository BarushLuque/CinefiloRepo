/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.cinefilo.ui;

import mx.itson.cinefilo.entity.Cine;
import mx.itson.cinefilo.entity.Funcion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CineUI extends JFrame {
    private Cine cine; // El sistema de cine que administra las funciones y boletos.
    private JTextArea displayArea; // Área de texto para mostrar información.
    
    public CineUI() {
        // Inicializar el cine con funciones
        cine = new Cine();
        cine.agregarFuncion(new Funcion("Venom el último baile", "2:00 PM", 100.0));
        cine.agregarFuncion(new Funcion("Sonríe 2", "5:00 PM", 120.0));
        cine.agregarFuncion(new Funcion("Gladiador (ReEstreno)", "8:00 PM", 150.0));

        // Configuración básica del JFrame
        setTitle("Somos Cinefilos de Corazón");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Panel superior: Título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("---CINEFILOS---");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel central: Área de texto para mostrar información
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Panel inferior: Botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        JButton showFunctionsButton = new JButton("Mostrar Funciones");
        JButton sellTicketButton = new JButton("Vender Boleto");
        JButton generalReportButton = new JButton("Reporte General");
        JButton functionReportButton = new JButton("Reporte por Función");
        JButton exitButton = new JButton("Salir");

        // Agregar botones al panel inferior
        buttonPanel.add(showFunctionsButton);
        buttonPanel.add(sellTicketButton);
        buttonPanel.add(generalReportButton);
        buttonPanel.add(functionReportButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Acciones para cada botón
        // Botón: Mostrar funciones
        showFunctionsButton.addActionListener(e -> mostrarFunciones());

        // Botón: Vender boleto
        sellTicketButton.addActionListener(e -> venderBoleto());

        // Botón: Reporte general
        generalReportButton.addActionListener(e -> reporteGeneral());

        // Botón: Reporte por función
        functionReportButton.addActionListener(e -> reportePorFuncion());

        // Botón: Salir
        exitButton.addActionListener(e -> System.exit(0));
    }

    // Métodos para manejar las acciones
    private void mostrarFunciones() {
        StringBuilder output = new StringBuilder("--- Funciones Disponibles ---\n");
        int index = 1;
        for (Funcion funcion : cine.getFunciones()) {
            output.append(index++).append(". ").append(funcion.getNombre())
                    .append(" | Horario: ").append(funcion.getHorario())
                    .append(" | Precio: $").append(funcion.getPrecio()).append("\n");
        }
        displayArea.setText(output.toString());
    }

   private void venderBoleto() {
    if (cine.getFunciones().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay funciones disponibles.");
        return;
    }

    String funcionIndex = JOptionPane.showInputDialog(this, "Ingrese el número de la función:");
    try {
        int index = Integer.parseInt(funcionIndex) - 1;
        Funcion funcion = cine.seleccionarFuncion(index + 1);

        // Crear un diálogo para mostrar los asientos
        JDialog asientoDialog = new JDialog(this, "Seleccione un asiento", true);
        asientoDialog.setLayout(new GridLayout(4, 4, 10, 10));
        asientoDialog.setSize(400, 400);

        // Panel para los asientos
        JPanel asientosPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        JButton[] botonesAsientos = new JButton[16];

        // Crear botones para los asientos
        for (int i = 0; i < 16; i++) {
            JButton asientoButton = new JButton(String.valueOf(i + 1));
            asientoButton.setPreferredSize(new Dimension(50,50));
            if (funcion.getAsientos().get(i).isOcupado()) {
                asientoButton.setBackground(Color.RED); // Asiento ocupado
                asientoButton.setEnabled(false);
            } else {
                asientoButton.setBackground(Color.GREEN); // Asiento disponible
            }

            int asientoNumero = i + 1; // Asiento relacionado con el botón
            asientoButton.addActionListener(e -> {
                // Confirmar la selección del asiento
                int confirm = JOptionPane.showConfirmDialog(asientoDialog, "¿Confirmar asiento " + asientoNumero + "?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String cliente = JOptionPane.showInputDialog(asientoDialog, "Ingrese su nombre (opcional):");
                    funcion.venderBoleto(cliente == null ? "" : cliente, asientoNumero);
                    asientoButton.setBackground(Color.RED); // Cambiar el color a rojo
                    asientoButton.setEnabled(false); // Deshabilitar el botón
                    asientoDialog.dispose();
                    JOptionPane.showMessageDialog(this, "Boleto vendido exitosamente.");
                }
            });

            botonesAsientos[i] = asientoButton;
            asientosPanel.add(asientoButton);
        }

        asientoDialog.add(asientosPanel);
        asientoDialog.setLocationRelativeTo(this);
        asientoDialog.setVisible(true);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Ocurrió un error: " + ex.getMessage());
    }
}


    private void reporteGeneral() {
        StringBuilder output = new StringBuilder("--- Reporte General ---\n");
        double totalGanancias = 0;
        int totalBoletos = 0;

        for (Funcion funcion : cine.getFunciones()) {
            totalGanancias += funcion.getTotalGanancia();
            totalBoletos += funcion.getBoletosVendidos();
        }

        output.append("Total de boletos vendidos: ").append(totalBoletos).append("\n");
        output.append("Ganancias totales: $").append(totalGanancias).append("\n");
        displayArea.setText(output.toString());
    }

    private void reportePorFuncion() {
        StringBuilder output = new StringBuilder("--- Reporte por Función ---\n");
        for (Funcion funcion : cine.getFunciones()) {
            output.append("Película: ").append(funcion.getNombre()).append("\n")
                    .append("Boletos vendidos: ").append(funcion.getBoletosVendidos()).append("\n")
                    .append("Ganancia total: $").append(funcion.getTotalGanancia()).append("\n\n");
        }
        displayArea.setText(output.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CineUI ui = new CineUI();
            ui.setVisible(true);
        });
    }
}

