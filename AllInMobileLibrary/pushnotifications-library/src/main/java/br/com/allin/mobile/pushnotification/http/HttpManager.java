package br.com.allin.mobile.pushnotification.http;

import android.content.Context;

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

import br.com.allin.mobile.pushnotification.AlliNPush;
import br.com.allin.mobile.pushnotification.identifiers.HttpBodyIdentifier;
import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.exception.WebServiceException;
import br.com.allin.mobile.pushnotification.helper.Util;
import br.com.allin.mobile.pushnotification.service.allin.CacheService;

/**
 * Class that manages connections to the server
 */
public class HttpManager extends HttpCertificate {
    /**
     * Sends data to the server AllIn
     *
     * @param url    ActionIdentifier to complete the URL of the request
     * @param data   SystemIdentifier passed in the request header
     * @param params SystemIdentifier that will be passed in the URL
     * @return Returns the responseData object according to the server information
     * @throws WebServiceException If the server is in trouble
     */
    public static AIResponse post(String url,
                                  JSONObject data, String[] params) throws WebServiceException {
        return post(url, data, params, false);
    }

    /**
     * Sends data to the server AllIn
     *
     * @param url       ActionIdentifier to complete the URL of the request
     * @param data      SystemIdentifier passed in the request header
     * @param params    SystemIdentifier that will be passed in the URL
     * @param withCache Determine whether there is any connection problem that
     *                  should be written to the cache for future synchronization
     * @return Returns the responseData object according to the server information
     * @throws WebServiceException If the server is in trouble
     */
    public static AIResponse post(String url, JSONObject data,
                                  String[] params, boolean withCache) throws WebServiceException {
        return makeRequest(url, RequestType.POST, params, data, withCache);
    }

    /**
     * Receives from the server data AllIn
     *
     * @param url    ActionIdentifier to complete the URL of the request
     * @param params SystemIdentifier that will be passed in the URL
     * @return Returns the responseData object according to the server information
     * @throws WebServiceException If the server is in trouble
     */
    public static AIResponse get(String url, String[] params) throws WebServiceException {
        return get(url, params, false);
    }

    /**
     * Receives from the server data AllIn
     *
     * @param url       ActionIdentifier to complete the URL of the request
     * @param params    SystemIdentifier that will be passed in the URL
     * @param withCache Determine whether there is any connection problem that should
     *                  be written to the cache for future synchronization
     * @return Returns the responseData object according to the server information
     * @throws WebServiceException If the server is in trouble
     */
    public static AIResponse get(String url,
                                 String[] params, boolean withCache) throws WebServiceException {
        return makeRequest(url, RequestType.GET, params, null, withCache);
    }

    private static AIResponse makeRequest(String url, RequestType requestType,
                                          String[] params, JSONObject data, boolean withCache) throws WebServiceException {
        if (params != null) {
            for (String param : params) {
                url = url.concat("/");
                url = url.concat(param);
            }
        }

        return makeRequestURL(url, requestType, data, withCache);
    }

    /**
     * Performs server request sending or receiving data
     *
     * @param urlString   URL to make the request to the server
     * @param requestType Tells whether a GET or a POST type of request param
     *                    arameters data that will be sent to the server
     * @param data        SystemIdentifier passed in the request header
     * @param withCache   Determine whether there is any connection problem that
     *                    should be written to the cache for future synchronization
     * @return Returns the responseData object according to the server information
     * @throws WebServiceException If the server is in trouble
     */
    public static AIResponse makeRequestURL(String urlString, RequestType requestType,
                                            JSONObject data, boolean withCache) throws WebServiceException {
        Context context = AlliNPush.getInstance().getContext();
        AIResponse response;
        HttpURLConnection connection = null;

        generateCertificate();

        try {
            URL url = new URL(urlString);

            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setConnectTimeout(HttpConstant.DEFAULT_REQUEST_TIMEOUT);
            connection.setRequestProperty(HttpBodyIdentifier.AUTHORIZATION, Util.getToken(context));
            connection.setRequestProperty("Content-Type", "application/json");

            String responseString = "";

            if (requestType == RequestType.POST) {
                connection.setDoOutput(true);

                if (data != null) {
                    OutputStream outputStream = connection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
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
                    responseString = responseString.concat(line);
                }

                try {
                    JSONObject responseJson = new JSONObject(responseString);

                    response = new AIResponse();
                    response.setSuccess(!responseJson.getBoolean("error"));
                    response.setMessage(responseJson.getString("message"));
                } catch (JSONException e) {
                    throw new WebServiceException("There was an error while trying to parse the request response:\n\n" + responseString);
                }
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    responseString = responseString.concat(line);
                }

                try {
                    JSONObject responseJson = new JSONObject(responseString);

                    response = new AIResponse();
                    response.setSuccess(!responseJson.getBoolean("error"));
                    response.setMessage(responseJson.getString("message"));
                } catch (JSONException e) {
                    throw new WebServiceException("There was an error while trying to parse the request response:\n\n" + responseString);
                }
            } else {
                if (withCache) {
                    new CacheService().insert(urlString, data != null ? data.toString() : "");
                }

                throw new WebServiceException(
                        "Code: " + String.valueOf(connection.getResponseCode()) + "\n" +
                                "Message: " + connection.getResponseMessage()
                );
            }
        } catch (IOException e) {
            if (withCache) {
                new CacheService().insert(urlString, data != null ? data.toString() : "");
            }

            throw new WebServiceException("There was an error while trying to parse the request response:\n\n" + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }

        return response;
    }
}