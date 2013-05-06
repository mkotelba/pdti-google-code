package gov.hhs.onc.pdti.ws.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import gov.hhs.onc.pdti.dsml.ProviderInformationDirectoryDsmlService;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ProviderInformationDirectoryPortType;

@Scope("singleton")
@Service("providerInformationDirectoryImpl")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService(serviceName = "ProviderInformationDirectory_Service", targetNamespace = "urn:ihe:iti:hpd:2010")
public class ProviderInformationDirectoryImpl implements ProviderInformationDirectoryPortType {
    private final static Logger LOGGER = Logger.getLogger(ProviderInformationDirectoryImpl.class);

    @Autowired
    private ProviderInformationDirectoryDsmlService dsmlService;

    @Override
    @WebMethod(operationName = "ProviderInformationQueryRequest", action = "urn:ihe:iti:hpd:2010:ProviderInformationQueryRequest")
    @WebResult(name = "batchResponse", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body")
    public BatchResponse providerInformationQueryRequest(
            @WebParam(name = "batchRequest", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body") BatchRequest body) {

        return this.dsmlService.processDsml(body);
    }

    @Override
    @WebMethod(operationName = "ProviderInformationFeedRequest", action = "urn:ihe:iti:hpd:2010:ProviderInformationFeedRequest")
    @WebResult(name = "batchResponse", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body")
    public BatchResponse providerInformationFeedRequest(
            @WebParam(name = "batchRequest", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body") BatchRequest body) {

        // TODO: implement
        return new BatchResponse();
    }
}
