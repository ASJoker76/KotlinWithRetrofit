package pokemon.co.id.connection

import pokemon.co.id.connection.Host.BASE_URL
import pokemon.co.id.connection.Host.BASE_URL_CHAT
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {

    companion object {

        fun getRetroInstance(): Retrofit {

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getRetroInstance2(): Retrofit {

            return Retrofit.Builder()
                .baseUrl(BASE_URL_CHAT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}