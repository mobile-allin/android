package br.com.allin.mobile.pushnotification.task;

import android.content.Context;

import org.json.JSONObject;

import java.util.Map;

import br.com.allin.mobile.pushnotification.constants.HttpBodyConstants;
import br.com.allin.mobile.pushnotification.constants.RouteConstants;
import br.com.allin.mobile.pushnotification.entity.ResponseEntity;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;

/**
 * Thread for list request
 */
public class ListTask extends BaseTask<String> {
    private String nameList;
    private String campos;
    private String valor;

    public ListTask(String nameList, Map<String, String> columnsAndValues,
                    Context context, OnRequest onRequest) {
        super(context, RequestType.POST, true, onRequest);

        StringBuilder campos = new StringBuilder();
        StringBuilder valor = new StringBuilder();

        for (String key : columnsAndValues.keySet()) {
            campos.append(key).append(";");

            String value = columnsAndValues.get(key);

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
        return RouteConstants.ADD_LIST;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            if (this.campos.endsWith(";")) {
                data.put(HttpBodyConstants.CAMPOS, this.campos.substring(0, this.campos.length() - 1));
            } else {
                data.put(HttpBodyConstants.CAMPOS, this.campos);
            }

            if (this.valor.endsWith(";")) {
                data.put(HttpBodyConstants.VALOR, this.valor.substring(0, this.valor.length() - 1));
            } else {
                data.put(HttpBodyConstants.VALOR, this.valor);
            }

            data.put(HttpBodyConstants.NAME_LIST, this.nameList);

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
