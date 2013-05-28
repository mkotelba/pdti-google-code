package gov.hhs.onc.pdti.service;

import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;

public interface DirectoryService {
    public HpdPlusResponse processRequest(HpdPlusRequest hpdPlusReq);
}
