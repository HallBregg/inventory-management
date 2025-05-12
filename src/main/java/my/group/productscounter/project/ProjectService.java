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


class CouldNotCreateProjectException extends ProjectServiceException {
    private static final String CODE = "COULD_NOT_CREATE_PROJECT";

    CouldNotCreateProjectException() {
        super(CODE);
    }

    CouldNotCreateProjectException(Throwable cause) {
        super(CODE, cause);
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

    List<Project> findAll() {
        return projectRepository.findAll();
    }

    Project findById(UUID id) {
        return projectRepository
                .findById(id)
                .orElseThrow(ProjectNotFoundException::new);
    }
}
