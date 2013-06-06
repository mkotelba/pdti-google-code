package gov.hhs.onc.pdti.data.federation.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.federation.DirectoryFederationException;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.error.DirectoryErrorBuilder;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.service.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusProviderInformationDirectoryService;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("federationService")
public class FederationServiceImpl implements FederationService {
    private final static Logger LOGGER = Logger.getLogger(FederationServiceImpl.class);

    @Autowired
    private gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory hpdPlusObjectFactory;

    @Autowired(required = false)
    @Qualifier("federated")
    private List<DirectoryDescriptor> federatedDirs;

    @Autowired(required = false)
    private SortedSet<DirectoryRequestInterceptor> reqInterceptors;

    @Autowired(required = false)
    private SortedSet<DirectoryResponseInterceptor> respInterceptors;

    @Autowired
    private DirectoryErrorBuilder errBuilder;

    @Override
    public List<HpdPlusResponse> federate(HpdPlusRequest hpdPlusReq) throws DirectoryFederationException {
        List<HpdPlusResponse> hpdPlusResps = new ArrayList<>();

        if (this.federatedDirs != null) {
            for (DirectoryDescriptor fedDir : this.federatedDirs) {
                hpdPlusResps.add(this.federate(fedDir, hpdPlusReq));
            }
        }

        return hpdPlusResps;
    }

    @Override
    public HpdPlusResponse federate(DirectoryDescriptor fedDir, HpdPlusRequest hpdPlusReq) throws DirectoryFederationException {
        String fedDirId = fedDir.getDirectoryId(), reqId = hpdPlusReq.getRequestId();
        HpdPlusRequest fedHpdPlusReq = (HpdPlusRequest) hpdPlusReq.clone();
        HpdPlusResponse fedHpdPlusResp = this.hpdPlusObjectFactory.createHpdPlusResponse();

        if (this.reqInterceptors != null) {
            for (DirectoryRequestInterceptor reqInterceptor : this.reqInterceptors) {
                LOGGER.trace("Intercepting federated directory (id=" + fedDirId + ") HPD Plus request (class=" + reqInterceptor.getClass().getName() + ").");

                try {
                    reqInterceptor.interceptRequest(fedDir, fedHpdPlusReq);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    fedHpdPlusResp.getErrors().add(this.errBuilder.buildError(fedDirId, reqId, HpdPlusErrorType.OTHER, th));
                }
            }
        }

        try {
            HpdPlusProviderInformationDirectoryService fedDirService = new HpdPlusProviderInformationDirectoryService(fedDir.getWsdlLocation());

            fedHpdPlusResp = fedDirService.getHpdPlusProviderInformationDirectoryPortSoap().hpdPlusProviderInformationQueryRequest(fedHpdPlusReq);
        } catch (Throwable th) {
            // TODO: improve error handling
            fedHpdPlusResp.getErrors()
                    .add(this.errBuilder.buildError(fedHpdPlusReq.getDirectoryId(), fedHpdPlusReq.getRequestId(), HpdPlusErrorType.OTHER, th));
        }

        if (this.respInterceptors != null) {
            for (DirectoryResponseInterceptor respInterceptor : this.respInterceptors) {
                LOGGER.trace("Intercepting federated directory (id=" + fedDirId + ") HPD Plus response (class=" + respInterceptor.getClass().getName() + ").");

                try {
                    respInterceptor.interceptResponse(fedDir, fedHpdPlusReq, fedHpdPlusResp);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    fedHpdPlusResp.getErrors().add(this.errBuilder.buildError(fedDirId, reqId, HpdPlusErrorType.OTHER, th));
                }
            }
        }

        return fedHpdPlusResp;
    }
}
