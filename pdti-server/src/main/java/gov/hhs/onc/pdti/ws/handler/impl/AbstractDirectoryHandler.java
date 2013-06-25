package gov.hhs.onc.pdti.ws.handler.impl;

import gov.hhs.onc.pdti.jaxb.DirectoryJaxb2Marshaller;
import gov.hhs.onc.pdti.ws.handler.DirectoryHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDirectoryHandler<T, U> implements DirectoryHandler<T, U> {
    @Autowired
    protected DirectoryJaxb2Marshaller dirJaxb2Marshaller;

    private final static Logger LOGGER = Logger.getLogger(AbstractDirectoryHandler.class);

    @Override
    public boolean handleMessage(LogicalMessageContext logicalMsgContext) {
        return true;
    }

    @Override
    public boolean handleFault(LogicalMessageContext logicalMsgContext) {
        return true;
    }

    @Override
    public void close(MessageContext msgContext) {
    }
}
