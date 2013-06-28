package gov.hhs.onc.pdti.test.interceptor.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.DirectoryType;
import gov.hhs.onc.pdti.DirectoryTypeId;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.test.DirectoryTestType;
import gov.hhs.onc.pdti.test.DirectoryTestTypeId;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("fedLoopTestInterceptor")
@DirectoryStandard(DirectoryStandardId.IHE)
@Order(0)
@Scope("singleton")
public class FederationLoopTestInterceptorImpl extends AbstractFederationLoopTestInterceptor<BatchRequest, BatchResponse> {
    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq) throws DirectoryInterceptorException {
        this.setFederationLoopTest(reqId, true);
    }

    @Override
    public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq, BatchResponse batchResp)
            throws DirectoryInterceptorException {
        this.setFederationLoopTest(reqId, false);
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.IHE)
    @DirectoryType(DirectoryTypeId.FEDERATED)
    @DirectoryTestType(DirectoryTestTypeId.FEDERATION_LOOP)
    @Override
    protected void setFederationLoopTestDirectories(List<DirectoryDescriptor> fedLoopTestDirs) {
        this.fedLoopTestDirs = fedLoopTestDirs;
    }
}
