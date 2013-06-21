package gov.hhs.onc.pdti.data.federation.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.DirectoryType;
import gov.hhs.onc.pdti.data.federation.DirectoryFederationException;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.service.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.springframework.beans.factory.annotation.DirectoryTypeQualifier;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.ErrorType;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import gov.hhs.onc.pdti.ws.api.ProviderInformationDirectoryService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@DirectoryTypeQualifier(DirectoryType.IHE)
@Scope("singleton")
@Service("fedService")
public class FederationServiceImpl extends AbstractFederationService<BatchRequest, BatchResponse> implements FederationService<BatchRequest, BatchResponse> {
    private final static Logger LOGGER = Logger.getLogger(FederationServiceImpl.class);

    @Autowired
    private ObjectFactory objectFactory;

    @Override
    public BatchResponse federate(DirectoryDescriptor fedDir, BatchRequest batchReq) throws DirectoryFederationException {
        String fedDirId = fedDir.getDirectoryId(), reqId = batchReq.getRequestId();
        BatchRequest fedBatchReq = (BatchRequest) batchReq.clone();
        BatchResponse fedBatchResp = this.objectFactory.createBatchResponse();

        if (this.reqInterceptors != null) {
            for (DirectoryRequestInterceptor reqInterceptor : this.reqInterceptors) {
                LOGGER.trace("Intercepting federated directory (id=" + fedDirId + ") DSML batch request (class=" + reqInterceptor.getClass().getName() + ").");

                try {
                    reqInterceptor.interceptRequest(fedDir, fedBatchReq);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    fedBatchResp.getBatchResponses().add(
                            this.objectFactory.createBatchResponseErrorResponse(this.errBuilder.buildErrorResponse(reqId, ErrorType.OTHER, th)));
                }
            }
        }

        try {
            ProviderInformationDirectoryService fedDirService = new ProviderInformationDirectoryService(fedDir.getWsdlLocation());

            fedBatchResp = fedDirService.getProviderInformationDirectoryPortSoap().providerInformationQueryRequest(fedBatchReq);
        } catch (Throwable th) {
            // TODO: improve error handling
            fedBatchResp.getBatchResponses().add(
                    this.objectFactory.createBatchResponseErrorResponse(this.errBuilder.buildErrorResponse(reqId, ErrorType.OTHER, th)));
        }

        if (this.respInterceptors != null) {
            for (DirectoryResponseInterceptor respInterceptor : this.respInterceptors) {
                LOGGER.trace("Intercepting federated directory (id=" + fedDirId + ") DSML batch response (class=" + respInterceptor.getClass().getName() + ").");

                try {
                    respInterceptor.interceptResponse(fedDir, fedBatchReq, fedBatchResp);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    fedBatchResp.getBatchResponses().add(
                            this.objectFactory.createBatchResponseErrorResponse(this.errBuilder.buildErrorResponse(reqId, ErrorType.OTHER, th)));
                }
            }
        }

        return fedBatchResp;
    }

    @Autowired(required = false)
    @DirectoryTypeQualifier(DirectoryType.IHE)
    @Qualifier("federated")
    @Override
    protected void setFederatedDirs(List<DirectoryDescriptor> federatedDirs) {
        this.federatedDirs = federatedDirs;
    }
}
