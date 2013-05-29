package gov.hhs.onc.pdti.data.federation;

import java.net.URL;
import org.apache.directory.api.ldap.model.name.Dn;

public interface FederatedDirectory {
    public URL getWsdlLocation();

    public void setWsdlLocation(URL wsdlLocation);

    public Dn getBaseDn();

    public void setBaseDn(Dn baseDn);
}
