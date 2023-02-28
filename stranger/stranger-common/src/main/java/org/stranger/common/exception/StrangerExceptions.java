package org.stranger.common.exception;

/**
 * Stranger exceptions
 */
public class StrangerExceptions {
    public static abstract class StrangerException extends Throwable {
        public StrangerException(String message) {
            super(message);
        }

        public StrangerException(String message, Throwable cause) {
            super(message, cause);
        }

        public StrangerException(Throwable cause) {
            super(cause);
        }
    }

    public static class InvalidConfigurationException extends StrangerException {

        public InvalidConfigurationException(String message) {
            super(message);
        }

        public InvalidConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidConfigurationException(Throwable cause) {
            super(cause);
        }
    }

    public static class ObjectNotFoundException extends StrangerException {

        public ObjectNotFoundException(String message) {
            super(message);
        }

        public ObjectNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public ObjectNotFoundException(Throwable cause) {
            super(cause);
        }
    }

    public static class SystemFailureException extends StrangerException {

        public SystemFailureException(String message) {
            super(message);
        }

        public SystemFailureException(String message, Throwable cause) {
            super(message, cause);
        }

        public SystemFailureException(Throwable cause) {
            super(cause);
        }
    }
}
