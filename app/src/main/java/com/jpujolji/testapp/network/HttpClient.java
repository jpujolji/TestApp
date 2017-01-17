package com.jpujolji.testapp.network;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * La clase HttpClient es usada para realizar las peticiones HTTP en la aplicación Android, utilizando
 * la librería externa <a href="http://loopj.com/android-async-http">Android Asynchronous Http Client</a>.
 */
public class HttpClient {

    private static final String URL = "https://www.reddit.com/reddits.json";
    private HttpInterface mHttpInterface;

    /**
     * Crea una nueva instancia del objeto HttpClient
     *
     * @param httpInterface interface para enviar los datos a la vista, vea {@link HttpInterface}.
     */
    public HttpClient(HttpInterface httpInterface) {
        mHttpInterface = httpInterface;
    }

    /**
     * Realiza una petición HTTP que puede ser síncrona o asíncrona, envía los datos de
     * respuesta a la vista por medio de un objeto de id_tipo_lote {@link HttpInterface}.
     */
    public void httpRequest() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20000);

        client.get(URL,new JsonHttpResponseHandler() {
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                long progressPercentage = 100 * bytesWritten
                        / totalSize;
                mHttpInterface.onProgress(progressPercentage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                mHttpInterface.onFailed(errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mHttpInterface.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("Depuracion", "Error " + statusCode + " responseString " + responseString);
            }
        });
    }
}