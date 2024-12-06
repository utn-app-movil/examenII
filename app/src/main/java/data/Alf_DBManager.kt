package cr.ac.utn.appmovil.data


import android.content.Context
import cr.ac.utn.appmovil.identities.Alf_Contact
import cr.ac.utn.appmovil.identities.Alf_LoginEvent
import cr.ac.utn.appmovil.interfaces.Alf_IDBManager
import interfaces.APIService

class Alf_DBManager(context: Context) : Alf_IDBManager {
    private val dbHelper = alf_DBHelper(context)

    override fun add(obj: Alf_Contact) {
        dbHelper.addContact(obj)
    }

    override fun update(obj: Alf_Contact) {
        dbHelper.modifyContact(obj)
    }

    override fun remove(id: String) {
        dbHelper.removeContactById(id)
    }

    override fun getAll(): List<Alf_Contact> {
        return dbHelper.fetchAllContacts()
    }

    override fun getById(id: String): Alf_Contact? {
        return dbHelper.fetchContactById(id)
    }

    override fun getByFullName(fullName: String): Alf_Contact? {
        return getAll().firstOrNull { "${it.Name} ${it.LastName}" == fullName }
    }

}