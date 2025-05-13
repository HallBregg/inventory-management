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

    @Transactional
    Project updateProject(UpdateProjectCommand command) {
        Project project = findById(command.projectId());
        project.setName(command.name());
        return projectRepository.save(project);
    }

    @Transactional
    void deleteProject(UUID projectId) {
        projectRepository.deleteById(projectId);
    }

    List<Project> findAll() {
        return projectRepository.findAll();
    }

    Project findById(UUID id) {
        return projectRepository
                .findById(id)
                .orElseThrow(ProjectNotFoundException::new);
    }

    @Transactional
    Stage createStage(CreateStageCommand command) {
        Project project = findById(command.projectId());
        return project.addStage(command.name());
    }
}
