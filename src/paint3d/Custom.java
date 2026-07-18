package paint3d;

import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.Scene;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.GraphicsConfiguration;
import javax.vecmath.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Custom {

    private SimpleUniverse universo;
    private BranchGroup rootGroup;
    private Canvas3D canvas;

    public Custom() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);

        universo = new SimpleUniverse(canvas);
        universo.getViewingPlatform().setNominalViewingTransform();
    }

    public Canvas3D getCanvas() {
        return canvas;
    }

    public void cargarObjeto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo .obj");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos OBJ", "obj");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                String ruta = file.getAbsolutePath();

                if (rootGroup != null) {
                    rootGroup.detach();
                }

                if (!file.exists()) {
                    throw new FileNotFoundException("El archivo no fue encontrado: " + ruta);
                }

                ObjectFile loader = new ObjectFile(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
                Scene escena = loader.load(file.toURI().toURL());

                rootGroup = new BranchGroup();
                rootGroup.setCapability(BranchGroup.ALLOW_DETACH);

                TransformGroup escalaTG = new TransformGroup();
                Transform3D escalaTransform = new Transform3D();

                escalaTransform.setScale(0.5);

                escalaTG.setTransform(escalaTransform);

                escalaTG.addChild(escena.getSceneGroup());
                escalaTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                escalaTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
                rootGroup.addChild(escalaTG);

                Transform3D ejeRotacion = new Transform3D();
                Alpha alpha = new Alpha(-1, 4000);
                RotationInterpolator rotator = new RotationInterpolator(alpha, escalaTG, ejeRotacion, 0.0f, (float) Math.PI * 2);
                rotator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
                escalaTG.addChild(rotator);

                añadirIluminacion(rootGroup);
                universo.addBranchGraph(rootGroup);

            } catch (FileNotFoundException e) {
                System.err.println("El archivo no fue encontrado: " + e.getMessage());
                e.printStackTrace();
            } catch (com.sun.j3d.loaders.ParsingErrorException pe) {
                System.err.println("Error de sintaxis en el archivo .obj: " + pe.getMessage());
                pe.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error al cargar el archivo .obj: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Carga de objeto cancelada por el usuario.");
        }
    }

    private void añadirIluminacion(BranchGroup group) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        AmbientLight luzAmbiente = new AmbientLight(new Color3f(0.5f, 0.5f, 0.5f));
        luzAmbiente.setInfluencingBounds(bounds);
        group.addChild(luzAmbiente);

        DirectionalLight luzDireccional = new DirectionalLight(
                new Color3f(1.0f, 1.0f, 1.0f),
                new Vector3f(-1.0f, -1.0f, -1.0f)
        );
        luzDireccional.setInfluencingBounds(bounds);
        group.addChild(luzDireccional);
    }
}
