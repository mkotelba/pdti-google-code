package gov.hhs.onc.pdti.service.interceptor.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.service.DirectoryServiceException;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.ws.api.DsmlMessage;
import gov.hhs.onc.pdti.ws.api.SearchRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
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
        Map<Integer, Class<? extends DsmlMessage>> invalidBatchReqMsgs = new LinkedHashMap<>();
        ListIterator<DsmlMessage> batchReqMsgIter = hpdPlusReq.getBatchRequest().getBatchRequests().listIterator();
        int batchReqMsgIndex;
        DsmlMessage batchReqMsg;
        Class<? extends DsmlMessage> batchReqMsgClass;

        while(batchReqMsgIter.hasNext() && ((batchReqMsgIndex = batchReqMsgIter.nextIndex()) >= 0) && ((batchReqMsg = batchReqMsgIter.next()) != null)
                && ((batchReqMsgClass = batchReqMsg.getClass()) != null)) {
            if (!ClassUtils.isAssignable(new Class<?>[] { batchReqMsgClass }, VALID_REQ_MSG_CLASSES)) {
                batchReqMsgIter.remove();
                invalidBatchReqMsgs.put(batchReqMsgIndex, batchReqMsgClass);
            }
        }

        if (!invalidBatchReqMsgs.isEmpty()) {
            throw new DirectoryServiceException("Invalid DSML batch request message(s) removed: " + StringUtils.join(invalidBatchReqMsgs, ", "));
        }
    }
}
