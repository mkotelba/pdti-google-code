package gov.hhs.onc.pdti.test.ldap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.annotations.CreateDS;
import org.apache.directory.server.core.annotations.CreatePartition;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.factory.DSAnnotationProcessor;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.apache.directory.server.protocol.shared.transport.Transport;
import org.apache.log4j.Logger;

@ApplyLdifFiles({ "ldap/schema/hc.ldif", "ldap/schema/hpd_plus.ldif" })
@CreateDS(name = "pdtiDs", allowAnonAccess = true, partitions = { @CreatePartition(name = "dev.provider-directories.com", suffix = "o=dev.provider-directories.com,dc=hpd") })
@CreateLdapServer(name = "pdtiLdap", allowAnonymousAccess = true, transports = { @CreateTransport(protocol = "LDAP") })
public class ApacheDsInstance implements ApacheDsInstanceMBean {
    private final static String MBEAN_NAME = "ApacheDS Instance";

    private final static String APACHEDS_WORK_DIR_PROP_NAME = "workingDirectory";

    private final static String PDTI_TEST_APACHEDS_INSTANCE_DIR_PROP_NAME = "pdti.test.apacheds.instance.dir";
    private final static String PDTI_TEST_APACHEDS_LDAP_HOST_PROP_NAME = "pdti.test.apacheds.ldap.host";
    private final static String PDTI_TEST_APACHEDS_LDAP_PORT_PROP_NAME = "pdti.test.apacheds.ldap.port";
    private final static String PDTI_TEST_APACHEDS_DATA_LDIF_FILE_PROP_NAME = "pdti.test.apacheds.data.ldif.file";

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
            System.setProperty(APACHEDS_WORK_DIR_PROP_NAME,
                    System.getProperty(PDTI_TEST_APACHEDS_INSTANCE_DIR_PROP_NAME));

            DirectoryService dirService = DSAnnotationProcessor.createDS(clazz.getAnnotation(CreateDS.class));

            DSAnnotationProcessor.injectLdifFiles(
                    clazz,
                    dirService,
                    ArrayUtils.add(clazz.getAnnotation(ApplyLdifFiles.class).value(),
                            System.getProperty(PDTI_TEST_APACHEDS_DATA_LDIF_FILE_PROP_NAME)));

            CreateLdapServer createLdapServer = clazz.getAnnotation(CreateLdapServer.class);
            CreateTransport createLdapTransport = createLdapServer.transports()[0];

            this.ldapServer = new LdapServer();
            this.ldapServer.setDirectoryService(dirService);
            this.ldapServer.setServiceName(createLdapServer.name());
            
            dirService.setAllowAnonymousAccess(createLdapServer.allowAnonymousAccess());
            
            Transport ldapTransport = new TcpTransport(StringUtils.defaultIfBlank(
                    System.getProperty(PDTI_TEST_APACHEDS_LDAP_HOST_PROP_NAME), createLdapTransport.address()),
                    Integer.parseInt(System.getProperty(PDTI_TEST_APACHEDS_LDAP_PORT_PROP_NAME)),
                    createLdapTransport.nbThreads(), createLdapTransport.backlog());

            this.ldapServer.setTransports(ldapTransport);

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
