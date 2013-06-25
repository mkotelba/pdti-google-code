package gov.hhs.onc.pdti.ws.handler.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.handler.DirectoryHandler;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("dirHandler")
@DirectoryStandard(DirectoryStandardId.IHE)
@Scope("prototype")
public class DirectoryHandlerImpl extends AbstractDirectoryHandler<BatchRequest, BatchResponse> implements DirectoryHandler<BatchRequest, BatchResponse> {
}
