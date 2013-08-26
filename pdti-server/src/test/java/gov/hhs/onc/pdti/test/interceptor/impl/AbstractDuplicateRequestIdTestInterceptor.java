package gov.hhs.onc.pdti.test.interceptor.impl;


import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.interceptor.impl.AbstractDirectoryInterceptor;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class AbstractDuplicateRequestIdTestInterceptor<T, U> extends AbstractDirectoryInterceptor implements DirectoryRequestInterceptor<T>,
        DirectoryResponseInterceptor<T, U> {
    protected final static String DUP_REQ_ID_TEST_REQ_ID_PATTERN = "^.*_dup_req_id_\\w*test_.*$";

    protected final static Logger LOGGER = Logger.getLogger(AbstractDuplicateRequestIdTestInterceptor.class);

    protected List<DirectoryDescriptor> dupReqIdTestDirs;

    protected void setDuplicateRequestIdTest(String reqId, boolean dupReqIdTestEnabled) {
        if (this.dupReqIdTestDirs != null) {
            for (DirectoryDescriptor dupReqIdTestDir : this.dupReqIdTestDirs) {
                dupReqIdTestDir.setEnabled(dupReqIdTestEnabled);

                LOGGER.info("Duplicate request ID test (requestId=" + reqId + ") federated directory (directoryId=" + dupReqIdTestDir.getDirectoryId()
                        + ") toggled: " + dupReqIdTestEnabled);
            }
        }
    }

    protected static boolean isDuplicateRequestIdTestRequestId(String reqId) {
        return reqId.matches(DUP_REQ_ID_TEST_REQ_ID_PATTERN);
    }

    protected abstract void setDuplicateRequestIdTestDirectories(List<DirectoryDescriptor> dupReqIdTestDirs);
}
