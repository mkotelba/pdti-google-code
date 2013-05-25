package gov.hhs.onc.pdti.ws.impl;

import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.HpdRequest;
import gov.hhs.onc.pdti.ws.api.HpdResponse;
import gov.hhs.onc.pdti.ws.api.ProviderInformationDirectoryPortType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.InstanceofPredicate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("providerInformationDirectory")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService(serviceName = "ProviderInformationDirectory_Service", targetNamespace = "urn:ihe:iti:hpd:2010")
public class ProviderInformationDirectoryImpl extends AbstractProviderInformationDirectory implements
        ProviderInformationDirectoryPortType {
    @Override
    @WebMethod(operationName = "ProviderInformationQueryRequest", action = "urn:ihe:iti:hpd:2010:ProviderInformationQueryRequest")
    @WebResult(name = "batchResponse", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body")
    public BatchResponse providerInformationQueryRequest(
            @WebParam(name = "batchRequest", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body") BatchRequest body) {

        HpdRequest hpdReq = this.objectFactory.createHpdRequest();
        hpdReq.setBatchRequest(body);

        HpdResponse hpdResp = this.dirService.processRequest(hpdReq);

        return (BatchResponse) CollectionUtils.find(hpdResp.getHpdResponseItems(),
                InstanceofPredicate.getInstance(BatchResponse.class));
    }

    @Override
    @WebMethod(operationName = "ProviderInformationFeedRequest", action = "urn:ihe:iti:hpd:2010:ProviderInformationFeedRequest")
    @WebResult(name = "batchResponse", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body")
    public BatchResponse providerInformationFeedRequest(
            @WebParam(name = "batchRequest", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body") BatchRequest body) {

        // TODO: implement
        return this.objectFactory.createBatchResponse();
    }
}
