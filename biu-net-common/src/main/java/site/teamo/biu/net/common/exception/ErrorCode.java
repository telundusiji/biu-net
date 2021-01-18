package site.teamo.biu.net.common.exception;

/**
 * @author 爱做梦的锤子
 * @create 2020/12/23
 */
public interface ErrorCode {

    String code();

    String description();

    default BiuNetRuntimeException createRuntimeException() {
        return new BiuNetRuntimeException(this);
    }

    default BiuNetRuntimeException createRuntimeException(String message) {
        return new BiuNetRuntimeException(this, message);
    }

    default BiuNetRuntimeException createRuntimeException(String message, Throwable e) {
        return new BiuNetRuntimeException(this, message, e);
    }

    default BiuNetException createException() {
        return new BiuNetException(this);
    }

    default BiuNetException createException(String message) {
        return new BiuNetException(this, message);
    }

    default BiuNetException createException(String message, Throwable e) {
        return new BiuNetException(this, message, e);
    }

    enum BUSINESS implements ErrorCode {
        QUERY_ERROR("B001", "Null input parameter"),
        CREATE_ERROR("B002", "Null input parameter"),
        UPDATE_ERROR("B002", "Null input parameter");

        public final String code;
        public final String description;

        BUSINESS(String code, String description) {
            this.code = code;
            this.description = description;
        }

        @Override
        public String code() {
            return code;
        }

        @Override
        public String description() {
            return description;
        }
    }

    enum PARAMETER implements ErrorCode {
        NULL_PARAMETER("P001", "Null input parameter"),
        MISSING_PARAMETER("P002", "Missing input parameter"),
        BAD_PARAMETER("P003", "Wrong parameter input");

        public final String code;
        public final String description;

        PARAMETER(String code, String description) {
            this.code = code;
            this.description = description;
        }

        @Override
        public String code() {
            return code;
        }

        @Override
        public String description() {
            return description;
        }
    }

    enum RESOURCE implements ErrorCode {
        UNKNOWN_RESOURCE_ERROR("R000", "Unknown resource error"),

        RESOURCE_HAS_EXISTED("R001", "Resource already exists"),

        RESOURCE_NOT_EXISTS("R002", "The resource does not exist"),

        READ_RESOURCE_ERROR("R003", "Read resource error"),

        WRITE_RESOURCE_ERROR("R004", "Write resource error");

        public final String code;
        public final String description;

        RESOURCE(String code, String description) {
            this.code = code;
            this.description = description;
        }

        @Override
        public String code() {
            return code;
        }

        @Override
        public String description() {
            return description;
        }
    }

}
