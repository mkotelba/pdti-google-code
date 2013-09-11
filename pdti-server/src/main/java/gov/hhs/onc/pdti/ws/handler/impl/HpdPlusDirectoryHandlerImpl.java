package gov.hhs.onc.pdti.ws.handler.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import gov.hhs.onc.pdti.ws.handler.DirectoryHandler;
import gov.hhs.onc.pdti.ws.handler.DirectoryHandlerException;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalMessageContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("hpdPlusDirHandler")
@DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
@Scope("prototype")
public class HpdPlusDirectoryHandlerImpl extends AbstractDirectoryHandler<HpdPlusRequest, HpdPlusResponse> implements
        DirectoryHandler<HpdPlusRequest, HpdPlusResponse> {
    public HpdPlusDirectoryHandlerImpl() {
        super(HpdPlusRequest.class, HpdPlusResponse.class);
    }

    @Override
    protected <V> String getRequestId(LogicalMessageContext logicalMsgContext, LogicalMessage logicalMsg, Class<V> payloadClass)
            throws DirectoryHandlerException {
        if (HpdPlusRequest.class.isAssignableFrom(payloadClass)) {
            return this.getPayload(logicalMsgContext, logicalMsg, HpdPlusRequest.class).getRequestId();
        } else if (HpdPlusResponse.class.isAssignableFrom(payloadClass)) {
            return this.getPayload(logicalMsgContext, logicalMsg, HpdPlusResponse.class).getRequestId();
        } else {
            return null;
        }
    }
}
