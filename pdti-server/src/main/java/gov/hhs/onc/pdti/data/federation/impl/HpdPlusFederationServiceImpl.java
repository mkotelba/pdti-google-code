package gov.hhs.onc.pdti.data.federation.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.DirectoryType;
import gov.hhs.onc.pdti.data.federation.DirectoryFederationException;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.service.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.springframework.beans.factory.annotation.DirectoryTypeQualifier;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusProviderInformationDirectoryService;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@DirectoryTypeQualifier(DirectoryType.HPD_PLUS_PROPOSED)
@Scope("singleton")
@Service("hpdPlusFederationService")
public class HpdPlusFederationServiceImpl extends AbstractFederationService<HpdPlusRequest, HpdPlusResponse> implements
        FederationService<HpdPlusRequest, HpdPlusResponse> {
    private final static Logger LOGGER = Logger.getLogger(HpdPlusFederationServiceImpl.class);

    @Autowired
    private ObjectFactory hpdPlusObjectFactory;

    @Override
    public HpdPlusResponse federate(DirectoryDescriptor fedDir, HpdPlusRequest hpdPlusReq) throws DirectoryFederationException {
        String fedDirId = fedDir.getDirectoryId(), reqId = hpdPlusReq.getRequestId();
        HpdPlusRequest fedHpdPlusReq = (HpdPlusRequest) hpdPlusReq.clone();
        HpdPlusResponse fedHpdPlusResp = this.hpdPlusObjectFactory.createHpdPlusResponse();

        if (this.reqInterceptors != null) {
            for (DirectoryRequestInterceptor reqInterceptor : this.reqInterceptors) {
                LOGGER.trace("Intercepting federated HPD Plus request (directoryId=" + fedDirId + ", requestId=" + reqId + ", class="
                        + reqInterceptor.getClass().getName() + ").");

                try {
                    reqInterceptor.interceptRequest(fedDir, reqId, fedHpdPlusReq);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    fedHpdPlusResp.getErrors().add(this.errBuilder.buildError(fedDirId, reqId, HpdPlusErrorType.OTHER, th));
                }
            }
        }

        try {
            HpdPlusProviderInformationDirectoryService fedHpdPlusDirService = new HpdPlusProviderInformationDirectoryService(fedDir.getWsdlLocation());

            fedHpdPlusResp = fedHpdPlusDirService.getHpdPlusProviderInformationDirectoryPortSoap().hpdPlusProviderInformationQueryRequest(fedHpdPlusReq);
        } catch (Throwable th) {
            // TODO: improve error handling
            fedHpdPlusResp.getErrors()
                    .add(this.errBuilder.buildError(fedHpdPlusReq.getDirectoryId(), fedHpdPlusReq.getRequestId(), HpdPlusErrorType.OTHER, th));
        }

        if (this.respInterceptors != null) {
            for (DirectoryResponseInterceptor respInterceptor : this.respInterceptors) {
                LOGGER.trace("Intercepting federated HPD Plus response (directoryId=" + fedDirId + ", requestId=" + reqId + ", class="
                        + respInterceptor.getClass().getName() + ").");

                try {
                    respInterceptor.interceptResponse(fedDir, reqId, fedHpdPlusReq, fedHpdPlusResp);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    fedHpdPlusResp.getErrors().add(this.errBuilder.buildError(fedDirId, reqId, HpdPlusErrorType.OTHER, th));
                }
            }
        }

        return fedHpdPlusResp;
    }

    @Autowired(required = false)
    @DirectoryTypeQualifier(DirectoryType.HPD_PLUS_PROPOSED)
    @Qualifier("federated")
    @Override
    protected void setFederatedDirs(List<DirectoryDescriptor> federatedDirs) {
        this.federatedDirs = federatedDirs;
    }
}
