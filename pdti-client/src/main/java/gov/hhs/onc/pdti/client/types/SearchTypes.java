package gov.hhs.onc.pdti.client.types;

public enum SearchTypes {

    PROVIDERS("Providers"), ORGANIZATIONS("Organizations"), CREDENTIALS("Credentials"), MEMBERSHIPS("Memberships"),
        SERVICES("Services");

    private String name;

    private SearchTypes(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

}
