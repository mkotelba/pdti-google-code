package gov.hhs.onc.pdti.ws.handler;


import gov.hhs.onc.pdti.DirectoryRuntimeException;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CUSTOM, customFaultCode = "dupReqId")
public class DuplicateRequestIdException extends DirectoryRuntimeException {
    public DuplicateRequestIdException() {
        super();
    }

    public DuplicateRequestIdException(String msg) {
        super(msg);
    }

    public DuplicateRequestIdException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DuplicateRequestIdException(Throwable cause) {
        super(cause);
    }
}
