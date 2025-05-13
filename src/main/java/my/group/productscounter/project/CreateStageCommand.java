package my.group.productscounter.project;

import java.util.UUID;

record CreateStageCommand(UUID projectId, String name) {};
