# Inteligencia Artificial

Repositorio de trabajos, ejercicios y proyectos desarrollados durante la materia de Inteligencia Artificial. El contenido esta organizado por temas: historia y fundamentos de la IA, agentes inteligentes, busquedas, aprendizaje automatico, vision artificial, redes neuronales y aplicaciones con modelos de IA.

## Contenido del repositorio

- [Historia de la Inteligencia Artificial](Historia%20de%20la%20Inteligencia%20Artificial/)
- [Exposicion](Exposici%C3%B3n/)
- [diagnostico](diagnostico/)
- [1.- Busquedas Inteligentes](1.-%20Busquedas%20Inteligentes/)
- [2.- Dataset Rostros](2.-%20Dataset%20Rostros/)
- [3.- Ejercicios de Machine Learning](3.-%20Ejercicios%20de%20Machine%20Learning/)
- [ComentaristaIA](ComentaristaIA/)
- [README.md](README.md)

## Proyectos y ejercicios

### [Historia de la Inteligencia Artificial](Historia%20de%20la%20Inteligencia%20Artificial/)

Material visual sobre la evolucion de la Inteligencia Artificial. Esta seccion funciona como apoyo teorico para ubicar los principales avances, etapas y conceptos historicos de la disciplina.

**Temas vistos:**

- Antecedentes de la IA.
- Evolucion historica de la tecnologia.
- Relacion entre IA, computacion y automatizacion.

### [Exposicion](Exposici%C3%B3n/)

Carpeta con documentos PDF usados como material de investigacion y presentacion. Incluye contenido relacionado con agentes inteligentes y articulos de apoyo para el analisis de temas actuales de IA.

**Temas vistos:**

- Agentes de IA.

### [Diagnostico](diagnostico/)

Ejercicio inicial en Java con estructuras de arbol y nodos. Sirve como practica base para representar datos conectados y recorrer una estructura de forma ordenada.

**Temas vistos:**

- Programacion orientada a objetos.
- Nodos y arboles binarios.
- Busqueda por recorrido en preorden.
- Representacion de estructuras para problemas de IA.

### [1.- Busquedas Inteligentes](1.-%20Busquedas%20Inteligentes/)

Seccion dedicada a problemas clasicos de busqueda. Los ejercicios trabajan con estados, nodos, movimientos validos, expansion de soluciones y comparacion de algoritmos.

#### [8 Puzzle](1.-%20Busquedas%20Inteligentes/8%20puzzle/)

Programa en Java que resuelve un tablero 3x3 usando busqueda en anchura. El estado inicial se expande hasta encontrar el estado objetivo y despues se muestra la ruta de solucion.

**Temas vistos:**

- Representacion de estados.
- Generacion de sucesores.
- Busqueda en anchura (BFS).
- Control de estados visitados.
- Reconstruccion de la ruta de solucion.

#### [24-Puzzle](1.-%20Busquedas%20Inteligentes/24-Puzzle/)

Programa en Java para resolver el 24-Puzzle en un tablero 5x5. Permite trabajar con estados manuales o aleatorios resolubles y medir el rendimiento de los algoritmos.

**Temas vistos:**

- Busqueda no informada.
- Busqueda informada.
- BFS, DFS y UCS.
- IDA*.
- Heuristica de distancia Manhattan.
- Manhattan con conflicto lineal.
- Comparacion por tiempo, movimientos y nodos expandidos.

**Descripcion general:**

El ejercicio modela el tablero como un arreglo de estados y calcula los movimientos posibles del espacio vacio. La solucion se obtiene mediante algoritmos de busqueda y se evalua el costo computacional de cada enfoque. La comparacion permite observar como una buena heuristica reduce el numero de nodos explorados.

### [2.- Dataset Rostros](2.-%20Dataset%20Rostros/)

Seccion enfocada en vision artificial y reconocimiento facial. Contiene datasets de rostros, scripts de procesamiento, aumentacion de imagenes, modelos entrenados y pruebas de reconocimiento.

#### [Reconocimiento Facial](2.-%20Dataset%20Rostros/Reconocimiento_Facial/)

Proyecto para construir un dataset de rostros y preparar imagenes para reconocimiento. Incluye captura con camara, descarga de imagenes, busqueda en internet, deteccion del rostro principal, aumentacion de datos y renumeracion de archivos.

**Temas vistos:**

- Deteccion facial.
- Construccion de datasets.
- Limpieza y organizacion de imagenes.
- Aumentacion de datos.
- Preparacion de datos para entrenamiento.

#### [Proyecto CNN](2.-%20Dataset%20Rostros/Proyecto%20CNN/)

Proyecto de reconocimiento facial basado en redes neuronales convolucionales. Usa datasets organizados por persona, datos aumentados, modelos entrenados y archivos de etiquetas para clasificar rostros.

**Temas vistos:**

- Redes neuronales convolucionales (CNN).
- Clasificacion de imagenes.
- Transferencia de aprendizaje.
- Entrenamiento y evaluacion de modelos.
- Reconocimiento facial con webcam o imagen.
- Clase de desconocido para personas fuera del equipo.

**Descripcion general:**

El proyecto toma imagenes de rostros, las organiza por clase, genera variaciones para mejorar el entrenamiento y entrena una CNN capaz de reconocer personas. Tambien incluye evaluacion del modelo y prediccion en imagenes nuevas o video en tiempo real.

### [3.- Ejercicios de Machine Learning](3.-%20Ejercicios%20de%20Machine%20Learning/)

Coleccion de notebooks y scripts de clase con ejercicios de aprendizaje automatico. Incluye datasets conocidos y ejemplos practicos para trabajar con datos numericos, texto e imagenes.

**Temas vistos:**

- Variables categoricas y numericas.
- Extraccion de caracteristicas de texto.
- Extraccion de caracteristicas de imagen.
- Regresion lineal, Ridge y Lasso.
- SVM y SVR.
- KNN.
- Gradient Boosting.
- KMeans y Mean Shift.
- PCA y reduccion de dimensionalidad.
- Perceptron y redes neuronales.
- Multilayer Perceptron.
- CNN aplicada a imagenes.

**Descripcion general:**

Esta seccion funciona como banco de practicas de Machine Learning. Los ejercicios muestran el flujo general de trabajo: cargar datos, limpiar o transformar caracteristicas, entrenar modelos, evaluar resultados y comparar tecnicas segun el tipo de problema.

### [ComentaristaIA](ComentaristaIA/)

Aplicacion de IA multimodal para narrar automaticamente clips de videojuegos. Esta enfocada en videos de Minecraft y combina vision artificial, modelos de lenguaje, reglas heuristicas, sintesis de voz e interfaz web.

**Temas vistos:**

- IA multimodal.
- Procesamiento de video.
- Clasificacion zero-shot con modelos de vision.
- Modelos de lenguaje para generar comentarios.
- Sintesis de voz en espanol.
- Heuristicas para controlar repeticion y confianza.
- Integracion de IA en una aplicacion interactiva.

**Descripcion general:**

El sistema analiza frames de un video, identifica acciones relevantes del gameplay y genera comentarios breves en espanol. Despues convierte esos comentarios a audio y puede combinarlos con el video original. La aplicacion incluye una interfaz para revisar la narracion y los frames donde la IA decidio comentar.

**Componentes principales:**

- `app.py`: interfaz y flujo principal de la aplicacion.
- `ia_engine.py`: carga y uso de modelos de vision, lenguaje y voz.
- `utils.py`: utilidades de video, comparacion visual, suavizado y mezcla final.
- `requirements.txt`: dependencias del proyecto.

## Tecnologias utilizadas

- Java.
- Python.
- Jupyter Notebook.
- Streamlit.
- OpenCV.
- TensorFlow y Keras.
- scikit-learn.
- Hugging Face Transformers.
- MoviePy.
- MTCNN.

## Aprendizajes principales

- Modelar problemas de IA mediante estados, nodos y transiciones.
- Aplicar algoritmos de busqueda para encontrar soluciones.
- Comparar algoritmos usando metricas como tiempo, profundidad y nodos expandidos.
- Preparar datasets para proyectos de vision artificial.
- Entrenar y evaluar modelos de Machine Learning y Deep Learning.
- Usar CNN para clasificacion de imagenes y reconocimiento facial.
- Integrar modelos preentrenados en una aplicacion funcional.
- Combinar vision, lenguaje y audio en un sistema multimodal.

## Organizacion general

El repositorio esta separado por bloques de aprendizaje. Las primeras carpetas contienen fundamentos teoricos y diagnostico; despues se encuentran ejercicios de busqueda inteligente, practicas de vision artificial y datasets de rostros; finalmente se incluyen ejercicios de Machine Learning y el proyecto multimodal ComentaristaIA.

El objetivo general del repositorio es documentar el avance practico en diferentes areas de Inteligencia Artificial, desde algoritmos clasicos de busqueda hasta modelos modernos aplicados a imagen, texto, audio y video.
