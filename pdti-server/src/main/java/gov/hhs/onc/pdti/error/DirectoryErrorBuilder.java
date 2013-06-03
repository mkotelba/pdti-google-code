package gov.hhs.onc.pdti.error;

import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusError;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;

public interface DirectoryErrorBuilder {
    public HpdPlusError buildError(String reqId, HpdPlusErrorType errType, Throwable th);
}
