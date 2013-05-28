package gov.hhs.onc.pdti.test.ldap;

import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.annotations.CreateDS;
import org.apache.directory.server.core.annotations.CreatePartition;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.factory.DSAnnotationProcessor;
import org.apache.directory.server.factory.ServerAnnotationProcessor;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.log4j.Logger;

@ApplyLdifFiles({ "ldap/schema/hc.ldif", "ldap/schema/hpd_plus.ldif", "ldap/data/hpd_plus_test_data.ldif" })
@CreateDS(name = "pdtiDs", allowAnonAccess = true, partitions = { @CreatePartition(name = "dev.provider-directories.com", suffix = "o=dev.provider-directories.com,dc=hpd") })
@CreateLdapServer(name = "pdtiLdap", allowAnonymousAccess = true, transports = { @CreateTransport(protocol = "LDAP", port = 20389) })
public class ApacheDsInstance implements ApacheDsInstanceMBean {
    private final static String MBEAN_NAME = "ApacheDS Instance";

    private final static String APACHEDS_WORK_DIR_PROP_NAME = "workingDirectory";

    private final static String PDTI_TEST_APACHEDS_DIR_PROP_NAME = "pdti.test.apacheds.dir";

    private final static Logger LOGGER = Logger.getLogger(ApacheDsInstance.class);

    private Thread thread;
    private LdapServer ldapServer;

    public ApacheDsInstance(Thread thread) {
        this.thread = thread;
    }

    @Override
    public void start() {
        Class<? extends ApacheDsInstance> clazz = this.getClass();

        try {
            System.setProperty(APACHEDS_WORK_DIR_PROP_NAME, System.getProperty(PDTI_TEST_APACHEDS_DIR_PROP_NAME));

            DirectoryService dirService = DSAnnotationProcessor.createDS(clazz.getAnnotation(CreateDS.class));

            DSAnnotationProcessor.injectLdifFiles(clazz, dirService, clazz.getAnnotation(ApplyLdifFiles.class).value());

            this.ldapServer = ServerAnnotationProcessor.instantiateLdapServer(
                    clazz.getAnnotation(CreateLdapServer.class), dirService);
            this.ldapServer.start();

            LOGGER.info("ApacheDS instance started.");
        } catch (Exception e) {
            // TODO: improve error handling
            LOGGER.error(e);
        }
    }

    @Override
    public void stop() {
        if (this.isStarted()) {
            try {
                this.ldapServer.stop();
            } catch (Exception e) {
                // TODO: improve error handling
                LOGGER.error(e);
            }
        }

        if (this.isRunning()) {
            this.thread.interrupt();
        }

        LOGGER.info("ApacheDS instance stopped.");
    }

    @Override
    public boolean isRunning() {
        return this.thread.isAlive();
    }

    @Override
    public boolean isStarted() {
        return this.isRunning() && (this.ldapServer != null) && this.ldapServer.isStarted();
    }

    @Override
    public String getName() {
        return MBEAN_NAME;
    }
}
