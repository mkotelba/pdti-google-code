package gov.hhs.onc.pdti.dsml;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.SERVER)
public class ProviderInformationDirectoryDsmlException extends RuntimeException
{
	public ProviderInformationDirectoryDsmlException()
	{
		super();
	}

	public ProviderInformationDirectoryDsmlException(String str)
	{
		super(str);
	}

	public ProviderInformationDirectoryDsmlException(String str, Throwable throwable)
	{
		super(str, throwable);
	}

	public ProviderInformationDirectoryDsmlException(Throwable throwable)
	{
		super(throwable);
	}
}