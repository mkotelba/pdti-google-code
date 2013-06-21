package gov.hhs.onc.pdti.data;

public enum DirectoryType {
    IHE("ihe"), HPD_PLUS_PROPOSED("hpd_plus_proposed");

    private String type;

    DirectoryType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public String getType() {
        return this.type;
    }
}
