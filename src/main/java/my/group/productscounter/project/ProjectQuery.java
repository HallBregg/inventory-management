package my.group.productscounter.project;

import my.group.productscounter.project.exception.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
class ProjectQuery {
    private final ProjectQueryRepository projectQueryRepository;

    @Autowired
    ProjectQuery(ProjectQueryRepository projectQueryRepository) {
        this.projectQueryRepository = projectQueryRepository;
    }

    ProjectView getFullProject(UUID projectId) {
        List<FlatProjectView> flatProjectViewList = projectQueryRepository.getFlatProjectViews(projectId);
        if(flatProjectViewList.isEmpty()) throw new ProjectNotFoundException();

        // Group flat rows by stageId
        Map<UUID, List<FlatProjectView>> byStage = flatProjectViewList
                .stream()
                .filter(row -> row.getStageId() != null)
                .collect(Collectors.groupingBy(
                        FlatProjectView::getStageId,
                        LinkedHashMap::new,
                        Collectors.toList()));

        List<StageView> stages = new ArrayList<>();
        for(Map.Entry<UUID, List<FlatProjectView>> stageEntry : byStage.entrySet()){
            UUID stageId = stageEntry.getKey();
            List<FlatProjectView> stageRows = stageEntry.getValue();
            String stageName = stageRows.getFirst().getStageName();

            // Group stage rows by product position. Each position might have multiple rows because each row represents each property (flat).
            Map<Integer, List<FlatProjectView>> byStagePosition = stageRows
                    .stream()
                    .filter(row -> row.getProductPosition() != null)
                    .collect(Collectors.groupingBy(
                            FlatProjectView::getProductPosition,
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));

            List<StageProductView> products = new ArrayList<>();
            for(Map.Entry<Integer, List<FlatProjectView>> productEntry : byStagePosition.entrySet()){
                List<FlatProjectView> productRows = productEntry.getValue();

                FlatProjectView sharedProductRow = productRows.getFirst();
                Integer productPosition = productEntry.getKey();
                String productName = sharedProductRow.getProductName();
                Long productId = sharedProductRow.getProductId();
                Integer productQuantity = sharedProductRow.getProductQuantity();

                List<StageProductPropertyView> productProperties = productRows
                        .stream()
                        .filter(row -> row.getProductPropertyName() != null && row.getProductPropertyValue() != null)
                        .map(row -> new StageProductPropertyView(
                                row.getProductPropertyName(),
                                row.getProductPropertyValue()))
                        .toList();

                products.add(new StageProductView(productPosition, productQuantity, productId, productName, productProperties));
            }

            stages.add(new StageView(stageId, stageName, products));
        }

        FlatProjectView sharedProjectRow = flatProjectViewList.getFirst();
        return new ProjectView(sharedProjectRow.getProjectId(), sharedProjectRow.getProjectName(), stages);
    }
}
