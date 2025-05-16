package my.group.productscounter.project.exception;


public class ProjectNotFoundException extends ProjectServiceException {
    private static final String CODE = "PROJECT_NOT_FOUND";

    public ProjectNotFoundException() {
        super(CODE);
    }
}
