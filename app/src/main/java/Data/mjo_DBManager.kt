package cr.ac.utn.appmovil.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager
import java.io.ByteArrayOutputStream

class SQLiteDBManager(context: Context) : IDBManager {

    private val dbHelper = DBHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase

    override fun remove(id: String) {
        db.delete(DBHelper.TABLE_CONTACTS, "${DBHelper.COLUMN_ID} = ?", arrayOf(id))
        db.delete(DBHelper.TABLE_CONTACT_PHOTOS, "contact_id = ?", arrayOf(id)) // Asegúrate de eliminar también las fotos asociadas
    }

    override fun update(contact: Contact) {
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_NAME, contact.Name)
            put(DBHelper.COLUMN_LASTNAME, contact.LastName)
            put(DBHelper.COLUMN_PHONE, contact.Phone)
            put(DBHelper.COLUMN_EMAIL, contact.Email)
            put(DBHelper.COLUMN_ADDRESS, contact.Address)
            put(DBHelper.COLUMN_COUNTRY, contact.Country)
        }
        db.update(DBHelper.TABLE_CONTACTS, values, "${DBHelper.COLUMN_ID} = ?", arrayOf(contact.Id))
        contact.Photo?.let {
            db.delete(DBHelper.TABLE_CONTACT_PHOTOS, "contact_id = ?", arrayOf(contact.Id)) // Borra fotos anteriores
            savePhotoInChunks(contact.Id, it.toByteArray())
        }
    }

    override fun add(contact: Contact) {
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_ID, contact.Id)
            put(DBHelper.COLUMN_NAME, contact.Name)
            put(DBHelper.COLUMN_LASTNAME, contact.LastName)
            put(DBHelper.COLUMN_PHONE, contact.Phone)
            put(DBHelper.COLUMN_EMAIL, contact.Email)
            put(DBHelper.COLUMN_ADDRESS, contact.Address)
            put(DBHelper.COLUMN_COUNTRY, contact.Country)
        }
        db.insert(DBHelper.TABLE_CONTACTS, null, values)
        savePhotoInChunks(contact.Id, contact.Photo?.toByteArray())
    }

    private fun savePhotoInChunks(contactId: String, photo: ByteArray?) {
        photo?.let {
            val chunkSize = 1000000
            var partIndex = 0

            for (i in it.indices step chunkSize) {
                val chunk = it.copyOfRange(i, minOf(i + chunkSize, it.size))
                val values = ContentValues().apply {
                    put("contact_id", contactId)
                    put("photo_part", chunk)
                    put("part_index", partIndex++)
                }
                db.insert(DBHelper.TABLE_CONTACT_PHOTOS, null, values)
            }
        }
    }

    override fun getById(id: String): Contact? {
        val cursor = db.query(DBHelper.TABLE_CONTACTS, null, "${DBHelper.COLUMN_ID} = ?", arrayOf(id), null, null, null)
        cursor?.moveToFirst()?.let {
            val contact = cursorToContact(cursor)
            contact.Photo = loadPhotoInChunks(contact.Id)
            cursor.close()
            return contact
        }
        cursor?.close()
        return null
    }

    override fun getAll(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val cursor = db.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val contact = cursorToContact(cursor)
                contact.Photo = loadPhotoInChunks(contact.Id) // Cargar la foto para cada contacto
                contacts.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contacts
    }

    private fun loadPhotoInChunks(contactId: String): Bitmap? {
        val cursor = db.query("ContactPhotos", null, "contact_id = ?", arrayOf(contactId), null, null, "part_index")
        val outputStream = ByteArrayOutputStream()

        if (cursor.moveToFirst()) {
            do {
                val chunk = cursor.getBlob(cursor.getColumnIndexOrThrow("photo_part"))
                outputStream.write(chunk)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val completePhoto = outputStream.toByteArray()
        return completePhoto.toBitmap()
    }

    private fun cursorToContact(cursor: Cursor): Contact {
        return Contact(
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_LASTNAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PHONE)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EMAIL)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ADDRESS)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_COUNTRY)),
            null
        )
    }

    fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun ByteArray?.toBitmap(): Bitmap? {
        return this?.let {
            BitmapFactory.decodeByteArray(it, 0, this.size)
        }
    }
}
