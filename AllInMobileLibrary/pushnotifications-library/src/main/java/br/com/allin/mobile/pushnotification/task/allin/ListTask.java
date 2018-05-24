package br.com.allin.mobile.pushnotification.task.allin;

import org.json.JSONObject;

import java.util.List;

import br.com.allin.mobile.pushnotification.constants.HttpBodyIdentifier;
import br.com.allin.mobile.pushnotification.constants.HttpConstant;
import br.com.allin.mobile.pushnotification.constants.Routes;
import br.com.allin.mobile.pushnotification.entity.allin.AIResponse;
import br.com.allin.mobile.pushnotification.entity.allin.AIValues;
import br.com.allin.mobile.pushnotification.enumarator.RequestType;
import br.com.allin.mobile.pushnotification.interfaces.OnRequest;
import br.com.allin.mobile.pushnotification.task.BaseTask;

/**
 * Thread for list request
 */
public class ListTask extends BaseTask<String> {
    private String nameList;
    private String campos;
    private String valor;

    public ListTask(String nameList, List<AIValues> columnsAndValues, OnRequest onRequest) {
        super(RequestType.POST, true, onRequest);

        StringBuilder campos = new StringBuilder();
        StringBuilder valor = new StringBuilder();

        for (AIValues values : columnsAndValues) {
            campos.append(values.getKey()).append(";");

            if (values.getValue() != null && values.getValue().trim().length() > 0) {
                valor.append(values.getValue());
            }

            valor.append(";");
        }

        this.nameList = nameList;
        this.campos = campos.toString();
        this.valor = valor.toString();
    }

    @Override
    public String getUrl() {
        return HttpConstant.URL_ALLIN + Routes.ADD_LIST;
    }

    @Override
    public JSONObject getData() {
        try {
            JSONObject data = new JSONObject();

            if (this.campos.endsWith(";")) {
                data.put(HttpBodyIdentifier.CAMPOS, this.campos.substring(0, this.campos.length() - 1));
            } else {
                data.put(HttpBodyIdentifier.CAMPOS, this.campos);
            }

            if (this.valor.endsWith(";")) {
                data.put(HttpBodyIdentifier.VALOR, this.valor.substring(0, this.valor.length() - 1));
            } else {
                data.put(HttpBodyIdentifier.VALOR, this.valor);
            }

            data.put(HttpBodyIdentifier.NAME_LIST, this.nameList);

            return data;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String onSuccess(AIResponse AIResponse) {
        return AIResponse.getMessage();
    }
}
