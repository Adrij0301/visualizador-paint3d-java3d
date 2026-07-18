package paint3d;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.util.ArrayList;
import java.util.List;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Figuras {

    private final Canvas3D canvas;
    private final SimpleUniverse universe;
    private BranchGroup currentGroup;

    private TransformGroup escalaTG;
    private TransformGroup posicionTG;
    private TransformGroup rotacionTG;
    private TransformGroup rotacionAutoTG;
    private TransformGroup rotacionManualTG;

    private final List<Shape3D> partesActivas = new ArrayList<>();
    private final List<Shape3D> tapasActivas = new ArrayList<>();
    private String modoVisualizacion = "POLYGON";

    public Figuras() {
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
        currentGroup = null;
    }

    public Canvas3D getCanvas() {
        return canvas;
    }

    public TransformGroup getEscalaTG() {
        return escalaTG;
    }

    public TransformGroup getPosicionTG() {
        return posicionTG;
    }

    public TransformGroup getRotacionTG() {
        return rotacionTG;
    }

    public TransformGroup getRotacionManualTG() {
        return rotacionManualTG;
    }

    public void mostrarFigura(String figura) {
        if (currentGroup != null) {
            currentGroup.detach();
            currentGroup = null;
        }

        partesActivas.clear();
        tapasActivas.clear();
        modoVisualizacion = "POLYGON";

        BranchGroup nuevoGrupo = new BranchGroup();
        nuevoGrupo.setCapability(BranchGroup.ALLOW_DETACH);

        Node figura3D;
        switch (figura.toUpperCase()) {
            case "CUBO":
                figura3D = crearCubo();
                break;
            case "ESFERA":
                figura3D = crearEsfera();
                break;
            case "CONO":
                figura3D = crearCono();
                break;
            case "CILINDRO":
                figura3D = crearCilindro();
                break;
            case "TETRAEDRO":
                figura3D = crearTetraedro();
                break;
            case "DODECAEDRO":
                figura3D = crearDodecaedro();
                break;
            case "OCTAEDRO":
                figura3D = crearOctaedro();
                break;
            default:
                System.out.println("Figura no implementada: " + figura);
                return;
        }

        escalaTG = crearTransformGroup();
        posicionTG = crearTransformGroup();
        rotacionManualTG = crearTransformGroup();
        rotacionAutoTG = crearTransformGroup();
        rotacionTG = rotacionAutoTG;

        Transform3D escalaBaseTransform = new Transform3D();
        escalaBaseTransform.setScale(0.68);
        TransformGroup escalaBaseTG = new TransformGroup(escalaBaseTransform);

        Transform3D inclinacionBaseTransform = new Transform3D();
        inclinacionBaseTransform.rotX(Math.toRadians(18.0));
        TransformGroup inclinacionBaseTG = new TransformGroup(inclinacionBaseTransform);

        escalaBaseTG.addChild(figura3D);
        inclinacionBaseTG.addChild(escalaBaseTG);
        rotacionAutoTG.addChild(inclinacionBaseTG);
        rotacionManualTG.addChild(rotacionAutoTG);
        posicionTG.addChild(rotacionManualTG);
        escalaTG.addChild(posicionTG);
        nuevoGrupo.addChild(escalaTG);

        Alpha alpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(alpha, rotacionAutoTG);
        rotator.setSchedulingBounds(crearBounds());
        rotacionAutoTG.addChild(rotator);

        MouseRotate mouseRotate = new MouseRotate(rotacionManualTG);
        mouseRotate.setSchedulingBounds(crearBounds());
        rotacionManualTG.addChild(mouseRotate);

        MouseTranslate mouseTranslate = new MouseTranslate(posicionTG);
        mouseTranslate.setSchedulingBounds(crearBounds());
        posicionTG.addChild(mouseTranslate);

        añadirIluminacion(nuevoGrupo);
        añadirFondo(nuevoGrupo);

        currentGroup = nuevoGrupo;
        universe.addBranchGraph(currentGroup);
    }

    private TransformGroup crearTransformGroup() {
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        tg.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        tg.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        return tg;
    }

    private BoundingSphere crearBounds() {
        return new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
    }

    public void cambiarModoVisualizacion(String modo) {
        if (partesActivas.isEmpty()) {
            System.out.println("No hay figura activa para cambiar el modo de visualización.");
            return;
        }

        modoVisualizacion = modo;

        if ("POLYGON".equals(modo)) {
            restaurarAparienciasOriginales();
            return;
        }

        if ("COLORES_ALEATORIOS".equals(modo)) {
            for (Shape3D shape : partesActivas) {
                shape.setAppearance(crearAparienciaColorAleatorio());
            }
            return;
        }

        Appearance apariencia = crearAparienciaParaModo(modo);
        if (apariencia == null) {
            System.out.println("Modo de visualización desconocido: " + modo);
            return;
        }

        for (Shape3D shape : partesActivas) {
            shape.setAppearance(apariencia);
        }

        if (esModoSinTapas(modo)) {
            Appearance oculta = crearAparienciaOculta();
            for (Shape3D tapa : tapasActivas) {
                tapa.setAppearance(oculta);
            }
        }
    }

    private boolean esModoSinTapas(String modo) {
        return "LINEAS".equals(modo)
                || "LINEAS_PUNTEADAS".equals(modo)
                || "PUNTOS".equals(modo)
                || "WIREFRAME".equals(modo)
                || "NEON".equals(modo);
    }

    private void restaurarAparienciasOriginales() {
        for (Shape3D shape : partesActivas) {
            Object data = shape.getUserData();
            if (data instanceof Appearance) {
                shape.setAppearance((Appearance) data);
            }
        }
    }

    private Appearance crearAparienciaParaModo(String modo) {
        Appearance apariencia = new Appearance();
        PolygonAttributes polygon = new PolygonAttributes();
        polygon.setCullFace(PolygonAttributes.CULL_NONE);
        polygon.setBackFaceNormalFlip(true);
        apariencia.setPolygonAttributes(polygon);

        switch (modo) {
            case "LINEAS":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_LINE);
                apariencia.setLineAttributes(crearLineas(2.0f, LineAttributes.PATTERN_SOLID));
                apariencia.setColoringAttributes(crearColor(new Color3f(0.92f, 0.95f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
                break;

            case "LINEAS_PUNTEADAS":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_LINE);
                apariencia.setLineAttributes(crearLineas(2.4f, LineAttributes.PATTERN_DASH));
                apariencia.setColoringAttributes(crearColor(new Color3f(0.92f, 0.95f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
                break;

            case "PUNTOS":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_POINT);
                PointAttributes puntos = new PointAttributes();
                puntos.setPointSize(6.0f);
                puntos.setPointAntialiasingEnable(true);
                apariencia.setPointAttributes(puntos);
                apariencia.setColoringAttributes(crearColor(new Color3f(1.0f, 1.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
                break;

            case "WIREFRAME":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_LINE);
                polygon.setCullFace(PolygonAttributes.CULL_BACK);
                apariencia.setLineAttributes(crearLineas(1.2f, LineAttributes.PATTERN_SOLID));
                apariencia.setColoringAttributes(crearColor(new Color3f(0.62f, 0.70f, 0.80f), ColoringAttributes.SHADE_GOURAUD));
                break;

            case "NEON":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_LINE);
                apariencia.setLineAttributes(crearLineas(4.5f, LineAttributes.PATTERN_SOLID));
                apariencia.setMaterial(crearMaterial(
                        new Color3f(0.0f, 0.05f, 0.06f),
                        new Color3f(0.0f, 0.10f, 0.12f),
                        new Color3f(0.8f, 1.0f, 1.0f),
                        new Color3f(0.0f, 0.95f, 1.0f),
                        100.0f));
                apariencia.setColoringAttributes(crearColor(new Color3f(0.0f, 1.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
                break;

            case "TRANSPARENTE":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_FILL);
                apariencia.setMaterial(crearMaterial(
                        new Color3f(0.06f, 0.09f, 0.13f),
                        new Color3f(0.20f, 0.35f, 0.48f),
                        new Color3f(0.95f, 0.98f, 1.0f),
                        new Color3f(0.0f, 0.0f, 0.0f),
                        110.0f));
                apariencia.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.58f));
                apariencia.setRenderingAttributes(crearRenderTransparente());
                break;

            case "FLAT":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_FILL);
                apariencia.setColoringAttributes(crearColor(new Color3f(1.0f, 1.0f, 1.0f), ColoringAttributes.SHADE_FLAT));
                apariencia.setMaterial(crearMaterial(
                        new Color3f(0.18f, 0.015f, 0.02f),
                        new Color3f(0.90f, 0.08f, 0.10f),
                        new Color3f(0.08f, 0.08f, 0.08f),
                        new Color3f(0.0f, 0.0f, 0.0f),
                        2.0f));
                break;

            case "GOURAUD":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_FILL);
                apariencia.setColoringAttributes(crearColor(new Color3f(1.0f, 1.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
                apariencia.setMaterial(crearMaterial(
                        new Color3f(0.15f, 0.015f, 0.02f),
                        new Color3f(0.92f, 0.08f, 0.11f),
                        new Color3f(1.0f, 0.72f, 0.72f),
                        new Color3f(0.0f, 0.0f, 0.0f),
                        70.0f));
                break;

            case "PLASTICO":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_FILL);
                apariencia.setColoringAttributes(crearColor(new Color3f(1.0f, 1.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
                apariencia.setMaterial(crearMaterial(
                        new Color3f(0.04f, 0.02f, 0.07f),
                        new Color3f(0.40f, 0.08f, 0.82f),
                        new Color3f(0.95f, 0.90f, 1.0f),
                        new Color3f(0.0f, 0.0f, 0.0f),
                        48.0f));
                break;

            case "METALICO":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_FILL);
                apariencia.setColoringAttributes(crearColor(new Color3f(1.0f, 1.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
                apariencia.setMaterial(crearMaterial(
                        new Color3f(0.08f, 0.09f, 0.11f),
                        new Color3f(0.28f, 0.31f, 0.36f),
                        new Color3f(1.0f, 1.0f, 1.0f),
                        new Color3f(0.0f, 0.0f, 0.0f),
                        126.0f));
                break;

            case "EMISIVO":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_FILL);
                apariencia.setMaterial(crearMaterial(
                        new Color3f(0.0f, 0.0f, 0.0f),
                        new Color3f(0.02f, 0.05f, 0.06f),
                        new Color3f(0.35f, 0.95f, 1.0f),
                        new Color3f(0.0f, 0.72f, 1.0f),
                        96.0f));
                break;

            case "ORO":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_FILL);
                apariencia.setColoringAttributes(crearColor(new Color3f(1.0f, 1.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
                apariencia.setMaterial(crearMaterial(
                        new Color3f(0.247f, 0.199f, 0.075f),
                        new Color3f(0.751f, 0.606f, 0.226f),
                        new Color3f(1.0f, 0.88f, 0.42f),
                        new Color3f(0.0f, 0.0f, 0.0f),
                        112.0f));
                break;

            case "RUBI":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_FILL);
                apariencia.setColoringAttributes(crearColor(new Color3f(1.0f, 1.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
                apariencia.setMaterial(crearMaterial(
                        new Color3f(0.16f, 0.01f, 0.02f),
                        new Color3f(0.62f, 0.015f, 0.035f),
                        new Color3f(1.0f, 0.62f, 0.70f),
                        new Color3f(0.04f, 0.0f, 0.0f),
                        126.0f));
                apariencia.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.16f));
                break;

            case "CRISTAL":
                polygon.setPolygonMode(PolygonAttributes.POLYGON_FILL);
                apariencia.setColoringAttributes(crearColor(new Color3f(0.82f, 0.94f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
                apariencia.setMaterial(crearMaterial(
                        new Color3f(0.03f, 0.07f, 0.10f),
                        new Color3f(0.16f, 0.34f, 0.45f),
                        new Color3f(1.0f, 1.0f, 1.0f),
                        new Color3f(0.015f, 0.05f, 0.07f),
                        128.0f));
                apariencia.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.67f));
                apariencia.setRenderingAttributes(crearRenderTransparente());
                break;

            default:
                return null;
        }

        return apariencia;
    }

    private ColoringAttributes crearColor(Color3f color, int sombreado) {
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(color);
        ca.setShadeModel(sombreado);
        return ca;
    }

    private LineAttributes crearLineas(float ancho, int patron) {
        LineAttributes lineas = new LineAttributes();
        lineas.setLineWidth(ancho);
        lineas.setLinePattern(patron);
        lineas.setLineAntialiasingEnable(true);
        return lineas;
    }

    private RenderingAttributes crearRenderTransparente() {
        RenderingAttributes render = new RenderingAttributes();
        render.setDepthBufferEnable(true);
        render.setDepthBufferWriteEnable(false);
        return render;
    }

    private Appearance crearAparienciaOculta() {
        Appearance apariencia = new Appearance();
        RenderingAttributes render = new RenderingAttributes();
        render.setVisible(false);
        apariencia.setRenderingAttributes(render);
        return apariencia;
    }

    private Appearance crearAparienciaColorAleatorio() {
        float r = 0.25f + (float) Math.random() * 0.75f;
        float g = 0.25f + (float) Math.random() * 0.75f;
        float b = 0.25f + (float) Math.random() * 0.75f;

        Appearance apariencia = new Appearance();
        PolygonAttributes poly = new PolygonAttributes();
        poly.setCullFace(PolygonAttributes.CULL_NONE);
        poly.setBackFaceNormalFlip(true);
        apariencia.setPolygonAttributes(poly);
        apariencia.setMaterial(crearMaterial(
                new Color3f(r * 0.15f, g * 0.15f, b * 0.15f),
                new Color3f(r * 0.55f, g * 0.55f, b * 0.55f),
                new Color3f(1.0f, 1.0f, 1.0f),
                new Color3f(r * 0.18f, g * 0.18f, b * 0.18f),
                80.0f));
        return apariencia;
    }

    private Material crearMaterial(Color3f ambiente, Color3f difuso, Color3f especular,
            Color3f emisivo, float brillo) {
        Material material = new Material();
        material.setAmbientColor(ambiente);
        material.setDiffuseColor(difuso);
        material.setSpecularColor(especular);
        material.setEmissiveColor(emisivo);
        material.setShininess(brillo);
        material.setLightingEnable(true);
        return material;
    }

    private Appearance crearAparienciaBase() {
        Appearance apariencia = new Appearance();
        PolygonAttributes poly = new PolygonAttributes();
        poly.setCullFace(PolygonAttributes.CULL_NONE);
        poly.setBackFaceNormalFlip(true);
        apariencia.setPolygonAttributes(poly);
        apariencia.setMaterial(crearMaterial(
                new Color3f(0.035f, 0.02f, 0.09f),
                new Color3f(0.28f, 0.10f, 0.78f),
                new Color3f(0.88f, 0.82f, 1.0f),
                new Color3f(0.0f, 0.0f, 0.0f),
                72.0f));
        return apariencia;
    }

    private void registrarShape(Shape3D shape, Appearance aparienciaOriginal, boolean esTapa) {
        shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        shape.setUserData(aparienciaOriginal);
        partesActivas.add(shape);
        if (esTapa) {
            tapasActivas.add(shape);
        }
    }

    private Node crearCubo() {
        float[] vertices = {
            -0.55f, -0.55f, 0.55f, 0.55f, -0.55f, 0.55f, 0.55f, 0.55f, 0.55f,
            -0.55f, -0.55f, 0.55f, 0.55f, 0.55f, 0.55f, -0.55f, 0.55f, 0.55f,
            0.55f, -0.55f, -0.55f, -0.55f, -0.55f, -0.55f, -0.55f, 0.55f, -0.55f,
            0.55f, -0.55f, -0.55f, -0.55f, 0.55f, -0.55f, 0.55f, 0.55f, -0.55f,
            -0.55f, -0.55f, -0.55f, -0.55f, -0.55f, 0.55f, -0.55f, 0.55f, 0.55f,
            -0.55f, -0.55f, -0.55f, -0.55f, 0.55f, 0.55f, -0.55f, 0.55f, -0.55f,
            0.55f, -0.55f, 0.55f, 0.55f, -0.55f, -0.55f, 0.55f, 0.55f, -0.55f,
            0.55f, -0.55f, 0.55f, 0.55f, 0.55f, -0.55f, 0.55f, 0.55f, 0.55f,
            -0.55f, 0.55f, 0.55f, 0.55f, 0.55f, 0.55f, 0.55f, 0.55f, -0.55f,
            -0.55f, 0.55f, 0.55f, 0.55f, 0.55f, -0.55f, -0.55f, 0.55f, -0.55f,
            -0.55f, -0.55f, -0.55f, 0.55f, -0.55f, -0.55f, 0.55f, -0.55f, 0.55f,
            -0.55f, -0.55f, -0.55f, 0.55f, -0.55f, 0.55f, -0.55f, -0.55f, 0.55f
        };
        return crearShapeTriangulado(vertices);
    }

    private Node crearEsfera() {
        Appearance apariencia = crearAparienciaBase();
        Sphere esfera = new Sphere(0.62f, Sphere.GENERATE_NORMALS, 96, apariencia);
        Shape3D shape = esfera.getShape();
        registrarShape(shape, apariencia, false);
        return esfera;
    }

    private Node crearCono() {
        Appearance apariencia = crearAparienciaBase();
        Cone cono = new Cone(0.55f, 1.15f, Cone.GENERATE_NORMALS, 96, 8, apariencia);

        Shape3D cuerpo = cono.getShape(Cone.BODY);
        Shape3D tapa = cono.getShape(Cone.CAP);
        registrarShape(cuerpo, apariencia, false);
        registrarShape(tapa, apariencia, true);

        return cono;
    }

    private Node crearCilindro() {
        Appearance apariencia = crearAparienciaBase();
        Cylinder cilindro = new Cylinder(0.52f, 1.10f, Cylinder.GENERATE_NORMALS, 96, 8, apariencia);

        Shape3D cuerpo = cilindro.getShape(Cylinder.BODY);
        Shape3D superior = cilindro.getShape(Cylinder.TOP);
        Shape3D inferior = cilindro.getShape(Cylinder.BOTTOM);
        registrarShape(cuerpo, apariencia, false);
        registrarShape(superior, apariencia, true);
        registrarShape(inferior, apariencia, true);

        return cilindro;
    }

    private Node crearTetraedro() {
        float[] vertices = {
            0.62f, 0.62f, 0.62f,
            -0.62f, -0.62f, 0.62f,
            -0.62f, 0.62f, -0.62f,
            0.62f, 0.62f, 0.62f,
            -0.62f, 0.62f, -0.62f,
            0.62f, -0.62f, -0.62f,
            0.62f, 0.62f, 0.62f,
            0.62f, -0.62f, -0.62f,
            -0.62f, -0.62f, 0.62f,
            -0.62f, -0.62f, 0.62f,
            0.62f, -0.62f, -0.62f,
            -0.62f, 0.62f, -0.62f
        };
        return crearShapeTriangulado(vertices);
    }

    private Node crearOctaedro() {
        Point3f[] v = {
            new Point3f(0.0f, 0.72f, 0.0f),
            new Point3f(0.72f, 0.0f, 0.0f),
            new Point3f(0.0f, 0.0f, 0.72f),
            new Point3f(-0.72f, 0.0f, 0.0f),
            new Point3f(0.0f, 0.0f, -0.72f),
            new Point3f(0.0f, -0.72f, 0.0f)
        };
        int[][] caras = {
            {0, 1, 2}, {0, 2, 3}, {0, 3, 4}, {0, 4, 1},
            {5, 2, 1}, {5, 3, 2}, {5, 4, 3}, {5, 1, 4}
        };
        return crearShapeDesdeCarasTriangulares(v, caras);
    }

    private Node crearDodecaedro() {
        float phi = (float) ((1.0 + Math.sqrt(5.0)) / 2.0);
        float invPhi = 1.0f / phi;
        float s = 0.38f;

        Point3f[] vertices = {
            new Point3f(-s, -s, -s), new Point3f(-s, -s, s),
            new Point3f(-s, s, -s), new Point3f(-s, s, s),
            new Point3f(s, -s, -s), new Point3f(s, -s, s),
            new Point3f(s, s, -s), new Point3f(s, s, s),
            new Point3f(0, -phi * s, -invPhi * s), new Point3f(0, -phi * s, invPhi * s),
            new Point3f(0, phi * s, -invPhi * s), new Point3f(0, phi * s, invPhi * s),
            new Point3f(-phi * s, -invPhi * s, 0), new Point3f(-phi * s, invPhi * s, 0),
            new Point3f(phi * s, -invPhi * s, 0), new Point3f(phi * s, invPhi * s, 0),
            new Point3f(-invPhi * s, 0, -phi * s), new Point3f(-invPhi * s, 0, phi * s),
            new Point3f(invPhi * s, 0, -phi * s), new Point3f(invPhi * s, 0, phi * s)
        };

        int[][] caras = {
            {1, 12, 0, 8, 9},
            {2, 16, 0, 12, 13},
            {4, 8, 0, 16, 18},
            {13, 12, 1, 17, 3},
            {19, 17, 1, 9, 5},
            {11, 10, 2, 13, 3},
            {18, 16, 2, 10, 6},
            {7, 11, 3, 17, 19},
            {9, 8, 4, 14, 5},
            {15, 14, 4, 18, 6},
            {7, 19, 5, 14, 15},
            {7, 15, 6, 10, 11}
        };

        int triangulos = caras.length * 5;
        TriangleArray geometria = new TriangleArray(
                triangulos * 3,
                GeometryArray.COORDINATES | GeometryArray.NORMALS);

        int cursor = 0;
        for (int[] cara : caras) {
            Point3f centro = new Point3f();
            for (int indice : cara) {
                centro.add(vertices[indice]);
            }
            centro.scale(1.0f / cara.length);

            Vector3f normal = calcularNormalCara(vertices[cara[0]], vertices[cara[1]], vertices[cara[2]], centro);

            for (int i = 0; i < cara.length; i++) {
                Point3f a = vertices[cara[i]];
                Point3f b = vertices[cara[(i + 1) % cara.length]];
                geometria.setCoordinate(cursor, centro);
                geometria.setNormal(cursor++, normal);
                geometria.setCoordinate(cursor, a);
                geometria.setNormal(cursor++, normal);
                geometria.setCoordinate(cursor, b);
                geometria.setNormal(cursor++, normal);
            }
        }

        Appearance apariencia = crearAparienciaBase();
        Shape3D shape = new Shape3D(geometria, apariencia);
        registrarShape(shape, apariencia, false);
        return shape;
    }

    private Vector3f calcularNormalCara(Point3f a, Point3f b, Point3f c, Point3f centroCara) {
        Vector3f ab = new Vector3f();
        ab.sub(b, a);
        Vector3f ac = new Vector3f();
        ac.sub(c, a);
        Vector3f normal = new Vector3f();
        normal.cross(ab, ac);
        normal.normalize();

        Vector3f haciaAfuera = new Vector3f(centroCara);
        if (normal.dot(haciaAfuera) < 0.0f) {
            normal.negate();
        }
        return normal;
    }

    private Node crearShapeDesdeCarasTriangulares(Point3f[] vertices, int[][] caras) {
        float[] triangulos = new float[caras.length * 9];
        int cursor = 0;
        for (int[] cara : caras) {
            for (int indice : cara) {
                Point3f p = vertices[indice];
                triangulos[cursor++] = p.x;
                triangulos[cursor++] = p.y;
                triangulos[cursor++] = p.z;
            }
        }
        return crearShapeTriangulado(triangulos);
    }

    private Shape3D crearShapeTriangulado(float[] vertices) {
        GeometryInfo geoInfo = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
        geoInfo.setCoordinates(vertices);
        NormalGenerator normalGenerator = new NormalGenerator(0.0);
        normalGenerator.generateNormals(geoInfo);

        Appearance apariencia = crearAparienciaBase();
        Shape3D shape = new Shape3D(geoInfo.getGeometryArray(), apariencia);
        registrarShape(shape, apariencia, false);
        return shape;
    }

    private void añadirIluminacion(BranchGroup group) {
        BoundingSphere bounds = crearBounds();

        AmbientLight ambiente = new AmbientLight(new Color3f(0.16f, 0.17f, 0.22f));
        ambiente.setInfluencingBounds(bounds);
        group.addChild(ambiente);

        DirectionalLight principal = new DirectionalLight(
                new Color3f(1.0f, 0.94f, 0.88f),
                new Vector3f(-0.75f, -1.0f, -0.65f));
        principal.setInfluencingBounds(bounds);
        group.addChild(principal);

        DirectionalLight relleno = new DirectionalLight(
                new Color3f(0.30f, 0.44f, 0.78f),
                new Vector3f(0.85f, -0.15f, -0.45f));
        relleno.setInfluencingBounds(bounds);
        group.addChild(relleno);

        DirectionalLight contraluz = new DirectionalLight(
                new Color3f(0.55f, 0.24f, 0.80f),
                new Vector3f(0.10f, 0.45f, 1.0f));
        contraluz.setInfluencingBounds(bounds);
        group.addChild(contraluz);

        PointLight brilloFrontal = new PointLight(
                new Color3f(0.90f, 0.96f, 1.0f),
                new Point3f(0.0f, 0.85f, 1.9f),
                new Point3f(1.0f, 0.12f, 0.02f));
        brilloFrontal.setInfluencingBounds(bounds);
        group.addChild(brilloFrontal);
    }

    private void añadirFondo(BranchGroup group) {
        Background fondo = new Background(new Color3f(0.004f, 0.006f, 0.012f));
        fondo.setApplicationBounds(crearBounds());
        group.addChild(fondo);
    }

    public void actualizarModoCubo(String modo) {
        cambiarModoVisualizacion(modo);
    }
}
