package gov.hhs.onc.pdti.ws.handler;

import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;

public interface DirectoryHandler<T, U> extends LogicalHandler<LogicalMessageContext> {
}
