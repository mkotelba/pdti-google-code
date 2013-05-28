package gov.hhs.onc.pdti.data.impl;

import gov.hhs.onc.pdti.data.DirectoryDataException;
import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.data.DirectoryDataSource;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.DsmlMessage;
import gov.hhs.onc.pdti.ws.api.ErrorResponse;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.Detail;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.ErrorType;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import gov.hhs.onc.pdti.ws.api.SearchRequest;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDataService<T extends DirectoryDataSource> implements DirectoryDataService<T> {
    private final static Class<? extends DsmlMessage>[] VALID_REQ_MSG_CLASSES = ArrayUtils.toArray(SearchRequest.class);

    private final static Logger LOGGER = Logger.getLogger(AbstractDataService.class);

    @Autowired
    protected ObjectFactory objectFactory;

    protected List<T> dataSources;

    @Override
    public List<BatchResponse> processData(BatchRequest batchReq) throws DirectoryDataException {
        String reqId = batchReq.getRequestID();
        List<BatchResponse> batchResps = new ArrayList<>();
        BatchResponse batchResp;

        for (T dataSource : this.dataSources) {
            batchResp = this.objectFactory.createBatchResponse();

            try {
                validateRequest(batchReq);

                batchResp = this.processData(dataSource, batchReq);
            } catch (Throwable th) {
                // TODO: improve error handling
                LOGGER.error(th);

                batchResp.getBatchResponses().add(
                        this.objectFactory.createBatchResponseErrorResponse(this.buildErrorResponse(reqId,
                                ErrorType.OTHER, th)));
            }

            batchResp.setRequestID(reqId);
            batchResps.add(batchResp);
        }

        return batchResps;
    }

    private static void validateRequest(BatchRequest batchReq) throws DirectoryDataException {
        Class<? extends DsmlMessage> batchReqMsgClass;

        for (DsmlMessage batchReqMsg : batchReq.getBatchRequests()) {
            batchReqMsgClass = batchReqMsg.getClass();

            if (!ClassUtils.isAssignable(new Class<?>[] { batchReqMsgClass }, VALID_REQ_MSG_CLASSES)) {
                throw new DirectoryDataException("Invalid DSML batch request message type (class="
                        + batchReqMsgClass.getName() + ").");
            }
        }
    }

    private ErrorResponse buildErrorResponse(String reqId, ErrorType errType, Throwable th) {
        ErrorResponse errResp = this.objectFactory.createErrorResponse();
        errResp.setRequestID(reqId);
        errResp.setType(errType);
        errResp.setMessage(th.getMessage());

        Detail errRespDetail = this.objectFactory.createErrorResponseDetail();
        errRespDetail.setAny(DirectoryUtils.getStackTraceJaxbElement(th));
        errResp.setDetail(errRespDetail);

        return errResp;
    }

    public abstract void setDataSources(List<T> dataSources);
}
