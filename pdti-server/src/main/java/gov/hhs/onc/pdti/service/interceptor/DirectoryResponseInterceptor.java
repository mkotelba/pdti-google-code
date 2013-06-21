package gov.hhs.onc.pdti.service.interceptor;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.service.DirectoryServiceException;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;

public interface DirectoryResponseInterceptor extends DirectoryInterceptor {
    public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq, HpdPlusResponse hpdPlusResp)
            throws DirectoryServiceException;

    public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq, BatchResponse batchResp) throws DirectoryServiceException;
}
