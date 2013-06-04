package gov.hhs.onc.pdti.data.federation;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import java.util.List;

public interface FederationService {
    public List<HpdPlusResponse> federate(HpdPlusRequest hpdPlusReq) throws DirectoryFederationException;

    public HpdPlusResponse federate(DirectoryDescriptor fedDir, HpdPlusRequest hpdPlusReq) throws DirectoryFederationException;
}
