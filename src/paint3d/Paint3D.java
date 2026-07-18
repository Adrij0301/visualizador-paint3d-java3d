package paint3d;

import javax.swing.*;
import java.awt.*;
import javax.media.j3d.Canvas3D;

public class Paint3D extends JFrame {

    private Figuras figuras;
    private Graficas graficas;
    private Arreglos arreglos;
    private Texturas texturas;
    private Controles controles;
    private ControlesTexturas controlesTexturas;
    private ControlesGraficas controlesGraficas;
    private Custom custom;
    private JPanel displayPanel;
    private JPanel controlsPanel;

    public Paint3D() {
        setTitle("PAINT 3D - JAOC");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        figuras = new Figuras();
        graficas = new Graficas();
        arreglos = new Arreglos();
        texturas = new Texturas();
        controles = new Controles();
        controlesTexturas = new ControlesTexturas();
        controlesGraficas = new ControlesGraficas();
        custom = new Custom();

        controles.setFiguras(figuras);
        controlesTexturas.setTexturas(texturas);

        JMenuBar menuBar = new JMenuBar();

        JMenu figurasMenu = new JMenu("Figuras");
        String[] figurasOpciones = {"Cubo", "Esfera", "Cono", "Cilindro", "Tetraedro", "Dodecaedro", "Octaedro"};
        for (String figura : figurasOpciones) {
            JMenuItem item = new JMenuItem(figura);
            figurasMenu.add(item);
            item.addActionListener(e -> {
                cambiarCanvas(figuras.getCanvas());
                figuras.mostrarFigura(e.getActionCommand());
                controles.setTransformGroups(
                        figuras.getEscalaTG(),
                        figuras.getPosicionTG(),
                        figuras.getRotacionTG(),
                        figuras.getRotacionManualTG()
                );
                mostrarControles("Controles");
            });
        }

        JMenu graficasMenu = new JMenu("Gráficas");

        String[] graficasOpciones = {
            "Gráfica 1 (Onda)", "Gráfica 2 (Onda Frec)", "Gráfica 3 (Sombrero)", "Gráfica 4 (Compleja)", "Gráfica 5 (Tangente)",
            "Gráfica 6 (Silla Montar)", "Gráfica 7 (Rejilla)", "Gráfica 8 (Exponencial)", "Gráfica 9 (Silla Mono)", "Gráfica 10 (Espiral)"
        };
        for (int i = 0; i < graficasOpciones.length; i++) {
            String graficaNombre = graficasOpciones[i];
            JMenuItem graficaItem = new JMenuItem(graficaNombre);
            graficasMenu.add(graficaItem);
            int graficaIndex = i + 1;
            graficaItem.addActionListener(e -> {
                cambiarCanvas(graficas.getCanvas());
                graficas.mostrarGrafica("grafica" + graficaIndex);
                controlesGraficas.setTransformGroups(
                        graficas.getEscalaTG(),
                        graficas.getPosicionTG(),
                        graficas.getRotacionManualTG()
                );
                mostrarControles("ControlesGraficas");
            });
        }

        JMenu arreglosMenu = new JMenu("Arreglos");
        String[] arreglosOpciones = {
            "Arreglo 1", "Arreglo 2", "Arreglo 3", "Arreglo 4",
            "Arreglo 5", "Arreglo 6", "Arreglo 7", "Arreglo 8",
            "Arreglo 9", "Arreglo 10"
        };
        for (int i = 0; i < arreglosOpciones.length; i++) {
            String arregloNombre = arreglosOpciones[i];
            JMenuItem arregloItem = new JMenuItem(arregloNombre);
            arreglosMenu.add(arregloItem);
            int arregloIndex = i + 1;
            arregloItem.addActionListener(e -> {
                cambiarCanvas(arreglos.getCanvas());
                arreglos.mostrarArreglo("arreglo" + arregloIndex);
                mostrarControles(null);
            });
        }

        JMenu texturasMenu = new JMenu("Texturas");
        String[] texturasOpciones = {"Abrir"};
        for (String textura : texturasOpciones) {
            JMenuItem texturaItem = new JMenuItem(textura);
            texturasMenu.add(texturaItem);
            texturaItem.addActionListener(e -> {
                cambiarCanvas(texturas.getCanvas());
                mostrarControles("ControlesTexturas");
            });
        }

        JMenu customMenu = new JMenu("Custom");
        JMenuItem cargarObjeto = new JMenuItem("Selecciona el archivo \".obj\"");
        customMenu.add(cargarObjeto);
        cargarObjeto.addActionListener(e -> {
            cambiarCanvas(custom.getCanvas());
            custom.cargarObjeto();
            mostrarControles(null);
        });

        menuBar.add(figurasMenu);
        menuBar.add(graficasMenu);
        menuBar.add(arreglosMenu);
        menuBar.add(texturasMenu);
        menuBar.add(customMenu);

        setJMenuBar(menuBar);

        displayPanel = new JPanel(new CardLayout());
        displayPanel.add(figuras.getCanvas(), "Figuras");
        displayPanel.add(graficas.getCanvas(), "Graficas");
        displayPanel.add(arreglos.getCanvas(), "Arreglos");
        displayPanel.add(texturas.getCanvas(), "Texturas");
        displayPanel.add(custom.getCanvas(), "Custom");

        controlsPanel = new JPanel(new CardLayout());
        controlsPanel.add(controles.getPanel(), "Controles");
        controlsPanel.add(controlesTexturas.getPanel(), "ControlesTexturas");
        controlsPanel.add(controlesGraficas.getPanel(), "ControlesGraficas");

        add(displayPanel, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private void cambiarCanvas(Canvas3D canvas) {
        CardLayout cl = (CardLayout) displayPanel.getLayout();
        if (canvas == figuras.getCanvas()) {
            cl.show(displayPanel, "Figuras");
        } else if (canvas == graficas.getCanvas()) {
            cl.show(displayPanel, "Graficas");
        } else if (canvas == arreglos.getCanvas()) {
            cl.show(displayPanel, "Arreglos");
        } else if (canvas == texturas.getCanvas()) {
            cl.show(displayPanel, "Texturas");
        } else if (canvas == custom.getCanvas()) {
            cl.show(displayPanel, "Custom");
        }
    }

    private void mostrarControles(String panel) {
        CardLayout cl = (CardLayout) controlsPanel.getLayout();
        if (panel != null) {
            controlsPanel.setVisible(true);
            cl.show(controlsPanel, panel);
        } else {
            controlsPanel.setVisible(false);
        }
    }

    public static void main(String[] args) {

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el LookAndFeel del sistema.");
        }

        new Paint3D();
    }
}
