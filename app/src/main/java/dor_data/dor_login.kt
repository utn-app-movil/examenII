package dor_data



data class dor_login (
    val responseCode: Int,
    val message: String,
    val data: dor_user_data?
)