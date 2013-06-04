package gov.hhs.onc.pdti.data.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import java.net.URL;
import org.apache.directory.api.ldap.model.name.Dn;

public class DirectoryDescriptorImpl implements DirectoryDescriptor {
    private String directoryId;
    private URL wsdlLocation;
    private Dn baseDn;

    @Override
    public Dn getBaseDn() {
        return this.baseDn;
    }

    @Override
    public void setBaseDn(Dn baseDn) {
        this.baseDn = baseDn;
    }

    public String getDirectoryId() {
        return this.directoryId;
    }

    public void setDirectoryId(String directoryId) {
        this.directoryId = directoryId;
    }

    @Override
    public URL getWsdlLocation() {
        return this.wsdlLocation;
    }

    @Override
    public void setWsdlLocation(URL wsdlLocation) {
        this.wsdlLocation = wsdlLocation;
    }
}
