package gov.hhs.onc.pdti.ws.handler;

import gov.hhs.onc.pdti.DirectoryException;

public class DirectoryHandlerException extends DirectoryException {
    public DirectoryHandlerException() {
        super();
    }

    public DirectoryHandlerException(String str) {
        super(str);
    }

    public DirectoryHandlerException(String str, Throwable throwable) {
        super(str, throwable);
    }

    public DirectoryHandlerException(Throwable throwable) {
        super(throwable);
    }
}
