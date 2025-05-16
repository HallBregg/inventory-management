package my.group.productscounter.project.exception;


public class ProjectStageUpdateException extends ProjectServiceException {
    private static final String CODE = "PROJECT_STAGE_UPDATE_ERROR";

    ProjectStageUpdateException() {
        super(CODE);
    }

    public ProjectStageUpdateException(Throwable cause) {
        super(CODE, cause);
    }
}
