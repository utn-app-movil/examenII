package cr.ac.utn.appmovil.interfaces

import cr.ac.utn.appmovil.identities.Alf_Contact

interface Alf_IDBManager {
    fun add (contact: Alf_Contact) //insert
    fun update (contact: Alf_Contact) //update
    fun remove (id: String) //delete
    fun getAll(): List<Alf_Contact> //Return all contacts
    fun getById(id: String): Alf_Contact? //Search a specific contact by id, if id does not exist it will return null value
    fun getByFullName(fullName: String): Alf_Contact?



}