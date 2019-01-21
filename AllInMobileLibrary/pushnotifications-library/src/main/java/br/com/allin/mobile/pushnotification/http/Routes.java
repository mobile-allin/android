package br.com.allin.mobile.pushnotification.http;

public interface Routes {
    String EMAIL = "/email";
    String ADD_LIST = "/addlist";
    String DEVICE = "/device";
    String DEVICE_LOGOUT = "/device/logout";
    String DEVICE_DISABLE = "/device/disable";
    String DEVICE_ENABLE = "/device/enable";
    String DEVICE_STATUS = "/device/status";
    String NOTIFICATION_CAMPAIGN = "/notification/campaign";
    String NOTIFICATION_TRANSACTIONAL = "/notification/transactional";
    String UPDATE = "update";
    String NOTIFICATION_MARK_AS_READ = "/notification/history/markasread";
}
