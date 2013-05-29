package gov.hhs.onc.pdti.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class DirectoryContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private final static Map<String, String> CONTEXT_DATA_CONFIG_LOCS = new LinkedHashMap<>();

    private final static Logger LOGGER = Logger.getLogger(DirectoryContextInitializer.class);

    static {
        CONTEXT_DATA_CONFIG_LOCS.put("pdti.context.config.data.ldap", "/META-INF/data/ldap/*.xml");
        CONTEXT_DATA_CONFIG_LOCS.put("pdti.context.config.data.federation", "/META-INF/data/federation/*.xml");
    }

    @Override
    public void initialize(ConfigurableApplicationContext appContext) {
        ConfigurableWebApplicationContext webAppContext = (ConfigurableWebApplicationContext) appContext;
        List<String> configLocs = new ArrayList<>(Arrays.asList(webAppContext.getConfigLocations()));
        Properties sysProps = System.getProperties();
        String configLoc;

        for (String configPropName : CONTEXT_DATA_CONFIG_LOCS.keySet()) {
            if (sysProps.containsKey(configPropName)) {
                configLoc = sysProps.getProperty(configPropName);

                LOGGER.debug("Using system property (name=" + configPropName
                        + ") for context data configuration location: " + configLoc);
            } else {
                configLoc = CONTEXT_DATA_CONFIG_LOCS.get(configPropName);

                LOGGER.debug("Using default property (name=" + configPropName
                        + ") value for context data configuration location: " + configLoc);
            }

            configLocs.add(configLoc);
        }

        String[] configLocsArr = configLocs.toArray(new String[configLocs.size()]);

        LOGGER.trace("Setting Spring web application context configuration locations: ["
                + StringUtils.join(configLocsArr, ",") + "]");

        webAppContext.setConfigLocations(configLocsArr);
    }
}
