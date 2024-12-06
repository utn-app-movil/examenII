package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.bai_Room


/**
 * Adapter para manejar y mostrar una lista de habitaciones en un RecyclerView.
 *
 * @property rooms Lista de habitaciones a mostrar.
 * @property onRoomClicked Acción a realizar al hacer clic en una habitación.
 */
class bai_RoomAdapter(
    private var rooms: List<bai_Room>,
    private val onRoomClicked: (bai_Room) -> Unit
) : RecyclerView.Adapter<bai_RoomAdapter.RoomViewHolder>() {

    /**
     * Actualiza la lista de habitaciones en el adaptador y notifica los cambios.
     *
     * @param newRooms Nueva lista de habitaciones.
     */
    fun updateRooms(newRooms: List<bai_Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    /**
     * Crea una nueva instancia de RoomViewHolder.
     *
     * @param parent ViewGroup donde se añadirá el nuevo ViewHolder.
     * @param viewType Tipo de vista (no utilizado aquí).
     * @return Una nueva instancia de RoomViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.bai_item_room, parent, false
        )
        return RoomViewHolder(view, onRoomClicked)
    }

    /**
     * Vincula los datos de una habitación específica al ViewHolder.
     *
     * @param holder ViewHolder que se utilizará para mostrar los datos.
     * @param position Posición de la habitación en la lista.
     */
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    /**
     * Devuelve el número total de habitaciones en la lista.
     *
     * @return Número de habitaciones.
     */
    override fun getItemCount() = rooms.size

    /**
     * ViewHolder para mostrar los datos de una habitación.
     *
     * @property itemView Vista principal del ViewHolder.
     * @property onRoomClicked Acción a realizar al hacer clic en una habitación.
     */
    class RoomViewHolder(itemView: View, private val onRoomClicked: (bai_Room) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        // TextViews para mostrar el nombre, la capacidad y el estado de la habitación.
        private val tvName: TextView = itemView.findViewById(R.id.bai_tv_room_name)
        private val tvCapacity: TextView = itemView.findViewById(R.id.bai_tv_capacity)
        private val tvStatus: TextView = itemView.findViewById(R.id.bai_tv_status)


        /**
         * Vincula los datos de una habitación a las vistas del ViewHolder.
         *
         * @param room Habitación cuyos datos se van a mostrar.
         */
        fun bind(room: bai_Room) {
            // Muestra el nombre de la habitación.
            tvName.text = room.room

            // Muestra la capacidad de la habitación.
            tvCapacity.text = itemView.context.getString(R.string.bai_room_capacity, room.capacity)

            // Configura el estado de la habitación y su color dependiendo de si está ocupada o no.
            if (room.is_busy && !room.user.isNullOrBlank()) {
                tvStatus.text = itemView.context.getString(R.string.bai_room_busy, room.user)
                tvStatus.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.bai_red_color
                    )
                )
            } else {
                tvStatus.text = itemView.context.getString(R.string.bai_room_free)
                tvStatus.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.bai_green_color
                    )
                )
            }

            // Configura la acción al hacer clic en una habitación.
            itemView.setOnClickListener {
                onRoomClicked(room)
            }
        }
    }
}
