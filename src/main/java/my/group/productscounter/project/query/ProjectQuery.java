package my.group.productscounter.project.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.group.productscounter.project.exception.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


class ProductSummaryBuilder{
    private final Long productId;
    private final String name;
    private final Integer quantity;
    private final Map<String, String> properties = new LinkedHashMap<>();

    ProductSummaryBuilder(Long productId, String name, Integer quantity){
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
    }

    void addProperty(String name, String value){
        this.properties.put(name, value);
    }

    ProductSummaryView build(){
        return new ProductSummaryView(productId, name, quantity, properties);
    }
}


@Component
class ProjectSummaryCsvExporter{
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String escape(String s) {
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }

    public String export(List<ProductSummaryView> products) throws JsonProcessingException {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Product ID,Product Name,Quantity,Attributes\n");
        for(ProductSummaryView product : products){
            stringBuilder
                    .append(product.productId()).append(",")
                    .append(product.name()).append(",")
                    .append(product.quantity()).append(",")
                    .append(escape(objectMapper.writeValueAsString(product.properties())))
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}


@Service
public class ProjectQuery {
    private final ProjectQueryRepository projectQueryRepository;
    private final ProjectSummaryCsvExporter projectSummaryCsvExporter;

    @Autowired
    ProjectQuery(ProjectQueryRepository projectQueryRepository, ProjectSummaryCsvExporter projectSummaryCsvExporter) {
        this.projectQueryRepository = projectQueryRepository;
        this.projectSummaryCsvExporter = projectSummaryCsvExporter;
    }

    public ProjectView getFullProject(UUID projectId) {
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

    private List<ProductSummaryView> getProjectSummary(UUID projectId) {
        List<FlatProjectSummary> projectFlatSummaries = projectQueryRepository.getFlatProjectSummaries(projectId);
        if(projectFlatSummaries.isEmpty()) throw new ProjectNotFoundException();

        Map<Long, ProductSummaryBuilder> productMap = new HashMap<>();

        for (FlatProjectSummary flatProjectSummary : projectFlatSummaries){
            productMap.computeIfAbsent(
                    flatProjectSummary.getProductId(),
                    productId -> new ProductSummaryBuilder(
                            productId,
                            flatProjectSummary.getProductName(),
                            flatProjectSummary.getQuantity()));

            if (flatProjectSummary.getPropertyName() != null){
                productMap.get(flatProjectSummary.getProductId())
                        .addProperty(
                                flatProjectSummary.getPropertyName(),
                                flatProjectSummary.getPropertyValue());
            }
        }

        return productMap
                .values()
                .stream()
                .map(ProductSummaryBuilder::build).toList();
    }

    public String exportProjectSummaryCSV(UUID projectId){
        try{
            return projectSummaryCsvExporter.export(getProjectSummary(projectId));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
