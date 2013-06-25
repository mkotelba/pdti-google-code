package gov.hhs.onc.pdti;

public class DirectoryException extends Exception {
    public DirectoryException() {
        super();
    }

    public DirectoryException(String str) {
        super(str);
    }

    public DirectoryException(String str, Throwable throwable) {
        super(str, throwable);
    }

    public DirectoryException(Throwable throwable) {
        super(throwable);
    }
}
