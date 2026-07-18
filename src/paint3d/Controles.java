package paint3d;

import javax.swing.*;
import javax.swing.event.*;
import javax.media.j3d.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.vecmath.Vector3f;

public class Controles {

    private JPanel panel;
    private TransformGroup escalaTG;
    private TransformGroup posicionTG;
    private TransformGroup rotacionTG;
    private TransformGroup rotacionManualTG;

    private JSlider escalaSlider;
    private JSlider[] posicionSliders;
    private JSlider[] rotacionSliders;

    private JButton lineasButton;
    private JButton lineasPunteadasButton;
    private JButton puntosButton;
    private JButton polygonButton;
    private JButton transparenteButton;
    private JButton coloresAleatoriosButton;
    private JButton wireframeButton;
    private JButton neonButton;
    private JButton metalicoButton;
    private JButton plasticoButton;
    private JButton flatButton;
    private JButton gouraudButton;

    private JButton oroButton;
    private JButton rubiButton;
    private JButton cristalButton;
    private JButton emisivoButton;

    private Figuras figuras;

    public Controles() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Controles de Figura", JLabel.CENTER);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);

        escalaSlider = new JSlider(50, 200, 100);
        escalaSlider.addChangeListener(e -> actualizarEscala());
        agregarSlider("Escala", escalaSlider);

        posicionSliders = new JSlider[3];
        String[] ejes = {"X", "Y", "Z"};
        for (int i = 0; i < 3; i++) {
            posicionSliders[i] = new JSlider(-100, 100, 0);
            posicionSliders[i].addChangeListener(e -> actualizarPosicion());
            agregarSlider("Posición " + ejes[i], posicionSliders[i]);
        }

        rotacionSliders = new JSlider[3];
        for (int i = 0; i < 3; i++) {
            rotacionSliders[i] = new JSlider(-180, 180, 0);
            rotacionSliders[i].addChangeListener(e -> actualizarRotacion());
            agregarSlider("Rotación " + ejes[i], rotacionSliders[i]);
        }

        lineasButton = new JButton("Líneas");
        lineasPunteadasButton = new JButton("Líneas Punteadas");
        puntosButton = new JButton("Puntos");
        polygonButton = new JButton("Polígono");
        transparenteButton = new JButton("Transparente");
        coloresAleatoriosButton = new JButton("Colores Aleatorios");
        wireframeButton = new JButton("Wireframe");
        neonButton = new JButton("Neón");
        metalicoButton = new JButton("Metálico");
        plasticoButton = new JButton("Plástico");
        flatButton = new JButton("Flat");
        gouraudButton = new JButton("Gouraud");

        oroButton = new JButton("Oro");
        rubiButton = new JButton("Rubí");
        cristalButton = new JButton("Cristal");
        emisivoButton = new JButton("Emisivo");

        lineasButton.addActionListener(e -> cambiarModo("LINEAS"));
        lineasPunteadasButton.addActionListener(e -> cambiarModo("LINEAS_PUNTEADAS"));
        puntosButton.addActionListener(e -> cambiarModo("PUNTOS"));
        polygonButton.addActionListener(e -> cambiarModo("POLYGON"));
        transparenteButton.addActionListener(e -> cambiarModo("TRANSPARENTE"));
        coloresAleatoriosButton.addActionListener(e -> cambiarModo("COLORES_ALEATORIOS"));
        wireframeButton.addActionListener(e -> cambiarModo("WIREFRAME"));
        neonButton.addActionListener(e -> cambiarModo("NEON"));
        metalicoButton.addActionListener(e -> cambiarModo("METALICO"));
        plasticoButton.addActionListener(e -> cambiarModo("PLASTICO"));
        flatButton.addActionListener(e -> cambiarModo("FLAT"));
        gouraudButton.addActionListener(e -> cambiarModo("GOURAUD"));

        oroButton.addActionListener(e -> cambiarModo("ORO"));
        rubiButton.addActionListener(e -> cambiarModo("RUBI"));
        cristalButton.addActionListener(e -> cambiarModo("CRISTAL"));
        emisivoButton.addActionListener(e -> cambiarModo("EMISIVO"));

        aplicarEstiloMinimalista(lineasButton);
        aplicarEstiloMinimalista(lineasPunteadasButton);
        aplicarEstiloMinimalista(puntosButton);
        aplicarEstiloMinimalista(polygonButton);
        aplicarEstiloMinimalista(transparenteButton);
        aplicarEstiloMinimalista(coloresAleatoriosButton);
        aplicarEstiloMinimalista(wireframeButton);
        aplicarEstiloMinimalista(neonButton);
        aplicarEstiloMinimalista(metalicoButton);
        aplicarEstiloMinimalista(plasticoButton);
        aplicarEstiloMinimalista(flatButton);
        aplicarEstiloMinimalista(gouraudButton);
        aplicarEstiloMinimalista(oroButton);
        aplicarEstiloMinimalista(rubiButton);
        aplicarEstiloMinimalista(cristalButton);
        aplicarEstiloMinimalista(emisivoButton);

        JPanel botonesPanel = new JPanel(new GridLayout(4, 4, 5, 5));

        botonesPanel.add(polygonButton);
        botonesPanel.add(flatButton);
        botonesPanel.add(gouraudButton);
        botonesPanel.add(transparenteButton);

        botonesPanel.add(lineasButton);
        botonesPanel.add(lineasPunteadasButton);
        botonesPanel.add(puntosButton);
        botonesPanel.add(wireframeButton);

        botonesPanel.add(plasticoButton);
        botonesPanel.add(metalicoButton);
        botonesPanel.add(neonButton);
        botonesPanel.add(emisivoButton);

        botonesPanel.add(oroButton);
        botonesPanel.add(rubiButton);
        botonesPanel.add(cristalButton);
        botonesPanel.add(coloresAleatoriosButton);

        panel.add(Box.createVerticalStrut(10));
        panel.add(botonesPanel);
    }

    private void agregarSlider(String label, JSlider slider) {
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel(label));
        panel.add(slider);
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

    private void actualizarEscala() {
        if (escalaTG != null) {
            Transform3D transform = new Transform3D();
            double escala = escalaSlider.getValue() / 100.0;
            transform.setScale(escala);
            escalaTG.setTransform(transform);
        }
    }

    private void actualizarPosicion() {
        if (posicionTG != null) {
            Transform3D transform = new Transform3D();
            Vector3f posicion = new Vector3f(
                    posicionSliders[0].getValue() / 60.0f,
                    posicionSliders[1].getValue() / 80.0f,
                    posicionSliders[2].getValue() / 30.0f
            );
            transform.setTranslation(posicion);
            posicionTG.setTransform(transform);
        }
    }

    private void actualizarRotacion() {
        if (rotacionManualTG != null) {
            Transform3D transformManual = new Transform3D();

            Transform3D rotacionX = new Transform3D();
            rotacionX.rotX(Math.toRadians(rotacionSliders[0].getValue()));

            Transform3D rotacionY = new Transform3D();
            rotacionY.rotY(Math.toRadians(rotacionSliders[1].getValue()));

            Transform3D rotacionZ = new Transform3D();
            rotacionZ.rotZ(Math.toRadians(rotacionSliders[2].getValue()));

            transformManual.mul(rotacionX);
            transformManual.mul(rotacionY);
            transformManual.mul(rotacionZ);

            rotacionManualTG.setTransform(transformManual);
        }
    }

    private void cambiarModo(String modo) {
        if (figuras != null) {
            figuras.cambiarModoVisualizacion(modo);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setTransformGroups(TransformGroup escala, TransformGroup posicion, TransformGroup rotacion, TransformGroup rotacionManual) {
        this.escalaTG = escala;
        this.posicionTG = posicion;
        this.rotacionTG = rotacion;
        this.rotacionManualTG = rotacionManual;
        reiniciarSliders();
    }

    public void setFiguras(Figuras figuras) {
        this.figuras = figuras;
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

    public void mostrarSliders() {
        panel.setVisible(true);
    }

    public void ocultarSliders() {
        panel.setVisible(false);
    }
}
