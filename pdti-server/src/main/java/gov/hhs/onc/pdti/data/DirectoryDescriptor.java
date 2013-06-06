package gov.hhs.onc.pdti.data;

import java.net.URL;
import org.apache.directory.api.ldap.model.name.Dn;

public interface DirectoryDescriptor {
    public String getDirectoryId();

    public void setDirectoryId(String directoryId);

    public URL getWsdlLocation();

    public void setWsdlLocation(URL wsdlLocation);

    public Dn getBaseDn();

    public void setBaseDn(Dn baseDn);
}
