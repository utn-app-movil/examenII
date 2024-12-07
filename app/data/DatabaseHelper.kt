package cr.ac.utn.appmovil.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "contacts.db"
        const val DATABASE_VERSION = 2 // Aumenta la versión de la base de datos

        // Tabla de contactos (ya existente)
        const val TABLE_CONTACTS = "contacts"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_LASTNAME = "lastname"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_COUNTRY = "country"
        const val COLUMN_PHOTO = "photo"

        // Nueva tabla para registros de inicio de sesión
        const val TABLE_LOGIN_EVENTS = "login_events"
        const val COLUMN_EVENT_ID = "event_id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_LOGIN_TIME = "login_time"
    }

    private val SQL_CREATE_CONTACTS_TABLE = """
        CREATE TABLE $TABLE_CONTACTS (
            $COLUMN_ID TEXT PRIMARY KEY,
            $COLUMN_NAME TEXT,
            $COLUMN_LASTNAME TEXT,
            $COLUMN_PHONE INTEGER,
            $COLUMN_EMAIL TEXT,
            $COLUMN_ADDRESS TEXT,
            $COLUMN_COUNTRY TEXT,
            $COLUMN_PHOTO BLOB
        )
    """.trimIndent()

    private val SQL_CREATE_LOGIN_EVENTS_TABLE = """
        CREATE TABLE $TABLE_LOGIN_EVENTS (
            $COLUMN_EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_USER_ID TEXT,
            $COLUMN_LOGIN_TIME TEXT
        )
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_CONTACTS_TABLE)
        db?.execSQL(SQL_CREATE_LOGIN_EVENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL(SQL_CREATE_LOGIN_EVENTS_TABLE)
        }
    }
}
