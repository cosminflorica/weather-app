package ro.mta.se.lab.factory;

import ro.mta.se.lab.exception.*;

public class ExceptionFactory {
    public static IException getException(String exceptionType) {
        if (exceptionType == null) {
            return null;
        }
        if (exceptionType.equalsIgnoreCase("FileEmpty")) {
            return new FileEmpty();
        }
        if (exceptionType.equalsIgnoreCase("InvalidInput")) {
            return new InvalidInput();
        }
        if (exceptionType.equalsIgnoreCase("NullException")) {
            return new NullException();
        }
        return null;
    }
}
