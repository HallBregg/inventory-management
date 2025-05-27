package my.group.productscounter.project.exception;


public class ProjectCreateException extends ProjectServiceException {
    private static final String CODE = "PROJECT_CREATE_ERROR";

    public ProjectCreateException() {
        super(CODE);
    }

    public ProjectCreateException(Throwable cause) {
        super(CODE, cause);
    }
}
