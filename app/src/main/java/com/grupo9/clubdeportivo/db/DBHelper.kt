package com.grupo9.clubdeportivo.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Helper de base de datos SQLite del Club Deportivo.
 *
 * Esquema portado desde la base C#/MySQL original.
 * Mapeo de tipos aplicado:
 *   INT AUTO_INCREMENT (PK) -> INTEGER PRIMARY KEY AUTOINCREMENT
 *   VARCHAR / CHAR          -> TEXT
 *   DATE / DATETIME         -> TEXT (ISO: 'yyyy-MM-dd' / 'yyyy-MM-dd HH:mm:ss')
 *   ENUM(...)               -> TEXT + CHECK (valores válidos)
 *   TINYINT / BOOLEAN       -> INTEGER (0/1)
 *   DOUBLE                  -> REAL
 *
 */
class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        // Las claves foráneas en SQLite están desactivadas por defecto.
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Orden de creación respetando dependencias (primero las tablas sin FK).
        db.execSQL(CREATE_ROLES)
        db.execSQL(CREATE_PERSONAS)
        db.execSQL(CREATE_CONFIGURACION_CUOTAS)
        db.execSQL(CREATE_USUARIO)
        db.execSQL(CREATE_SOCIOS)
        db.execSQL(CREATE_NO_SOCIOS)
        db.execSQL(CREATE_CUOTAS_SOCIOS)
        db.execSQL(CREATE_PASES_DIARIOS)
        db.execSQL(CREATE_VIEW_PERSONAS_DATA)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Orden inverso al de creación.
        db.execSQL("DROP VIEW IF EXISTS $VIEW_PERSONAS_DATA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PASES_DIARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CUOTAS_SOCIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NO_SOCIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SOCIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONFIGURACION_CUOTAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PERSONAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROLES")
        onCreate(db)
    }

    companion object {
        const val DB_NAME = "club_deportivo.db"
        const val DB_VERSION = 1

        // Nombres de tablas y vista
        const val TABLE_ROLES = "roles"
        const val TABLE_PERSONAS = "personas"
        const val TABLE_CONFIGURACION_CUOTAS = "configuracion_cuotas"
        const val TABLE_USUARIO = "usuario"
        const val TABLE_SOCIOS = "socios"
        const val TABLE_NO_SOCIOS = "no_socios"
        const val TABLE_CUOTAS_SOCIOS = "cuotas_socios"
        const val TABLE_PASES_DIARIOS = "pases_diarios"
        const val VIEW_PERSONAS_DATA = "personas_data"

        // ----------------------------------------------------------------
        // roles  (RolUsu se asigna manualmente: 1 = Administrador, 2 = Empleado)
        // ----------------------------------------------------------------
        private const val CREATE_ROLES = """
            CREATE TABLE $TABLE_ROLES (
                RolUsu INTEGER PRIMARY KEY,
                NomRol TEXT
            )
        """

        // ----------------------------------------------------------------
        // personas
        // ----------------------------------------------------------------
        private const val CREATE_PERSONAS = """
            CREATE TABLE $TABLE_PERSONAS (
                id_persona         INTEGER PRIMARY KEY AUTOINCREMENT,
                nombres            TEXT NOT NULL,
                apellidos          TEXT NOT NULL,
                sexo               TEXT NOT NULL CHECK (sexo IN ('Masculino', 'Femenino', 'Otros')),
                tipo_documento     TEXT NOT NULL CHECK (tipo_documento IN ('DNI', 'Pasaporte')),
                nro_documento      TEXT NOT NULL,
                fecha_nacimiento   TEXT,
                email              TEXT,
                telefono           TEXT,
                domicilio          TEXT,
                es_activo          INTEGER NOT NULL DEFAULT 1,
                fecha_modificacion TEXT,
                fecha_alta         TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
            )
        """

        // ----------------------------------------------------------------
        // configuracion_cuotas
        // ----------------------------------------------------------------
        private const val CREATE_CONFIGURACION_CUOTAS = """
            CREATE TABLE $TABLE_CONFIGURACION_CUOTAS (
                id             INTEGER PRIMARY KEY AUTOINCREMENT,
                tipo_cuota     TEXT NOT NULL DEFAULT 'Diaria' CHECK (tipo_cuota IN ('Mensual', 'Diaria')),
                importe_actual REAL NOT NULL,
                vigente_desde  TEXT NOT NULL
            )
        """

        // ----------------------------------------------------------------
        // usuario  (FK -> roles)
        // ----------------------------------------------------------------
        private const val CREATE_USUARIO = """
            CREATE TABLE $TABLE_USUARIO (
                CodUsu    INTEGER PRIMARY KEY AUTOINCREMENT,
                NombreUsu TEXT,
                PassUsu   TEXT,
                RolUsu    INTEGER,
                Activo    INTEGER DEFAULT 1,
                FOREIGN KEY (RolUsu) REFERENCES $TABLE_ROLES (RolUsu)
            )
        """

        // ----------------------------------------------------------------
        // socios  (FK -> personas)
        // ----------------------------------------------------------------
        private const val CREATE_SOCIOS = """
            CREATE TABLE $TABLE_SOCIOS (
                id_socio                INTEGER PRIMARY KEY AUTOINCREMENT,
                id_persona              INTEGER NOT NULL,
                fecha_alta              TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                fecha_baja              TEXT,
                apto_fisico_vencimiento TEXT,
                observaciones           TEXT,
                FOREIGN KEY (id_persona) REFERENCES $TABLE_PERSONAS (id_persona)
                    ON DELETE CASCADE ON UPDATE CASCADE
            )
        """

        // ----------------------------------------------------------------
        // no_socios  (FK -> personas)
        // ----------------------------------------------------------------
        private const val CREATE_NO_SOCIOS = """
            CREATE TABLE $TABLE_NO_SOCIOS (
                id_no_socio             INTEGER PRIMARY KEY AUTOINCREMENT,
                id_persona              INTEGER NOT NULL,
                estado                  TEXT DEFAULT 'Adherente'
                                        CHECK (estado IN ('Adherente', 'Baja Administrativa', 'Baja Voluntaria')),
                apto_fisico_vencimiento TEXT,
                motivo                  TEXT,
                fecha_registro          TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                fecha_actualizacion     TEXT,
                FOREIGN KEY (id_persona) REFERENCES $TABLE_PERSONAS (id_persona)
                    ON DELETE CASCADE ON UPDATE CASCADE
            )
        """

        // ----------------------------------------------------------------
        // cuotas_socios  (FK -> socios)
        // ----------------------------------------------------------------
        private const val CREATE_CUOTAS_SOCIOS = """
            CREATE TABLE $TABLE_CUOTAS_SOCIOS (
                id_pago           INTEGER PRIMARY KEY AUTOINCREMENT,
                id_socio          INTEGER NOT NULL,
                periodo           TEXT NOT NULL,
                fecha_vencimiento TEXT NOT NULL,
                fecha_pago        TEXT,
                monto             REAL NOT NULL,
                medio             TEXT CHECK (medio IN ('Efectivo', 'Virtual', 'Debito', 'Credito')),
                usuario_registro  TEXT,
                FOREIGN KEY (id_socio) REFERENCES $TABLE_SOCIOS (id_socio)
                    ON DELETE RESTRICT ON UPDATE CASCADE
            )
        """

        // ----------------------------------------------------------------
        // pases_diarios  (FK -> no_socios)
        // ----------------------------------------------------------------
        private const val CREATE_PASES_DIARIOS = """
            CREATE TABLE $TABLE_PASES_DIARIOS (
                id_pase          INTEGER PRIMARY KEY AUTOINCREMENT,
                id_no_socio      INTEGER NOT NULL,
                fecha            TEXT NOT NULL,
                monto            REAL NOT NULL,
                medio            TEXT DEFAULT 'Efectivo'
                                 CHECK (medio IN ('Efectivo', 'Virtual', 'Debito', 'Credito')),
                usuario_registro TEXT,
                FOREIGN KEY (id_no_socio) REFERENCES $TABLE_NO_SOCIOS (id_no_socio)
            )
        """

        // ----------------------------------------------------------------
        // Vista personas_data: unifica socios activos y no socios.
        // ----------------------------------------------------------------
        private const val CREATE_VIEW_PERSONAS_DATA = """
            CREATE VIEW $VIEW_PERSONAS_DATA AS
            SELECT s.id_socio                 AS Id,
                   'Socio'                    AS Categoria,
                   p.nombres                  AS Nombres,
                   p.apellidos                AS Apellidos,
                   p.sexo                     AS Sexo,
                   p.tipo_documento           AS Tipo,
                   p.nro_documento            AS NroDocumento,
                   p.fecha_nacimiento         AS Nacimiento,
                   p.email                    AS Email,
                   s.apto_fisico_vencimiento  AS VtoAptoFisico,
                   'Activo'                   AS Estado,
                   s.fecha_alta               AS FechaAlta
            FROM $TABLE_PERSONAS p
            JOIN $TABLE_SOCIOS s ON p.id_persona = s.id_persona
            WHERE s.fecha_baja IS NULL
            UNION
            SELECT n.id_no_socio              AS Id,
                   'No Socio'                 AS Categoria,
                   p1.nombres                 AS Nombres,
                   p1.apellidos               AS Apellidos,
                   p1.sexo                    AS Sexo,
                   p1.tipo_documento          AS Tipo,
                   p1.nro_documento           AS NroDocumento,
                   p1.fecha_nacimiento        AS Nacimiento,
                   p1.email                   AS Email,
                   n.apto_fisico_vencimiento  AS VtoAptoFisico,
                   n.estado                   AS Estado,
                   n.fecha_registro           AS FechaAlta
            FROM $TABLE_PERSONAS p1
            JOIN $TABLE_NO_SOCIOS n ON p1.id_persona = n.id_persona
        """
    }
}