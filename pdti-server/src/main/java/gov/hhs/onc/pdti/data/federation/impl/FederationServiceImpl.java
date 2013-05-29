package gov.hhs.onc.pdti.data.federation.impl;

import gov.hhs.onc.pdti.data.federation.DirectoryFederationException;
import gov.hhs.onc.pdti.data.federation.FederatedDirectory;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import gov.hhs.onc.pdti.ws.api.SearchRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusProviderInformationDirectoryService;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service("federationService")
public class FederationServiceImpl implements FederationService {
    private final static Logger LOGGER = Logger.getLogger(FederationServiceImpl.class);

    @Autowired
    private ObjectFactory objectFactory;

    @Autowired
    private gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory hpdPlusObjectFactory;

    @Autowired(required = false)
    private List<FederatedDirectory> federatedDirs;

    @Override
    public List<HpdPlusResponse> federate(HpdPlusRequest hpdPlusReq) throws DirectoryFederationException {
        List<HpdPlusResponse> hpdPlusResps = new ArrayList<>();

        if (this.federatedDirs != null) {
            for (FederatedDirectory fedDir : this.federatedDirs) {
                hpdPlusResps.add(this.federate(fedDir, hpdPlusReq));
            }
        }

        return hpdPlusResps;
    }

    @Override
    public HpdPlusResponse federate(FederatedDirectory fedDir, HpdPlusRequest hpdPlusReq)
            throws DirectoryFederationException {
        HpdPlusRequest fedHpdPlusReq = (HpdPlusRequest) hpdPlusReq.clone();
        Dn fedDirBaseDn = fedDir.getBaseDn();

        targetSearchRequests(fedDirBaseDn, (Iterable<SearchRequest>) CollectionUtils.select(fedHpdPlusReq
                .getBatchRequest().getBatchRequests(), PredicateUtils.instanceofPredicate(SearchRequest.class)));

        HpdPlusResponse fedHpdPlusResp = this.hpdPlusObjectFactory.createHpdPlusResponse();

        try {
            HpdPlusProviderInformationDirectoryService fedDirService = new HpdPlusProviderInformationDirectoryService(
                    fedDir.getWsdlLocation());

            fedHpdPlusResp = fedDirService.getHpdPlusProviderInformationDirectoryPortSoap()
                    .hpdPlusProviderInformationQueryRequest(fedHpdPlusReq);
        } catch (Throwable th) {
            // TODO: improve error handling
            throw new DirectoryFederationException(th);
        }

        fedHpdPlusResp.setRequestID(fedHpdPlusReq.getRequestID());

        return fedHpdPlusResp;
    }

    private static void targetSearchRequests(Dn fedDirBaseDn, Iterable<SearchRequest> searchReqs)
            throws DirectoryFederationException {
        for (SearchRequest searchReq : searchReqs) {
            try {
                searchReq.setDn(DirectoryUtils.replaceAncestorDn(new Dn(searchReq.getDn()), fedDirBaseDn).toString());
            } catch (LdapInvalidDnException e) {
                // TODO: improve error handling
                throw new DirectoryFederationException(e);
            }
        }
    }
}
