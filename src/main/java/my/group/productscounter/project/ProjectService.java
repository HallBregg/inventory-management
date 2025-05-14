package my.group.productscounter.project;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

class ProjectStageUpdateException extends ProjectServiceException {
    private static final String CODE = "PROJECT_STAGE_UPDATE_ERROR";

    ProjectStageUpdateException() {
        super(CODE);
    }

    ProjectStageUpdateException(Throwable cause) {
        super(CODE, cause);
    }
}


class ProductNotFound extends ProjectServiceException {
    private static final String CODE = "PRODUCT_NOT_FOUND";

    ProductNotFound(Long productId) {
        super(CODE, String.format("Product with id %s does not exist.", productId));
    }
}


@Service
class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProductFinder productFinder;

    @Autowired
    ProjectService(ProjectRepository projectRepository, ProductFinder productFinder) {
        this.projectRepository = projectRepository;
        this.productFinder = productFinder;
    }

    @Transactional
    Project createProject(CreateProjectCommand command) {
        return projectRepository.save(new Project(command.name()));
    }

    List<Project> listAllProjects() {
        return projectRepository.findAll();
    }

    Project getProject(UUID projectId) {
        return projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);
    }

    @Transactional
    Project updateProject(UpdateProjectCommand command) {
        Project project = getProject(command.projectId());
        project.setName(command.name());
        return projectRepository.save(project);
    }

    @Transactional
    void deleteProject(UUID projectId) {
        projectRepository.deleteById(projectId);
    }

    @Transactional
    Stage createStage(CreateStageCommand command) {
        Project project = getProject(command.projectId());
        return project.addStage(command.name());
    }

    @Transactional
    Stage updateStage(UpdateStageCommand command) {
        Project project = getProject(command.projectId());
        final Stage stage;

        try {
            stage = project.getStage(command.stageId());
        } catch (StageNotFound e) {
            throw new ProjectStageUpdateException(e);
        }

        // Not performant! Maybe some cache on impl. etc.
        command.products().forEach(product -> {
            if (!productFinder.existsById(product.productId())) throw new ProductNotFound(product.productId());
        });
        stage.replaceProducts(command.products());
        return stage;
    }

    @Transactional
    void deleteStage(DeleteStageCommand command) {
        Project project = getProject(command.projectId());
        project.deleteStage(command.stageId());
    }

}
