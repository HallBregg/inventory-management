package my.group.productscounter.project.query;


import java.util.List;
import java.util.UUID;

record StageProductPropertyView(String name, String value) {
}

record StageProductView(int position, int quantity, Long id, String name, List<StageProductPropertyView> properties) {
}

record StageView(UUID id, String name, List<StageProductView> products) {
}

public record ProjectView(UUID id, String name, List<StageView> stages) {
}
