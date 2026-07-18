package paint3d;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Graficas {

    private final Canvas3D canvas;
    private final SimpleUniverse universe;
    private BranchGroup currentGroup;

    private TransformGroup escalaTG;
    private TransformGroup posicionTG;
    private TransformGroup rotacionManualTG;
    private TransformGroup rotacionAutoTG;

    public Graficas() {
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
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

    public TransformGroup getRotacionManualTG() {
        return rotacionManualTG;
    }

    public void mostrarGrafica(String tipo) {
        if (currentGroup != null) {
            currentGroup.detach();
            currentGroup = null;
        }

        BranchGroup nuevoGrupo = new BranchGroup();
        nuevoGrupo.setCapability(BranchGroup.ALLOW_DETACH);
        nuevoGrupo.addChild(crearGraficaInteractiva(tipo));
        añadirIluminacion(nuevoGrupo);
        añadirFondo(nuevoGrupo);

        currentGroup = nuevoGrupo;
        universe.addBranchGraph(currentGroup);
    }

    private TransformGroup crearGraficaInteractiva(String tipo) {
        Shape3D grafica = crearSuperficie(tipo);

        Transform3D escalaBaseTransform = new Transform3D();
        escalaBaseTransform.setScale(0.34);
        TransformGroup escalaBaseTG = new TransformGroup(escalaBaseTransform);
        escalaBaseTG.addChild(grafica);

        Transform3D inclinacionBaseTransform = new Transform3D();
        inclinacionBaseTransform.rotX(Math.toRadians(18.0));
        TransformGroup inclinacionBaseTG = new TransformGroup(inclinacionBaseTransform);
        inclinacionBaseTG.addChild(escalaBaseTG);

        escalaTG = crearTransformGroup();
        posicionTG = crearTransformGroup();
        rotacionManualTG = crearTransformGroup();
        rotacionAutoTG = crearTransformGroup();

        rotacionAutoTG.addChild(inclinacionBaseTG);
        rotacionManualTG.addChild(rotacionAutoTG);
        posicionTG.addChild(rotacionManualTG);
        escalaTG.addChild(posicionTG);

        Alpha rotationAlpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rotacionAutoTG);
        rotator.setSchedulingBounds(crearBounds());
        rotacionAutoTG.addChild(rotator);

        PersistentRotationBehavior rotationBehavior = new PersistentRotationBehavior(rotationAlpha);
        rotationBehavior.setSchedulingBounds(crearBounds());
        rotacionAutoTG.addChild(rotationBehavior);

        MouseRotate mouseRotate = new MouseRotate(rotacionManualTG);
        mouseRotate.setSchedulingBounds(crearBounds());
        rotacionManualTG.addChild(mouseRotate);

        MouseTranslate mouseTranslate = new MouseTranslate(posicionTG);
        mouseTranslate.setSchedulingBounds(crearBounds());
        posicionTG.addChild(mouseTranslate);

        return escalaTG;
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

    private Shape3D crearSuperficie(String tipo) {
        int pasos = 100;
        float tamaño = 2.0f;
        float paso = tamaño / pasos;

        Point3f[] puntos = new Point3f[(pasos + 1) * (pasos + 1)];
        int[] indices = new int[pasos * pasos * 6];
        int contadorPuntos = 0;
        int contadorIndices = 0;

        for (int i = 0; i <= pasos; i++) {
            for (int j = 0; j <= pasos; j++) {
                float x = -1.0f + i * paso;
                float z = -1.0f + j * paso;
                float y = calcularEcuacion(x, z, tipo);
                puntos[contadorPuntos++] = new Point3f(x, y, z);
            }
        }

        for (int i = 0; i < pasos; i++) {
            for (int j = 0; j < pasos; j++) {
                int p0 = i * (pasos + 1) + j;
                int p1 = p0 + 1;
                int p2 = p0 + (pasos + 1);
                int p3 = p2 + 1;

                indices[contadorIndices++] = p0;
                indices[contadorIndices++] = p2;
                indices[contadorIndices++] = p1;

                indices[contadorIndices++] = p1;
                indices[contadorIndices++] = p2;
                indices[contadorIndices++] = p3;
            }
        }

        IndexedTriangleArray geometria = new IndexedTriangleArray(
                puntos.length,
                GeometryArray.COORDINATES | GeometryArray.NORMALS,
                indices.length);
        geometria.setCoordinates(0, puntos);
        geometria.setCoordinateIndices(0, indices);

        GeometryInfo geometryInfo = new GeometryInfo(geometria);
        NormalGenerator normalGen = new NormalGenerator();
        normalGen.generateNormals(geometryInfo);

        return new Shape3D(geometryInfo.getGeometryArray(), crearApariencia());
    }

    private float calcularEcuacion(float x, float z, String tipo) {
        float r = (float) Math.sqrt(x * x + z * z) + 0.01f;

        switch (tipo) {
            case "grafica1":
                return (float) Math.sin(Math.PI * x) * (float) Math.cos(Math.PI * z);
            case "grafica2":
                return (float) Math.sin(2 * Math.PI * x) * (float) Math.cos(2 * Math.PI * z);
            case "grafica3":
                return (float) (Math.sin(10 * r) / (10 * r));
            case "grafica4":
                return (float) Math.cos((x * x + z * z) * Math.sqrt(x * x * z * z));
            case "grafica5":
                return (float) Math.tanh(Math.sin(x * 5) * Math.cos(z * 5));
            case "grafica6":
                return x * x - z * z;
            case "grafica7":
                return (float) (Math.sin(5 * x) * 0.5 + Math.cos(5 * z) * 0.5);
            case "grafica8":
                return (float) (Math.exp(-r * r) * Math.cos(15 * r));
            case "grafica9":
                return (float) (x * x * x - 3 * x * z * z);
            case "grafica10":
                return (float) (Math.atan2(x, z) / (Math.PI * 2));
            default:
                return 0.0f;
        }
    }

    private Appearance crearApariencia() {
        Appearance apariencia = new Appearance();

        Material material = new Material();
        material.setAmbientColor(new Color3f(0.025f, 0.12f, 0.14f));
        material.setDiffuseColor(new Color3f(0.08f, 0.78f, 0.86f));
        material.setSpecularColor(new Color3f(0.88f, 1.0f, 1.0f));
        material.setEmissiveColor(new Color3f(0.0f, 0.018f, 0.022f));
        material.setShininess(118.0f);
        material.setLightingEnable(true);
        apariencia.setMaterial(material);

        PolygonAttributes polyAttr = new PolygonAttributes();
        polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
        polyAttr.setBackFaceNormalFlip(true);
        apariencia.setPolygonAttributes(polyAttr);

        return apariencia;
    }

    private void añadirIluminacion(BranchGroup group) {
        BoundingSphere bounds = crearBounds();

        AmbientLight ambiente = new AmbientLight(new Color3f(0.13f, 0.16f, 0.18f));
        ambiente.setInfluencingBounds(bounds);
        group.addChild(ambiente);

        DirectionalLight principal = new DirectionalLight(
                new Color3f(1.0f, 0.98f, 0.94f),
                new Vector3f(-0.70f, -1.0f, -0.65f));
        principal.setInfluencingBounds(bounds);
        group.addChild(principal);

        DirectionalLight lateral = new DirectionalLight(
                new Color3f(0.28f, 0.55f, 0.78f),
                new Vector3f(0.90f, -0.15f, -0.35f));
        lateral.setInfluencingBounds(bounds);
        group.addChild(lateral);

        PointLight brillo = new PointLight(
                new Color3f(0.86f, 0.98f, 1.0f),
                new Point3f(0.0f, 1.0f, 1.8f),
                new Point3f(1.0f, 0.12f, 0.02f));
        brillo.setInfluencingBounds(bounds);
        group.addChild(brillo);
    }

    private void añadirFondo(BranchGroup group) {
        Background fondo = new Background(new Color3f(0.003f, 0.006f, 0.010f));
        fondo.setApplicationBounds(crearBounds());
        group.addChild(fondo);
    }

    private class PersistentRotationBehavior extends Behavior {

        private final Alpha alpha;

        PersistentRotationBehavior(Alpha alpha) {
            this.alpha = alpha;
        }

        @Override
        public void initialize() {
            wakeupOn(new WakeupOnAWTEvent(java.awt.event.MouseEvent.MOUSE_CLICKED));
        }

        @Override
        public void processStimulus(java.util.Enumeration criteria) {
            while (criteria.hasMoreElements()) {
                WakeupCriterion criterion = (WakeupCriterion) criteria.nextElement();
                if (criterion instanceof WakeupOnAWTEvent) {
                    java.awt.AWTEvent[] eventos = ((WakeupOnAWTEvent) criterion).getAWTEvent();
                    if (eventos.length > 0 && eventos[0] instanceof java.awt.event.MouseEvent) {
                        java.awt.event.MouseEvent evento = (java.awt.event.MouseEvent) eventos[0];
                        if (evento.getID() == java.awt.event.MouseEvent.MOUSE_CLICKED) {
                            if (alpha.isPaused()) {
                                alpha.resume();
                            } else {
                                alpha.pause();
                            }
                        }
                    }
                }
            }
            wakeupOn(new WakeupOnAWTEvent(java.awt.event.MouseEvent.MOUSE_CLICKED));
        }
    }
}
