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
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequestMetadata;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponseMetadata;
import gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory;
import java.util.SortedSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
@Scope("singleton")
@Service("hpdPlusDirService")
public class HpdPlusDirectoryServiceImpl extends AbstractDirectoryService<HpdPlusRequest, HpdPlusResponse> implements
        DirectoryService<HpdPlusRequest, HpdPlusResponse> {
    private final static Logger LOGGER = Logger.getLogger(HpdPlusDirectoryServiceImpl.class);

    @Autowired
    @DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
    private ObjectFactory hpdPlusObjectFactory;

    @Override
    public HpdPlusResponse processRequest(HpdPlusRequest hpdPlusReq) {
        String dirId = this.dirDesc.getDirectoryId(), reqId = DirectoryUtils.defaultRequestId(hpdPlusReq.getRequestId());
        BatchRequest batchReq = hpdPlusReq.getBatchRequest();
        HpdPlusRequestMetadata reqMeta = hpdPlusReq.getRequestMetadata();
        HpdPlusResponse hpdPlusResp = this.hpdPlusObjectFactory.createHpdPlusResponse();

        HpdPlusResponseMetadata respMeta = this.hpdPlusObjectFactory.createHpdPlusResponseMetadata();
        respMeta.setRequestMetadata(reqMeta);
        hpdPlusResp.setResponseMetadata(respMeta);

        this.interceptRequests(dirDesc, dirId, reqId, hpdPlusReq, hpdPlusResp);

        LOGGER.debug("Processing HPD Plus request (directoryId=" + dirId + ", requestId=" + reqId + ").");
        LOGGER.trace("Processing HPD Plus request (directoryId=" + dirId + ", requestId=" + reqId + "):\n"
                + this.dirJaxb2Marshaller.marshal(this.hpdPlusObjectFactory.createHpdPlusRequest(hpdPlusReq)));

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

        this.interceptResponses(dirDesc, dirId, reqId, hpdPlusReq, hpdPlusResp);

        LOGGER.debug("Processed HPD Plus request (directoryId=" + dirId + ", requestId=" + reqId + ") into HPD Plus response.");
        LOGGER.trace("Processed HPD Plus request (directoryId=" + dirId + ", requestId=" + reqId + ") into HPD Plus response:\n"
                + this.dirJaxb2Marshaller.marshal(this.hpdPlusObjectFactory.createHpdPlusResponse(hpdPlusResp)));

        return hpdPlusResp;
    }

    @Override
    protected void addError(String dirId, String reqId, HpdPlusResponse hpdPlusResp, Throwable th) {
        // TODO: improve error handling
        hpdPlusResp.getErrors().add(this.errBuilder.buildError(dirId, reqId, HpdPlusErrorType.OTHER, th));
    }

    @Autowired
    @DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
    @DirectoryType(DirectoryTypeId.MAIN)
    @Override
    protected void setDirectoryDescriptor(DirectoryDescriptor dirDesc) {
        this.dirDesc = dirDesc;
    }

    @Autowired
    @DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
    @Override
    protected void setFederationService(FederationService<HpdPlusRequest, HpdPlusResponse> fedService) {
        this.fedService = fedService;
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
    @Override
    protected void setRequestInterceptors(SortedSet<DirectoryRequestInterceptor<HpdPlusRequest>> reqInterceptors) {
        this.reqInterceptors = reqInterceptors;
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
    @Override
    protected void setResponseInterceptors(SortedSet<DirectoryResponseInterceptor<HpdPlusRequest, HpdPlusResponse>> respInterceptors) {
        this.respInterceptors = respInterceptors;
    }
}
