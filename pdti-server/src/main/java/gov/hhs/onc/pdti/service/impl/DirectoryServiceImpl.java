package gov.hhs.onc.pdti.service.impl;

import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.DirectoryType;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.service.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.springframework.beans.factory.annotation.DirectoryTypeQualifier;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.ErrorType;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@DirectoryTypeQualifier(DirectoryType.IHE)
@Scope("singleton")
@Service("dirService")
public class DirectoryServiceImpl extends AbstractDirectoryService<BatchRequest, BatchResponse> implements DirectoryService<BatchRequest, BatchResponse> {
    private final static Logger LOGGER = Logger.getLogger(DirectoryServiceImpl.class);

    @Autowired
    @DirectoryTypeQualifier(DirectoryType.IHE)
    private ObjectFactory objectFactory;

    @Override
    public BatchResponse processRequest(BatchRequest batchReq) {
        String dirId = this.dirDesc.getDirectoryId(), reqId = DirectoryUtils.defaultRequestId(batchReq.getRequestId());
        BatchResponse batchResp = this.objectFactory.createBatchResponse();

        if (this.reqInterceptors != null) {
            for (DirectoryRequestInterceptor reqInterceptor : this.reqInterceptors) {
                LOGGER.trace("Intercepting DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ", class="
                        + reqInterceptor.getClass().getName() + ").");

                try {
                    reqInterceptor.interceptRequest(this.dirDesc, reqId, batchReq);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    batchResp.getBatchResponses().add(
                            this.objectFactory.createBatchResponseErrorResponse(this.errBuilder.buildErrorResponse(reqId, ErrorType.OTHER, th)));
                }
            }
        }

        LOGGER.debug("Processing DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ").");
        LOGGER.trace("Processing DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + "):\n"
                + this.jaxb2Marshaller.marshal(this.objectFactory.createBatchRequest(batchReq)));

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

        if (this.respInterceptors != null) {
            for (DirectoryResponseInterceptor respInterceptor : this.respInterceptors) {
                LOGGER.trace("Intercepting DSML batch response (directoryId=" + dirId + ", requestId=" + reqId + ", class="
                        + respInterceptor.getClass().getName() + ").");

                try {
                    respInterceptor.interceptResponse(this.dirDesc, reqId, batchReq, batchResp);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    batchResp.getBatchResponses().add(
                            this.objectFactory.createBatchResponseErrorResponse(this.errBuilder.buildErrorResponse(reqId, ErrorType.OTHER, th)));
                }
            }
        }

        LOGGER.debug("Processed DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ") into DSML batch response.");
        LOGGER.trace("Processed DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ") into DSML batch response:\n"
                + this.jaxb2Marshaller.marshal(this.objectFactory.createBatchResponse(batchResp)));

        return batchResp;
    }

    private static void combineBatchResponses(BatchResponse batchResp, List<BatchResponse> batchRespCombine) {
        for (BatchResponse batchRespCombineItem : batchRespCombine) {
            batchResp.getBatchResponses().addAll(batchRespCombineItem.getBatchResponses());
        }
    }

    @Autowired
    @DirectoryTypeQualifier(DirectoryType.IHE)
    @Override
    @Qualifier("main")
    protected void setDirectoryDescriptor(DirectoryDescriptor dirDesc) {
        this.dirDesc = dirDesc;
    }

    @Autowired
    @DirectoryTypeQualifier(DirectoryType.IHE)
    @Override
    protected void setFederationService(FederationService<BatchRequest, BatchResponse> fedService) {
        this.fedService = fedService;
    }
}
