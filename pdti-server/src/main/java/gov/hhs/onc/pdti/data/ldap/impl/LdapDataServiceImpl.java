package gov.hhs.onc.pdti.data.ldap.impl;

import gov.hhs.onc.pdti.data.DirectoryDataException;
import gov.hhs.onc.pdti.data.dsml.DirectoryDsmlService;
import gov.hhs.onc.pdti.data.impl.AbstractDataService;
import gov.hhs.onc.pdti.data.ldap.DirectoryLdapException;
import gov.hhs.onc.pdti.data.ldap.LdapDataService;
import gov.hhs.onc.pdti.data.ldap.LdapDataSource;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import java.io.IOException;
import java.util.List;
import org.apache.directory.api.dsmlv2.engine.Dsmlv2Engine;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Qualifier("ldap")
@Scope("singleton")
@Service("dataService")
public class LdapDataServiceImpl extends AbstractDataService<LdapDataSource> implements LdapDataService {
    private final static Logger LOGGER = Logger.getLogger(LdapDataServiceImpl.class);

    @Autowired
    private DirectoryDsmlService dsmlService;

    @Override
    public BatchResponse processData(LdapDataSource dataSource, BatchRequest batchReq) throws DirectoryDataException {
        LdapConnectionConfig ldapConnConfig = dataSource.toConfig();
        LdapConnection ldapConn = null;

        try {
            ldapConn = new LdapNetworkConnection(ldapConnConfig);

            connectAndBind(dataSource, ldapConn);

            return this.dsmlService.processDsml(
                    new Dsmlv2Engine(ldapConn, ldapConnConfig.getName(), ldapConnConfig.getCredentials()), batchReq);
        } finally {
            if (ldapConn != null) {
                unbindAndDisconnect(dataSource, ldapConn);
            }
        }
    }

    private static boolean connectAndBind(LdapDataSource dataSource, LdapConnection ldapConn)
            throws DirectoryLdapException {
        try {
            ldapConn.connect();
        } catch (IOException | LdapException e) {
            throw new DirectoryLdapException("Unable to connect to LDAP data source (" + dataSource + ").", e);
        }

        try {
            if (dataSource.getCredentials().isAnonymous()) {
                LOGGER.debug("Anonymously binding to LDAP data source (" + dataSource + ") ...");

                ldapConn.anonymousBind();
            } else {
                LOGGER.debug("Binding to LDAP data source (" + dataSource + ").");

                ldapConn.bind();
            }
        } catch (IOException | LdapException e) {
            throw new DirectoryLdapException("Unable to bind to LDAP data source (" + dataSource + ").", e);
        }

        return ldapConn.isAuthenticated();
    }

    private static boolean unbindAndDisconnect(LdapDataSource dataSource, LdapConnection ldapConn)
            throws DirectoryLdapException {
        if (ldapConn.isAuthenticated()) {
            try {
                ldapConn.unBind();
            } catch (LdapException e) {
                throw new DirectoryLdapException("Unable to unbind from LDAP data source (" + dataSource + ").", e);
            }
        }

        if (ldapConn.isConnected()) {
            try {
                ldapConn.close();
            } catch (IOException e) {
                throw new DirectoryLdapException("Unable to disconnect from LDAP data source (" + dataSource + ").", e);
            }
        }

        return !ldapConn.isConnected();
    }

    @Autowired
    @Override
    public void setDataSources(List<LdapDataSource> dataSources) {
        this.dataSources = dataSources;
    }
}
