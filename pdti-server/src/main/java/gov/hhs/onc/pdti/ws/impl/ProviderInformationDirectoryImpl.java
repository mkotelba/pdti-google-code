package gov.hhs.onc.pdti.ws.impl;

import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ProviderInformationDirectoryPortType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class ProviderInformationDirectoryImpl extends SpringBeanAutowiringSupport implements ProviderInformationDirectoryPortType {

	private static final Logger LOGGER = Logger.getLogger(ProviderInformationDirectoryImpl.class.getName());
	
	static {
		LOGGER.info("sanity check!");
	}

	@Override
	@WebMethod(operationName = "ProviderInformationQueryRequest", action = "urn:ihe:iti:hpd:2010:ProviderInformationQueryRequest")
	@WebResult(name = "batchResponse", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body")
	public BatchResponse providerInformationQueryRequest(
			@WebParam(name = "batchRequest", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body") BatchRequest body) {
		
		return new BatchResponse();
	}

	@Override
	@WebMethod(operationName = "ProviderInformationFeedRequest", action = "urn:ihe:iti:hpd:2010:ProviderInformationFeedRequest")
	@WebResult(name = "batchResponse", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body")
	public BatchResponse providerInformationFeedRequest(
			@WebParam(name = "batchRequest", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body") BatchRequest body) {
		
		return new BatchResponse();
	}

}
