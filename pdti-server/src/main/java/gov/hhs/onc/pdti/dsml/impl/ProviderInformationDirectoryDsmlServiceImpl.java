package gov.hhs.onc.pdti.dsml.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.directory.api.dsmlv2.engine.Dsmlv2Engine;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;
import org.xmlpull.v1.XmlPullParserException;

import gov.hhs.onc.pdti.dsml.ProviderInformationDirectoryDsmlException;
import gov.hhs.onc.pdti.dsml.ProviderInformationDirectoryDsmlService;
import gov.hhs.onc.pdti.ldap.ProviderInformationDirectoryLdapConnectionConfig;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.DsmlMessage;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import gov.hhs.onc.pdti.ws.api.SearchRequest;

@Scope("singleton")
@Service("providerInformationDirectoryDsmlService")
public class ProviderInformationDirectoryDsmlServiceImpl implements ProviderInformationDirectoryDsmlService {
    private final static Class<? extends DsmlMessage>[] VALID_REQUEST_TYPES = ArrayUtils.toArray(SearchRequest.class);
	
    private final static Logger LOGGER = Logger.getLogger(ProviderInformationDirectoryDsmlServiceImpl.class);

	@Autowired
    private ObjectFactory objectFactory;
	
    @Autowired
    private Jaxb2Marshaller marshaller;

    @Override
    public BatchResponse processDsml(BatchRequest batchReq) throws ProviderInformationDirectoryDsmlException {
        Class<? extends DsmlMessage> reqClass;

        for (DsmlMessage req : batchReq.getBatchRequests()) {
            reqClass = req.getClass();

            if (!ArrayUtils.contains(VALID_REQUEST_TYPES, reqClass)) {
                throw new ProviderInformationDirectoryDsmlException("Invalid request type: " + reqClass.getName());
            }
        }

        List<BatchResponse> batchResps = new ArrayList<>();

        for (ProviderInformationDirectoryLdapConnectionConfig ldapConnConfig : this.getLdapConnectionConfigs()) {
            batchResps.add(this.processDsml(batchReq, ldapConnConfig));
        }

        return this.combineDsmlResponses(batchResps);
    }

    private BatchResponse combineDsmlResponses(List<BatchResponse> batchResps) {
        // TODO: implement
        return batchResps.get(0);
    }

    private BatchResponse processDsml(BatchRequest batchReq,
            ProviderInformationDirectoryLdapConnectionConfig ldapConnConfig)
            throws ProviderInformationDirectoryDsmlException {
        LdapConnection ldapConn = new LdapNetworkConnection(ldapConnConfig);

        try {
            try {
                ldapConn.connect();
            } catch (IOException | LdapException e) {
                throw new ProviderInformationDirectoryDsmlException("Unable to connect to LDAP service ("
                        + ldapConnConfig + ").", e);
            }

            try {
                if (ldapConnConfig.isAnonymous()) {
                    LOGGER.debug("Anonymously binding to LDAP service (" + ldapConnConfig + ") ...");

                    ldapConn.anonymousBind();
                } else {
                    LOGGER.debug("Binding to LDAP service (" + ldapConnConfig + ") ...");

                    ldapConn.bind();
                }
            } catch (IOException | LdapException e) {
                throw new ProviderInformationDirectoryDsmlException("Unable to bind to LDAP service (" + ldapConnConfig
                        + ").", e);
            }

            Dsmlv2Engine dsmlEngine = new Dsmlv2Engine(ldapConn, ldapConnConfig.getName(),
                    ldapConnConfig.getCredentials());

            try {
                String batchReqStr = this.marshal(this.objectFactory.createBatchRequest(batchReq));

                LOGGER.trace("Processing DSML batch request:\n" + batchReqStr);

                JAXBElement<BatchResponse> batchRespElement = (JAXBElement<BatchResponse>) this.unmarshal(dsmlEngine
                        .processDSML(batchReqStr));
                String batchRespStr = this.marshal(batchRespElement);

                LOGGER.trace("Processed DSML batch request into batch response:\n" + batchRespStr);

                return batchRespElement.getValue();
            } catch (XmlMappingException | XmlPullParserException e) {
                throw new ProviderInformationDirectoryDsmlException("Unable to process DSML transaction.", e);
            }
        } finally {
            if (ldapConn.isAuthenticated()) {
                try {
                    ldapConn.unBind();
                } catch (LdapException e) {
                    throw new ProviderInformationDirectoryDsmlException("Unable to unbind from LDAP service ("
                            + ldapConnConfig + ").", e);
                }
            }

            if (ldapConn.isConnected()) {
                try {
                    ldapConn.close();
                } catch (IOException e) {
                    throw new ProviderInformationDirectoryDsmlException("Unable to disconnect from LDAP service ("
                            + ldapConnConfig + ").", e);
                }
            }
        }
    }

    private List<ProviderInformationDirectoryLdapConnectionConfig> getLdapConnectionConfigs() {
        // TODO: un-hardcode once a configuration architecture is determined
        ProviderInformationDirectoryLdapConnectionConfig localLdapConnConfig = new ProviderInformationDirectoryLdapConnectionConfig();
        localLdapConnConfig.setLdapPort(10389);

        return Arrays.asList(localLdapConnConfig);
    }

    private String marshal(Object obj) throws XmlMappingException {
        StringResult strResult = new StringResult();

        this.marshaller.marshal(obj, strResult);

        return strResult.toString();
    }

    private Object unmarshal(String str) throws XmlMappingException {
        return this.marshaller.unmarshal(new StringSource(str));
    }
}
