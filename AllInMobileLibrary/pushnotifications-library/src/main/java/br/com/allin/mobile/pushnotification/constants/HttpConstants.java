package br.com.allin.mobile.pushnotification.constants;

import br.com.allin.mobile.pushnotification.http.HttpManager;

/**
 * Created by lucasrodrigues on 9/20/16.
 */

public class HttpConstants {
    public static String TAG = HttpManager.class.toString().toUpperCase();

    public static final String ACTION_CAMPAIGN = "campanha";

    public static final String ACTION_DEVICE = "device";

    public static final String ACTION_DEVICE_STATUS = "device/status";

    public static final String ACTION_DEVICE_ENABLE = "device/enable";

    public static final String ACTION_DEVICE_DISABLE = "device/disable";

    public static final String ACTION_DEVICE_LOGOUT = "device/logout";

    public static final String ACTION_EMAIL = "email";

    public static final String ACTION_ADD_LIST = "addlist";

    public static final String PARAM_KEY_DEVICE_TOKEN = "device_token";

    public static final String PARAM_KEY_PLATFORM = "platform";

    public static final String PARAM_KEY_USER_EMAIL = "user_email";

    public static final String PARAM_VALUE_PLATFORM = "ANDROID";

    public static final String PARAM_NM_LISTA = "nm_lista";

    public static final String PARAM_CAMPOS = "campos";

    public static final String PARAM_VALOR = "valor";

    public static final String ACTION_NOTIFICATION = "notification";

    public static final String PARAM_ACTION = "action";

    public static final String PARAM_LATITUDE = "latitude";

    public static final String PARAM_LONGITUDE = "longitude";

    public static final int DEFAULT_REQUEST_TIMEOUT = 15000;

    public static final String SERVER_URL = "http://lucasrodrigues.allinmedia.com.br/webservice/mobile-api/";
//    public static final String SERVER_URL = "http://homol-mob.allinmedia.com.br/src/api/";
//    public static final String SERVER_URL = "https://mobile-api.allin.com.br/";
}
