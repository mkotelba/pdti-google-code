package gov.hhs.onc.pdti.ws.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import org.apache.log4j.Logger;

import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ProviderInformationDirectoryPortType;

public class ProviderInformationDirectoryImpl implements ProviderInformationDirectoryPortType {

	private static final Logger LOGGER = Logger.getLogger(ProviderInformationDirectoryImpl.class.getName());
	
	static {
		LOGGER.info("sanity check!");
	}

	@Override
	@WebMethod(operationName = "ProviderInformationQueryRequest", action = "urn:ihe:iti:hpd:2010:ProviderInformationQueryRequest")
	@WebResult(name = "batchResponse", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body")
	public BatchResponse providerInformationQueryRequest(
			@WebParam(name = "batchRequest", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body") BatchRequest body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "ProviderInformationFeedRequest", action = "urn:ihe:iti:hpd:2010:ProviderInformationFeedRequest")
	@WebResult(name = "batchResponse", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body")
	public BatchResponse providerInformationFeedRequest(
			@WebParam(name = "batchRequest", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "body") BatchRequest body) {
		// TODO Auto-generated method stub
		return null;
	}

}
