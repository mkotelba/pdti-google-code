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
import gov.hhs.onc.pdti.ws.api.HpdPlusProviderInformationDirectoryPortType;
import gov.hhs.onc.pdti.ws.api.HpdRequest;
import gov.hhs.onc.pdti.ws.api.HpdResponse;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;

@Scope("singleton")
@Service("hpdPlusProviderInformationDirectoryImpl")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService(serviceName = "Hpd_Plus_ProviderInformationDirectory_Service", targetNamespace = "urn:gov:hhs:onc:hpdplus:2013")
public class HpdPlusProviderInformationDirectoryImpl implements HpdPlusProviderInformationDirectoryPortType {
    private final static Logger LOGGER = Logger.getLogger(HpdPlusProviderInformationDirectoryImpl.class);

    @Autowired
    private ObjectFactory objectFactory;

    @Autowired
    private ProviderInformationDirectoryDsmlService dsmlService;

    @Override
    @WebMethod(operationName = "Hpd_Plus_ProviderInformationQueryRequest", action = "urn:gov:hhs:onc:hpdplus:2013:Hpd_Plus_ProviderInformationQueryRequest")
    @WebResult(name = "hpdResponse", targetNamespace = "urn:gov:hhs:onc:hpdplus:2013", partName = "body")
    public HpdResponse hpdPlusProviderInformationQueryRequest(
            @WebParam(name = "hpdRequest", targetNamespace = "urn:gov:hhs:onc:hpdplus:2013", partName = "body") HpdRequest body) {
        // TODO: implement
        return this.objectFactory.createHpdResponse();
    }

    @Override
    @WebMethod(operationName = "Hpd_Plus_ProviderInformationFeedRequest", action = "urn:gov:hhs:onc:hpdplus:2013:Hpd_Plus_ProviderInformationFeedRequest")
    @WebResult(name = "hpdResponse", targetNamespace = "urn:gov:hhs:onc:hpdplus:2013", partName = "body")
    public HpdResponse hpdPlusProviderInformationFeedRequest(
            @WebParam(name = "hpdRequest", targetNamespace = "urn:gov:hhs:onc:hpdplus:2013", partName = "body") HpdRequest body) {
        // TODO: implement
        return this.objectFactory.createHpdResponse();
    }
}
