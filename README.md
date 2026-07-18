# Paint 3D - Visualizador de Figuras, Texturas, Modelos 3D y Gráficas 3D

Aplicación de escritorio desarrollada en **Java** con **Java 3D** para visualizar, transformar y aplicar diferentes modos de representación a figuras geométricas, gráficas matemáticas, arreglos tridimensionales, texturas y modelos personalizados en formato `.obj`.

El programa permite modificar la escala, posición y rotación de los objetos, además de aplicar materiales, iluminación, transparencia, sombreado y efectos visuales en tiempo real.

## Vista previa

### Demostración en video

<p align="center">
  <a href="src/img/demo/demojava3d.mp4">
    <strong>▶ Ver demostración completa de Paint 3D</strong>
  </a>
</p>

Esta es la interfaz principal al seleccionar una figura. En el lado izquierdo se muestra el objeto 3D y en el panel derecho se encuentran los controles de transformación, representación y materiales.

<p align="center">
  <img src="src/img/Figuras/CapFigurasCubo.png" alt="Interfaz principal de Paint 3D" width="90%">
</p>

## Figuras

### Cubo

<table>
  <tr>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Cubo/CuboFlat.png" alt="Cubo con sombreado Flat" width="100%"><br>
      <sub><b>Flat</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Cubo/CuboLine.png" alt="Cubo con líneas" width="100%"><br>
      <sub><b>Líneas</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Cubo/CuboLinePunteada.png" alt="Cubo con líneas punteadas" width="100%"><br>
      <sub><b>Líneas punteadas</b></sub>
    </td>
  </tr>
  <tr>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Cubo/CuboPoints.png" alt="Cubo representado con puntos" width="100%"><br>
      <sub><b>Puntos</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Cubo/CuboTransparente.png" alt="Cubo transparente" width="100%"><br>
      <sub><b>Transparente</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Cubo/CuboWireframe.png" alt="Cubo en modo Wireframe" width="100%"><br>
      <sub><b>Wireframe</b></sub>
    </td>
  </tr>
</table>

### Esfera

<table>
  <tr>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Esfera/EsferaFlat.png" alt="Esfera con sombreado Flat" width="100%"><br>
      <sub><b>Flat</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Esfera/EsferaLine.png" alt="Esfera con líneas" width="100%"><br>
      <sub><b>Líneas</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Esfera/EsferaWireframe.png" alt="Esfera en modo Wireframe" width="100%"><br>
      <sub><b>Wireframe</b></sub>
    </td>
  </tr>
</table>

### Cono

<table>
  <tr>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Cono/ConoPoints.png" alt="Cono representado con puntos" width="100%"><br>
      <sub><b>Puntos</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Cono/ConoPoligono.png" alt="Cono en modo polígono" width="100%"><br>
      <sub><b>Polígono</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Cono/ConoWireframe.png" alt="Cono en modo Wireframe" width="100%"><br>
      <sub><b>Wireframe</b></sub>
    </td>
  </tr>
</table>

### Dodecaedro

<table>
  <tr>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Dodecaedro/DodecaedroFlat.png" alt="Dodecaedro con sombreado Flat" width="100%"><br>
      <sub><b>Flat</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Dodecaedro/DodecaedroLine.png" alt="Dodecaedro con líneas" width="100%"><br>
      <sub><b>Líneas</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Dodecaedro/DodecaedroLinePunteada.png" alt="Dodecaedro con líneas punteadas" width="100%"><br>
      <sub><b>Líneas punteadas</b></sub>
    </td>
  </tr>
  <tr>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Dodecaedro/DodecaedroPoints.png" alt="Dodecaedro representado con puntos" width="100%"><br>
      <sub><b>Puntos</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Dodecaedro/DodecaedroTransparente.png" alt="Dodecaedro transparente" width="100%"><br>
      <sub><b>Transparente</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Dodecaedro/DodecaedroWireframe.png" alt="Dodecaedro en modo Wireframe" width="100%"><br>
      <sub><b>Wireframe</b></sub>
    </td>
  </tr>
</table>

### Octaedro

<table>
  <tr>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Octaedro/OctaedroFlat.png" alt="Octaedro con sombreado Flat" width="100%"><br>
      <sub><b>Flat</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Octaedro/OctaedroLine.png" alt="Octaedro con líneas" width="100%"><br>
      <sub><b>Líneas</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Octaedro/OctaedroLinePunteada.png" alt="Octaedro con líneas punteadas" width="100%"><br>
      <sub><b>Líneas punteadas</b></sub>
    </td>
  </tr>
  <tr>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Octaedro/OctaedroPoints.png" alt="Octaedro representado con puntos" width="100%"><br>
      <sub><b>Puntos</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Octaedro/OctaedroTransparente.png" alt="Octaedro transparente" width="100%"><br>
      <sub><b>Transparente</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Octaedro/OctaedroWireframe.png" alt="Octaedro en modo Wireframe" width="100%"><br>
      <sub><b>Wireframe</b></sub>
    </td>
  </tr>
</table>

### Tetraedro

<table>
  <tr>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Tetraedro/TetraedroFlat.png" alt="Tetraedro con sombreado Flat" width="100%"><br>
      <sub><b>Flat</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Tetraedro/TetraedroLine.png" alt="Tetraedro con líneas" width="100%"><br>
      <sub><b>Líneas</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Tetraedro/TetraedroLinePunteada.png" alt="Tetraedro con líneas punteadas" width="100%"><br>
      <sub><b>Líneas punteadas</b></sub>
    </td>
  </tr>
  <tr>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Tetraedro/TetraedroPoints.png" alt="Tetraedro representado con puntos" width="100%"><br>
      <sub><b>Puntos</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Tetraedro/TetraedroTransparente.png" alt="Tetraedro transparente" width="100%"><br>
      <sub><b>Transparente</b></sub>
    </td>
    <td align="center" width="33%">
      <img src="src/img/Figuras/Tetraedro/TetraedroWireframe.png" alt="Tetraedro en modo Wireframe" width="100%"><br>
      <sub><b>Wireframe</b></sub>
    </td>
  </tr>
</table>

## Funcionalidades

### Visualización de figuras geométricas

El programa permite seleccionar y visualizar las siguientes figuras tridimensionales:

- Cubo.
- Esfera.
- Cono.
- Cilindro.
- Tetraedro.
- Dodecaedro.
- Octaedro.

### Transformaciones

Las figuras, gráficas y objetos con textura incluyen controles independientes para modificar:

- Escala.
- Posición en los ejes X, Y y Z.
- Rotación en los ejes X, Y y Z.

Es posible interactuar directamente con algunos objetos directamente con el clic derecho o izquierdo.

### Modos de representación, sombreado y materiales

Las figuras pueden mostrarse con diferentes estilos geométricos:

- Polígono sólido.
- Líneas.
- Líneas punteadas.
- Puntos.
- Wireframe.
- Transparente.
- Colores aleatorios.
--- 
- Flat.
- Gouraud.
- Plástico.
- Metálico.
- Neón.
- Emisivo.
- Oro.
- Rubí.
- Cristal.

### Gráficas tridimensionales

El apartado **Gráficas** genera diez superficies matemáticas diferentes

### Arreglos tridimensionales

El apartado **Arreglos** contiene diez composiciones animadas formadas mediante diferentes combinaciones de:

- Cubos.
- Esferas.
- Conos.
- Cilindros.

Se distribuyen en espirales y otras formaciones tridimensionales, con rotaciones y materiales variados.

### Texturas

El apartado **Texturas** permite:

- Seleccionar una figura base.
- Cargar una imagen desde el equipo.
- Aplicar la textura mediante coordenadas UV.
- Conserva correctamente la proporción de la textura en cada cara de las figuras.
- Modificar escala, posición y rotación.

Algunos efectos interesantes de este apartado son:

- **Modo normal:** muestra la textura directamente sobre la figura.
- **Modo cristal:** aplica transparencia, brillo especular y un acabado similar a liquid glass.
- **Modo reflejo:** aplica un efecto visual de reflexión tipo agua.

### Modelos personalizados

En el apartado **Custom** se puede seleccionar un archivo con extensión:

```text
.obj
```

El modelo se carga dentro del escenario 3D.s

## Tecnologías utilizadas

- **Java**.
- **Java Swing**
- **Java 3D 1.6.2**.
- **Java Vecmath 1.6.2**.
- **Apache NetBeans**.
- **Wavefront OBJ**
- **Java 8**.

## Instalación y ejecución

### Requisitos 

Tener instalado:

- **Java JDK 18** o una versión compatible.
- **Apache NetBeans**.

El repositorio contiene las bibliotecas utilizadas por el proyecto dentro de:

```text
src/lib/j3dcore.jar
src/lib/j3dutils.jar
src/lib/vecmath.jar
```

Las tres bibliotecas pertenecen a la versión **Java 3D 1.6.2**.

## Descargar el proyecto

Antes de descargar el proyecto recomiendo crear una carpeta llamada Paint3D, despues puedes descargar el repositorio como ZIP desde GitHub o clonarlo directamente en la carpeta con Git:

```bash
git clone https://github.com/Adrij0301.....
```

## Abrir el proyecto en NetBeans

- Abre **Apache NetBeans**.
- Selecciona `File`.
- Selecciona `Open Project`.
- Busca la carpeta del proyecto `Paint3D`.
- Selecciona la carpeta que contiene el archivo `build.xml`.
- Presiona `Open Project`.
- Ejecuta `Clean and Build`.
- Presiona `Run Project`.

La clase principal es:

```text
paint3d.Paint3D
```

### Agregar las bibliotecas manualmente

En caso de que Netbeans no reconozca las librerias o esten rotas, ingresalas manualmente:

- Haz clic derecho sobre el proyecto.
- Selecciona `Properties`.
- Entra a `Libraries`.
- Selecciona `Add JAR/Folder`.
- Agrega los siguientes archivos:

```text
src/lib/j3dcore.jar
src/lib/j3dutils.jar
src/lib/vecmath.jar
```
