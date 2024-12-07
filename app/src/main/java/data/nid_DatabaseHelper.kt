package cr.ac.utn.appmovil.rooms

import android.content.Context

// La clase para gestionar las operaciones de la base de datos
class nid_DataBaseManager(private val context: Context) {

    // Instanciamos el DatabaseHelper
    private val dbHelper = DatabaseHelper(context)

    // Método para agregar o actualizar una sala
    fun addOrUpdateRoom(room: Room) {
        dbHelper.addOrUpdateRoom(room)
    }

    // Método para obtener todas las salas de la base de datos
    fun getAllRooms(): List<Room> {
        return dbHelper.getAllRooms()
    }

    // Método para verificar si una sala existe en la base de datos
    fun roomExists(roomId: String): Boolean {
        return dbHelper.roomExists(roomId)
    }

    // Método para eliminar una sala de la base de datos
    fun deleteRoom(roomId: String) {
        dbHelper.deleteRoom(roomId)
    }

    // Método para reservar una sala
    fun bookRoom(roomId: String) {
        val room = Room(roomId, "reserved")
        addOrUpdateRoom(room)
    }

    // Método para liberar una sala
    fun unbookRoom(roomId: String) {
        val room = Room(roomId, "available")
        addOrUpdateRoom(room)
    }
}
