# 🏋️ Grupo 9 – Gimnasio y Club
### Aplicación Android — Gestión de Club Deportivo

---

## 📋 Descripción

Aplicación móvil nativa para Android que digitaliza la gestión del **Grupo 9 – Gimnasio y Club**.  
Permite administrar socios, registrar pagos, controlar vencimientos de cuotas y consultar el cronograma de actividades.

Este proyecto es la migración del sistema de escritorio desarrollado en C#/.NET/WinForms durante las materias **DSOO** y **MDS**, adaptado al ecosistema Android con Kotlin.

---

## 👥 Equipo

| Integrante | Comisión |
|---|---|
| Albornoz | A |
| Blanco | A |
| Olivera | A |
| Tome | A |
| Zalazar | A |

**Grupo:** Nro. 9  
**Materia:** Desarrollo de Aplicaciones Móviles  
**Profesor:** Prof. Kevin Del Bello  
**Instituto:** IFTS Nº 29 — 2026, 1° cuatrimestre

---

## 🛠️ Tecnologías

| Herramienta | Versión / Detalle |
|---|---|
| Lenguaje | Kotlin |
| IDE | Android Studio |
| UI | Empty Views Activity + LinearLayout |
| Base de datos | SQLite (Room) |
| Min SDK | API 24 (Android 7.0) |
| Target SDK | API 36 (Android 16) |

---

## 📦 Package

```
com.grupo9.clubdeportivo
```

---

## 🗂️ Estructura del proyecto

```
app/
└── src/
    └── main/
        ├── java/com/grupo9/clubdeportivo/
        │   ├── MainActivity.kt          # Punto de entrada (Splash)
        │   ├── LoginActivity.kt         # Pantalla de login
        │   │
        │   ├── admin/
        │   │   ├── DashboardAdminActivity.kt
        │   │   ├── socios/
        │   │   │   ├── ListaSociosActivity.kt
        │   │   │   ├── BuscarSociosActivity.kt
        │   │   │   ├── DetalleSocioActivity.kt
        │   │   │   └── AltaSocioActivity.kt
        │   │   ├── noSocios/
        │   │   │   ├── ListaNoSociosActivity.kt
        │   │   │   └── CobroActividadActivity.kt
        │   │   ├── pagos/
        │   │   │   └── RegistrarPagoActivity.kt
        │   │   └── vencimientos/
        │   │       └── VencimientosActivity.kt
        │   │
        │   ├── socio/                   # (pendiente)
        │   │   ├── DashboardSocioActivity.kt
        │   │   ├── PerfilSocioActivity.kt
        │   │   └── ActividadesActivity.kt
        │   │
        │   ├── db/                      # (pendiente — Room)
        │   │   ├── AppDatabase.kt
        │   │   ├── dao/
        │   │   └── entities/
        │   │
        │   └── model/                   # (pendiente)
        │       ├── Socio.kt
        │       ├── NoSocio.kt
        │       ├── Pago.kt
        │       └── Actividad.kt
        │
        └── res/
            ├── layout/                  # XML de cada Activity
            ├── values/
            │   ├── colors.xml
            │   ├── strings.xml
            │   └── themes.xml
            └── drawable/
```

---

## 🖥️ Pantallas

| # | Pantalla | Rol |
|---|---|---|
| 1 | Splash | Todos |
| 2 | Login | Todos |
| 3 | Dashboard Admin | Administrador |
| 4 | Listado de Socios | Administrador |
| 5 | Búsqueda de Socios | Administrador |
| 6 | Detalle de Socio | Administrador |
| 7 | Alta de Socio / No Socio | Administrador |
| 8 | Vencimientos | Administrador |
| 9 | Registrar Pago | Administrador |
| 10 | Listado de No Socios | Administrador |
| 11 | Cobro de Actividad (No Socio) | Administrador |
| 12 | Dashboard Socio | Socio |
| 13 | Perfil Socio | Socio |
| 14 | Actividades | Socio / Admin |

---

## 🎨 Identidad visual

| Nombre | Hex | Uso |
|---|---|---|
| `colorPrimary` | `#1B4F8A` | Header, íconos, texto primario |
| `colorSecondary` | `#6AA8D0` | Avatar, acentos secundarios |
| `colorPrimaryDark` | `#1A3A5C` | Botones activos, selección |
| `colorPrimaryLight` | `#CCE4F7` | Subtítulos sobre fondo primario |
| `colorBackground` | `#F5F8FC` | Fondo de pantallas |
| `colorBackgroundGray` | `#F0F0F0` | Botones desactivados |
| `colorTextPrimary` | `#1A1A1A` | Texto principal |
| `colorTextMuted` | `#888888` | Texto secundario / hint |
| `colorTextHint` | `#9E9E9E` | Texto muy tenue |
| `colorStatusOk` | `#2E7D32` | Texto "Al día" |
| `colorStatusOkLight` | `#E0F4E3` | Fondo badge "Al día" |
| `colorError` | `#C62828` | Texto "Vencida" |
| `colorErrorLight` | `#FDDEDE` | Fondo badge "Vencida" |
| `colorSuccess` | `#34C759` | Íconos de éxito |
| `colorWarning` | `#FF8D28` | Íconos de advertencia |
| Tipografía | Roboto | — |

---

## 🗄️ Modelo de datos

### Entidades principales

**Socio**
- id, nombre, apellido, dni, email, telefono
- estado (Al día / Vencida), fechaVencimiento, nroCarnet, aptoFisico

**NoSocio**
- id, nombre, apellido, dni, email, telefono, nroCarnet

**Pago**
- id, idPersona, tipo (Mensual / Diario), monto, fecha, metodoPago

**Actividad**
- id, nombre, horario, profesor, cupoTotal, cupoOcupado, costoDiario

---

## 🔀 Flujo de navegación

```
Splash
  └── Login
        ├── Dashboard Admin
        │     ├── Listado Socios
        │     │     ├── Detalle Socio
        │     │     │     └── Registrar Pago
        │     │     └── Alta Socio
        │     ├── No Socios
        │     │     └── Cobro Actividad
        │     ├── Vencimientos
        │     └── Actividades
        └── Dashboard Socio
              ├── Perfil Socio
              └── Actividades
```

---

## 🔗 Prototipo Figma

[Grupo9 - Club Deportivo App](https://www.figma.com/design/KieX4MyKZjmrFta27p31le)

---

## 📁 Sistema desktop original

El sistema de escritorio del que parte este proyecto está disponible en:  
[club-deportivo-dotnet](https://github.com/Nicoalazar/club-deportivo-dotnet)

Desarrollado en C# / .NET / WinForms con base de datos MySQL.

---

## 🚀 Cómo ejecutar el proyecto

1. Clonar el repositorio
2. Abrir con **Android Studio**
3. Esperar a que Gradle sincronice las dependencias
4. Conectar un dispositivo Android o iniciar un emulador (API 24 o superior)
5. Presionar **Run ▶**

---

## 🔒 Credenciales

En etapa de desarrollo se ingresa con:
- Usuario: **admin**
- Password: **admin123**

El botón **¿Olvidaste tu contraseña?** muestra un Toast con las credenciales.

**⚠️ SOLO PARA DESARROLLO ⚠️**

---

## 📌 Estado del proyecto

| Etapa                      | Estado |
|----------------------------|---|
| Análisis                   | ✅ Completo |
| Diseño (Figma)             | ✅ Completo |
| Entorno Android Studio     | 🔄 En progreso |
| Codificación               | 🔄 En progreso |
| Conexion con base de datos | ⏳ Pendiente |
| Presentación               | ⏳ Pendiente |