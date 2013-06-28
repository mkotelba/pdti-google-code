package gov.hhs.onc.pdti.ws.handler.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.handler.DirectoryHandler;
import gov.hhs.onc.pdti.ws.handler.DirectoryHandlerException;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalMessageContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("dirHandler")
@DirectoryStandard(DirectoryStandardId.IHE)
@Scope("prototype")
public class DirectoryHandlerImpl extends AbstractDirectoryHandler<BatchRequest, BatchResponse> implements DirectoryHandler<BatchRequest, BatchResponse> {
    public DirectoryHandlerImpl() {
        super(BatchRequest.class, BatchResponse.class);
    }

    @Override
    protected <V> String getRequestId(LogicalMessageContext logicalMsgContext, LogicalMessage logicalMsg, Class<V> payloadClass)
            throws DirectoryHandlerException {
        if (BatchRequest.class.isAssignableFrom(payloadClass)) {
            return this.getPayload(logicalMsgContext, logicalMsg, BatchRequest.class).getRequestId();
        } else if (BatchResponse.class.isAssignableFrom(payloadClass)) {
            return this.getPayload(logicalMsgContext, logicalMsg, BatchResponse.class).getRequestId();
        } else {
            return null;
        }
    }
}
