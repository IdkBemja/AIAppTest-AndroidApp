<img src="https://github.com/IdkBemja/Urrahost/blob/main/assets/imgs/logo.png" alt="UrraHosting Logo" width="180"/>

# AI Chat Test (Android App)

**Conectado a [https://aichattest.urrahost.app](https://aichattest.urrahost.app)**  
**Desarrollado por [UrraHosting](https://urrahost.app)**

AI Chat Test es una aplicación Android desarrollada en Kotlin con Jetpack Compose, diseñada para interactuar con un backend de IA alojado en UrraHosting. La app permite iniciar sesión, enviar preguntas a modelos de IA y ver las respuestas en tiempo real.

## Características principales

- **Pantalla de login** con interfaz moderna y minimalista.
- **Chat en tiempo real** con modelo de IA a través de API REST.
- **Autenticación JWT** para conexiones seguras con el servidor.
- **Interfaz de usuario intuitiva** con Jetpack Compose.
- **Soporte para caracteres especiales** (tildes, eñes, etc.).
- **Scroll automático** al enviar nuevos mensajes.
- **Envío por teclado** (compatibilidad con Enter/Send).
- **Almacenamiento seguro de tokens** con TokenManager.
- **Conexión asíncrona** con el servidor sin bloquear la interfaz de usuario.

## Tecnologías utilizadas

- **Kotlin** - Lenguaje principal
- **Jetpack Compose** - Framework de UI
- **Coroutines** - Para operaciones asíncronas
- **Material Design 3** - Principios de diseño
- **SharedPreferences** - Almacenamiento seguro de tokens

## Estructura del proyecto

```
app/
  ├── src/main/
  │   ├── java/me/idkbemja/aichatapp/
  │   │   ├── templates/
  │   │   │   ├── ChatUI.kt        # Pantalla de chat
  │   │   │   └── LoginUI.kt       # Pantalla de login
  │   │   ├── utils/
  │   │   │   ├── AuthHandler.kt   # Manejo de autenticación
  │   │   │   ├── IAChatHandler.kt # Comunicación con la IA
  │   │   │   └── TokenManager.kt  # Gestión de token JWT
  │   │   └── MainActivity.kt      # Punto de entrada de la app
  │   ├── res/                     # Recursos (layouts, imágenes, etc.)
  │   └── AndroidManifest.xml      # Configuración de la app
  └── build.gradle.kts             # Configuración de dependencias
```

## Integración con Backend

La aplicación se conecta al backend alojado en UrraHosting a través de los siguientes endpoints:

- `POST /api/login` — Autenticación y obtención de JWT
- `POST /api/ask` — Envío de preguntas a la IA (autenticado)
- `POST /api/logout` — Cierre de sesión y invalidación del token

## Características de diseño

- **Tema oscuro** - Interfaz minimalista con fondo oscuro y acentos claros
- **Burbujas de chat** - Diferenciación visual entre mensajes del usuario y respuestas de la IA
- **Indicador de carga** - Muestra "Esperando respuesta..." mientras la IA procesa la consulta

## Seguridad

- **Sin almacenamiento persistente** de conversaciones (solo en memoria)
- **Gestión segura** de tokens JWT usando SharedPreferences
- **Permisos mínimos** - Solo se requiere acceso a internet

## Requisitos

- **Android 5.0+** (API Level 21)
- **Conexión a internet**

## Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/IdkBemja/AIAppTest-Android.git
   ```

2. Abre el proyecto en Android Studio

3. Ejecuta la app en un emulador o dispositivo físico

## Contribución

Las contribuciones son bienvenidas. Por favor, haz fork del proyecto y crea un Pull Request con tus cambios.

## Licencia

MIT

---

### Desarrollado por [UrraHosting](https://www.urrahost.app)
