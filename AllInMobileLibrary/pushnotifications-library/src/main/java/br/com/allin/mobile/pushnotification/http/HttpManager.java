package br.com.allin.mobile.pushnotification.http;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import br.com.allin.mobile.pushnotification.Util;
import br.com.allin.mobile.pushnotification.constants.HttpConstants;
import br.com.allin.mobile.pushnotification.dao.CacheDAO;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.exception.WebServiceException;
import br.com.allin.mobile.pushnotification.entity.ResponseData;

/**
 * Class that manages connections to the server
 */
public class HttpManager {
    /**
     * Sends data to the server AllIn
     *
     * @param context Application context
     * @param action  Action to complete the URL of the request
     * @param data    Parameters passed in the request header
     * @param params  Parameters that will be passed in the URL
     * @return Returns the responseData object according to the server information
     * @throws WebServiceException If the server is in trouble
     */
    public static ResponseData post(Context context, String action,
                                    JSONObject data, String[] params) throws WebServiceException {
        return post(context, action, data, params, false);
    }

    /**
     * Sends data to the server AllIn
     *
     * @param context   Application context
     * @param action    Action to complete the URL of the request
     * @param data      Parameters passed in the request header
     * @param params    Parameters that will be passed in the URL
     * @param withCache Determine whether there is any connection problem that
     *                  should be written to the cache for future synchronization
     * @return Returns the responseData object according to the server information
     * @throws WebServiceException If the server is in trouble
     */
    public static ResponseData post(Context context, String action,
                                    JSONObject data, String[] params,
                                    boolean withCache) throws WebServiceException {
        return makeRequest(context, action, RequestType.POST, params, data, withCache);
    }

    /**
     * Receives from the server data AllIn
     *
     * @param context Application context
     * @param action  Action to complete the URL of the request
     * @param params  Parameters that will be passed in the URL
     * @return Returns the responseData object according to the server information
     * @throws WebServiceException If the server is in trouble
     */
    public static ResponseData get(Context context,
                                   String action, String[] params) throws WebServiceException {
        return get(context, action, params, false);
    }

    /**
     * Receives from the server data AllIn
     *
     * @param context   Application context
     * @param action    Action to complete the URL of the request
     * @param params    Parameters that will be passed in the URL
     * @param withCache Determine whether there is any connection problem that should
     *                  be written to the cache for future synchronization
     * @return Returns the responseData object according to the server information
     * @throws WebServiceException If the server is in trouble
     */
    public static ResponseData get(Context context, String action,
                                   String[] params, boolean withCache) throws WebServiceException {
        return makeRequest(context, action, RequestType.GET, params, null, withCache);
    }

    private static ResponseData makeRequest(
            Context context, String action, RequestType requestType,
            String[] params, JSONObject data, boolean withCache) throws WebServiceException {
        String urlString = HttpConstants.SERVER_URL + action;

        if (params != null) {
            for (String param : params) {
                urlString += "/" + param;
            }
        }

        return makeRequestURL(context, urlString, requestType, data, withCache);
    }

    /**
     * Performs server request sending or receiving data
     *
     * @param context     Application context
     * @param urlString   URL to make the request to the server
     * @param requestType Tells whether a GET or a POST type of request param
     *                    arameters data that will be sent to the server
     * @param data        Parameters passed in the request header
     * @param withCache   Determine whether there is any connection problem that
     *                    should be written to the cache for future synchronization
     * @return Returns the responseData object according to the server information
     * @throws WebServiceException If the server is in trouble
     */
    public static ResponseData makeRequestURL(Context context, String urlString,
                                              RequestType requestType, JSONObject data,
                                              boolean withCache) throws WebServiceException {
        if (withCache && !Util.isNetworkAvailable(context)) {
            CacheDAO.getInstance(context).insert(urlString, data != null ? data.toString() : "");

            throw new WebServiceException("Internet não está disponível");
        }

        URL url;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new WebServiceException("URL inválida: " + e.getMessage());
        }

        String token = Util.getToken(context);

        ResponseData response = null;
        HttpURLConnection connection = null;

        new Certificate().start();

        try {
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setConnectTimeout(HttpConstants.DEFAULT_REQUEST_TIMEOUT);
            connection.setRequestProperty("Authorization", token);

            String responseString = "";

            if (requestType == RequestType.POST) {
                connection.setDoOutput(true);
                connection.setConnectTimeout(30000);

                if (data != null) {
                    OutputStream outputStream = connection.getOutputStream();
                    BufferedWriter bufferedWriter =
                            new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(data.toString());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                }
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    responseString += line;
                }
            } else {
                if (withCache) {
                    CacheDAO.getInstance(context)
                            .insert(urlString, data != null ? data.toString() : "");
                }

                throw new WebServiceException(
                        "Code: " + String.valueOf(connection.getResponseCode()) + "\n" +
                                "Message: " + connection.getResponseMessage()
                );
            }

            try {
                JSONObject responseJson = new JSONObject(responseString);

                response = new ResponseData();

                boolean isError = responseJson.getBoolean("error");
                String message = responseJson.getString("message");

                if (isError) {
                    response.setSuccess(false);
                } else {
                    response.setSuccess(true);
                }

                response.setMessage(message);

            } catch (JSONException e) {
                throw new WebServiceException("Ocorreu um erro " +
                        "ao tentar realizar o parse da resposta da requisição:\n\n" + responseString);
            }

        } catch (IOException e) {
            if (withCache) {
                CacheDAO.getInstance(context).insert(urlString, data != null ? data.toString() : "");
            }

            throw new WebServiceException("Ocorreu um erro " +
                    "durante a execução da requisição: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }

        return response;
    }
}