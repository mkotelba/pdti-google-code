package gov.hhs.onc.pdti.service.interceptor.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.service.DirectoryServiceException;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.SearchRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("baseDnInterceptor")
@Order(1)
@Scope("singleton")
public class BaseDnInterceptor extends AbstractDirectoryInterceptor implements DirectoryRequestInterceptor {
    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq) throws DirectoryServiceException {
        this.interceptRequest(dirDesc, reqId, hpdPlusReq.getBatchRequest());
    }

    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq) throws DirectoryServiceException {
        Dn dirBaseDn = dirDesc.getBaseDn();

        for (SearchRequest searchReqMsg : (Collection<SearchRequest>) CollectionUtils.select(batchReq.getBatchRequests(),
                PredicateUtils.instanceofPredicate(SearchRequest.class))) {
            try {
                searchReqMsg.setDn(DirectoryUtils.replaceAncestorDn(new Dn(searchReqMsg.getDn()), dirBaseDn).toString());
            } catch (LdapInvalidDnException e) {
                throw new DirectoryServiceException("Unable to target DSML search request at directory (id=" + dirDesc.getDirectoryId()
                        + ") Distinguished Name: " + dirBaseDn.getName(), e);
            }
        }
    }
}
