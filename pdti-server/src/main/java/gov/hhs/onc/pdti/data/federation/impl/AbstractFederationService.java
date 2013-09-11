package gov.hhs.onc.pdti.data.federation.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.federation.DirectoryFederationException;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.error.DirectoryErrorBuilder;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.regex.Pattern;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractFederationService<T, U> implements FederationService<T, U> {
    protected final static Pattern DUP_REQ_ID_MSG_PATTERN = Pattern.compile("^.+Duplicate directory request ID.+$", Pattern.DOTALL);

    @Autowired
    protected DirectoryErrorBuilder errBuilder;

    protected List<DirectoryDescriptor> fedDirs;

    protected Set<DirectoryRequestInterceptor<T>> fedReqInterceptors;

    protected Set<DirectoryResponseInterceptor<T, U>> fedRespInterceptors;

    private final static Logger LOGGER = Logger.getLogger(AbstractFederationService.class);

    @Override
    public List<U> federate(T queryReq) throws DirectoryFederationException {
        List<U> queryResps = new ArrayList<>();

        if (this.fedDirs != null) {
            for (DirectoryDescriptor fedDir : this.fedDirs) {
                if (!fedDir.isEnabled()) {
                    LOGGER.trace("Skipping disabled federated directory (directoryId=" + fedDir.getDirectoryId() + ").");
                    continue;
                }

                queryResps.add(this.federate(fedDir, queryReq));
            }
        }

        return queryResps;
    }

    protected void interceptRequests(DirectoryDescriptor fedDir, String fedDirId, String reqId, T fedQueryReq, U fedQueryResp) {
        if (this.fedReqInterceptors != null) {
            for (DirectoryRequestInterceptor fedReqInterceptor : this.fedReqInterceptors) {
                LOGGER.trace("Intercepting federated request (directoryId=" + fedDirId + ", requestId=" + reqId + ", requestClass="
                        + fedQueryReq.getClass().getName() + "): class=" + fedReqInterceptor.getClass().getName());

                try {
                    fedReqInterceptor.interceptRequest(fedDir, reqId, fedQueryReq);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    this.addError(fedDirId, reqId, fedQueryResp, th);
                }
            }
        }
    }

    protected void interceptResponses(DirectoryDescriptor fedDir, String fedDirId, String reqId, T fedQueryReq, U fedQueryResp) {
        if (this.fedRespInterceptors != null) {
            for (DirectoryResponseInterceptor fedRespInterceptor : this.fedRespInterceptors) {
                LOGGER.trace("Intercepting federated response (directoryId=" + fedDirId + ", requestId=" + reqId + ", responseClass="
                        + fedQueryResp.getClass().getName() + "): class=" + fedRespInterceptor.getClass().getName());

                try {
                    fedRespInterceptor.interceptResponse(fedDir, reqId, fedQueryReq, fedQueryResp);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    this.addError(fedDirId, reqId, fedQueryResp, th);
                }
            }
        }
    }

    protected boolean isDuplicateRequestIdSoapFault(SOAPFaultException e) {
        return DUP_REQ_ID_MSG_PATTERN.matcher(e.getMessage()).matches();
    }

    protected abstract void addError(String fedDirId, String reqId, U fedQueryResp, Throwable th);

    protected abstract void setFederatedDirs(List<DirectoryDescriptor> federatedDirs);

    protected abstract void setFederatedRequestInterceptors(SortedSet<DirectoryRequestInterceptor<T>> fedReqInterceptors);

    protected abstract void setFederatedResponseInterceptors(SortedSet<DirectoryResponseInterceptor<T, U>> fedRespInterceptors);
}
