package util

import android.content.Context
import android.content.Intent

class bai_Util {
    companion object {
        /**
         * Navega a otra actividad con un extra opcional.
         *
         * @param context Contexto actual de la actividad.
         * @param objclass Clase de la actividad destino.
         * @param extraName Nombre del extra (opcional).
         * @param value Valor del extra (opcional).
         */
        fun openActivity(context: Context, objclass: Class<*>, extraName: String = "", value: String? = null) {
            val intent = Intent(context, objclass).apply {
                if (extraName.isNotEmpty() && value != null) {
                    putExtra(extraName, value)
                }
            }
            context.startActivity(intent)
        }
    }
}
