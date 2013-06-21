package gov.hhs.onc.pdti.data.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import java.net.URL;
import org.apache.directory.api.ldap.model.name.Dn;

public class DirectoryDescriptorImpl implements DirectoryDescriptor {
    private String dirId;
    private URL wsdlLoc;
    private Dn baseDn;

    @Override
    public Dn getBaseDn() {
        return this.baseDn;
    }

    @Override
    public void setBaseDn(Dn baseDn) {
        this.baseDn = baseDn;
    }

    @Override
    public String getDirectoryId() {
        return this.dirId;
    }

    @Override
    public void setDirectoryId(String dirId) {
        this.dirId = dirId;
    }

    @Override
    public URL getWsdlLocation() {
        return this.wsdlLoc;
    }

    @Override
    public void setWsdlLocation(URL wsdlLoc) {
        this.wsdlLoc = wsdlLoc;
    }
}
