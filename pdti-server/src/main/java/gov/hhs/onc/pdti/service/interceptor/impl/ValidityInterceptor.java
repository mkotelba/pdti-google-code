package gov.hhs.onc.pdti.service.interceptor.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.service.DirectoryServiceException;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.DsmlMessage;
import gov.hhs.onc.pdti.ws.api.SearchRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("validityInterceptor")
@Order(2)
@Scope("singleton")
public class ValidityInterceptor extends AbstractDirectoryInterceptor implements DirectoryRequestInterceptor {
    private final static Class<? extends DsmlMessage>[] VALID_REQ_MSG_CLASSES = ArrayUtils.toArray(SearchRequest.class);

    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, HpdPlusRequest hpdPlusReq) throws DirectoryServiceException {
        this.interceptRequest(dirDesc, hpdPlusReq.getBatchRequest());
    }

    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, BatchRequest batchReq) throws DirectoryServiceException {
        Class<? extends DsmlMessage> batchReqMsgClass;

        for (DsmlMessage batchReqMsg : batchReq.getBatchRequests()) {
            batchReqMsgClass = batchReqMsg.getClass();

            if (!ClassUtils.isAssignable(new Class<?>[] { batchReqMsgClass }, VALID_REQ_MSG_CLASSES)) {
                throw new DirectoryServiceException("Invalid DSML batch request message (class=" + batchReqMsgClass.getName() + ").");
            }
        }
    }
}
