package br.cademeubicho.webservice

import br.cademeubicho.BuildConfig
import br.cademeubicho.webservice.model.Localidade
import br.cademeubicho.webservice.rotas.ConsultasServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebServiceInitializer {

    fun getWebService(): Retrofit? {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}

/*

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        when (response?.code()) {
            HttpsURLConnection.HTTP_OK,
            HttpsURLConnection.HTTP_CREATED,
            HttpsURLConnection.HTTP_ACCEPTED,
            HttpsURLConnection.HTTP_NOT_AUTHORITATIVE ->
                response.body()?.apply { onSuccess(this) }
            else -> onFailed(Throwable("Default " + response?.code() + " " + response?.message()))
        }
    }

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        // handle error mechanism
        t?.apply { onFailed(this) }
    }
    fun onSuccess(responseBody: T)
    fun onUnauthorized()
    fun onFailed(throwable: Throwable)
}
*/
