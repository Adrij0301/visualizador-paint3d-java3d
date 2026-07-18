package paint3d;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.vecmath.Vector3f;

public class ControlesTexturas {

    private final JPanel panel;
    private Texturas texturas;

    private final JSlider escalaSlider;
    private final JSlider[] posicionSliders;
    private final JSlider[] rotacionSliders;

    private TransformGroup escalaTG;
    private TransformGroup posicionTG;
    private TransformGroup rotacionManualTG;

    private final JComboBox<String> figuraSelector;
    private final JButton cargarButton;
    private final JRadioButton normalButton;
    private final JRadioButton cristalButton;
    private final JRadioButton reflejoButton;

    public ControlesTexturas() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        panel.setPreferredSize(new Dimension(435, 650));

        JPanel transformacionesPanel = new JPanel();
        transformacionesPanel.setLayout(new BoxLayout(transformacionesPanel, BoxLayout.Y_AXIS));
        transformacionesPanel.setBorder(crearBorde("Transformaciones"));

        escalaSlider = new JSlider(50, 200, 100);
        escalaSlider.addChangeListener(e -> actualizarEscala());
        agregarSlider(transformacionesPanel, "Escala", escalaSlider);

        posicionSliders = new JSlider[3];
        rotacionSliders = new JSlider[3];
        String[] ejes = {"X", "Y", "Z"};

        for (int i = 0; i < 3; i++) {
            posicionSliders[i] = new JSlider(-100, 100, 0);
            posicionSliders[i].addChangeListener(e -> actualizarPosicion());
            agregarSlider(transformacionesPanel, "Posición " + ejes[i], posicionSliders[i]);
        }

        for (int i = 0; i < 3; i++) {
            rotacionSliders[i] = new JSlider(-180, 180, 0);
            rotacionSliders[i].addChangeListener(e -> actualizarRotacion());
            agregarSlider(transformacionesPanel, "Rotación " + ejes[i], rotacionSliders[i]);
        }

        panel.add(transformacionesPanel);
        panel.add(Box.createVerticalStrut(10));

        JPanel figuraPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        figuraPanel.setBorder(crearBorde("Figura"));

        String[] figurasOpciones = {
            "Cubo", "Esfera", "Cono", "Cilindro", "Tetraedro", "Dodecaedro", "Octaedro"
        };
        figuraSelector = new JComboBox<>(figurasOpciones);
        figuraSelector.addActionListener(this::seleccionarFigura);

        figuraPanel.add(new JLabel("Seleccionar Figura Base:"));
        figuraPanel.add(figuraSelector);
        panel.add(figuraPanel);
        panel.add(Box.createVerticalStrut(10));

        JPanel cargaPanel = new JPanel(new GridLayout(1, 1));
        cargarButton = new JButton("Cargar Textura");
        aplicarEstiloMinimalista(cargarButton);
        cargarButton.addActionListener(this::cargarTextura);
        cargaPanel.add(cargarButton);
        panel.add(cargaPanel);
        panel.add(Box.createVerticalStrut(10));

        JPanel efectosPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        efectosPanel.setBorder(crearBorde("Efectos"));

        normalButton = new JRadioButton("Imagen normal");
        cristalButton = new JRadioButton("Cristal");
        reflejoButton = new JRadioButton("Reflejo");
        normalButton.setSelected(true);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(normalButton);
        grupo.add(cristalButton);
        grupo.add(reflejoButton);

        normalButton.addActionListener(e -> aplicarEfecto("NORMAL"));
        cristalButton.addActionListener(e -> aplicarEfecto("CRISTAL"));
        reflejoButton.addActionListener(e -> aplicarEfecto("REFLEJO"));

        efectosPanel.add(normalButton);
        efectosPanel.add(cristalButton);
        efectosPanel.add(reflejoButton);
        panel.add(efectosPanel);
        panel.add(Box.createVerticalGlue());
    }

    private TitledBorder crearBorde(String titulo) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                titulo,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                null,
                new Color(50, 50, 50));
    }

    private void agregarSlider(JPanel contenedor, String texto, JSlider slider) {
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        contenedor.add(label);
        contenedor.add(slider);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setTexturas(Texturas texturas) {
        this.texturas = texturas;
        seleccionarFigura(null);
    }

    private void seleccionarFigura(ActionEvent e) {
        if (texturas == null) {
            return;
        }

        String figuraSeleccionada = (String) figuraSelector.getSelectedItem();
        texturas.mostrarFigura(figuraSeleccionada);
        actualizarReferenciasTransformacion();
        reiniciarSliders();

        if (normalButton.isSelected()) {
            aplicarEfecto("NORMAL");
        } else if (cristalButton.isSelected()) {
            aplicarEfecto("CRISTAL");
        } else {
            aplicarEfecto("REFLEJO");
        }
    }

    private void actualizarReferenciasTransformacion() {
        escalaTG = texturas.getEscalaTG();
        posicionTG = texturas.getPosicionTG();
        rotacionManualTG = texturas.getRotacionManualTG();
    }

    private void cargarTextura(ActionEvent e) {
        if (texturas == null) {
            System.out.println("Error: La clase Texturas no está configurada.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar imagen para la textura");
        int result = fileChooser.showOpenDialog(panel);
        if (result == JFileChooser.APPROVE_OPTION) {
            texturas.aplicarTextura(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void aplicarEfecto(String efecto) {
        if (texturas != null) {
            texturas.aplicarEfecto(efecto);
        }
    }

    private void actualizarEscala() {
        if (escalaTG == null) {
            return;
        }
        Transform3D transform = new Transform3D();
        transform.setScale(escalaSlider.getValue() / 100.0);
        escalaTG.setTransform(transform);
    }

    private void actualizarPosicion() {
        if (posicionTG == null) {
            return;
        }
        Transform3D transform = new Transform3D();
        transform.setTranslation(new Vector3f(
                posicionSliders[0].getValue() / 70.0f,
                posicionSliders[1].getValue() / 70.0f,
                posicionSliders[2].getValue() / 45.0f));
        posicionTG.setTransform(transform);
    }

    private void actualizarRotacion() {
        if (rotacionManualTG == null) {
            return;
        }

        Transform3D total = new Transform3D();
        Transform3D rx = new Transform3D();
        Transform3D ry = new Transform3D();
        Transform3D rz = new Transform3D();

        rx.rotX(Math.toRadians(rotacionSliders[0].getValue()));
        ry.rotY(Math.toRadians(rotacionSliders[1].getValue()));
        rz.rotZ(Math.toRadians(rotacionSliders[2].getValue()));

        total.mul(rx);
        total.mul(ry);
        total.mul(rz);
        rotacionManualTG.setTransform(total);
    }

    private void reiniciarSliders() {
        escalaSlider.setValue(100);
        for (JSlider slider : posicionSliders) {
            slider.setValue(0);
        }
        for (JSlider slider : rotacionSliders) {
            slider.setValue(0);
        }
    }

    private void aplicarEstiloMinimalista(JButton boton) {
        Color colorFondo = new Color(235, 235, 235);
        Color colorHover = new Color(220, 220, 220);
        Color colorBorde = new Color(200, 200, 200);
        Color colorTexto = new Color(50, 50, 50);

        boton.setOpaque(true);
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(colorBorde, 1));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorFondo);
            }
        });
    }
}