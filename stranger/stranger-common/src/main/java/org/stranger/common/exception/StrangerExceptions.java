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
}
