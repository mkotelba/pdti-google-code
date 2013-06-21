package gov.hhs.onc.pdti.service.impl;

import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.DirectoryType;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.service.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.springframework.beans.factory.annotation.DirectoryTypeQualifier;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequestMetadata;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponseMetadata;
import gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@DirectoryTypeQualifier(DirectoryType.HPD_PLUS_PROPOSED)
@Scope("singleton")
@Service("hpdPlusDirService")
public class HpdPlusDirectoryServiceImpl extends AbstractDirectoryService<HpdPlusRequest, HpdPlusResponse> implements
        DirectoryService<HpdPlusRequest, HpdPlusResponse> {
    private final static Logger LOGGER = Logger.getLogger(HpdPlusDirectoryServiceImpl.class);

    @Autowired
    @DirectoryTypeQualifier(DirectoryType.HPD_PLUS_PROPOSED)
    private ObjectFactory hpdPlusObjectFactory;

    @Override
    public HpdPlusResponse processRequest(HpdPlusRequest hpdPlusReq) {
        String dirId = this.dirDesc.getDirectoryId(), reqId = hpdPlusReq.getRequestId();
        BatchRequest batchReq = hpdPlusReq.getBatchRequest();
        HpdPlusRequestMetadata reqMeta = hpdPlusReq.getRequestMetadata();
        HpdPlusResponse hpdPlusResp = this.hpdPlusObjectFactory.createHpdPlusResponse();

        HpdPlusResponseMetadata respMeta = this.hpdPlusObjectFactory.createHpdPlusResponseMetadata();
        respMeta.setRequestMetadata(reqMeta);
        hpdPlusResp.setResponseMetadata(respMeta);

        if (this.reqInterceptors != null) {
            for (DirectoryRequestInterceptor reqInterceptor : this.reqInterceptors) {
                LOGGER.trace("Intercepting directory (id=" + dirId + ") HPD Plus request (class=" + reqInterceptor.getClass().getName() + ").");

                try {
                    reqInterceptor.interceptRequest(this.dirDesc, hpdPlusReq);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    hpdPlusResp.getErrors().add(this.errBuilder.buildError(dirId, reqId, HpdPlusErrorType.OTHER, th));
                }
            }
        }

        LOGGER.debug("Processing HPD Plus request (directoryId=" + dirId + ", requestId=" + reqId + ").");
        LOGGER.trace("Processing HPD Plus request (directoryId=" + dirId + ", requestId=" + reqId + "):\n"
                + this.jaxb2Marshaller.marshal(this.hpdPlusObjectFactory.createHpdPlusRequest(hpdPlusReq)));

        if (this.dataServices != null) {
            for (DirectoryDataService<?> dataService : this.dataServices) {
                try {
                    hpdPlusResp.getResponseItems().addAll(dataService.processData(batchReq));
                } catch (Throwable th) {
                    // TODO: improve error handling
                    hpdPlusResp.getErrors().add(this.errBuilder.buildError(dirId, reqId, HpdPlusErrorType.OTHER, th));
                }
            }
        }

        try {
            hpdPlusResp.getResponseItems().addAll(this.fedService.federate(hpdPlusReq));
        } catch (Throwable th) {
            // TODO: improve error handling
            hpdPlusResp.getErrors().add(this.errBuilder.buildError(dirId, reqId, HpdPlusErrorType.OTHER, th));
        }

        if (this.respInterceptors != null) {
            for (DirectoryResponseInterceptor respInterceptor : this.respInterceptors) {
                LOGGER.trace("Intercepting directory (id=" + dirId + ") HPD Plus response (class=" + respInterceptor.getClass().getName() + ").");

                try {
                    respInterceptor.interceptResponse(this.dirDesc, hpdPlusReq, hpdPlusResp);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    hpdPlusResp.getErrors().add(this.errBuilder.buildError(dirId, reqId, HpdPlusErrorType.OTHER, th));
                }
            }
        }

        LOGGER.debug("Processed HPD Plus request (directoryId=" + dirId + ", requestId=" + reqId + ") into HPD Plus response.");
        LOGGER.trace("Processed HPD Plus request (directoryId=" + dirId + ", requestId=" + reqId + ") into HPD Plus response:\n"
                + this.jaxb2Marshaller.marshal(this.hpdPlusObjectFactory.createHpdPlusResponse(hpdPlusResp)));

        return hpdPlusResp;
    }

    @Autowired
    @DirectoryTypeQualifier(DirectoryType.HPD_PLUS_PROPOSED)
    @Override
    @Qualifier("main")
    protected void setDirectoryDescriptor(DirectoryDescriptor dirDesc) {
        this.dirDesc = dirDesc;
    }

    @Autowired
    @DirectoryTypeQualifier(DirectoryType.HPD_PLUS_PROPOSED)
    @Override
    protected void setFederationService(FederationService<HpdPlusRequest, HpdPlusResponse> fedService) {
        this.fedService = fedService;
    }
}
