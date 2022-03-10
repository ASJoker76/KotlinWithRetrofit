package pokemon.co.id.connection

import com.example.kotlin.model.res.ResBursaPengiriman
import com.example.kotlin.model.res.ResChat
import com.example.kotlin.model.res.ResGetProfile
import com.example.kotlin.model.res.ResLogin
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetroService {

    @GET("messages")
    fun getMessage(): Call<List<ResChat>>

    @POST("api_v1/login/loginAll")
    fun loginRequest(@Body reqLogin: RequestBody): Call<ResLogin>

    @POST("api_v1/Transporter/list_bursa_pengiriman")
    fun listBursaPengiriman(@Header("Authorization") authorization: String,
                            @Body reqBursaPengiriman: RequestBody
    ): Call<List<ResBursaPengiriman>>

    @POST("api_v1/Api/getProfile")
    fun getProfile(@Header("Authorization") authorization: String?): Call<ResGetProfile>
}