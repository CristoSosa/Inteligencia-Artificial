# 🎙️ ComentaristaIA: Narrador de Videojuegos Multimodal

ComentaristaIA es un sistema inteligente multimodal de caster/narrador de videojuegos en español para jugadas grabadas en archivos de video `.mp4`. La aplicación está diseñada con un tema oscuro premium tipo esports y aprovecha el poder del procesamiento paralelo de modelos en GPU (CUDA) para un análisis y síntesis de voz en milisegundos.

---

## 🏗️ Arquitectura del Sistema

El flujo del sistema integra visión artificial, procesamiento heurístico lógico y síntesis de voz mediante los siguientes módulos comunicados de forma asíncrona y reactiva:

```mermaid
graph TD
    A[Cargar Video .mp4] --> B[Extracción de Cuadros a FPS IA]
    B --> C[Análisis Visual: CLIP Zero-Shot]
    B --> D[Similitud de Movimiento: Histograma HSV]
    C --> E[Controlador de Heurísticas]
    D --> E
    E -->|1. Cooldown Listo?<br/>2. Confianza Mínima?<br/>3. Cambio Significativo?| F[NLP Hugging Face: FLAN-T5]
    F --> G[Sintetizador de Voz: MMS-TTS-SPA]
    G --> H[Reproducción Automática en UI]
    G --> I[Guardar Timeline y Buffer de Audio]
    I --> J[Renderizar e Integrar Video Final con MoviePy]
```

### 1. Modelos de Inteligencia Artificial (Hugging Face)
* **Visión (SigLIP + CLIP Large)**: Usamos `google/siglip-so400m-patch14-384` como modelo principal y `openai/clip-vit-large-patch14` como segundo voto de ensemble. El motor compara cada frame contra prompts descriptivos por videojuego, agrega el score por categoría y aplica pesos/umbrales para reducir falsos positivos en estados raros.
* **Lenguaje / Narración (FLAN-T5)**: Usamos `google/flan-t5-small` con `pipeline("text2text-generation")` para convertir el estado detectado en una frase breve de comentarista en español. Si el modelo no está disponible o genera texto fuera de contexto, la app cae a plantillas de respaldo.
* **Audio / Voz (MMS-TTS)**: Usamos el modelo `facebook/mms-tts-spa` de Meta optimizado para español. Este modelo de Text-to-Speech corre en la GPU local para generar voz de forma veloz.

### 2. Lógica de Control (Heurísticas en `utils.py`)
Para evitar que el narrador hable continuamente o repita comentarios ante escenas estáticas:
* **Filtro de Similitud HSV**: Compara los histogramas HSV de dos frames consecutivos. Si la correlación supera el umbral (por defecto `95%`), el frame se ignora porque no hay cambios significativos de acción.
* **Manejador de Cooldown**: Se establece un tiempo de espera (por defecto `8s`) entre audios para evitar solapamientos.
* **Confianza mínima y suavizado**: Se promedian las últimas 3 predicciones para evitar saltos por ruido. Además, la app solo comenta si la confianza supera el umbral configurado.
* **Clasificación multicrop**: Para Minecraft se evalúa el frame completo, una zona sin hotbar y recortes centrados en la mira. Esto mejora la detección de bloques/minerales porque el modelo mira donde normalmente actúa el jugador.
* **Disparador por Cambio de Estado**: Si el modelo detecta que el estado cambió con suficiente confianza (ej. de *Exploración* a *Combate*), se genera un comentario sin hablar en cada frame.
* **Reconocimiento extendido para Minecraft**: La app está enfocada en Minecraft y distingue acciones como `talar`, `crafteo`, `fundición`, `cultivo`, `pesca`, `ganadería`, `cofre`, `encantamiento`, `pociones`, `comer`, `dormir`, `nadar`, `bote`, `portal`, `nether`, `aldea`, `comercio` y minería específica por mineral: carbón, hierro, cobre, oro, redstone, lapislázuli, diamante, esmeralda y escombros ancestrales.

### 3. Mezcla Final de Narración
Al finalizar la transmisión de análisis, la aplicación permite exportar el video original. El script utiliza `MoviePy` para calcular el posicionamiento temporal exacto de los audios `.wav` generados y mezclarlos sobre la pista de audio original del juego (cuyo volumen se atenúa para asegurar que la voz del narrador se escuche de forma clara y nítida).

---

## 📁 Estructura del Proyecto

* **[app.py](file:///c:/Users/XPG/Desktop/ComentaristaIA/app.py)**: Interfaz Streamlit premium optimizada con estilos CSS personalizados. Administra el bucle de procesamiento, las variables de estado y la reproducción multimedia.
* **[ia_engine.py](file:///c:/Users/XPG/Desktop/ComentaristaIA/ia_engine.py)**: Orquestador de pipelines de Hugging Face. Detecta y configura CUDA (GPU) y genera diálogos creativos adaptados para *Minecraft*.
* **[utils.py](file:///c:/Users/XPG/Desktop/ComentaristaIA/utils.py)**: Contiene las utilidades para metadatos de video, comparación de histogramas de color y mezcla/fusión final de audio y video.
* **[requirements.txt](file:///c:/Users/XPG/Desktop/ComentaristaIA/requirements.txt)**: Lista de dependencias del entorno de ejecución, configurado con soporte para CUDA 12.1.

---

## 🚀 Requisitos e Instalación

### Requisitos Previos
* **Python**: `3.10` a `3.13` (El entorno se configuró con Python 3.13).
* **NVIDIA GPU**: Recomendado GeForce RTX (el proyecto corre en una **RTX 4060 Ti con 8GB VRAM** con soporte para CUDA).

### Configuración e Instalación
El entorno ya está creado y configurado en tu directorio. Si necesitas volver a configurar el entorno virtual, corre:

```powershell
# Crear entorno virtual
py -m venv venv

# Activar entorno
.\venv\Scripts\Activate.ps1

# Instalar dependencias
pip install -r requirements.txt
```

---

## 🎮 Ejecución

Para iniciar la aplicación, ejecuta el siguiente comando desde la raíz del proyecto:

```powershell
.\venv\Scripts\python -m streamlit run app.py
```

La consola te proporcionará una URL local (normalmente `http://localhost:8501`) donde podrás interactuar con la interfaz del sistema.

### Pasos de Uso en la App:
1. **Sube tu video** `.mp4` de gameplay en la barra lateral izquierda.
2. Revisa o ajusta las etiquetas de clasificación de Minecraft si quieres forzar un conjunto más pequeño de acciones.
3. Ajusta **Confianza mínima para comentar** si el video tiene overlays, baja calidad o escenas oscuras. Un valor más alto reduce comentarios equivocados; uno más bajo hace que el bot hable más.
4. Haz clic en **🎙️ Iniciar Transmisión y Narración** para procesar el video.
5. Observa el análisis en tiempo real en la pantalla izquierda y el top de predicciones CLIP en la consola del caster.
6. Al finalizar, ajusta el volumen del juego de fondo y haz clic en **🚀 Renderizar Video Final** para descargar tu gameplay con la voz de la IA integrada.
