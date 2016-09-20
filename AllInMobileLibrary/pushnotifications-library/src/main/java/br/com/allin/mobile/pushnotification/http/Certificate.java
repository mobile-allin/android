package br.com.allin.mobile.pushnotification.http;

import android.util.Log;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import br.com.allin.mobile.pushnotification.constants.HttpConstants;

/**
 * Created by lucasrodrigues on 9/20/16.
 */

public class Certificate {
    public void start() {
        try {
            TrustManager[] trustManagerArray = new TrustManager[] {
                    new X509TrustManager() {

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            checkTrusted(certs, authType);
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            checkTrusted(certs, authType);
                        }

                        private void checkTrusted(X509Certificate[] chain, String authType) {
                            if (authType == null || authType.length() == 0) {
                                Log.e(HttpConstants.TAG, "Null or zero-length authentication type");
                            }

                            try {
                                chain[0].checkValidity();
                            } catch (Exception e) {
                                Log.e(HttpConstants.TAG, "Invalid certificate");
                            }
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagerArray, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
