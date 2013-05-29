package gov.hhs.onc.pdti.service.impl;

import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusError;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorDetail;
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

    @Autowired(required = false)
    private List<DirectoryDataService<?>> dataServices;

    @Autowired
    private FederationService federationService;

    @Override
    public HpdPlusResponse processRequest(HpdPlusRequest hpdPlusReq) {
        String reqId = hpdPlusReq.getRequestID();

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
                    LOGGER.error(th);

                    hpdPlusResp.getErrors().add(this.buildError(reqId, HpdPlusErrorType.OTHER, th));
                }
            }
        }

        try {
            hpdPlusResp.getResponseItems().addAll(this.federationService.federate(hpdPlusReq));
        } catch (Throwable th) {
            // TODO: improve error handling
            LOGGER.error(th);

            hpdPlusResp.getErrors().add(this.buildError(reqId, HpdPlusErrorType.OTHER, th));
        }

        return hpdPlusResp;
    }

    private HpdPlusError buildError(String reqId, HpdPlusErrorType errType, Throwable th) {
        HpdPlusError err = this.hpdPlusObjectFactory.createHpdPlusError();
        err.setRequestID(reqId);
        err.setType(errType);
        err.setMessage(th.getMessage());

        HpdPlusErrorDetail errDetail = this.hpdPlusObjectFactory.createHpdPlusErrorDetail();
        errDetail.setAny(DirectoryUtils.getStackTraceJaxbElement(th));
        err.setDetail(errDetail);

        return err;
    }
}
