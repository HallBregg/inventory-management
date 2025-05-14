package my.group.productscounter.project;

class ProjectQuery {
    private final ProjectQueryRepository projectQueryRepository;

    ProjectQuery(ProjectQueryRepository projectQueryRepository) {
        this.projectQueryRepository = projectQueryRepository;
    }

    FullProjectView getFullProject() {
        return null;
    }
}
