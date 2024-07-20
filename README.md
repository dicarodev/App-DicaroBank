
# DICARO BANK ANDROID APP

Esta aplicación móvil proporciona servicios bancarios a los usuarios de Dicaro Bank, permitiéndoles gestionar sus cuentas, realizar transferencias y utilizar servicios como Bizum.

## Comenzando 🚀
Estas instrucciones te permitirán obtener una copia de la API en funcionamiento en tu máquina local para propósitos de desarrollo y pruebas.

## Pre-requisitos 📋
Necesitarás las siguientes herramientas y entornos instalados en tu máquina:

- Java Development Kit (JDK) 21.0.2
- Android Studio

## Instalación 🔧
Sigue estos pasos para configurar el entorno de desarrollo:

1. Clonar el repositorio del proyecto:
```bash
git clone https://github.com/dicarodev/App-DicaroBank.git
cd App-DicaroBank
```
2. Abrir el proyecto en Android Studio:
   Descargar la imagen de Oracle:
- Abre Android Studio.
- Selecciona "Open an existing Android Studio project".
- Navega a la carpeta donde clonaste el repositorio y selecciona la carpeta del proyecto.
3. Configurar Retrofit para la comunicación con la API:
   En el archivo `DicaroBankApiAdapter.java`, asegúrate de que la URL base esté configurada correctamente para apuntar a la API REST del servidor:
```bash
final String BASE_URL = "http://tu-servidor:puerto/";
```
```bash
final String BASE_URL = "http://10.0.2.2:9001/"; // 10.0.2.2 en el emulador hace referencia a localhost
```

4. Construir y ejecutar la aplicación:
- Conecta un dispositivo Android o usa un emulador.
- En Android Studio, selecciona "Run" > "Run 'app'" para construir y ejecutar la aplicación en el dispositivo/emulador.

## Construido con 🛠️
- Android Studio - Entorno de desarrollo integrado para Android
- Retrofit - Cliente HTTP para Android y Java
- Java - Lenguaje de programación


## Authors

- [@dicarodev](https://www.github.com/dicarodev)