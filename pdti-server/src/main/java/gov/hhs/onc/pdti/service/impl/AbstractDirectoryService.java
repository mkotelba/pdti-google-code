package gov.hhs.onc.pdti.service.impl;

import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.error.DirectoryErrorBuilder;
import gov.hhs.onc.pdti.jaxb.DirectoryJaxb2Marshaller;
import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.service.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.service.interceptor.DirectoryResponseInterceptor;
import java.util.List;
import java.util.SortedSet;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDirectoryService<T, U> implements DirectoryService<T, U> {
    @Autowired
    protected DirectoryJaxb2Marshaller jaxb2Marshaller;

    @Autowired
    protected DirectoryErrorBuilder errBuilder;

    @Autowired(required = false)
    protected SortedSet<DirectoryRequestInterceptor> reqInterceptors;

    @Autowired(required = false)
    protected SortedSet<DirectoryResponseInterceptor> respInterceptors;

    @Autowired(required = false)
    protected List<DirectoryDataService<?>> dataServices;

    protected DirectoryDescriptor dirDesc;

    protected FederationService<T, U> fedService;

    protected abstract void setDirectoryDescriptor(DirectoryDescriptor dirDesc);

    protected abstract void setFederationService(FederationService<T, U> fedService);
}
