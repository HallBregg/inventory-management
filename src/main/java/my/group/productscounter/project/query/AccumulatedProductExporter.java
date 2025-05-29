package my.group.productscounter.project.query;

import java.util.List;


public interface AccumulatedProductExporter {
    String export(List<AccumulatedProductView> products);
}
