package gov.hhs.onc.pdti.test.interceptor.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.DirectoryType;
import gov.hhs.onc.pdti.DirectoryTypeId;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.interceptor.impl.AbstractDirectoryInterceptor;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("fedLoopTestInterceptor")
@DirectoryStandard(DirectoryStandardId.IHE)
@Order(0)
@Scope("singleton")
public class FederationLoopTestInterceptorImpl extends AbstractDirectoryInterceptor implements DirectoryRequestInterceptor<BatchRequest>,
        DirectoryResponseInterceptor<BatchRequest, BatchResponse> {
    private final static String FED_LOOP_TEST_REQ_ID_PATTERN = "federation_loop_test";

    private final static Logger LOGGER = Logger.getLogger(FederationLoopTestInterceptorImpl.class);

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    @DirectoryType(DirectoryTypeId.FEDERATED)
    private List<DirectoryDescriptor> fedLoopTestDirs;

    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq) throws DirectoryInterceptorException {

    }

    @Override
    public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq, BatchResponse batchResp)
            throws DirectoryInterceptorException {

    }

    private void setFederationLoopTest(String reqId, boolean fedLoopTestEnabled) {
        for (DirectoryDescriptor fedLoopTestDir : this.fedLoopTestDirs) {
            fedLoopTestDir.setEnabled(fedLoopTestEnabled);

            LOGGER.info("Federation loop test (requestId=" + reqId + ") federated directory (directoryId=" + fedLoopTestDir.getDirectoryId() + ") toggled: "
                    + fedLoopTestEnabled);
        }
    }

    private static boolean isFederationLoopTestRequestId(String reqId) {
        return reqId.matches(FED_LOOP_TEST_REQ_ID_PATTERN);
    }
}
