package gov.hhs.onc.pdti.service.impl;

import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.error.DirectoryErrorBuilder;
import gov.hhs.onc.pdti.jaxb.DirectoryJaxb2Marshaller;
import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequestMetadata;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponseMetadata;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("dirService")
public class DirectoryServiceImpl implements DirectoryService {
    private final static Logger LOGGER = Logger.getLogger(DirectoryServiceImpl.class);

    @Autowired
    private ObjectFactory objectFactory;

    @Autowired
    private gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory hpdPlusObjectFactory;

    @Autowired
    private DirectoryJaxb2Marshaller jaxb2Marshaller;

    @Autowired
    private DirectoryErrorBuilder errBuilder;

    @Autowired(required = false)
    private List<DirectoryDataService<?>> dataServices;

    @Autowired
    private FederationService federationService;

    @Override
    public HpdPlusResponse processRequest(HpdPlusRequest hpdPlusReq) {
        String reqId = hpdPlusReq.getRequestID();

        LOGGER.debug("Processing HPD Plus request (id=" + reqId + ").");
        LOGGER.trace("Processing HPD Plus request (id=" + reqId + "):\n"
                + this.jaxb2Marshaller.marshal(this.hpdPlusObjectFactory.createHpdPlusRequest(hpdPlusReq)));

        BatchRequest batchReq = hpdPlusReq.getBatchRequest();
        batchReq.setRequestID(reqId);

        HpdPlusRequestMetadata reqMeta = hpdPlusReq.getRequestMetadata();

        HpdPlusResponse hpdPlusResp = this.hpdPlusObjectFactory.createHpdPlusResponse();
        hpdPlusResp.setRequestID(reqId);

        HpdPlusResponseMetadata respMeta = this.hpdPlusObjectFactory.createHpdPlusResponseMetadata();
        respMeta.setRequestMetadata(reqMeta);
        hpdPlusResp.setResponseMetadata(respMeta);

        if (this.dataServices != null) {
            for (DirectoryDataService<?> dataService : this.dataServices) {
                try {
                    hpdPlusResp.getResponseItems().addAll(dataService.processData(batchReq));
                } catch (Throwable th) {
                    // TODO: improve error handling
                    hpdPlusResp.getErrors().add(this.errBuilder.buildError(reqId, HpdPlusErrorType.OTHER, th));
                }
            }
        }

        try {
            hpdPlusResp.getResponseItems().addAll(this.federationService.federate(hpdPlusReq));
        } catch (Throwable th) {
            // TODO: improve error handling
            hpdPlusResp.getErrors().add(this.errBuilder.buildError(reqId, HpdPlusErrorType.OTHER, th));
        }

        LOGGER.debug("Processed HPD Plus request (id=" + reqId + ") into HPD Plus response.");
        LOGGER.trace("Processed HPD Plus request (id=" + reqId + ") into HPD Plus response:\n"
                + this.jaxb2Marshaller.marshal(this.hpdPlusObjectFactory.createHpdPlusResponse(hpdPlusResp)));

        return hpdPlusResp;
    }
}
