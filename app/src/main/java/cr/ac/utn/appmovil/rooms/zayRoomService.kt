package cr.ac.utn.appmovil.rooms

object ZayRoomService {
    val apiService: ZayApiService = ZayRetrofitInstance.retrofitInstance.create(ZayApiService::class.java)
}