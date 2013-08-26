package gov.hhs.onc.pdti.data.federation.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.DirectoryType;
import gov.hhs.onc.pdti.DirectoryTypeId;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.federation.DirectoryFederationException;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusError;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusProviderInformationDirectoryService;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory;
import java.util.List;
import java.util.SortedSet;
import javax.xml.ws.soap.SOAPFaultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
@Scope("singleton")
@Service("hpdPlusFederationService")
public class HpdPlusFederationServiceImpl extends AbstractFederationService<HpdPlusRequest, HpdPlusResponse> implements
        FederationService<HpdPlusRequest, HpdPlusResponse> {
    @Autowired
    @DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
    private ObjectFactory hpdPlusObjectFactory;

    @Override
    public HpdPlusResponse federate(DirectoryDescriptor fedDir, HpdPlusRequest hpdPlusReq) throws DirectoryFederationException {
        String fedDirId = fedDir.getDirectoryId(), reqId = hpdPlusReq.getRequestId();
        HpdPlusRequest fedHpdPlusReq = (HpdPlusRequest) hpdPlusReq.clone();
        HpdPlusResponse fedHpdPlusResp = this.hpdPlusObjectFactory.createHpdPlusResponse();

        this.interceptRequests(fedDir, fedDirId, reqId, fedHpdPlusReq, fedHpdPlusResp);

        try {
            HpdPlusProviderInformationDirectoryService fedHpdPlusDirService = new HpdPlusProviderInformationDirectoryService(fedDir.getWsdlLocation());

            fedHpdPlusResp = fedHpdPlusDirService.getHpdPlusProviderInformationDirectoryPortSoap().hpdPlusProviderInformationQueryRequest(fedHpdPlusReq);
        } catch (Throwable th) {
            this.addError(fedDirId, reqId, fedHpdPlusResp, th);
        }

        this.interceptResponses(fedDir, fedDirId, reqId, fedHpdPlusReq, fedHpdPlusResp);

        return fedHpdPlusResp;
    }

    // TODO: improve error handling
    @Override
    protected void addError(String fedDirId, String reqId, HpdPlusResponse fedHpdPlusResp, Throwable th) {
        Class<? extends Throwable> thClass = th.getClass();
        HpdPlusError hpdPlusErr = null;

        if (SOAPFaultException.class.isAssignableFrom(thClass)) {
            if (this.isDuplicateRequestIdSoapFault((SOAPFaultException) th)) {
                hpdPlusErr = this.errBuilder.buildError(fedDirId, reqId, HpdPlusErrorType.DUPLICATE_REQUEST_ID, th);
            }
        }

        if (hpdPlusErr == null) {
            hpdPlusErr = this.errBuilder.buildError(fedDirId, reqId, HpdPlusErrorType.OTHER, th);
        }

        fedHpdPlusResp.getErrors().add(hpdPlusErr);
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
    @DirectoryType(DirectoryTypeId.FEDERATED)
    @Override
    protected void setFederatedDirs(List<DirectoryDescriptor> fedDirs) {
        this.fedDirs = fedDirs;
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
    @Override
    protected void setFederatedRequestInterceptors(SortedSet<DirectoryRequestInterceptor<HpdPlusRequest>> fedReqInterceptors) {
        this.fedReqInterceptors = fedReqInterceptors;
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
    @Override
    protected void setFederatedResponseInterceptors(SortedSet<DirectoryResponseInterceptor<HpdPlusRequest, HpdPlusResponse>> fedRespInterceptors) {
        this.fedRespInterceptors = fedRespInterceptors;
    }
}
