package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import static sk.stuba.fei.uim.oop.utility.Constants.COMPANY_INIT_OWN_RESOURCES;

public class Company extends Organization {
    private int ownResources = COMPANY_INIT_OWN_RESOURCES;

    @Override
    public int getProjectBudget(ProjectInterface pi) {
        registerProjectInOrganization(pi);
        if (!this.projects.contains(pi)) {
            return 0;
        }
        if (pi.getTotalBudget() < ownResources) {
            ownResources -= pi.getTotalBudget();
            projectBudget = 2 * pi.getTotalBudget();
        }
        else {
            projectBudget = pi.getTotalBudget() + ownResources;
        }

        return projectBudget;
    }
}