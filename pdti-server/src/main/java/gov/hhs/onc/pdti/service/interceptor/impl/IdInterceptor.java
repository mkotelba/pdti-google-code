package gov.hhs.onc.pdti.service.interceptor.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.service.DirectoryServiceException;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.service.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusError;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import java.util.Collection;
import java.util.List;
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
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq) throws DirectoryServiceException {
        String dirId = dirDesc.getDirectoryId();

        hpdPlusReq.setDirectoryId(dirId);

        this.interceptRequest(dirDesc, reqId, hpdPlusReq.getBatchRequest());
    }

    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq) throws DirectoryServiceException {
        DirectoryUtils.setRequestId(batchReq, reqId);
    }

    @Override
    public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq, HpdPlusResponse hpdPlusResp)
            throws DirectoryServiceException {
        String dirId = dirDesc.getDirectoryId();

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

            for (BatchResponse batchResp : (Collection<BatchResponse>) CollectionUtils.select(respItems,
                    PredicateUtils.instanceofPredicate(BatchResponse.class))) {
                this.interceptResponse(dirDesc, reqId, hpdPlusReq.getBatchRequest(), batchResp);
            }
        }
    }

    @Override
    public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq, BatchResponse batchResp) throws DirectoryServiceException {
        DirectoryUtils.setRequestId(batchResp, reqId);
    }
}
