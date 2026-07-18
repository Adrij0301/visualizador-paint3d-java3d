package paint3d;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Texturas {

    private final SimpleUniverse universo;
    private BranchGroup rootGroup;
    private final Canvas3D canvas;

    private final Appearance aparienciaBase;
    private Texture currentTexture;
    private String efectoActual = "NORMAL";

    private TransformGroup escalaTG;
    private TransformGroup posicionTG;
    private TransformGroup rotacionManualTG;
    private TransformGroup rotacionAutoTG;

    public Texturas() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        universo = new SimpleUniverse(canvas);
        universo.getViewingPlatform().setNominalViewingTransform();

        aparienciaBase = new Appearance();
        aparienciaBase.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
        aparienciaBase.setCapability(Appearance.ALLOW_TEXTURE_READ);
        aparienciaBase.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        aparienciaBase.setCapability(Appearance.ALLOW_MATERIAL_READ);
        aparienciaBase.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
        aparienciaBase.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        aparienciaBase.setCapability(Appearance.ALLOW_TEXGEN_WRITE);
        aparienciaBase.setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
        aparienciaBase.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
        aparienciaBase.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);

        aplicarEfecto("NORMAL");
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

    public void mostrarFigura(String figura) {
        if (rootGroup != null) {
            rootGroup.detach();
        }

        rootGroup = new BranchGroup();
        rootGroup.setCapability(BranchGroup.ALLOW_DETACH);

        Node figuraNode;
        switch (figura.toLowerCase()) {
            case "cubo":
                figuraNode = crearCubo();
                break;
            case "esfera":
                figuraNode = crearEsfera();
                break;
            case "cono":
                figuraNode = crearCono();
                break;
            case "cilindro":
                figuraNode = crearCilindro();
                break;
            case "tetraedro":
                figuraNode = crearTetraedro();
                break;
            case "octaedro":
                figuraNode = crearOctaedro();
                break;
            case "dodecaedro":
                figuraNode = crearDodecaedro();
                break;
            default:
                System.out.println("Figura desconocida: " + figura);
                return;
        }

        escalaTG = crearTransformGroup();
        posicionTG = crearTransformGroup();
        rotacionManualTG = crearTransformGroup();
        rotacionAutoTG = crearTransformGroup();

        Transform3D escalaBaseTransform = new Transform3D();
        escalaBaseTransform.setScale(0.68);
        TransformGroup escalaBaseTG = new TransformGroup(escalaBaseTransform);

        Transform3D inclinacionBaseTransform = new Transform3D();
        inclinacionBaseTransform.rotX(Math.toRadians(18.0));
        TransformGroup inclinacionBaseTG = new TransformGroup(inclinacionBaseTransform);

        escalaBaseTG.addChild(figuraNode);
        inclinacionBaseTG.addChild(escalaBaseTG);
        rotacionAutoTG.addChild(inclinacionBaseTG);
        rotacionManualTG.addChild(rotacionAutoTG);
        posicionTG.addChild(rotacionManualTG);
        escalaTG.addChild(posicionTG);
        rootGroup.addChild(escalaTG);

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

        añadirIluminacion(rootGroup);
        añadirFondo(rootGroup);
        aplicarEfecto(efectoActual);

        universo.addBranchGraph(rootGroup);
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

    public void aplicarTextura(String texturaPath) {
        try {
            TextureLoader loader = new TextureLoader(texturaPath, null);
            Texture textura = loader.getTexture();
            if (textura == null) {
                System.out.println("No se pudo cargar la textura: " + texturaPath);
                return;
            }

            textura.setBoundaryModeS(Texture.WRAP);
            textura.setBoundaryModeT(Texture.WRAP);
            currentTexture = textura;
            aplicarEfecto(efectoActual);
            System.out.println("Textura aplicada correctamente.");
        } catch (Exception e) {
            System.err.println("Error al aplicar textura: " + texturaPath);
            e.printStackTrace();
        }
    }

    public void aplicarEfecto(String efecto) {
        if (efecto == null) {
            return;
        }

        efectoActual = efecto.toUpperCase();

        aparienciaBase.setTexture(currentTexture);
        aparienciaBase.setTextureAttributes(null);
        aparienciaBase.setTexCoordGeneration(null);
        aparienciaBase.setTransparencyAttributes(null);
        aparienciaBase.setColoringAttributes(null);
        aparienciaBase.setRenderingAttributes(null);

        PolygonAttributes polygon = new PolygonAttributes();
        polygon.setCullFace(PolygonAttributes.CULL_NONE);
        polygon.setBackFaceNormalFlip(true);
        aparienciaBase.setPolygonAttributes(polygon);

        TextureAttributes texturaAttr = new TextureAttributes();

        switch (efectoActual) {
            case "NORMAL":

                aparienciaBase.setMaterial(crearMaterialNormal());
                texturaAttr.setTextureMode(TextureAttributes.MODULATE);
                aparienciaBase.setTextureAttributes(texturaAttr);
                break;

            case "CRISTAL":
                aparienciaBase.setMaterial(crearMaterialCristal());
                aparienciaBase.setTransparencyAttributes(
                        new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.52f));

                RenderingAttributes renderCristal = new RenderingAttributes();
                renderCristal.setDepthBufferEnable(true);
                renderCristal.setDepthBufferWriteEnable(false);
                aparienciaBase.setRenderingAttributes(renderCristal);

                texturaAttr.setTextureMode(TextureAttributes.MODULATE);
                aparienciaBase.setTextureAttributes(texturaAttr);

                if (currentTexture != null) {
                    TexCoordGeneration esferaMap = new TexCoordGeneration();
                    esferaMap.setGenMode(TexCoordGeneration.SPHERE_MAP);
                    aparienciaBase.setTexCoordGeneration(esferaMap);
                }
                break;

            case "REFLEJO":
                aparienciaBase.setMaterial(crearMaterialReflejo());
                texturaAttr.setTextureMode(TextureAttributes.REPLACE);
                aparienciaBase.setTextureAttributes(texturaAttr);

                if (currentTexture != null) {
                    TexCoordGeneration reflejo = new TexCoordGeneration();
                    reflejo.setGenMode(TexCoordGeneration.SPHERE_MAP);
                    aparienciaBase.setTexCoordGeneration(reflejo);
                }
                break;

            default:
                efectoActual = "NORMAL";
                aplicarEfecto("NORMAL");
                break;
        }
    }

    private Material crearMaterialNormal() {
        return crearMaterial(
                new Color3f(0.20f, 0.20f, 0.20f),
                new Color3f(0.86f, 0.86f, 0.86f),
                new Color3f(0.35f, 0.35f, 0.35f),
                new Color3f(0.0f, 0.0f, 0.0f),
                36.0f);
    }

    private Material crearMaterialCristal() {
        return crearMaterial(
                new Color3f(0.025f, 0.065f, 0.085f),
                new Color3f(0.16f, 0.34f, 0.43f),
                new Color3f(1.0f, 1.0f, 1.0f),
                new Color3f(0.015f, 0.045f, 0.06f),
                128.0f);
    }

    private Material crearMaterialReflejo() {
        return crearMaterial(
                new Color3f(0.05f, 0.08f, 0.10f),
                new Color3f(0.32f, 0.48f, 0.55f),
                new Color3f(1.0f, 1.0f, 1.0f),
                new Color3f(0.0f, 0.025f, 0.035f),
                118.0f);
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

    private Node crearCubo() {
        TriangleArray geometria = new TriangleArray(
                36,
                GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2);

        Point3f p000 = new Point3f(-0.58f, -0.58f, -0.58f);
        Point3f p001 = new Point3f(-0.58f, -0.58f, 0.58f);
        Point3f p010 = new Point3f(-0.58f, 0.58f, -0.58f);
        Point3f p011 = new Point3f(-0.58f, 0.58f, 0.58f);
        Point3f p100 = new Point3f(0.58f, -0.58f, -0.58f);
        Point3f p101 = new Point3f(0.58f, -0.58f, 0.58f);
        Point3f p110 = new Point3f(0.58f, 0.58f, -0.58f);
        Point3f p111 = new Point3f(0.58f, 0.58f, 0.58f);

        int cursor = 0;
        cursor = agregarCaraCuadrada(geometria, cursor, p001, p101, p111, p011, new Vector3f(0, 0, 1));
        cursor = agregarCaraCuadrada(geometria, cursor, p100, p000, p010, p110, new Vector3f(0, 0, -1));
        cursor = agregarCaraCuadrada(geometria, cursor, p000, p001, p011, p010, new Vector3f(-1, 0, 0));
        cursor = agregarCaraCuadrada(geometria, cursor, p101, p100, p110, p111, new Vector3f(1, 0, 0));
        cursor = agregarCaraCuadrada(geometria, cursor, p011, p111, p110, p010, new Vector3f(0, 1, 0));
        agregarCaraCuadrada(geometria, cursor, p000, p100, p101, p001, new Vector3f(0, -1, 0));

        return crearShape(geometria);
    }

    private int agregarCaraCuadrada(TriangleArray geometria, int cursor,
            Point3f a, Point3f b, Point3f c, Point3f d, Vector3f normal) {
        TexCoord2f uv00 = new TexCoord2f(0.0f, 0.0f);
        TexCoord2f uv10 = new TexCoord2f(1.0f, 0.0f);
        TexCoord2f uv11 = new TexCoord2f(1.0f, 1.0f);
        TexCoord2f uv01 = new TexCoord2f(0.0f, 1.0f);

        cursor = agregarVertice(geometria, cursor, a, normal, uv00);
        cursor = agregarVertice(geometria, cursor, b, normal, uv10);
        cursor = agregarVertice(geometria, cursor, c, normal, uv11);

        cursor = agregarVertice(geometria, cursor, a, normal, uv00);
        cursor = agregarVertice(geometria, cursor, c, normal, uv11);
        cursor = agregarVertice(geometria, cursor, d, normal, uv01);
        return cursor;
    }

    private Node crearEsfera() {
        Sphere esfera = new Sphere(
                0.64f,
                Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS,
                96,
                aparienciaBase);
        prepararShape(esfera.getShape());
        return esfera;
    }

    private Node crearCono() {
        final int segmentos = 96;
        final float radio = 0.58f;
        final float yBase = -0.62f;
        final float yPunta = 0.62f;

        TriangleArray geometria = new TriangleArray(
                segmentos * 6,
                GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2);

        int cursor = 0;
        float pendiente = radio / (yPunta - yBase);

        for (int i = 0; i < segmentos; i++) {
            double a0 = 2.0 * Math.PI * i / segmentos;
            double a1 = 2.0 * Math.PI * (i + 1) / segmentos;

            Point3f base0 = new Point3f((float) Math.cos(a0) * radio, yBase, (float) Math.sin(a0) * radio);
            Point3f base1 = new Point3f((float) Math.cos(a1) * radio, yBase, (float) Math.sin(a1) * radio);
            Point3f punta = new Point3f(0.0f, yPunta, 0.0f);

            Vector3f n0 = new Vector3f((float) Math.cos(a0), pendiente, (float) Math.sin(a0));
            Vector3f n1 = new Vector3f((float) Math.cos(a1), pendiente, (float) Math.sin(a1));
            n0.normalize();
            n1.normalize();
            Vector3f nPunta = new Vector3f();
            nPunta.add(n0, n1);
            nPunta.normalize();

            float u0 = (float) i / segmentos;
            float u1 = (float) (i + 1) / segmentos;

            cursor = agregarVertice(geometria, cursor, base0, n0, new TexCoord2f(u0, 0.0f));
            cursor = agregarVertice(geometria, cursor, base1, n1, new TexCoord2f(u1, 0.0f));
            cursor = agregarVertice(geometria, cursor, punta, nPunta, new TexCoord2f((u0 + u1) * 0.5f, 1.0f));

            Vector3f abajo = new Vector3f(0.0f, -1.0f, 0.0f);
            Point3f centro = new Point3f(0.0f, yBase, 0.0f);
            cursor = agregarVertice(geometria, cursor, centro, abajo, new TexCoord2f(0.5f, 0.5f));
            cursor = agregarVertice(geometria, cursor, base0, abajo,
                    new TexCoord2f(0.5f + 0.5f * (float) Math.cos(a0), 0.5f + 0.5f * (float) Math.sin(a0)));
            cursor = agregarVertice(geometria, cursor, base1, abajo,
                    new TexCoord2f(0.5f + 0.5f * (float) Math.cos(a1), 0.5f + 0.5f * (float) Math.sin(a1)));
        }

        return crearShape(geometria);
    }

    private Node crearCilindro() {
        final int segmentos = 96;
        final float radio = 0.56f;
        final float yAbajo = -0.62f;
        final float yArriba = 0.62f;

        TriangleArray geometria = new TriangleArray(
                segmentos * 12,
                GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2);

        int cursor = 0;
        for (int i = 0; i < segmentos; i++) {
            double a0 = 2.0 * Math.PI * i / segmentos;
            double a1 = 2.0 * Math.PI * (i + 1) / segmentos;

            float x0 = (float) Math.cos(a0) * radio;
            float z0 = (float) Math.sin(a0) * radio;
            float x1 = (float) Math.cos(a1) * radio;
            float z1 = (float) Math.sin(a1) * radio;

            Point3f b0 = new Point3f(x0, yAbajo, z0);
            Point3f b1 = new Point3f(x1, yAbajo, z1);
            Point3f t0 = new Point3f(x0, yArriba, z0);
            Point3f t1 = new Point3f(x1, yArriba, z1);

            Vector3f n0 = new Vector3f((float) Math.cos(a0), 0.0f, (float) Math.sin(a0));
            Vector3f n1 = new Vector3f((float) Math.cos(a1), 0.0f, (float) Math.sin(a1));
            float u0 = (float) i / segmentos;
            float u1 = (float) (i + 1) / segmentos;

            cursor = agregarVertice(geometria, cursor, b0, n0, new TexCoord2f(u0, 0.0f));
            cursor = agregarVertice(geometria, cursor, b1, n1, new TexCoord2f(u1, 0.0f));
            cursor = agregarVertice(geometria, cursor, t1, n1, new TexCoord2f(u1, 1.0f));

            cursor = agregarVertice(geometria, cursor, b0, n0, new TexCoord2f(u0, 0.0f));
            cursor = agregarVertice(geometria, cursor, t1, n1, new TexCoord2f(u1, 1.0f));
            cursor = agregarVertice(geometria, cursor, t0, n0, new TexCoord2f(u0, 1.0f));

            Vector3f arriba = new Vector3f(0.0f, 1.0f, 0.0f);
            Point3f centroArriba = new Point3f(0.0f, yArriba, 0.0f);
            cursor = agregarVertice(geometria, cursor, centroArriba, arriba, new TexCoord2f(0.5f, 0.5f));
            cursor = agregarVertice(geometria, cursor, t1, arriba,
                    new TexCoord2f(0.5f + 0.5f * (float) Math.cos(a1), 0.5f + 0.5f * (float) Math.sin(a1)));
            cursor = agregarVertice(geometria, cursor, t0, arriba,
                    new TexCoord2f(0.5f + 0.5f * (float) Math.cos(a0), 0.5f + 0.5f * (float) Math.sin(a0)));

            Vector3f abajo = new Vector3f(0.0f, -1.0f, 0.0f);
            Point3f centroAbajo = new Point3f(0.0f, yAbajo, 0.0f);
            cursor = agregarVertice(geometria, cursor, centroAbajo, abajo, new TexCoord2f(0.5f, 0.5f));
            cursor = agregarVertice(geometria, cursor, b0, abajo,
                    new TexCoord2f(0.5f + 0.5f * (float) Math.cos(a0), 0.5f + 0.5f * (float) Math.sin(a0)));
            cursor = agregarVertice(geometria, cursor, b1, abajo,
                    new TexCoord2f(0.5f + 0.5f * (float) Math.cos(a1), 0.5f + 0.5f * (float) Math.sin(a1)));
        }

        return crearShape(geometria);
    }

    private Node crearTetraedro() {
        Point3f[] vertices = {
            new Point3f(0.62f, 0.62f, 0.62f),
            new Point3f(-0.62f, -0.62f, 0.62f),
            new Point3f(-0.62f, 0.62f, -0.62f),
            new Point3f(0.62f, -0.62f, -0.62f)
        };
        int[][] caras = {
            {0, 1, 2}, {0, 2, 3}, {0, 3, 1}, {1, 3, 2}
        };
        return crearPoliedroTriangular(vertices, caras);
    }

    private Node crearOctaedro() {
        Point3f[] vertices = {
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
        return crearPoliedroTriangular(vertices, caras);
    }

    private Node crearPoliedroTriangular(Point3f[] vertices, int[][] caras) {
        TriangleArray geometria = new TriangleArray(
                caras.length * 3,
                GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2);

        int cursor = 0;
        for (int[] cara : caras) {
            Point3f a = vertices[cara[0]];
            Point3f b = vertices[cara[1]];
            Point3f c = vertices[cara[2]];
            Point3f centro = new Point3f(
                    (a.x + b.x + c.x) / 3.0f,
                    (a.y + b.y + c.y) / 3.0f,
                    (a.z + b.z + c.z) / 3.0f);
            Vector3f normal = calcularNormalCara(a, b, c, centro);

            cursor = agregarVertice(geometria, cursor, a, normal, new TexCoord2f(0.5f, 1.0f));
            cursor = agregarVertice(geometria, cursor, b, normal, new TexCoord2f(0.0f, 0.0f));
            cursor = agregarVertice(geometria, cursor, c, normal, new TexCoord2f(1.0f, 0.0f));
        }

        return crearShape(geometria);
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

        TriangleArray geometria = new TriangleArray(
                caras.length * 5 * 3,
                GeometryArray.COORDINATES | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2);

        int cursor = 0;
        for (int[] cara : caras) {
            Point3f centro = new Point3f();
            for (int indice : cara) {
                centro.add(vertices[indice]);
            }
            centro.scale(1.0f / cara.length);
            Vector3f normal = calcularNormalCara(vertices[cara[0]], vertices[cara[1]], vertices[cara[2]], centro);

            for (int i = 0; i < cara.length; i++) {
                int siguiente = (i + 1) % cara.length;
                double angulo0 = Math.PI / 2.0 - 2.0 * Math.PI * i / cara.length;
                double angulo1 = Math.PI / 2.0 - 2.0 * Math.PI * siguiente / cara.length;

                cursor = agregarVertice(geometria, cursor, centro, normal, new TexCoord2f(0.5f, 0.5f));
                cursor = agregarVertice(geometria, cursor, vertices[cara[i]], normal,
                        new TexCoord2f(0.5f + 0.48f * (float) Math.cos(angulo0),
                                0.5f + 0.48f * (float) Math.sin(angulo0)));
                cursor = agregarVertice(geometria, cursor, vertices[cara[siguiente]], normal,
                        new TexCoord2f(0.5f + 0.48f * (float) Math.cos(angulo1),
                                0.5f + 0.48f * (float) Math.sin(angulo1)));
            }
        }

        return crearShape(geometria);
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

    private int agregarVertice(TriangleArray geometria, int indice,
            Point3f punto, Vector3f normal, TexCoord2f uv) {
        geometria.setCoordinate(indice, punto);
        geometria.setNormal(indice, normal);
        geometria.setTextureCoordinate(0, indice, uv);
        return indice + 1;
    }

    private Shape3D crearShape(GeometryArray geometria) {
        Shape3D shape = new Shape3D(geometria, aparienciaBase);
        prepararShape(shape);
        return shape;
    }

    private void prepararShape(Shape3D shape) {
        shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        shape.setAppearance(aparienciaBase);
    }

    private void añadirIluminacion(BranchGroup group) {
        BoundingSphere bounds = crearBounds();

        AmbientLight ambiente = new AmbientLight(new Color3f(0.18f, 0.19f, 0.23f));
        ambiente.setInfluencingBounds(bounds);
        group.addChild(ambiente);

        DirectionalLight principal = new DirectionalLight(
                new Color3f(1.0f, 0.96f, 0.91f),
                new Vector3f(-0.75f, -1.0f, -0.65f));
        principal.setInfluencingBounds(bounds);
        group.addChild(principal);

        DirectionalLight relleno = new DirectionalLight(
                new Color3f(0.30f, 0.48f, 0.72f),
                new Vector3f(0.9f, -0.1f, -0.45f));
        relleno.setInfluencingBounds(bounds);
        group.addChild(relleno);

        DirectionalLight borde = new DirectionalLight(
                new Color3f(0.36f, 0.72f, 0.95f),
                new Vector3f(0.0f, 0.35f, 1.0f));
        borde.setInfluencingBounds(bounds);
        group.addChild(borde);

        PointLight brillo = new PointLight(
                new Color3f(0.95f, 0.98f, 1.0f),
                new Point3f(0.0f, 0.85f, 1.85f),
                new Point3f(1.0f, 0.10f, 0.02f));
        brillo.setInfluencingBounds(bounds);
        group.addChild(brillo);
    }

    private void añadirFondo(BranchGroup group) {
        Background fondo = new Background(new Color3f(0.004f, 0.007f, 0.012f));
        fondo.setApplicationBounds(crearBounds());
        group.addChild(fondo);
    }
}
