package br.com.allin.mobile.pushnotification.task;

import android.os.AsyncTask;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.exception.WebServiceException;
import br.com.allin.mobile.pushnotification.http.HttpManager;
import br.com.allin.mobile.pushnotification.interfaces.OnInvoke;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Base class that implements interface to return the request
 */

public abstract class BaseTask<T> extends AsyncTask<Void, Void, Object> implements OnInvoke<T> {
    protected OnRequest onRequest;
    private RequestType requestType;
    private boolean withCache;

    public BaseTask(RequestType requestType, boolean withCache, OnRequest onRequest) {
        this.onRequest = onRequest;
        this.requestType = requestType;
        this.withCache = withCache;
    }

    @Override
    public JSONObject getData() {
        return null;
    }

    @Override
    public String[] getParams() {
        return null;
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {
            if (requestType == RequestType.GET) {
                return HttpManager.get(getUrl(), getParams());
            } else {
                return HttpManager.post(getUrl(), getData(), getParams(), withCache);
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);

        if (object instanceof AIResponse) {
            AIResponse AIResponse = (AIResponse) object;

            if (!AIResponse.isSuccess()) {
                if (onRequest != null) {
                    onRequest.onError(new WebServiceException(AIResponse.getMessage()));
                }
            } else {
                if (onRequest != null) {
                    onRequest.onFinish(onSuccess(AIResponse));
                }
            }
        } else {
            if (onRequest != null) {
                onRequest.onError(new WebServiceException(String.valueOf(object)));
            }
        }
    }
}
