package br.com.allin.mobile.pushnotification.configuration;

/**
 * Created by lucasrodrigues on 25/08/17.
 */

public class AlliNConfiguration {
    private AlliNConfiguration() {
    }

    private static AlliNConfiguration alliNConfiguration;

    public static AlliNConfiguration getInstance() {
        if (AlliNConfiguration.alliNConfiguration == null) {
            AlliNConfiguration.alliNConfiguration = new AlliNConfiguration();
        }

        return AlliNConfiguration.alliNConfiguration;
    }
}
