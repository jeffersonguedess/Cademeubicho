package br.cademeubicho.webservice

import br.cademeubicho.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebService {

    fun getWebService(): Retrofit? {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        val instance = WebService().getWebService()
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
