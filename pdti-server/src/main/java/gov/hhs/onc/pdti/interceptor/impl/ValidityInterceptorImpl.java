package gov.hhs.onc.pdti.interceptor.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.DsmlMessage;
import gov.hhs.onc.pdti.ws.api.SearchRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("validityInterceptor")
@DirectoryStandard(DirectoryStandardId.IHE)
@Order(300)
@Scope("singleton")
public class ValidityInterceptorImpl extends AbstractDirectoryInterceptor implements DirectoryRequestInterceptor<BatchRequest> {
    private final static Class<? extends DsmlMessage>[] VALID_REQ_MSG_CLASSES = ArrayUtils.toArray(SearchRequest.class);

    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq) throws DirectoryInterceptorException {
        Class<? extends DsmlMessage> batchReqMsgClass;

        for (DsmlMessage batchReqMsg : batchReq.getBatchRequests()) {
            batchReqMsgClass = batchReqMsg.getClass();

            if (!ClassUtils.isAssignable(new Class<?>[] { batchReqMsgClass }, VALID_REQ_MSG_CLASSES)) {
                throw new DirectoryInterceptorException("Invalid DSML batch request message (directoryId=" + dirDesc.getDirectoryId() + ", requestId=" + reqId
                        + ", class=" + batchReqMsgClass.getName() + ").");
            }
        }
    }
}
