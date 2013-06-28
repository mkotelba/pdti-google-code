package gov.hhs.onc.pdti.test.interceptor.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.interceptor.impl.AbstractDirectoryInterceptor;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class AbstractFederationLoopTestInterceptor<T, U> extends AbstractDirectoryInterceptor implements DirectoryRequestInterceptor<T>,
        DirectoryResponseInterceptor<T, U> {
    protected final static String FED_LOOP_TEST_REQ_ID_PATTERN = "^.*_federation_loop_test_.*$";

    protected final static Logger LOGGER = Logger.getLogger(AbstractFederationLoopTestInterceptor.class);

    protected List<DirectoryDescriptor> fedLoopTestDirs;

    protected void setFederationLoopTest(String reqId, boolean fedLoopTestEnabled) {
        if (this.fedLoopTestDirs != null) {
            for (DirectoryDescriptor fedLoopTestDir : this.fedLoopTestDirs) {
                fedLoopTestDir.setEnabled(fedLoopTestEnabled);

                LOGGER.info("Federation loop test (requestId=" + reqId + ") federated directory (directoryId=" + fedLoopTestDir.getDirectoryId()
                        + ") toggled: " + fedLoopTestEnabled);
            }
        }
    }

    protected static boolean isFederationLoopTestRequestId(String reqId) {
        return reqId.matches(FED_LOOP_TEST_REQ_ID_PATTERN);
    }

    protected abstract void setFederationLoopTestDirectories(List<DirectoryDescriptor> fedLoopTestDirs);
}
