package my.group.productscounter.project;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

abstract class ProjectServiceException extends RuntimeException {
    private String code = "BASE_ERROR";

    protected ProjectServiceException(String code) {
        super();
        this.code = code;
    }

    protected ProjectServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    protected ProjectServiceException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    protected ProjectServiceException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    String getCode() {
        return code;
    }
}


class ProjectNotFoundException extends ProjectServiceException {
    private static final String CODE = "PROJECT_NOT_FOUND";

    ProjectNotFoundException() {
        super(CODE);
    }
}


@Service
class ProjectService {
    private final ProjectRepository projectRepository;

    ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    Project createProject(CreateProjectCommand command) {
        return projectRepository.save(new Project(command.name()));
    }

    List<Project> listAllProjects(){
        return projectRepository.findAll();
    }

    Project getProject(UUID projectId){
        return projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);
    }

    @Transactional
    Project updateProject(UpdateProjectCommand command){
        Project project = getProject(command.projectId());
        project.setName(command.name());
        return projectRepository.save(project);
    }

    @Transactional
    void deleteProject(UUID projectId){
        projectRepository.deleteById(projectId);
    }

    @Transactional
    Stage createStage(CreateStageCommand command){
        Project project = getProject(command.projectId());
        return project.addStage(command.name());
    }

    Stage updateStage(UpdateStageCommand command){
        // Validate if command.product.position and command.product.quantity are "correct" (unique position larger than 0 and quantity larger than 0).
        return null;
    };

    @Transactional
    void deleteStage(DeleteStageCommand command){
        Project project = getProject(command.projectId());
        project.deleteStage(command.stageId());
    };

    @Deprecated
    List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Deprecated
    Project findById(UUID id) {
        return projectRepository
                .findById(id)
                .orElseThrow(ProjectNotFoundException::new);
    }

}
