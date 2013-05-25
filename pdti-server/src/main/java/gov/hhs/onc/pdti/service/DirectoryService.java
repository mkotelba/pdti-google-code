package gov.hhs.onc.pdti.service;

import gov.hhs.onc.pdti.ws.api.HpdRequest;
import gov.hhs.onc.pdti.ws.api.HpdResponse;

public interface DirectoryService {
    public HpdResponse processRequest(HpdRequest hpdReq);
}
