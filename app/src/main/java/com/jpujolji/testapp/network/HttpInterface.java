/*
 * Copyright (c) 2016. BPT Integration S.A.S. - Todos los derechos reservados.
 * La copia no autorizada de este archivo, a través de cualquier medio está estrictamente prohibida.
 * Escrito por Jorge Pujol <jpujolji@gmail.com>, diciembre 2016.
 */

package com.jpujolji.testapp.network;

import org.json.JSONObject;

/**
 * Interface utilizada para enviar los datos de las peticiones HTTP desde la clase {@link .HttpClient}
 * hasta la vista o activity que implementa.
 */
public interface HttpInterface {

    /**
     * Envía el progreso o porcentaje de descarga de la respuesta de la petición HTTP para actualizar la vista.
     *
     * @param progress porcentaje de descarga de los datos
     */
    void onProgress(long progress);

    /**
     * Envía la respuesta de la petición HTTP de id_tipo_lote JSONObject a la vista.
     *
     * @param response resultado de la petición HTTP.
     */
    void onSuccess(JSONObject response);

    /**
     * Envía el error ocurrido durante la petición HTTP a la vista.
     *
     * @param errorResponse error que devuelve la petición.
     */
    void onFailed(JSONObject errorResponse);
}