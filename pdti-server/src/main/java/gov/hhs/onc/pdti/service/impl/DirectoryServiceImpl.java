package gov.hhs.onc.pdti.service.impl;

import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.error.DirectoryErrorBuilder;
import gov.hhs.onc.pdti.jaxb.DirectoryJaxb2Marshaller;
import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.service.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequestMetadata;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponseMetadata;
import java.util.List;
import java.util.SortedSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("dirService")
public class DirectoryServiceImpl implements DirectoryService {
    private final static Logger LOGGER = Logger.getLogger(DirectoryServiceImpl.class);

    @Autowired
    private gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory hpdPlusObjectFactory;

    @Autowired
    private DirectoryJaxb2Marshaller jaxb2Marshaller;

    @Autowired
    private DirectoryErrorBuilder errBuilder;

    @Autowired
    @Qualifier("main")
    private DirectoryDescriptor dirDesc;

    @Autowired(required = false)
    private SortedSet<DirectoryRequestInterceptor> reqInterceptors;

    @Autowired(required = false)
    private SortedSet<DirectoryResponseInterceptor> respInterceptors;

    @Autowired(required = false)
    private List<DirectoryDataService<?>> dataServices;

    @Autowired
    private FederationService federationService;

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
            hpdPlusResp.getResponseItems().addAll(this.federationService.federate(hpdPlusReq));
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
}
