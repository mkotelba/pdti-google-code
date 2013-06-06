package gov.hhs.onc.pdti.service.interceptor.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.service.DirectoryServiceException;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.service.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.DsmlMessage;
import gov.hhs.onc.pdti.ws.api.ErrorResponse;
import gov.hhs.onc.pdti.ws.api.SearchResponse;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusError;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("idInterceptor")
@Order(0)
@Scope("singleton")
public class IdInterceptor extends AbstractDirectoryInterceptor implements DirectoryRequestInterceptor, DirectoryResponseInterceptor {
    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, HpdPlusRequest hpdPlusReq) throws DirectoryServiceException {
        String dirId = dirDesc.getDirectoryId(), reqId = hpdPlusReq.getRequestId();

        hpdPlusReq.setDirectoryId(dirId);

        BatchRequest batchReq = hpdPlusReq.getBatchRequest();
        batchReq.setRequestId(reqId);

        for (DsmlMessage batchReqMsg : batchReq.getBatchRequests()) {
            batchReqMsg.setRequestId(reqId);
        }
    }

    @Override
    public void interceptResponse(DirectoryDescriptor dirDesc, HpdPlusRequest hpdPlusReq, HpdPlusResponse hpdPlusResp) throws DirectoryServiceException {
        String dirId = dirDesc.getDirectoryId(), reqId = hpdPlusReq.getRequestId();

        hpdPlusResp.setDirectoryId(dirId);
        hpdPlusResp.setRequestId(reqId);

        if (hpdPlusResp.isSetErrors()) {
            for (HpdPlusError hpdPlusErr : hpdPlusResp.getErrors()) {
                hpdPlusErr.setDirectoryId(dirId);
                hpdPlusErr.setRequestId(reqId);
            }
        }

        if (hpdPlusResp.isSetResponseItems()) {
            List<Object> respItems = hpdPlusResp.getResponseItems();
            SearchResponse searchRespMsg;

            for (BatchResponse batchResp : (Collection<BatchResponse>) CollectionUtils.select(respItems,
                    PredicateUtils.instanceofPredicate(BatchResponse.class))) {
                batchResp.setRequestId(reqId);

                for (JAXBElement<?> batchRespItem : batchResp.getBatchResponses()) {
                    if (DsmlMessage.class.isAssignableFrom(batchRespItem.getDeclaredType())) {
                        ((DsmlMessage) batchRespItem.getValue()).setRequestId(reqId);
                    } else if (ErrorResponse.class.isAssignableFrom(batchRespItem.getDeclaredType())) {
                        ((ErrorResponse) batchRespItem.getValue()).setRequestId(reqId);
                    } else if (SearchResponse.class.isAssignableFrom(batchRespItem.getDeclaredType())) {
                        searchRespMsg = (SearchResponse) batchRespItem.getValue();
                        searchRespMsg.setRequestId(reqId);
                        searchRespMsg.getSearchResultDone().setRequestId(reqId);
                    }
                }
            }
        }
    }
}
