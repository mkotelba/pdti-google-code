package gov.hhs.onc.pdti;

public enum DirectoryStandardId {
    IHE("ihe"), HPD_PLUS_PROPOSED("hpd_plus_proposed");

    private String id;

    DirectoryStandardId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id;
    }

    public String getId() {
        return this.id;
    }
}
