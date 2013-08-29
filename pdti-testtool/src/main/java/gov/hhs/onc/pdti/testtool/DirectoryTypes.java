package gov.hhs.onc.pdti.testtool;

public enum DirectoryTypes {

    IHE("IHE WSDL"), MSPD("MSPD WSDL");
    
    private String name;
    
    private DirectoryTypes(final String name) {
        this.name = name;
    }
    
    public String toString() {
      return name;
  }
    
}
