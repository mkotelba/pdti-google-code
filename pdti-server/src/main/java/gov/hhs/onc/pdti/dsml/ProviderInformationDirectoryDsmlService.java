package gov.hhs.onc.pdti.dsml;

import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;

public interface ProviderInformationDirectoryDsmlService
{
	public abstract BatchResponse processDsml(BatchRequest batchReq) throws ProviderInformationDirectoryDsmlException;
}