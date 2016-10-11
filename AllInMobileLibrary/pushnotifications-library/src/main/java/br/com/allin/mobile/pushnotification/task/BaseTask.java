package br.com.allin.mobile.pushnotification.task;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.exception.WebServiceException;
import br.com.allin.mobile.pushnotification.http.HttpManager;
import br.com.allin.mobile.pushnotification.interfaces.OnInvoke;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Base class that implements interface to return the request
 */

abstract class BaseTask<T> extends AsyncTask<Void, Void, Object> implements OnInvoke<T> {
    protected Context context;
    protected OnRequest onRequest;
    protected RequestType requestType;
    protected boolean withCache;

    public BaseTask(Context context, RequestType requestType,
                    boolean withCache, OnRequest onRequest) {
        this.onRequest = onRequest;
        this.context = context;
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
                return HttpManager.get(context, this.getUrl(), this.getParams());
            } else {
                return HttpManager.post(context,
                        this.getUrl(), this.getData(), this.getParams(), this.withCache);
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);

        if (object instanceof ResponseEntity) {
            ResponseEntity responseEntity = (ResponseEntity) object;

            if (!responseEntity.isSuccess()) {
                if (onRequest != null) {
                    onRequest.onError(
                            new WebServiceException(responseEntity.getMessage()));
                }
            } else {
                if (onRequest != null) {
                    onRequest.onFinish(this.onSuccess(responseEntity));
                }
            }
        } else {
            if (onRequest != null) {
                onRequest.onError(
                        new WebServiceException(String.valueOf(object)));
            }
        }
    }
}
