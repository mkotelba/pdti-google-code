package gov.hhs.onc.pdti.data.federation.impl;

import gov.hhs.onc.pdti.data.federation.FederatedDirectory;
import java.net.URL;
import org.apache.directory.api.ldap.model.name.Dn;

public class FederatedDirectoryImpl implements FederatedDirectory {
    private URL wsdlLocation;
    private Dn baseDn;

    @Override
    public Dn getBaseDn()
    {
        return this.baseDn;
    }

    @Override
    public void setBaseDn(Dn baseDn)
    {
        this.baseDn = baseDn;
    }

    @Override
    public URL getWsdlLocation()
    {
        return this.wsdlLocation;
    }

    @Override
    public void setWsdlLocation(URL wsdlLocation)
    {
        this.wsdlLocation = wsdlLocation;
    }
}
