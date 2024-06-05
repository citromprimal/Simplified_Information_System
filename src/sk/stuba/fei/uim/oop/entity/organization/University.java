package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;

public class University extends Organization {

    @Override
    public int getProjectBudget(ProjectInterface pi) {
        registerProjectInOrganization(pi);
        if (!this.projects.contains(pi)) {
            return 0;
        }
        projectBudget = pi.getTotalBudget();

        return projectBudget;
    }
}