package gov.hhs.onc.pdti.interceptor;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;

public interface DirectoryRequestInterceptor<T> extends DirectoryInterceptor {
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, T queryReq) throws DirectoryInterceptorException;

    /*
     * public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq) throws DirectoryServiceException;
     * 
     * public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq) throws DirectoryServiceException;
     */
}
