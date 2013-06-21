package gov.hhs.onc.pdti.service.interceptor;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.service.DirectoryServiceException;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;

public interface DirectoryRequestInterceptor extends DirectoryInterceptor {
    public void interceptRequest(DirectoryDescriptor dirDesc, HpdPlusRequest hpdPlusReq) throws DirectoryServiceException;

    public void interceptRequest(DirectoryDescriptor dirDesc, BatchRequest batchReq) throws DirectoryServiceException;
}
