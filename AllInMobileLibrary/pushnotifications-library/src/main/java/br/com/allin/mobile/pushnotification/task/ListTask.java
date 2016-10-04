package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import org.json.JSONObject;

import java.util.Map;

import br.com.allin.mobile.pushnotification.constants.HttpBody;
import br.com.allin.mobile.pushnotification.constants.Route;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Created by lucasrodrigues on 10/3/16.
 */

public class ListTask extends BaseTask<String> {
    private String nameList;
    private String campos;
    private String valor;

    public ListTask(String nameList, Map<String, String> values,
                    Context context, OnRequest onRequest) {
        super(context, RequestType.POST, true, onRequest);

        StringBuilder campos = new StringBuilder();
        StringBuilder valor = new StringBuilder();

        for (String key : values.keySet()) {
            campos.append(key).append(";");

            String value = values.get(key);

            if (value != null && value.trim().length() > 0) {
                valor.append(value);
            }

            valor.append(";");
        }

        this.nameList = nameList;
        this.campos = campos.toString();
        this.valor = valor.toString();
    }

    @Override
    public String getUrl() {
        return Route.ADD_LIST;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            if (this.campos.endsWith(";")) {
                data.put(HttpBody.CAMPOS, this.campos.substring(0, this.campos.length() - 1));
            } else {
                data.put(HttpBody.CAMPOS, this.campos);
            }

            if (this.valor.endsWith(";")) {
                data.put(HttpBody.VALOR, this.valor.substring(0, this.valor.length() - 1));
            } else {
                data.put(HttpBody.VALOR, this.valor);
            }

            data.put(HttpBody.NAME_LIST, this.nameList);

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(ResponseEntity responseEntity) {
        return responseEntity.getMessage();
    }
}
