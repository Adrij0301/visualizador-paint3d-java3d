package paint3d;

import java.awt.Component;
import java.awt.Dimension;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.*;
import javax.vecmath.Vector3f;

public class ControlesGraficas {

    private final JPanel panel;
    private final JSlider escalaSlider;
    private final JSlider[] posicionSliders;
    private final JSlider[] rotacionSliders;

    private TransformGroup escalaTG;
    private TransformGroup posicionTG;
    private TransformGroup rotacionManualTG;

    public ControlesGraficas() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(435, 650));

        JLabel titulo = new JLabel("Controles de Gráfica", JLabel.CENTER);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(8));
        panel.add(titulo);

        escalaSlider = new JSlider(50, 200, 100);
        escalaSlider.addChangeListener(e -> actualizarEscala());
        agregarSlider("Escala", escalaSlider);

        posicionSliders = new JSlider[3];
        rotacionSliders = new JSlider[3];
        String[] ejes = {"X", "Y", "Z"};

        for (int i = 0; i < 3; i++) {
            posicionSliders[i] = new JSlider(-100, 100, 0);
            posicionSliders[i].addChangeListener(e -> actualizarPosicion());
            agregarSlider("Posición " + ejes[i], posicionSliders[i]);
        }

        for (int i = 0; i < 3; i++) {
            rotacionSliders[i] = new JSlider(-180, 180, 0);
            rotacionSliders[i].addChangeListener(e -> actualizarRotacion());
            agregarSlider("Rotación " + ejes[i], rotacionSliders[i]);
        }

        panel.add(Box.createVerticalGlue());
    }

    private void agregarSlider(String texto, JSlider slider) {
        panel.add(Box.createVerticalStrut(7));
        JLabel label = new JLabel(texto, JLabel.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(slider);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setTransformGroups(TransformGroup escala, TransformGroup posicion,
            TransformGroup rotacionManual) {
        escalaTG = escala;
        posicionTG = posicion;
        rotacionManualTG = rotacionManual;
        reiniciarSliders();
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
                posicionSliders[0].getValue() / 65.0f,
                posicionSliders[1].getValue() / 65.0f,
                posicionSliders[2].getValue() / 35.0f));
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
}