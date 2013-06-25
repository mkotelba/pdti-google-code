package gov.hhs.onc.pdti;

public enum DirectoryTypeId {
    MAIN("main"), FEDERATED("federated");

    private String id;

    DirectoryTypeId(String id) {
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
