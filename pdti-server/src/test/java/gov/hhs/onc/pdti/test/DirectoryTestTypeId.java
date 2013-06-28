package gov.hhs.onc.pdti.test;

import gov.hhs.onc.pdti.DirectoryEnumId;

public enum DirectoryTestTypeId implements DirectoryEnumId {
    FEDERATION_LOOP("federation_loop");

    private String id;

    DirectoryTestTypeId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
