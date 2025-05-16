package my.group.productscounter.project.query;


import my.group.productscounter.project.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


interface ProjectQueryRepository extends Repository<Project, Long> {

    @Query("""
              SELECT p.id                   AS projectId,
                     p.name                 AS projectName,
                     s.id                   AS stageId,
                     s.name                 AS stageName,
                     sp.id.position.value         AS productPosition,
                     sp.quantity            AS productQuantity,
                     sp.productId           AS productId,
                     prod.name              AS productName,
                     prop.name              AS productPropertyName,
                     prop.value             AS productPropertyValue
              FROM Project p
              LEFT JOIN p.stages s
              LEFT JOIN s.products sp
              LEFT JOIN Product prod        ON prod.id = sp.productId
              LEFT JOIN prod.properties prop
              WHERE p.id = :projectId
            """)
    List<FlatProjectView> getFlatProjectViews(@Param("projectId") UUID projectId);
}
