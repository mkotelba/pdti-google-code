package gov.hhs.onc.pdti.service.impl;

import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.error.DirectoryErrorBuilder;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.jaxb.DirectoryJaxb2Marshaller;
import gov.hhs.onc.pdti.service.DirectoryService;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDirectoryService<T, U> implements DirectoryService<T, U> {
    @Autowired
    protected DirectoryJaxb2Marshaller dirJaxb2Marshaller;

    @Autowired
    protected DirectoryErrorBuilder errBuilder;

    @Autowired(required = false)
    protected List<DirectoryDataService<?>> dataServices;

    protected DirectoryDescriptor dirDesc;

    protected FederationService<T, U> fedService;

    protected Set<DirectoryRequestInterceptor<T>> reqInterceptors;

    protected Set<DirectoryResponseInterceptor<T, U>> respInterceptors;

    private final static Logger LOGGER = Logger.getLogger(AbstractDirectoryService.class);

    protected void interceptRequests(DirectoryDescriptor dirDesc, String dirId, String reqId, T queryReq, U queryResp) {
        if (this.reqInterceptors != null) {
            for (DirectoryRequestInterceptor reqInterceptor : this.reqInterceptors) {
                LOGGER.trace("Intercepting request (directoryId=" + dirId + ", requestId=" + reqId + ", requestClass=" + queryReq.getClass().getName()
                        + "): class=" + reqInterceptor.getClass().getName());

                try {
                    reqInterceptor.interceptRequest(dirDesc, reqId, queryReq);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    this.addError(dirId, reqId, queryResp, th);
                }
            }
        }
    }

    protected void interceptResponses(DirectoryDescriptor dirDesc, String dirId, String reqId, T queryReq, U queryResp) {
        if (this.respInterceptors != null) {
            for (DirectoryResponseInterceptor respInterceptor : this.respInterceptors) {
                LOGGER.trace("Intercepting response (directoryId=" + dirId + ", requestId=" + reqId + ", responseClass=" + queryResp.getClass().getName()
                        + "): class=" + respInterceptor.getClass().getName());

                try {
                    respInterceptor.interceptResponse(dirDesc, reqId, queryReq, queryResp);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    this.addError(dirId, reqId, queryResp, th);
                }
            }
        }
    }

    protected abstract void addError(String dirId, String reqId, U queryResp, Throwable th);

    protected abstract void setDirectoryDescriptor(DirectoryDescriptor dirDesc);

    protected abstract void setFederationService(FederationService<T, U> fedService);

    protected abstract void setRequestInterceptors(SortedSet<DirectoryRequestInterceptor<T>> reqInterceptors);

    protected abstract void setResponseInterceptors(SortedSet<DirectoryResponseInterceptor<T, U>> respInterceptors);
}
