package my.group.productscounter.project;

import java.util.UUID;

interface FlatProjectView {
    UUID getProjectId();

    String getProjectName();

    UUID getStageId();

    String getStageName();

    Long getProductId();

    String getProductName();

    Integer getProductPosition();

    Integer getProductQuantity();

    String getProductPropertyName();

    String getProductPropertyValue();
}
