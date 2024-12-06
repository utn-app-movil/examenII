package interfaces

import cr.ac.utn.appmovil.identities.nid_Contact

interface nid_IDBManager {
    fun add (contact: nid_Contact) //insert
    fun update (contact: nid_Contact) //update
    fun remove (id: String) //delete
    fun getAll(): List<nid_Contact> //Return all contacts
    fun getById(id: String): nid_Contact? //Search a specific contact by id, if id does not exist it will return null value
}