package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Organization implements OrganizationInterface {
    private String name;
    protected int projectBudget;
    protected Set<PersonInterface> employees;
    protected Set<ProjectInterface> projects;
    protected Map<PersonInterface, Integer> employmentMap;
    protected Set<String> employeeNames = new HashSet<>();
    protected static Set<String> projectNames = new HashSet<>();

    public Organization() {
        this.employees = new HashSet<>();
        this.projects = new HashSet<>();
        this.employmentMap = new HashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addEmployee(PersonInterface p, int employment) {
        if (employeeNames.contains(p.getName())) {
            return;
        }
        employeeNames.add(p.getName());
        employees.add(p);
        employmentMap.put(p, employment);
    }

    @Override
    public Set<PersonInterface> getEmployees() {
        return new HashSet<>(employees);
    }

    @Override
    public int getEmploymentForEmployee(PersonInterface p) {
        Integer employment = employmentMap.get(p);
        if (employment == null) {
            return 0;
        }
        return employment;
    }

    @Override
    public Set<ProjectInterface> getAllProjects() {
        return new HashSet<>(this.projects);
    }

    @Override
    public Set<ProjectInterface> getRunningProjects(int year) {
        Set<ProjectInterface> runningProjects = new HashSet<>();
        for (ProjectInterface project : projects) {
            if (project.getStartingYear() <= year && project.getEndingYear() >= year) {
                runningProjects.add(project);
            }
        }
        return runningProjects;
    }

    @Override
    public void registerProjectInOrganization(ProjectInterface project) {
        projectNames.add(project.getProjectName());
        this.projects.add(project);
    }

    @Override
    public abstract int getProjectBudget(ProjectInterface pi);

    @Override
    public int getBudgetForAllProjects() {
        int totalBudget = 0;
        for (ProjectInterface project : this.projects) {
            totalBudget += getProjectBudget(project);
        }
        return totalBudget;
    }

    @Override
    public void projectBudgetUpdateNotification(ProjectInterface pi, int year, int budgetForYear) {
        if (this.projects.contains(pi)) {
            pi.setBudgetForYear(year, budgetForYear);
        }
    }
}