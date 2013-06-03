package gov.hhs.onc.pdti.error.impl;

import gov.hhs.onc.pdti.error.DirectoryErrorBuilder;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusError;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorDetail;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("errBuilder")
@Scope("singleton")
public class DirectoryErrorBuilderImpl implements DirectoryErrorBuilder {
    @Autowired
    private ObjectFactory objectFactory;

    @Autowired
    private gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory hpdPlusObjectFactory;

    @Override
    public HpdPlusError buildError(String reqId, HpdPlusErrorType errType, Throwable th) {
        HpdPlusError err = this.hpdPlusObjectFactory.createHpdPlusError();
        err.setRequestID(reqId);
        err.setType(errType);
        err.setMessage(th.getMessage());

        HpdPlusErrorDetail errDetail = this.hpdPlusObjectFactory.createHpdPlusErrorDetail();
        errDetail.setAny(DirectoryUtils.getStackTraceJaxbElement(th));
        err.setDetail(errDetail);

        return err;
    }
}
