package gov.hhs.onc.pdti.service.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.DirectoryType;
import gov.hhs.onc.pdti.DirectoryTypeId;
import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.ErrorType;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import java.util.List;
import java.util.SortedSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@DirectoryStandard(DirectoryStandardId.IHE)
@Scope("singleton")
@Service("dirService")
public class DirectoryServiceImpl extends AbstractDirectoryService<BatchRequest, BatchResponse> implements DirectoryService<BatchRequest, BatchResponse> {
    private final static Logger LOGGER = Logger.getLogger(DirectoryServiceImpl.class);

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    private ObjectFactory objectFactory;

    @Override
    public BatchResponse processRequest(BatchRequest batchReq) {
        String dirId = this.dirDesc.getDirectoryId(), reqId = DirectoryUtils.defaultRequestId(batchReq.getRequestId());
        BatchResponse batchResp = this.objectFactory.createBatchResponse();

        this.interceptRequests(dirDesc, dirId, reqId, batchReq, batchResp);

        LOGGER.debug("Processing DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ").");
        LOGGER.trace("Processing DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + "):\n"
                + this.dirJaxb2Marshaller.marshal(this.objectFactory.createBatchRequest(batchReq)));

        if (this.dataServices != null) {
            for (DirectoryDataService<?> dataService : this.dataServices) {
                try {
                    combineBatchResponses(batchResp, dataService.processData(batchReq));
                } catch (Throwable th) {
                    // TODO: improve error handling
                    batchResp.getBatchResponses().add(
                            this.objectFactory.createBatchResponseErrorResponse(this.errBuilder.buildErrorResponse(reqId, ErrorType.OTHER, th)));
                }
            }
        }

        try {
            combineBatchResponses(batchResp, this.fedService.federate(batchReq));
        } catch (Throwable th) {
            // TODO: improve error handling
            batchResp.getBatchResponses().add(
                    this.objectFactory.createBatchResponseErrorResponse(this.errBuilder.buildErrorResponse(reqId, ErrorType.OTHER, th)));
        }

        this.interceptResponses(dirDesc, dirId, reqId, batchReq, batchResp);

        LOGGER.debug("Processed DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ") into DSML batch response.");
        LOGGER.trace("Processed DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ") into DSML batch response:\n"
                + this.dirJaxb2Marshaller.marshal(this.objectFactory.createBatchResponse(batchResp)));

        return batchResp;
    }

    @Override
    protected void addError(String fedDirId, String reqId, BatchResponse fedBatchResp, Throwable th) {
        // TODO: improve error handling
        fedBatchResp.getBatchResponses().add(
                this.objectFactory.createBatchResponseErrorResponse(this.errBuilder.buildErrorResponse(reqId, ErrorType.OTHER, th)));
    }

    private static void combineBatchResponses(BatchResponse batchResp, List<BatchResponse> batchRespCombine) {
        for (BatchResponse batchRespCombineItem : batchRespCombine) {
            batchResp.getBatchResponses().addAll(batchRespCombineItem.getBatchResponses());
        }
    }

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    @DirectoryType(DirectoryTypeId.MAIN)
    @Override
    protected void setDirectoryDescriptor(DirectoryDescriptor dirDesc) {
        this.dirDesc = dirDesc;
    }

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    @Override
    protected void setFederationService(FederationService<BatchRequest, BatchResponse> fedService) {
        this.fedService = fedService;
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.IHE)
    @Override
    protected void setRequestInterceptors(SortedSet<DirectoryRequestInterceptor<BatchRequest>> reqInterceptors) {
        this.reqInterceptors = reqInterceptors;
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.IHE)
    @Override
    protected void setResponseInterceptors(SortedSet<DirectoryResponseInterceptor<BatchRequest, BatchResponse>> respInterceptors) {
        this.respInterceptors = respInterceptors;
    }
}
