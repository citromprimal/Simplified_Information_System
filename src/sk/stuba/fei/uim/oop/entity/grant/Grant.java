package sk.stuba.fei.uim.oop.entity.grant;

import java.util.*;

import sk.stuba.fei.uim.oop.entity.people.PersonInterface;
import sk.stuba.fei.uim.oop.utility.Constants;

import static sk.stuba.fei.uim.oop.entity.grant.GrantState.*;
import static sk.stuba.fei.uim.oop.utility.Constants.MAX_EMPLOYMENT_PER_AGENCY;
import static sk.stuba.fei.uim.oop.utility.Constants.PROJECT_DURATION_IN_YEARS;

public class Grant implements GrantInterface {
    private String identifier;
    private int year;
    private int budget;
    private int remainingBudget;

    private LinkedHashSet<ProjectInterface> registeredProjects;
    private Map<ProjectInterface, Integer> ValidatedProjects;
    private AgencyInterface agency;

    private static Map<ProjectInterface, Integer> projectBudgets;
    private static List<ProjectInterface> allRegisteredProjects;
    private static Map<ProjectInterface, GrantInterface> projectGrantMap;
    private static Set<GrantInterface> allGrants;
    private GrantState state;


    public Grant() {
        this.registeredProjects = new LinkedHashSet<>();
        ValidatedProjects = new LinkedHashMap<>();
        projectBudgets = new LinkedHashMap<>();
        allRegisteredProjects = new ArrayList<>();
        projectGrantMap = new HashMap<>();
        allGrants = new HashSet<>();
        allGrants.add(this);
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public void setAgency(AgencyInterface agency) {
        this.agency = agency;
    }

    @Override
    public void setBudget(int budget) {
        this.budget = budget;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public int getYear() {
        return this.year;
    }

    @Override
    public AgencyInterface getAgency() {
        return agency;
    }

    @Override
    public int getBudget() {
        return budget;
    }

    @Override
    public int getRemainingBudget() {
        return this.remainingBudget;
    }

    @Override
    public Set<ProjectInterface> getRegisteredProjects() {
        return registeredProjects;
    }

    @Override
    public GrantState getState() {
        return state;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void callForProjects() {
        if (this.state == EVALUATING || this.state == CLOSED) {
            return;
        }
        this.state = STARTED;
    }

    @Override
    public boolean registerProject(ProjectInterface project) {
        if (getState() != STARTED) {
            return false;
        }

        if (project.getStartingYear() != this.getYear()) {
            return false;
        }

        for (PersonInterface participant : project.getAllParticipants()) {
            if (!participant.getEmployers().contains(project.getApplicant())) {
                return false;
            }
        }

        if (project.getAllParticipants().isEmpty()) {
            return false;
        }

        projectGrantMap.put(project, this);
        this.registeredProjects.add(project);
        allRegisteredProjects.add(project);
        return true;
    }

    @Override
    public void evaluateProjects() {
        if (this.state != STARTED) {
            return;
        }
        this.state = EVALUATING;

        for (ProjectInterface project : this.registeredProjects) {
            boolean isValidProject = validateProject(project);
            ValidatedProjects.put(project, isValidProject ? 1 : 0);
        }

        int validProjectCount = countValidProjects();
        int projectsGettingFunding = Math.max(1, validProjectCount / 2);

        remainingBudget = this.budget;
        int budgetPerProject = this.budget / projectsGettingFunding;

        distributeFunds(projectsGettingFunding, budgetPerProject);
    }

    private boolean validateProject(ProjectInterface project) {
        for (PersonInterface participant : project.getAllParticipants()) {
            int employment = project.getApplicant().getEmploymentForEmployee(participant);

            for (ProjectInterface projectsCheck : allRegisteredProjects) {
                if (projectsCheck != project && isEmploymentExceeded(project, projectsCheck, participant, employment)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isEmploymentExceeded(ProjectInterface project, ProjectInterface projectsCheck, PersonInterface participant, int employment) {
        GrantInterface grant = projectGrantMap.get(projectsCheck);
        int yearDifference = project.getStartingYear() - projectsCheck.getStartingYear();

        if (0 < yearDifference && yearDifference < PROJECT_DURATION_IN_YEARS && this.getAgency() == grant.getAgency()) {
            for (PersonInterface participantsCheck : projectsCheck.getAllParticipants()) {
                if (participant == participantsCheck) {
                    employment += projectsCheck.getApplicant().getEmploymentForEmployee(participantsCheck);
                    if (employment > MAX_EMPLOYMENT_PER_AGENCY) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int countValidProjects() {
        int validProjectCount = 0;
        for (Integer value : ValidatedProjects.values()) {
            if (value == 1) {
                validProjectCount++;
            }
        }
        return validProjectCount;
    }

    private void distributeFunds(int projectsGettingFunding, int budgetPerProject) {
        for (ProjectInterface project : ValidatedProjects.keySet()) {
            if (projectsGettingFunding > 0){
                projectsGettingFunding--;
            } else {
                ValidatedProjects.put(project, 0);
            }
        }

        for (ProjectInterface project : this.registeredProjects) {
            int value = ValidatedProjects.get(project);
            if (value == 0) {
                projectBudgets.put(project, 0);
            } else if (value == 1) {
                project.getApplicant().registerProjectInOrganization(project);
                int projectBudget = (budgetPerProject / PROJECT_DURATION_IN_YEARS) * PROJECT_DURATION_IN_YEARS;
                projectBudgets.put(project, projectBudget);
                this.remainingBudget -= projectBudget;
            }
        }
    }

    @Override
    public int getBudgetForProject(ProjectInterface project) {
        return projectBudgets.getOrDefault(project, 0);
    }

    @Override
    public void closeGrant() {
        if (this.state != EVALUATING) {
            return;
        }

        for (ProjectInterface project : this.registeredProjects) {
            int yearlyBudget = getBudgetForProject(project) / Constants.PROJECT_DURATION_IN_YEARS;

            for (int year = project.getStartingYear(); year <= project.getEndingYear(); year++) {
                project.getApplicant().projectBudgetUpdateNotification(project, year, yearlyBudget);
            }
        }

        state = CLOSED;
    }
}
