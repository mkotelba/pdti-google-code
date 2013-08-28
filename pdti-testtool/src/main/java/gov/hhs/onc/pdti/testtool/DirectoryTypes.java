package gov.hhs.onc.pdti.testtool;

public enum DirectoryTypes {

    IHE_IWG("IHE-IWG DSML-Based"), MSPD("Mod Specs HPDPlus");
    
    private String name;
    
    private DirectoryTypes(final String name) {
        this.name = name;
    }
    
    public String toString() {
      return name;
  }
    
}
