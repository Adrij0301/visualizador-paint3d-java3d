package paint3d;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.util.Enumeration;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Arreglos {

    private Canvas3D canvas;
    private SimpleUniverse universe;
    private BranchGroup currentGroup;

    public Arreglos() {

        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
        currentGroup = null;
    }

    public Canvas3D getCanvas() {
        return canvas;
    }

    public void mostrarArreglo(String tipo) {

        if (currentGroup != null) {
            currentGroup.detach();
            currentGroup = null;
        }

        BranchGroup newGroup = new BranchGroup();
        newGroup.setCapability(BranchGroup.ALLOW_DETACH);

        TransformGroup arregloTG;
        if (tipo.equals("arreglo1")) {
            arregloTG = crearEspiralCubo();
        } else if (tipo.equals("arreglo2")) {
            arregloTG = crearEspiralEsfera();
        } else if (tipo.equals("arreglo3")) {
            arregloTG = crearEspiralCombinada();
        } else if (tipo.equals("arreglo4")) {
            arregloTG = crearArregloEsferasConos();
        } else if (tipo.equals("arreglo5")) {
            arregloTG = crearArregloCuboCilindro();
        } else if (tipo.equals("arreglo6")) {
            arregloTG = crearArregloEsferaEsfera();
        } else if (tipo.equals("arreglo7")) {
            arregloTG = crearArregloCono();
        } else if (tipo.equals("arreglo8")) {
            arregloTG = crearArregloCilindro();
        } else if (tipo.equals("arreglo9")) {
            arregloTG = crearArregloTetraedro();
        } else if (tipo.equals("arreglo10")) {
            arregloTG = crearArregloMix();
        } else {
            System.out.println("Arreglo no implementado: " + tipo);
            return;
        }

        newGroup.addChild(arregloTG);

        añadirIluminacion(newGroup);

        currentGroup = newGroup;
        universe.addBranchGraph(currentGroup);
    }

    private TransformGroup crearEspiralCubo() {
        TransformGroup rootTG = new TransformGroup();
        rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        double radioBase = 0.0;
        double radioMaximo = 1.5;
        double alturaIncremento = 0.05;
        double anguloIncremento = Math.PI / 15;

        for (int fila = 0; fila < 3; fila++) {
            for (int i = 0; i < 50; i++) {

                double porcentajeAltura = (double) i / 30;
                double radio = radioBase + (radioMaximo - radioBase) * porcentajeAltura;

                double angulo = i * anguloIncremento + (fila * Math.PI * 2 / 3);
                float x = (float) (radio * Math.cos(angulo));
                float y = (float) (i * alturaIncremento);
                float z = (float) (radio * Math.sin(angulo));

                Transform3D posicion = new Transform3D();
                posicion.setTranslation(new Vector3f(x, y, z));
                TransformGroup cuboTG = new TransformGroup(posicion);

                Box cubo = new Box(0.03f, 0.03f, 0.03f, crearApariencia());
                cuboTG.addChild(cubo);

                rootTG.addChild(cuboTG);
            }
        }

        for (int fila = 0; fila < 3; fila++) {
            for (int i = 0; i < 50; i++) {

                double porcentajeAltura = (double) i / 30;
                double radio = radioBase + (radioMaximo - radioBase) * porcentajeAltura;

                double angulo = i * anguloIncremento + (fila * Math.PI * 2 / 3);
                float x = (float) (radio * Math.cos(angulo));
                float y = (float) (-i * alturaIncremento);
                float z = (float) (radio * Math.sin(angulo));

                Transform3D posicion = new Transform3D();
                posicion.setTranslation(new Vector3f(x, y, z));
                TransformGroup cuboTG = new TransformGroup(posicion);

                Box cubo = new Box(0.03f, 0.03f, 0.03f, crearApariencia());
                cuboTG.addChild(cubo);

                rootTG.addChild(cuboTG);
            }
        }

        Alpha rotationAlpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rootTG);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        rootTG.addChild(rotator);

        return rootTG;
    }

    private TransformGroup crearEspiralEsfera() {
        TransformGroup rootTG = new TransformGroup();
        rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        double radioBase = 0.0;
        double radioMaximo = 1.5;
        double alturaIncremento = 0.05;
        double anguloIncremento = Math.PI / 15;

        for (int fila = 0; fila < 3; fila++) {
            for (int i = 0; i < 50; i++) {

                double porcentajeAltura = (double) i / 30;
                double radio = radioBase + (radioMaximo - radioBase) * porcentajeAltura;

                double angulo = i * anguloIncremento + (fila * Math.PI * 2 / 3);
                float x = (float) (radio * Math.cos(angulo));
                float y = (float) (i * alturaIncremento);
                float z = (float) (radio * Math.sin(angulo));

                Transform3D posicion = new Transform3D();
                posicion.setTranslation(new Vector3f(x, y, z));
                TransformGroup esferaTG = new TransformGroup(posicion);

                Sphere esfera = new Sphere(0.03f, Sphere.GENERATE_NORMALS, 32, crearApariencia());
                esferaTG.addChild(esfera);

                rootTG.addChild(esferaTG);
            }
        }

        for (int fila = 0; fila < 3; fila++) {
            for (int i = 0; i < 50; i++) {

                double porcentajeAltura = (double) i / 30;
                double radio = radioBase + (radioMaximo - radioBase) * porcentajeAltura;

                double angulo = i * anguloIncremento + (fila * Math.PI * 2 / 3);
                float x = (float) (radio * Math.cos(angulo));
                float y = (float) (-i * alturaIncremento);
                float z = (float) (radio * Math.sin(angulo));

                Transform3D posicion = new Transform3D();
                posicion.setTranslation(new Vector3f(x, y, z));
                TransformGroup esferaTG = new TransformGroup(posicion);

                Sphere esfera = new Sphere(0.03f, Sphere.GENERATE_NORMALS, 32, crearApariencia());
                esferaTG.addChild(esfera);

                rootTG.addChild(esferaTG);
            }
        }

        Alpha rotationAlpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rootTG);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        rootTG.addChild(rotator);

        return rootTG;
    }

    private TransformGroup crearEspiralCombinada() {
        TransformGroup rootTG = new TransformGroup();
        rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        double radioMaximo = 1.2;
        double alturaTotal = 2.0;
        double alturaIncremento = alturaTotal / 250;
        double anguloIncremento = Math.PI / 25;
        int numeroLíneas = 5;
        int figurasPorLinea = 50;

        for (int linea = 0; linea < numeroLíneas; linea++) {
            for (int i = 0; i < figurasPorLinea; i++) {
                double altura = i * alturaIncremento + (linea * alturaTotal / numeroLíneas);
                double radio = radioMaximo * Math.sin((double) i / figurasPorLinea * Math.PI);
                double angulo = i * anguloIncremento + (linea * Math.PI * 2 / numeroLíneas);
                float x = (float) (radio * Math.cos(angulo));
                float y = (float) (altura - alturaTotal / 2);
                float z = (float) (radio * Math.sin(angulo));

                Node figura = (i % 2 == 0)
                        ? new Sphere(0.03f, Sphere.GENERATE_NORMALS, 32, crearAparienciaAzul())
                        : new Box(0.03f, 0.03f, 0.03f, crearApariencia());

                Transform3D posicion = new Transform3D();
                posicion.setTranslation(new Vector3f(x, y, z));
                TransformGroup figuraTG = new TransformGroup(posicion);
                figuraTG.addChild(figura);

                rootTG.addChild(figuraTG);
            }
        }

        Alpha rotationAlpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rootTG);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        rootTG.addChild(rotator);

        return rootTG;
    }

    private TransformGroup crearArregloEsferasConos() {
        TransformGroup rootTG = new TransformGroup();
        rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        double radioMaximo = 2.0;
        double alturaTotal = 1.0;
        double alturaIncremento = alturaTotal / 200;
        double anguloIncremento = Math.PI / 10;
        int ramificaciones = 6;

        for (int brazo = 0; brazo < ramificaciones; brazo++) {
            for (int i = 0; i < 200 / ramificaciones; i++) {
                double altura = (i * alturaIncremento) - (alturaTotal / 2);
                double radio = radioMaximo * Math.sin((double) i / (200 / ramificaciones) * Math.PI);
                double angulo = i * anguloIncremento + (brazo * Math.PI * 2 / ramificaciones);
                float x = (float) (radio * Math.cos(angulo));
                float y = (float) altura;
                float z = (float) (radio * Math.sin(angulo));

                Node figura = (i % 2 == 0)
                        ? new Sphere(0.05f, Sphere.GENERATE_NORMALS, 32, crearAparienciaMorada())
                        : new Cone(0.05f, 0.1f, Cone.GENERATE_NORMALS, crearAparienciaAzul());

                Transform3D posicion = new Transform3D();
                posicion.setTranslation(new Vector3f(x, y, z));
                TransformGroup figuraTG = new TransformGroup(posicion);
                figuraTG.addChild(figura);

                rootTG.addChild(figuraTG);
            }
        }

        Alpha rotationAlpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rootTG);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        rootTG.addChild(rotator);

        return rootTG;
    }

    private TransformGroup crearArregloCuboCilindro() {
        TransformGroup rootTG = new TransformGroup();
        rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        double radioMaximo = 0.5;
        double alturaTotal = 1.0;
        double alturaIncremento = alturaTotal / 250;
        double anguloIncremento = Math.PI / 12;
        int totalFiguras = 250;

        for (int i = 0; i < totalFiguras; i++) {

            double altura = (i * alturaIncremento) - (alturaTotal / 2);

            double radio = radioMaximo * Math.sin((double) i / totalFiguras * Math.PI);

            double angulo = i * anguloIncremento;
            float x = (float) (radio * Math.cos(angulo));
            float y = (float) (altura);
            float z = (float) (radio * Math.sin(angulo));

            Node figura = (i % 2 == 0)
                    ? new Box(0.03f, 0.03f, 0.03f, crearAparienciaRojo())
                    : new Cylinder(0.02f, 0.07f, Cylinder.GENERATE_NORMALS, crearAparienciaNaranjaRojizo());

            Transform3D posicion = new Transform3D();
            posicion.setTranslation(new Vector3f(x, y, z));
            TransformGroup figuraTG = new TransformGroup(posicion);
            figuraTG.addChild(figura);

            rootTG.addChild(figuraTG);
        }

        Alpha rotationAlpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rootTG);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        rootTG.addChild(rotator);

        return rootTG;
    }

    private TransformGroup crearArregloEsferaEsfera() {
        TransformGroup rootTG = new TransformGroup();
        rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        double radioMaximo = 0.5;
        double alturaTotal = 1.0;
        double alturaIncremento = alturaTotal / 300;
        double anguloIncremento = Math.PI / 12;
        int totalFiguras = 300;

        for (int i = 0; i < totalFiguras; i++) {

            double altura = (i * alturaIncremento) - (alturaTotal / 2);

            double radio = radioMaximo * Math.sin((double) i / totalFiguras * Math.PI);

            double angulo = i * anguloIncremento;
            float x = (float) (radio * Math.cos(angulo));
            float y = (float) (altura);
            float z = (float) (radio * Math.sin(angulo));

            Node figura = new Sphere(0.03f, Sphere.GENERATE_NORMALS, 32, crearAparienciaAzulCeleste());

            Transform3D posicion = new Transform3D();
            posicion.setTranslation(new Vector3f(x, y, z));
            TransformGroup figuraTG = new TransformGroup(posicion);
            figuraTG.addChild(figura);

            rootTG.addChild(figuraTG);
        }

        Alpha rotationAlpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rootTG);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        rootTG.addChild(rotator);

        return rootTG;
    }

    private TransformGroup crearArregloCono() {
        TransformGroup rootTG = new TransformGroup();
        rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        double radioMaximo = 0.6;
        double alturaTotal = 1.2;
        double alturaIncremento = alturaTotal / 200;
        double anguloIncremento = Math.PI / 20;
        int totalFiguras = 200;

        for (int i = 0; i < totalFiguras; i++) {

            double altura = (i * alturaIncremento) - (alturaTotal / 2);

            double radio = radioMaximo * Math.abs(Math.sin((double) i / totalFiguras * Math.PI * 2));

            double angulo = i * anguloIncremento;
            float x = (float) (radio * Math.cos(angulo));
            float y = (float) (altura);
            float z = (float) (radio * Math.sin(angulo));

            Node figura = new Cone(0.03f, 0.1f, Cone.GENERATE_NORMALS, crearAparienciaAzulCielo());

            Transform3D posicion = new Transform3D();
            posicion.setTranslation(new Vector3f(x, y, z));
            TransformGroup figuraTG = new TransformGroup(posicion);
            figuraTG.addChild(figura);

            rootTG.addChild(figuraTG);
        }

        Alpha rotationAlpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rootTG);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        rootTG.addChild(rotator);

        return rootTG;
    }

    private TransformGroup crearArregloCilindro() {
        TransformGroup rootTG = new TransformGroup();
        rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        double radioMaximo = 0.8;
        double alturaTotal = 0.4;
        double alturaIncremento = alturaTotal / 80;
        double anguloIncremento = Math.PI / 15;
        int totalFiguras = 300;
        int ramificaciones = 4;

        for (int linea = 0; linea < ramificaciones; linea++) {
            for (int i = 0; i < totalFiguras / ramificaciones; i++) {

                double altura = (i * alturaIncremento) - (alturaTotal / 2);

                double angulo = i * anguloIncremento + (linea * Math.PI * 2 / ramificaciones);
                float x = (float) (radioMaximo * Math.cos(angulo));
                float y = (float) (altura);
                float z = (float) (radioMaximo * Math.sin(angulo));

                Appearance aparienciaRGB = crearMaterialRGBAnimado();
                Material material = aparienciaRGB.getMaterial();

                RGBBehavior rgbBehavior = new RGBBehavior(material);
                rgbBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));

                Node figura = new Cylinder(0.02f, 0.07f, Cylinder.GENERATE_NORMALS, aparienciaRGB);

                Transform3D posicion = new Transform3D();
                posicion.setTranslation(new Vector3f(x, y, z));
                TransformGroup figuraTG = new TransformGroup(posicion);
                figuraTG.addChild(figura);

                BranchGroup behaviorGroup = new BranchGroup();
                behaviorGroup.addChild(rgbBehavior);
                figuraTG.addChild(behaviorGroup);

                rootTG.addChild(figuraTG);
            }
        }

        Alpha rotationAlpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rootTG);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        rootTG.addChild(rotator);

        return rootTG;
    }

    private Node crearTetraedroArreglos() {

        Appearance apariencia = new Appearance();
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.2f, 0.2f, 0.7f));
        material.setDiffuseColor(new Color3f(0.4f, 0.6f, 1.0f));
        material.setSpecularColor(new Color3f(0.8f, 0.8f, 1.0f));
        material.setShininess(120);
        apariencia.setMaterial(material);

        float[] vertices = {
            0.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            0.0f, -1.0f, -1.0f
        };

        int[] indices = {
            0, 1, 2,
            0, 2, 3,
            0, 3, 1,
            1, 3, 2
        };

        IndexedTriangleArray geometry = new IndexedTriangleArray(4, GeometryArray.COORDINATES | GeometryArray.NORMALS, 12);
        geometry.setCoordinates(0, vertices);
        geometry.setCoordinateIndices(0, indices);

        GeometryInfo geoInfo = new GeometryInfo(geometry);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(geoInfo);

        Shape3D tetraedro = new Shape3D(geoInfo.getGeometryArray(), apariencia);

        Transform3D escala = new Transform3D();
        escala.setScale(0.05);
        TransformGroup transformGroupEscala = new TransformGroup(escala);

        transformGroupEscala.addChild(tetraedro);

        return transformGroupEscala;
    }

    private TransformGroup crearArregloTetraedro() {
        TransformGroup rootTG = new TransformGroup();
        rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        double radioBase = 0.5;
        double alturaTotal = 1.0;
        double anguloIncremento = Math.PI / 20;
        int totalFiguras = 200;
        int capas = 4;

        for (int capa = 0; capa < capas; capa++) {
            for (int i = 0; i < totalFiguras / capas; i++) {

                double altura = (i * (alturaTotal / totalFiguras)) - (alturaTotal / 2);

                double radio = radioBase * (1 + 0.5 * Math.sin(capa + (double) i / 10));
                double angulo = i * anguloIncremento + capa * Math.PI / 2;

                float x = (float) (radio * Math.cos(angulo));
                float y = (float) (altura + 0.3 * Math.sin((double) i / 5));
                float z = (float) (radio * Math.sin(angulo));

                Node figura = crearTetraedroArreglos();

                Transform3D posicion = new Transform3D();
                posicion.setTranslation(new Vector3f(x, y, z));
                TransformGroup figuraTG = new TransformGroup(posicion);
                figuraTG.addChild(figura);

                rootTG.addChild(figuraTG);
            }
        }

        Color3f lightColor = new Color3f(0.5f, 0.7f, 1.0f);
        Vector3f lightDirection = new Vector3f(-1.0f, -1.0f, -1.0f);
        DirectionalLight light = new DirectionalLight(lightColor, lightDirection);
        light.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        rootTG.addChild(light);

        Alpha rotationAlpha = new Alpha(-1, 10000);
        Transform3D ejeRotacion = new Transform3D();
        ejeRotacion.rotX(Math.PI / 4);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rootTG, ejeRotacion, 0.0f, (float) Math.PI * 2);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        rootTG.addChild(rotator);

        return rootTG;
    }

    private Appearance crearMaterialRGBAnimado() {
        Appearance apariencia = new Appearance();
        Material material = new Material();

        material.setAmbientColor(new Color3f(0.5f, 0.5f, 0.5f));
        material.setDiffuseColor(new Color3f(5.0f, 5.0f, 5.0f));
        material.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f));
        material.setShininess(100);
        apariencia.setMaterial(material);

        apariencia.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
        material.setCapability(Material.ALLOW_COMPONENT_WRITE);

        return apariencia;
    }

    private class RGBBehavior extends Behavior {

        private Material material;
        private WakeupOnElapsedTime wakeupCondition;

        public RGBBehavior(Material material) {
            this.material = material;
            this.wakeupCondition = new WakeupOnElapsedTime(300);
        }

        @Override
        public void initialize() {
            wakeupOn(wakeupCondition);
        }

        @Override
        public void processStimulus(Enumeration criteria) {

            float r = (float) Math.random();
            float g = (float) Math.random();
            float b = (float) Math.random();

            material.setDiffuseColor(new Color3f(r, g, b));
            material.setAmbientColor(new Color3f(r * 0.5f, g * 0.5f, b * 0.5f));

            wakeupOn(wakeupCondition);
        }
    }

    private TransformGroup crearArregloMix() {
        TransformGroup rootTG = new TransformGroup();
        rootTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        double radio = 0.5;
        double alturaTotal = 2.0;
        double alturaIncremento = alturaTotal / 200;
        double anguloIncremento = Math.PI / 20;
        int totalFiguras = 200;

        for (int i = 0; i < totalFiguras; i++) {

            double altura = (i * alturaIncremento) - (alturaTotal / 2);

            double angulo = i * anguloIncremento;
            float x = (float) (radio * Math.cos(angulo));
            float y = (float) altura;
            float z = (float) (radio * Math.sin(angulo));

            Appearance apariencia = crearMaterialRGBAnimado();
            Material material = apariencia.getMaterial();

            RGBBehavior rgbBehavior = new RGBBehavior(material);
            rgbBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));

            Node figura;
            switch (i % 4) {
                case 0:
                    figura = new Box(0.03f, 0.03f, 0.03f, apariencia);
                    break;
                case 1:
                    figura = new Sphere(0.03f, Sphere.GENERATE_NORMALS, 32, apariencia);
                    break;
                case 2:
                    figura = new Cone(0.03f, 0.06f, Cone.GENERATE_NORMALS, apariencia);
                    break;
                default:
                    figura = new Cylinder(0.02f, 0.06f, Cylinder.GENERATE_NORMALS, apariencia);
                    break;
            }

            Transform3D posicion = new Transform3D();
            posicion.setTranslation(new Vector3f(x, y, z));
            TransformGroup figuraTG = new TransformGroup(posicion);
            figuraTG.addChild(figura);

            BranchGroup behaviorGroup = new BranchGroup();
            behaviorGroup.addChild(rgbBehavior);
            figuraTG.addChild(behaviorGroup);

            rootTG.addChild(figuraTG);
        }

        Alpha rotationAlpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, rootTG);
        rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        rootTG.addChild(rotator);

        return rootTG;
    }

    private Appearance crearAparienciaAzul() {
        Appearance apariencia = new Appearance();
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.1f, 0.1f, 0.8f));
        material.setDiffuseColor(new Color3f(0.4f, 0.4f, 1.0f));
        material.setSpecularColor(new Color3f(0.9f, 0.9f, 1.0f));
        material.setShininess(120);
        apariencia.setMaterial(material);

        RenderingAttributes renderingAttributes = new RenderingAttributes();
        renderingAttributes.setDepthBufferEnable(true);
        renderingAttributes.setAlphaTestValue(0.1f);
        apariencia.setRenderingAttributes(renderingAttributes);

        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(
                TransparencyAttributes.BLENDED,
                0.1f
        );
        apariencia.setTransparencyAttributes(transparencyAttributes);

        return apariencia;
    }

    private Appearance crearAparienciaMorada() {
        Appearance apariencia = new Appearance();
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.3f, 0.1f, 0.6f));
        material.setDiffuseColor(new Color3f(0.7f, 0.5f, 1.0f));
        material.setSpecularColor(new Color3f(1.2f, 0.8f, 1.5f));
        material.setShininess(120);
        apariencia.setMaterial(material);

        RenderingAttributes renderingAttributes = new RenderingAttributes();
        renderingAttributes.setDepthBufferEnable(true);
        renderingAttributes.setAlphaTestValue(0.1f);
        apariencia.setRenderingAttributes(renderingAttributes);

        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(
                TransparencyAttributes.BLENDED,
                0.1f
        );
        apariencia.setTransparencyAttributes(transparencyAttributes);

        return apariencia;
    }

    private Appearance crearAparienciaRojo() {
        Appearance apariencia = new Appearance();
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.6f, 0.1f, 0.1f));
        material.setDiffuseColor(new Color3f(1.0f, 0.2f, 0.2f));
        material.setSpecularColor(new Color3f(1.2f, 0.6f, 0.6f));
        material.setShininess(128);
        apariencia.setMaterial(material);

        RenderingAttributes renderingAttributes = new RenderingAttributes();
        renderingAttributes.setDepthBufferEnable(true);
        renderingAttributes.setAlphaTestValue(0.22f);
        apariencia.setRenderingAttributes(renderingAttributes);

        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(
                TransparencyAttributes.BLENDED,
                0.22f
        );
        apariencia.setTransparencyAttributes(transparencyAttributes);

        return apariencia;
    }

    private Appearance crearAparienciaNaranjaRojizo() {
        Appearance apariencia = new Appearance();
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.8f, 0.2f, 0.1f));
        material.setDiffuseColor(new Color3f(1.0f, 0.4f, 0.2f));
        material.setSpecularColor(new Color3f(1.3f, 0.7f, 0.5f));
        material.setShininess(128);
        apariencia.setMaterial(material);

        RenderingAttributes renderingAttributes = new RenderingAttributes();
        renderingAttributes.setDepthBufferEnable(true);
        renderingAttributes.setAlphaTestValue(0.1f);
        apariencia.setRenderingAttributes(renderingAttributes);

        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(
                TransparencyAttributes.BLENDED,
                0.1f
        );
        apariencia.setTransparencyAttributes(transparencyAttributes);

        return apariencia;
    }

    private Appearance crearAparienciaAzulCielo() {
        Appearance apariencia = new Appearance();
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.2f, 0.4f, 0.8f));
        material.setDiffuseColor(new Color3f(0.4f, 0.6f, 1.0f));
        material.setSpecularColor(new Color3f(0.9f, 0.9f, 1.2f));
        material.setShininess(128);
        apariencia.setMaterial(material);

        RenderingAttributes renderingAttributes = new RenderingAttributes();
        renderingAttributes.setDepthBufferEnable(true);
        renderingAttributes.setAlphaTestValue(0.1f);
        apariencia.setRenderingAttributes(renderingAttributes);

        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(
                TransparencyAttributes.BLENDED,
                0.1f
        );
        apariencia.setTransparencyAttributes(transparencyAttributes);

        return apariencia;
    }

    private Appearance crearAparienciaAzulCeleste() {
        Appearance apariencia = new Appearance();
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.1f, 0.3f, 0.9f));
        material.setDiffuseColor(new Color3f(0.3f, 0.5f, 1.0f));
        material.setSpecularColor(new Color3f(1.0f, 1.0f, 1.3f));
        material.setShininess(128);
        apariencia.setMaterial(material);

        RenderingAttributes renderingAttributes = new RenderingAttributes();
        renderingAttributes.setDepthBufferEnable(true);
        renderingAttributes.setAlphaTestValue(0.2f);
        apariencia.setRenderingAttributes(renderingAttributes);

        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(
                TransparencyAttributes.BLENDED,
                0.2f
        );
        apariencia.setTransparencyAttributes(transparencyAttributes);

        return apariencia;
    }

    private Appearance crearApariencia() {
        Appearance apariencia = new Appearance();
        Material material = new Material();
        material.setAmbientColor(new Color3f(0.2f, 0.2f, 0.2f));
        material.setDiffuseColor(new Color3f(0.8f, 0.5f, 1.0f));
        material.setSpecularColor(new Color3f(0.5f, 0.5f, 0.5f));
        material.setShininess(50);
        apariencia.setMaterial(material);
        return apariencia;
    }

    private void añadirIluminacion(BranchGroup group) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        DirectionalLight luzSuperior = new DirectionalLight(
                new Color3f(1.0f, 1.0f, 1.0f), new Vector3f(-1f, -1f, -1f));
        luzSuperior.setInfluencingBounds(bounds);
        group.addChild(luzSuperior);

        AmbientLight luzAmbiente = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));
        luzAmbiente.setInfluencingBounds(bounds);
        group.addChild(luzAmbiente);
    }
}
