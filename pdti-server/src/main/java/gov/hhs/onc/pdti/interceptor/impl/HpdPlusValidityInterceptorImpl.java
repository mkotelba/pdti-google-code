package gov.hhs.onc.pdti.interceptor.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("hpdPlusValidityInterceptor")
@DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
@Order(300)
@Scope("singleton")
public class HpdPlusValidityInterceptorImpl extends AbstractDirectoryInterceptor implements DirectoryRequestInterceptor<HpdPlusRequest> {
    @Autowired
    private ValidityInterceptorImpl validityInterceptor;

    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq) throws DirectoryInterceptorException {
        this.validityInterceptor.interceptRequest(dirDesc, reqId, hpdPlusReq.getBatchRequest());
    }
}
