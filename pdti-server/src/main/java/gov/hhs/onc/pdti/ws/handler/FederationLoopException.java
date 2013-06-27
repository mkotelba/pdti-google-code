package gov.hhs.onc.pdti.ws.handler;

import gov.hhs.onc.pdti.DirectoryRuntimeException;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CUSTOM, customFaultCode = "fedLoop")
public class FederationLoopException extends DirectoryRuntimeException {
    public FederationLoopException() {
        super();
    }

    public FederationLoopException(String msg) {
        super(msg);
    }

    public FederationLoopException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FederationLoopException(Throwable cause) {
        super(cause);
    }
}
