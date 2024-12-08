package util
import android.content.Context
import android.widget.Toast

object dia_ResponseHandler {
    fun handleResponse(responseCode: String, message: String, context: Context) {
        when (responseCode) {
            "INFO_FOUND" -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            "INFO_NOT_FOUND" -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(context, "Unexpected Error: $message", Toast.LENGTH_SHORT).show()
        }
    }
}