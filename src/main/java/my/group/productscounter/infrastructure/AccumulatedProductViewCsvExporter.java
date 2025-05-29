package my.group.productscounter.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.group.productscounter.project.query.AccumulatedProductExporter;
import my.group.productscounter.project.query.AccumulatedProductView;
import my.group.productscounter.project.query.ExporterException;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
class AccumulatedProductViewCsvExporter implements AccumulatedProductExporter {
    ObjectMapper objectMapper = new ObjectMapper();

    private String escape(String s) {
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }

    @Override
    public String export(List<AccumulatedProductView> products) {
        try {
            var stringBuilder = new StringBuilder();
            stringBuilder.append("Product ID,Product Name,Quantity,Attributes\n");
            for (AccumulatedProductView product : products) {
                stringBuilder
                        .append(product.productId()).append(",")
                        .append(product.name()).append(",")
                        .append(product.quantity()).append(",")
                        .append(escape(objectMapper.writeValueAsString(product.properties())))
                        .append("\n");
            }
            return stringBuilder.toString();
        } catch (JsonProcessingException e){
            throw new ExporterException(e.getMessage());
        }
    }
}
