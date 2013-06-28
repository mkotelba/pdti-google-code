package gov.hhs.onc.pdti.interceptor;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;

public interface DirectoryResponseInterceptor<T, U> extends DirectoryInterceptor {
    public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, T queryReq, U queryResp) throws DirectoryInterceptorException;

    /*
     * public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq, HpdPlusResponse hpdPlusResp) throws
     * DirectoryServiceException;
     * 
     * public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq, BatchResponse batchResp) throws
     * DirectoryServiceException;
     */
}
