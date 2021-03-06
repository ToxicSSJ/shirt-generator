
# 馃憰 Shirt Generator

ShirtGenerator es el proyecto resultado del primer parcial de Computaci贸n Grafica, es entonces la implementaci贸n de un programa que permite graficar la figura de una camiseta en base a unas medidas que el usuario puede introducir. Asi mismo, el usuario puede rotar la figura generada, transladarla y escalarla. Para esto el programa maneja internamente un arreglo bidimensional donde guarda las posiciones de los puntos al momento de graficar y al momento de hacer las operaciones 茅ste las aplica sobre el vector provocando que se actualice la vista (canvas) y se vean reflejados esos cambios.

- Para el c谩lculo de puntos se utiliz贸 meramente las medidas partiendo de un punto origen (x, y) c谩lculado en base al tama帽o del canvas que es de `600 x 600` pixeles. De esta forma cada punto se c谩lcula por medio de formulas matematicas en base a la medida proporcionada hasta tener todos los puntos y poder finalmente realizar los trazos.

- Para el trazo de las lineas se opt贸 por usar el algoritmo de Casteljau, con una variaci贸n (offset x, y) definido en c贸digo para cada tipo de trazo, lo que permite generar puntos de control automaticos y darle as铆 una forma m谩s realista a la camiseta.

- Para las operaciones se utiliz贸 un arreglo bidimensional de posiciones, donde cada posici贸n tiene un tipo de posici贸n y a su vez tiene otra posici贸n hermana, por lo cual para la operaci贸n rotar simplemente se aplica una rotaci贸n a este arreglo bidimensional por medio del algoritmo definido, asi mismo para el escalamiento, y finalmente para la translaci贸n se manejan valores relativos al punto origen (x, y), lo cual le permite al usuario mover el objeto libremente. De este modo cuando el programa detecta un cambio en cualquiera de los parametros de entrada vuelve a pintar el canvas, aplicando graficamente estas operaciones y haciendo que el rederizado se haga solo cuando es necesario.

## Tecnolog铆as Utilizadas

- OpenJDK 17.0.2
- JavaFX
- Maven

## Descargar

Para descargar se puede ir al apartado de `releases` o utilizar este link: <https://github.com/ToxicSSJ/shirt-generator/releases/tag/v0.1>. Una vez all铆 descargar la versi贸n disponible seg煤n tu sistema operativo.
> Si va a ejecutar en MacOS tan solo debe ejecutar el archivo `bin/app`. En este sistema operativo es posible que lance multiples errores de permisos, en ese caso es porque el software no tiene licencia para correr en MacOS y este lo detecta como posible Malware, asi que se debe permitir la ejecuci贸n desde el apartado de seguridad.

> Si va a ejecutar en Windows tan solo debe ejecutar el archivo `bin/app.bat`.

## Compilar

Para compilar es importante contar con las tecnolog铆as utilizadas en el apartado de arriba, se recomienda de hecho usar el JDK de `GraalVM 22.0.0.2` para tener la menor cantidad de problemas posibles. Asi mismo, una vez instalados todos los requisitos es necesario clonar el proyecto y basta con ejecutar la task `mvn javafx:run` en la raiz del `pom.xml` para compilar y ejecutar programa.

## Utilizar

1. Al abrir el programa se debe mostrar visualmente de esta forma:
   ![image](https://imgur.com/RjuGS2g.png)

2. A continuaci贸n cada parte del programa explicada:
   ![image](https://imgur.com/rxZorl2.png)

3. Ejemplo de uso cambiando varias configuraciones:
   ![image](https://imgur.com/CDC71Fp.png)

## Desarrollador
- Abraham M. Lora
