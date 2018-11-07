package br.com.allin.mobile.pushnotification.configuration;

import br.com.allin.mobile.pushnotification.interfaces.AllInDelegate;

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

    private AllInDelegate allInDelegate;

    public AllInDelegate getDelegate() {
        return this.allInDelegate;
    }

    public void initialize(AllInDelegate allInDelegate) {
        this.allInDelegate = allInDelegate;
    }
}
