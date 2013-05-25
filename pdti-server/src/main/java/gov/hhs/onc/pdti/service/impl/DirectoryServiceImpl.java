package gov.hhs.onc.pdti.service.impl;

import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.HpdError;
import gov.hhs.onc.pdti.ws.api.HpdErrorDetail;
import gov.hhs.onc.pdti.ws.api.HpdErrorType;
import gov.hhs.onc.pdti.ws.api.HpdRequest;
import gov.hhs.onc.pdti.ws.api.HpdRequestMetadata;
import gov.hhs.onc.pdti.ws.api.HpdResponse;
import gov.hhs.onc.pdti.ws.api.HpdResponseMetadata;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
    private List<DirectoryDataService<?>> dataServices;

    @Override
    public HpdResponse processRequest(HpdRequest hpdReq) {
        HpdResponse hpdResp = this.objectFactory.createHpdResponse();

        HpdRequestMetadata reqMeta = ObjectUtils.defaultIfNull(hpdReq.getHpdRequestMetadata(),
                this.objectFactory.createHpdRequestMetadata());

        HpdResponseMetadata respMeta = this.objectFactory.createHpdResponseMetadata();
        respMeta.setProperties(reqMeta.getProperties());
        hpdResp.setHpdResponseMetadata(respMeta);

        BatchRequest batchReq = hpdReq.getBatchRequest();

        for (DirectoryDataService<?> dataService : this.dataServices) {
            try {
                hpdResp.getHpdResponseItems().addAll(dataService.processData(batchReq));
            } catch (Throwable th) {
                // TODO: improve error handling
                LOGGER.error(th);

                hpdResp.getHpdErrors().add(this.buildError(batchReq.getRequestID(), HpdErrorType.OTHER, th));
            }
        }

        return hpdResp;
    }

    private HpdError buildError(String reqId, HpdErrorType errType, Throwable th) {
        HpdError err = this.objectFactory.createHpdError();
        err.setRequestID(reqId);
        err.setType(errType);
        err.setMessage(th.getMessage());

        HpdErrorDetail errDetail = this.objectFactory.createHpdErrorDetail();
        errDetail.setAny(ExceptionUtils.getStackTrace(th));
        err.setDetail(errDetail);

        return err;
    }
}
