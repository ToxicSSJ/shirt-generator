
# 👕 Shirt Generator

ShirtGenerator es el proyecto resultado del primer parcial de Computación Grafica, es entonces la implementación de un programa que permite graficar la figura de una camiseta en base a unas medidas que el usuario puede introducir. Asi mismo, el usuario puede rotar la figura generada, transladarla y escalarla. Para esto el programa maneja internamente un arreglo bidimensional donde guarda las posiciones de los puntos al momento de graficar y al momento de hacer las operaciones éste las aplica sobre el vector provocando que se actualice la vista (canvas) y se vean reflejados esos cambios.

- Para el cálculo de puntos se utilizó meramente las medidas partiendo de un punto origen (x, y) cálculado en base al tamaño del canvas que es de `600 x 600` pixeles. De esta forma cada punto se cálcula por medio de formulas matematicas en base a la medida proporcionada hasta tener todos los puntos y poder finalmente realizar los trazos.

- Para el trazo de las lineas se optó por usar el algoritmo de Casteljau, con una variación (offset x, y) definido en código para cada tipo de trazo, lo que permite generar puntos de control automaticos y darle así una forma más realista a la camiseta.

- Para las operaciones se utilizó un arreglo bidimensional de posiciones, donde cada posición tiene un tipo de posición y a su vez tiene otra posición hermana, por lo cual para la operación rotar simplemente se aplica una rotación a este arreglo bidimensional por medio del algoritmo definido, asi mismo para el escalamiento, y finalmente para la translación se manejan valores relativos al punto origen (x, y), lo cual le permite al usuario mover el objeto libremente. De este modo cuando el programa detecta un cambio en cualquiera de los parametros de entrada vuelve a pintar el canvas, aplicando graficamente estas operaciones y haciendo que el rederizado se haga solo cuando es necesario.

## Tecnologías Utilizadas

- OpenJDK 17.0.2
- JavaFX
- Maven

## Descargar

Para descargar se puede ir al apartado de `releases` o utilizar este link: <https://github.com/ToxicSSJ/shirt-generator/releases/tag/v0.1>. Una vez allí descargar la versión disponible según tu sistema operativo.
> Si va a ejecutar en MacOS tan solo debe ejecutar el archivo `bin/app`.

> Si va a ejecutar en Windows tan solo debe ejecutar el archivo `bin/app.bat`.

## Compilar

Para compilar es importante contar con las tecnologías utilizadas en el apartado de arriba, se recomienda de hecho usar `GraalVM 22.0.0.2` para tener la menor cantidad de problemas posibles. Asi mismo, una vez instalados todos los requisitos es necesario clonar el proyecto y basta con ejecutar la task `mvn javafx:run` en la raiz del `pom.xml` para compilar y ejecutar programa.

## Utilizar

1. Al abrir el programa se debe mostrar visualmente de esta forma:
   ![image](https://imgur.com/RjuGS2g.png)

2. A continuación cada parte del programa explicada:
   ![image](https://imgur.com/rxZorl2.png)

3. Ejemplo de uso cambiando varias configuraciones:
   ![image](https://imgur.com/CDC71Fp.png)

## Desarrollador
- Abraham M. Lora