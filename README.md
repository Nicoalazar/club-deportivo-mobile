# 🏋️ Grupo 20 – Gimnasio y Club
### Aplicación Android — Gestión de Club Deportivo

---

## 📋 Descripción

Aplicación móvil nativa para Android que digitaliza la gestión del **Grupo 20 – Gimnasio y Club**.  
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

**Grupo:** Nro. 20  
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
| Target SDK | API 34 (Android 14) |

---

## 📦 Package

```
com.grupo20.clubdeportivo
```

---

## 🗂️ Estructura del proyecto

```
app/
└── src/
    └── main/
        ├── java/com/grupo20/clubdeportivo/
        │   ├── MainActivity.kt          # Punto de entrada (Splash)
        │   ├── LoginActivity.kt         # Pantalla de login
        │   │
        │   ├── admin/
        │   │   ├── DashboardAdminActivity.kt
        │   │   ├── socios/
        │   │   │   ├── ListaSociosActivity.kt
        │   │   │   ├── DetalleSocioActivity.kt
        │   │   │   └── AltaSocioActivity.kt
        │   │   ├── nosocios/
        │   │   │   ├── ListaNoSociosActivity.kt
        │   │   │   └── CobroNoSocioActivity.kt
        │   │   ├── pagos/
        │   │   │   └── RegistrarPagoActivity.kt
        │   │   └── vencimientos/
        │   │       └── VencimientosActivity.kt
        │   │
        │   ├── socio/
        │   │   ├── DashboardSocioActivity.kt
        │   │   ├── PerfilSocioActivity.kt
        │   │   └── ActividadesActivity.kt
        │   │
        │   ├── db/
        │   │   ├── AppDatabase.kt       # Configuración Room
        │   │   ├── dao/                 # Data Access Objects
        │   │   └── entities/            # Entidades (Socio, Pago, Actividad...)
        │   │
        │   └── model/
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
| 5 | Detalle de Socio | Administrador |
| 6 | Alta de Socio / No Socio | Administrador |
| 7 | Vencimientos | Administrador |
| 8 | Registrar Pago | Administrador |
| 9 | Listado de No Socios | Administrador |
| 10 | Cobro de Actividad (No Socio) | Administrador |
| 11 | Dashboard Socio | Socio |
| 12 | Perfil Socio | Socio |
| 13 | Actividades | Socio / Admin |

---

## 🎨 Identidad visual

| Token | Valor |
|---|---|
| Azul oscuro (primario) | `#1B4F8A` |
| Azul claro (secundario) | `#6AA8D0` |
| Fondo | `#F5F8FC` |
| Texto principal | `#1A1A1A` |
| Estado al día | `#2E7D32` |
| Estado vencido | `#C62828` |
| Tipografía | Roboto |

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

[Grupo20 - Club Deportivo App](https://www.figma.com/design/KieX4MyKZjmrFta27p31le)

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

## 📌 Estado del proyecto

| Etapa | Estado |
|---|---|
| Análisis | ✅ Completo |
| Diseño (Figma) | ✅ Completo |
| Entorno Android Studio | 🔄 En progreso |
| Codificación | ⏳ Pendiente |
| Distribución | ⏳ Pendiente |