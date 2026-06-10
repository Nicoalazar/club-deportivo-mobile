# 🏋️ Grupo 9 – Gimnasio y Club

### Aplicación Android — Gestión de Club Deportivo

---

## 📋 Descripción

Aplicación móvil nativa para Android que digitaliza la gestión del **Grupo 9 – Gimnasio y Club**.
Permite a un **administrador** registrar socios y no socios, cobrar la cuota mensual (socios) o el pase diario (no socios) y controlar el listado diario de cuotas que vencen.

Este proyecto es la migración del sistema de escritorio desarrollado en C#/.NET/WinForms durante las materias **DSOO** y **MDS**, adaptado al ecosistema Android con Kotlin.

---

## 👥 Equipo

| Integrante | Comisión |
| ---------- | -------- |
| Albornoz   | A        |
| Blanco     | A        |
| Olivera    | A        |
| Tome       | A        |
| Zalazar    | A        |

**Grupo:** Nro. 9
**Materia:** Desarrollo de Aplicaciones Móviles
**Profesor:** Prof. Kevin Del Bello
**Instituto:** IFTS Nº 29 — 2026, 1° cuatrimestre

---

## 🛠️ Tecnologías

| Herramienta   | Versión / Detalle                     |
| ------------- | ------------------------------------- |
| Lenguaje      | Kotlin                                |
| IDE           | Android Studio                        |
| UI            | Empty Views Activity + LinearLayout   |
| Base de datos | SQLite (SQLiteOpenHelper)             |
| Min SDK       | API 24 (Android 7.0)                  |
| Target SDK    | API 36 (Android 16)                   |

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
        │   │   ├── cuotas/
        │   │   │   └── GenerarCuotasActivity.kt
        │   │   └── vencimientos/
        │   │       └── VencimientosActivity.kt
        │   │
        │   ├── db/                      # (pendiente — SQLiteOpenHelper)
        │   │   ├── DBHelper.kt
        │   │   └── dao/
        │   │       ├── PersonaDao.kt
        │   │       ├── SocioDao.kt
        │   │       ├── NoSocioDao.kt
        │   │       ├── CuotaDao.kt
        │   │       ├── PaseDiarioDao.kt
        │   │       └── UsuarioDao.kt
        │   │
        │   └── model/                   # (pendiente)
        │       ├── Persona.kt
        │       ├── Socio.kt
        │       ├── NoSocio.kt
        │       ├── CuotaSocio.kt
        │       ├── PaseDiario.kt
        │       └── SesionUsuario.kt
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

| #  | Pantalla                       | Rol           |
| -- | ------------------------------ | ------------- |
| 1  | Splash                         | Todos         |
| 2  | Login                          | Administrador |
| 3  | Dashboard Admin                | Administrador |
| 4  | Listado de Socios              | Administrador |
| 5  | Búsqueda de Socios             | Administrador |
| 6  | Detalle de Socio + Carnet      | Administrador |
| 7  | Alta de Socio / No Socio       | Administrador |
| 8  | Vencimientos                   | Administrador |
| 9  | Registrar Pago (cuota mensual) | Administrador |
| 10 | Listado de No Socios           | Administrador |
| 11 | Cobro de Actividad (No Socio)  | Administrador |
| 12 | Generar Cuotas del Periodo     | Administrador |

---

## 🎨 Identidad visual

| Nombre                | Hex       | Uso                             |
| --------------------- | --------- | ------------------------------- |
| `colorPrimary`        | `#1B4F8A` | Header, íconos, texto primario  |
| `colorSecondary`      | `#6AA8D0` | Avatar, acentos secundarios     |
| `colorPrimaryDark`    | `#1A3A5C` | Botones activos, selección      |
| `colorPrimaryLight`   | `#CCE4F7` | Subtítulos sobre fondo primario |
| `colorBackground`     | `#F5F8FC` | Fondo de pantallas              |
| `colorBackgroundGray` | `#F0F0F0` | Botones desactivados            |
| `colorTextPrimary`    | `#1A1A1A` | Texto principal                 |
| `colorTextMuted`      | `#888888` | Texto secundario / hint         |
| `colorTextHint`       | `#9E9E9E` | Texto muy tenue                 |
| `colorStatusOk`       | `#2E7D32` | Texto "Al día"                  |
| `colorStatusOkLight`  | `#E0F4E3` | Fondo badge "Al día"            |
| `colorError`          | `#C62828` | Texto "Vencida"                 |
| `colorErrorLight`     | `#FDDEDE` | Fondo badge "Vencida"           |
| `colorSuccess`        | `#34C759` | Íconos de éxito                 |
| `colorWarning`        | `#FF8D28` | Íconos de advertencia           |
| Tipografía            | Roboto    | —                               |

---

## 🗄️ Modelo de datos

El modelo replica el esquema de la base C#/MySQL del sistema original, portado a SQLite. Trabaja con **periodos** (`AAAAMM`): las cuotas mensuales se generan por adelantado para los socios activos y el pago actualiza la cuota ya generada.

### Entidades principales

**Persona** (entidad base)
- id_persona, nombres, apellidos, sexo, tipo_documento, nro_documento
- fecha_nacimiento, email, telefono, domicilio
- es_activo, fecha_alta, fecha_modificacion

**Socio** (referencia a Persona)
- id_socio, id_persona, fecha_alta, fecha_baja (baja lógica)
- apto_fisico, observaciones

**NoSocio** (referencia a Persona)
- id_no_socio, id_persona, estado (Adherente / Baja Administrativa / Baja Voluntaria)
- apto_fisico, motivo, fecha_registro, fecha_actualizacion

**CuotaSocio**
- id_pago, id_socio, periodo (AAAAMM), fecha_vencimiento
- fecha_pago (NULL si está impaga), monto, medio, usuario_registro

**PaseDiario** (cobro diario de no socios)
- id_pase, id_no_socio, fecha, monto, medio, usuario_registro

**ConfiguracionCuota**
- id, tipo_cuota (Mensual / Diaria), importe_actual, vigente_desde

**Usuario** y **Rol** (autenticación)
- usuario: CodUsu, NombreUsu, PassUsu, RolUsu, Activo
- rol: RolUsu, NomRol

---

## 🔀 Flujo de navegación

```
Splash
  └── Login
        └── Dashboard Admin
              ├── Listado Socios
              │     ├── Detalle Socio + Carnet
              │     │     └── Registrar Pago
              │     └── Alta Socio / No Socio
              ├── No Socios
              │     └── Cobro Actividad
              ├── Generar Cuotas del Periodo
              └── Vencimientos
                    └── Registrar Pago
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

El login valida contra la tabla `usuario` de la base de datos local. La base se inicializa con un usuario administrador por defecto:

- Usuario: **Admin**
- Password: **admin**

**⚠️ El password se almacena en texto plano por simplicidad del trabajo práctico; en un entorno real iría hasheado. ⚠️**

---

## 📌 Estado del proyecto

| Etapa                      | Estado         |
| -------------------------- | -------------- |
| Análisis                   | ✅ Completo     |
| Diseño (Figma)             | ✅ Completo     |
| Entorno Android Studio     | 🔄 En progreso |
| Codificación               | 🔄 En progreso |
| Conexión con base de datos | ⏳ Pendiente    |
| Presentación               | ⏳ Pendiente    |