package gov.hhs.onc.pdti.data.federation.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.federation.DirectoryFederationException;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.error.DirectoryErrorBuilder;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.service.interceptor.DirectoryResponseInterceptor;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractFederationService<T, U> implements FederationService<T, U> {
    @Autowired(required = false)
    protected SortedSet<DirectoryRequestInterceptor> reqInterceptors;

    @Autowired(required = false)
    protected SortedSet<DirectoryResponseInterceptor> respInterceptors;

    @Autowired
    protected DirectoryErrorBuilder errBuilder;

    protected List<DirectoryDescriptor> federatedDirs;

    @Override
    public List<U> federate(T queryReq) throws DirectoryFederationException {
        List<U> queryResps = new ArrayList<>();

        if (this.federatedDirs != null) {
            for (DirectoryDescriptor fedDir : this.federatedDirs) {
                queryResps.add(this.federate(fedDir, queryReq));
            }
        }

        return queryResps;
    }

    protected abstract void setFederatedDirs(List<DirectoryDescriptor> federatedDirs);
}
