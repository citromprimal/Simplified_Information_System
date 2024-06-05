package sk.stuba.fei.uim.oop.entity.grant;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;
import sk.stuba.fei.uim.oop.utility.Constants;

import java.util.*;

public class Project implements ProjectInterface{
    private String projectName;
    private int startingYear;
    private int totalBudget;

    private Map<Integer, Integer> budgetForYear;
    private Set<PersonInterface> participants;
    private Set<String> participantNames;
    private OrganizationInterface applicant;

    public Project() {
        this.budgetForYear = new LinkedHashMap<>();
        this.participants = new HashSet<>();
        this.participantNames = new HashSet<>();
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    @Override
    public void setProjectName(String name) {
        this.projectName = name;
    }

    @Override
    public int getStartingYear() {
        return startingYear;
    }

    @Override
    public void setStartingYear(int year) {
        this.startingYear = year;
    }

    @Override
    public int getEndingYear() {
        return this.startingYear + Constants.PROJECT_DURATION_IN_YEARS - 1;
    }

    @Override
    public void setBudgetForYear(int year, int budget) {
        this.budgetForYear.put(year, budget);
    }

    @Override
    public int getBudgetForYear(int year) {
        return this.budgetForYear.getOrDefault(year, 0);
    }

    @Override
    public int getTotalBudget() {
        totalBudget = this.getBudgetForYear(this.getStartingYear()) * Constants.PROJECT_DURATION_IN_YEARS;
        return totalBudget;
    }

    @Override
    public void addParticipant(PersonInterface participant) {
        if (participantNames.contains(participant.getName())) {
            return;
        }
        participantNames.add(participant.getName());
        this.participants.add(participant);
    }

    @Override
    public Set<PersonInterface> getAllParticipants() {
        return new HashSet<>(this.participants);
    }

    @Override
    public OrganizationInterface getApplicant() {
        return applicant;
    }

    @Override
    public void setApplicant(OrganizationInterface applicant) {
        this.applicant = applicant;
    }
}
