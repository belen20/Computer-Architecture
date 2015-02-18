# MIPS SIMULATION ENVIRONMENT – TEACHING TOOL
En entorno de simulación de MIPS es una herramienta docente para la explicación y la ejercitación del conjunto de técnicas de paralelismo a nivel de instrucciones, conocido con el acrónimo en inglés de ILP. Se ha programado un diseño modular de la arquitectura MIPS que permite la ejecución segmentada de una instrucción, con el objetivo de ofrecer la posibilidad de comparar diferentes escenarios. Estos escenarios son las propuestas de mejora sobre ILP. Además, se adjuntan unos recursos didácticos junto con la herramienta.

#Abstract 
In this beta version, the code comments and teaching resources are in Spanish. We will work to translate to English.

#Actividades docentes

(pendiente)

#Clases
El diagrama de clases de la aplicación es el siguiente.
![alt text](https://www.dropbox.com/s/dmqe12mc2etfyha/Class.PNG?dl=0 "Logo Title Text 1")
#####Clase _Main_
En esta clase se crea toda la estructura necesaria para simular la ejecución segmentada, es decir, la arquitectura correspondiente y la comprobación de que se dispone de todos los ficheros necesarios para llevar a cabo la ejecución.

#####_Interface MIPS_
En esta _interface_ se declaran todas las características típicas de una arquitectura general: el número de bits, el número de registros de proósito general, las unidades funcionales implementaas y los estados por lo cuales pasa una instrucción.

#####Clase _Architecture_
Se crean todas las estructuras de la arquitectura: registros de propósito general, ROB, memoria de datos y memoria de instrucciones.

#####Clase _BrachManager_
En esta clase se implementa la gestión de los saltos. Se decide si un salto se ejecuta o no a partir de la comprobación de las condiciones de salto y se carga el nuevo estado de la máquina.

#####Clase _Dependence_
Se implementa el análisis de las dependencias entre las instrucciones y se crea el grafo de dependencias correspondiente.

#####Clase _Instruction_
Se define lo que es una instrucción (OPCODE y operandos) y otros atributos necesarios para la ejecución: información sobre que tipo de instrucción es, si es la causante de una dependencia, si es una instrucción ejecutada de forma especulativa y cuando ha entrado y ha salido de cada una de las fases de la segmentación.

#####Clase _Memory_
En esta clase se define lo que es la memoria de datos como un conjunto de palabras, así como las funciones necesarias para almacenar variables y vectores y su correspondiente lectura.

#####Clase _Word_
Se implementan todas aquellas funciones relacionadas con el tratamiento de una palabra de memoria: almacenamiento a nivel de word y direccionamiento.

#####Clase _Operations_
En esta clase se implementan las operaciones que se llevan a cabo por cada uno de los tipos de instrucción. Es decir, para cada uno de los OPCODEs qué acciones se llevan a cabo en la arquitectura.

#####Clase _Pipeline_
Se implementa la ejecución de forma segmentada de las instrucciones a partir de la simulación de un reloj que controla que la llamada en paralelo a cada una de las fases.

#####Clase _ROB_
En esta clase se implementan las funciones relacionadas con el funcionamiento de la ROB, como la entrada de una instruccion, mantener el orden de las instrucciones y la salida de las mismas, así como la realización de la fase de _commit_.

#####Clase _Scheduler_
Clase que define todas las funciones necesarias para llevar a cabo la planificación de instrucciones en función de los parámetros de la arquitectura definidos.

#####Clase _State_
Se definen los estados por los que puede pasar una instrucción mientras está dentro de la unidad de segmentación.

#####Clase _Statistics_
Se implementa la realización de los cálculos necesarios para extraer las medidas de rendimiento resultantes de la ejecución de las instrucciones.

#####Clases para el tratamiento de los ficheros de configuración.
- Clase _ConfigFile_: clase encargada de cargar la configuración definida de la arquitectura.
- Clase _ConfigRegisters_: clase encargada de cargar el valor de los registros de propósito general.
- Clase _SimResult_: clase encargada del volcado de los resultados de la ejecución.

#####Clase _FunctionalUnit_
Clase abstracta donde se define qué es y qué funciones tiene una unidad funcional genérica. Las clases FuGen, FuMult y FuAdd son instacias de ésta clase que definen una unidad funcinal genérica, de multiplicación y de suma correspondientemente.

#####Clases encargadas de la generación de código aleatorio
- Clase _GenCodeFile_: clase encargada de cargar la configuración del código a generar.
- Clase _GenCode_: clase que implementa las funciones encargadas de generar el código de forma aleatoria a partir del comportamiento del código definido.


#Colaboración
Cualquier duda, sugerencia, participación y mejora del código presentado será bienvenida. Por favor, utilice la sección "Issues" de GITHUB para tal fin. 

#Licencia
######Copyright 2015 UIB-DMI. All rights reserved.  Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:   1. Redistributions of source code must retain the above copyright notice, this list of     conditions and the following disclaimer.   2. Redistributions in binary form must reproduce the above copyright notice, this list     of conditions and the following disclaimer in the documentation and/or other materials     provided with the distribution.  THIS SOFTWARE IS PROVIDED BY UIB-DMI ''AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  The views and conclusions contained in the software and documentation are those of the authors and should not be interpreted as representing official policies, either expressed or implied, of authors.


