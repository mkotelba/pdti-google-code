package gov.hhs.onc.pdti.ws.impl;

import gov.hhs.onc.pdti.ws.api.HpdPlusProviderInformationDirectoryPortType;
import gov.hhs.onc.pdti.ws.api.HpdRequest;
import gov.hhs.onc.pdti.ws.api.HpdResponse;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("hpdPlusProviderInformationDirectory")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService(serviceName = "Hpd_Plus_ProviderInformationDirectory_Service", targetNamespace = "urn:gov:hhs:onc:hpdplus:2013")
public class HpdPlusProviderInformationDirectoryImpl extends AbstractProviderInformationDirectory implements
        HpdPlusProviderInformationDirectoryPortType {

    @Override
    @WebMethod(operationName = "Hpd_Plus_ProviderInformationQueryRequest", action = "urn:gov:hhs:onc:hpdplus:2013:Hpd_Plus_ProviderInformationQueryRequest")
    @WebResult(name = "hpdResponse", targetNamespace = "urn:gov:hhs:onc:hpdplus:2013", partName = "body")
    public HpdResponse hpdPlusProviderInformationQueryRequest(
            @WebParam(name = "hpdRequest", targetNamespace = "urn:gov:hhs:onc:hpdplus:2013", partName = "body") HpdRequest body) {

        return this.dirService.processRequest(body);
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
