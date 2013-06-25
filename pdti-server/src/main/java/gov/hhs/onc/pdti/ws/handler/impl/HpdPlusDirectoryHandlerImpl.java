package gov.hhs.onc.pdti.ws.handler.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import gov.hhs.onc.pdti.ws.handler.DirectoryHandler;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("hpdPlusDirHandler")
@DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
@Scope("prototype")
public class HpdPlusDirectoryHandlerImpl extends AbstractDirectoryHandler<HpdPlusRequest, HpdPlusResponse> implements
        DirectoryHandler<HpdPlusRequest, HpdPlusResponse> {
}
