package sk.stuba.fei.uim.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.stuba.fei.uim.oop.entity.grant.AgencyInterface;
import sk.stuba.fei.uim.oop.entity.grant.GrantInterface;
import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;
import sk.stuba.fei.uim.oop.factory.DataFactory;
import sk.stuba.fei.uim.oop.utility.Constants;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateTest {
    private LinkedList<AgencyInterface> agencies;
    private LinkedList<GrantInterface> grants;
    private LinkedList<OrganizationInterface> organizations;
    private LinkedList<ProjectInterface> projects;
    private LinkedList<PersonInterface> persons;

    @BeforeEach
    void setUp() {
        Constants.PROJECT_DURATION_IN_YEARS = 4;
        Constants.MAX_EMPLOYMENT_PER_AGENCY = 5;

        agencies = DataFactory.getAgencies(2);
        agencies.get(0).setName("VEGA");
        agencies.get(1).setName("APVV");

        grants = DataFactory.getGrants(4);
        List<Integer> grantBudgets = Arrays.asList(100000, 8000, 100000, 8000);
        List<AgencyInterface> grantAgencies = Arrays.asList(agencies.get(1), agencies.get(0), agencies.get(1), agencies.get(0));
        List<Integer> grantYears = Arrays.asList(2022, 2023, 2024, 2024);
        for (int i = 0; i < grants.size(); i++) {
            GrantInterface grant = grants.get(i);
            grant.setIdentifier(String.valueOf(i + 1));
            grant.setAgency(grantAgencies.get(i));
            grantAgencies.get(i).addGrant(grant, grantYears.get(i));
            grant.setBudget(grantBudgets.get(i));
            grant.setYear(grantYears.get(i));
        }

        List<String> organizationNames = Arrays.asList("STU", "UK", "Eset");
        organizations = new LinkedList<>();
        organizations.addAll(DataFactory.getUniversities(2));
        organizations.addAll(DataFactory.getCompanies(1));
        for (int i = 0; i < organizations.size(); i++) {
            organizations.get(i).setName(organizationNames.get(i));
        }

        List<String> personNames = Arrays.asList("Peter", "Jozef", "Anna", "Karol");
        persons = DataFactory.getPersons(personNames.size());
        for (int i = 0; i < persons.size(); i++) {
            PersonInterface person = persons.get(i);
            person.setName(personNames.get(i));
            person.setAddress("Address " + (i + 1));
        }
        // STU Peter, Jozef
        organizations.get(0).addEmployee(persons.get(0), 2);
        persons.get(0).addEmployer(organizations.get(0));
        organizations.get(0).addEmployee(persons.get(1), 3);
        persons.get(1).addEmployer(organizations.get(0));
        // UK Anna, Karol
        organizations.get(1).addEmployee(persons.get(2), 3);
        persons.get(2).addEmployer(organizations.get(1));
        organizations.get(1).addEmployee(persons.get(3), 1);
        persons.get(3).addEmployer(organizations.get(1));
        // Eset Peter, Anna, Karol
        organizations.get(2).addEmployee(persons.get(0), 4);
        persons.get(0).addEmployer(organizations.get(2));
        organizations.get(2).addEmployee(persons.get(2), 2);
        persons.get(2).addEmployer(organizations.get(2));
        organizations.get(2).addEmployee(persons.get(3), 1);
        persons.get(3).addEmployer(organizations.get(2));

        List<OrganizationInterface> projectOrganizations = Arrays.asList(organizations.get(0), organizations.get(1), organizations.get(2), organizations.get(2), organizations.get(0), organizations.get(1), organizations.get(1));
        List<Integer> projectYears = Arrays.asList(2022, 2023, 2024, 2024, 2024, 2024, 2024);

        projects = DataFactory.getProjects(7);
        for (int i = 0; i < projects.size(); i++) {
            ProjectInterface proj = projects.get(i);
            proj.setProjectName("P" + (i + 1));
            OrganizationInterface applicant = projectOrganizations.get(i);
            proj.setApplicant(applicant);
            applicant.registerProjectInOrganization(projects.get(i));
            proj.setStartingYear(projectYears.get(i));
        }
        projects.get(0).addParticipant(persons.get(0)); // Peter
        projects.get(0).addParticipant(persons.get(1)); // Jozef
        projects.get(1).addParticipant(persons.get(2)); // Anna
        projects.get(1).addParticipant(persons.get(3)); // Karol
        projects.get(2).addParticipant(persons.get(0)); // Peter
        projects.get(3).addParticipant(persons.get(2)); // Anna
        projects.get(3).addParticipant(persons.get(3)); // Karol
        projects.get(4).addParticipant(persons.get(0)); // Peter
        projects.get(4).addParticipant(persons.get(1)); // Jozef
        projects.get(5).addParticipant(persons.get(2)); // Anna
        projects.get(6).addParticipant(persons.get(3)); // Karol
    }

    @Test
    void testEntityNames_1_() { // _1_ == passed test for 1 point
        // agency, project, and person getter test
        assertEquals("VEGA", agencies.get(0).getName());
        assertEquals("APVV", agencies.get(1).getName());
        List<String> personNames = Arrays.asList("Peter", "Jozef", "Anna", "Karol");
        for (int i = 0; i < persons.size(); i++) {
            assertEquals(personNames.get(i), persons.get(i).getName());
        }

        for (int i = 0; i < projects.size(); i++) {
            assertEquals("P" + (i + 1), projects.get(i).getProjectName());
        }
    }

    @Test
    void testProjectStartingYears_1_() {
        // project starting year getter test
        List<Integer> projectYears = Arrays.asList(2022, 2023, 2024, 2024, 2024, 2024, 2024);
        for (int i = 0; i < projects.size(); i++) {
            assertEquals(projectYears.get(i), projects.get(i).getStartingYear());
        }
    }

    @Test
    void testProjectEndingYears_1_required() {
        // project ending year test
        List<Integer> projectYears = Arrays.asList(2022, 2023, 2024, 2024, 2024, 2024, 2024);
        for (int i = 0; i < projects.size(); i++) {
            assertEquals(projectYears.get(i) + (Constants.PROJECT_DURATION_IN_YEARS - 1), projects.get(i).getEndingYear());
        }
    }

    @Test
    void testGrantYears_1_() {
        // grant year getter test
        List<Integer> grantYears = Arrays.asList(2022, 2023, 2024, 2024);
        for (int i = 0; i < grants.size(); i++) {
            GrantInterface grant = grants.get(i);
            assertEquals(grantYears.get(i), grant.getYear());
        }
    }

    @Test
    void testEmployerNotNullAndNotEmpty_1_() {
        // if person employers and organization employees are set (values added)
        // this test will not check if the employer and employee relations are set correctly

        for (int i = 0; i < persons.size(); i++) {
            assertNotNull(persons.get(0).getEmployers());
            assertNotEquals(0, persons.get(0).getEmployers().size());
        }

        for (int i = 0; i < organizations.size(); i++) {
            assertNotNull(organizations.get(0).getEmployees());
            assertNotEquals(0, organizations.get(0).getEmployees().size());
        }
    }

    @Test
    void testEmployerEmployeeRelation_1_required() {
        // if employer and employee relations are set correctly
        //Peter
        assertTrue(organizations.get(0).getEmployees().contains(persons.get(0)));
        assertFalse(organizations.get(1).getEmployees().contains(persons.get(0)));
        assertTrue(organizations.get(2).getEmployees().contains(persons.get(0)));
        //Jozef
        assertTrue(organizations.get(0).getEmployees().contains(persons.get(1)));
        assertFalse(organizations.get(1).getEmployees().contains(persons.get(1)));
        assertFalse(organizations.get(2).getEmployees().contains(persons.get(1)));
        //Anna
        assertFalse(organizations.get(0).getEmployees().contains(persons.get(2)));
        assertTrue(organizations.get(1).getEmployees().contains(persons.get(2)));
        assertTrue(organizations.get(2).getEmployees().contains(persons.get(2)));
        //Karol
        assertFalse(organizations.get(0).getEmployees().contains(persons.get(3)));
        assertTrue(organizations.get(1).getEmployees().contains(persons.get(3)));
        assertTrue(organizations.get(2).getEmployees().contains(persons.get(3)));

        // Peter STU, Eset
        assertTrue(persons.get(0).getEmployers().containsAll(Arrays.asList(organizations.get(0),organizations.get(2))));
        // Jozef STU
        assertTrue(persons.get(1).getEmployers().containsAll(Arrays.asList(organizations.get(0))));
        // Anna UK, Eset
        assertTrue(persons.get(2).getEmployers().containsAll(Arrays.asList(organizations.get(1),organizations.get(2))));
        // Karol UK, Eset
        assertTrue(persons.get(3).getEmployers().containsAll(Arrays.asList(organizations.get(1),organizations.get(2))));
    }

    @Test
    void testEntityEqualsAndHash_1_(){
        // if equals and hash methods are not implemented in the entity classes, entities will be added multiple times into the set
        Set<AgencyInterface> agencySet = new HashSet<>(agencies);
        AgencyInterface agency = DataFactory.getAgencies(1).get(0);
        agency.setName("VEGA");
        agencySet.add(agency);
        assertEquals(agencies.size(), agencySet.size());

        Set<PersonInterface> personSet = new HashSet<>(persons);
        PersonInterface person = DataFactory.getPersons(1).get(0);
        person.setName("Anna");
        personSet.add(person);
        assertEquals(persons.size(), personSet.size());

        Set<ProjectInterface> projectSet = new HashSet<>(projects);
        ProjectInterface project = DataFactory.getProjects(1).get(0);
        project.setProjectName("P1");
        projectSet.add(project);
        assertEquals(projects.size(), projectSet.size());
    }

    @Test
    void testEmploymentCheck_1_() {
        // if the employment is set correctly (as passed in the OrganizationInterface.addEmployee method)
        assertEquals(2,organizations.get(0).getEmploymentForEmployee(persons.get(0)));
        assertEquals(3,organizations.get(0).getEmploymentForEmployee(persons.get(1)));
        assertEquals(3,organizations.get(1).getEmploymentForEmployee(persons.get(2)));
        assertEquals(1,organizations.get(1).getEmploymentForEmployee(persons.get(3)));
        assertEquals(4,organizations.get(2).getEmploymentForEmployee(persons.get(0)));
        assertEquals(2,organizations.get(2).getEmploymentForEmployee(persons.get(2)));
        assertEquals(1,organizations.get(2).getEmploymentForEmployee(persons.get(3)));
    }


    @Test
    void testProjectRegistrationReturnValue_1_required(){
        // the registration return value should be true only between the callForProjects, and the evaluateProjects method calls
        ProjectInterface pi = projects.getFirst();
        GrantInterface gi = grants.getFirst();
        boolean reg = gi.registerProject(pi);
        assertFalse(reg);
        gi.callForProjects();
        reg = gi.registerProject(pi);
        gi.evaluateProjects();
        assertTrue(reg);
        gi.closeGrant();
        reg = gi.registerProject(pi);
        assertFalse(reg);
    }

    @Test
    void testAddingNotEmployedProjectParticipant_1_(){
        // adding participants not employed in the applicant organization
        PersonInterface person = persons.get(1);
        ProjectInterface project = DataFactory.getProjects(1).get(0);
        project.addParticipant(person);
        assertEquals(0,project.getAllParticipants().size());
        project.setApplicant(organizations.get(2));
        project.addParticipant(person);
        assertEquals(0,project.getAllParticipants().size());
    }
    @Test
    void testProjectRegistrationOrder_1_(){
        // project should be stord according to the registration order
        GrantInterface gi = grants.getLast();
        LinkedList<ProjectInterface> pList = DataFactory.getProjects(10);
        PersonInterface person = DataFactory.getPersons(1).get(0);
        organizations.get(0).addEmployee(person,1);
        person.addEmployer(organizations.get(0));
        gi.callForProjects();
        for(int i=0; i < pList.size(); i++){
            ProjectInterface pi = pList.get(i);
            pi.setProjectName("PROJ"+(pList.size()-i));
            pi.setStartingYear(2024);
            pi.setApplicant(organizations.get(0));
            pi.addParticipant(person);
            gi.registerProject(pi);
        }
        gi.evaluateProjects();
        gi.closeGrant();

        List<ProjectInterface> regProjects = new ArrayList<>(gi.getRegisteredProjects());
        for(int i=0; i < pList.size(); i++) {
            assertEquals(pList.get(i), regProjects.get(i));
        }
    }

    @Test
    void testProjectRegistrationPassedProjects_1_(){
        ProjectInterface pi = projects.getFirst();
        GrantInterface gi = grants.getFirst();
        gi.registerProject(pi);
        assertEquals(0, gi.getRegisteredProjects().size());
        gi.callForProjects();
        gi.registerProject(pi);
        assertEquals(1, gi.getRegisteredProjects().size());
        gi.evaluateProjects();
        assertEquals(1, gi.getRegisteredProjects().size());
        gi.closeGrant();
        assertEquals(1, gi.getRegisteredProjects().size());
    }

    @Test
    void testProjectRegistrationIncorrectYear_1_(){
        // project registrations should fail for wrong year
        ProjectInterface pi = projects.getFirst();
        GrantInterface gi = grants.getFirst();
        pi.setStartingYear(gi.getYear()+1);
        gi.callForProjects();
        boolean reg = gi.registerProject(pi);
        assertFalse(reg);
    }

    @Test
    void testProjectRegistrationNoParticipants_1_(){
        // project registrations should fail for project without valid participants
        ProjectInterface pi = projects.getFirst(); // STU project
        GrantInterface gi = grants.getFirst();
        pi.getAllParticipants().clear();
        pi.addParticipant(persons.get(2)); // Anna is not employee of STU
        pi.setStartingYear(gi.getYear());
        gi.callForProjects();
        boolean reg = gi.registerProject(pi);
        assertFalse(reg);
    }

    @Test
    void testSimpleProjectBudgetZeroPassed_2_() {
        Constants.PROJECT_DURATION_IN_YEARS = 2;
        Constants.MAX_EMPLOYMENT_PER_AGENCY = 2;
        // employments: Peter 2STU/4Eset, Jozef 3STU
        // for this reason the proposed projects fail the test and their budget will be 0

        ProjectInterface P1 = projects.get(0);
        P1.addParticipant(persons.get(0));
        P1.addParticipant(persons.get(1));
        P1.setStartingYear(2022);
        GrantInterface grant1 = grants.get(0);
        grant1.callForProjects();
        grant1.registerProject(P1);
        grant1.evaluateProjects();
        grant1.closeGrant();
        assertEquals(0, grant1.getBudgetForProject(P1));

        ProjectInterface P3 =  projects.get(2);
        P3.addParticipant(persons.get(0));
        P3.setStartingYear(2024);
        GrantInterface grant2 = grants.get(2);
        grant2.callForProjects();
        grant2.registerProject(P3);
        grant2.evaluateProjects();
        grant2.closeGrant();

        assertEquals(0, grant2.getBudgetForProject(P3));
//        Constants.PROJECT_DURATION_IN_YEARS = 4;
//        Constants.MAX_EMPLOYMENT_PER_AGENCY = 5;
    }


    @Test
    void testSimpleProjectBudgetAllPassed_2_required() {
        Constants.PROJECT_DURATION_IN_YEARS = 2;
        //Constants.MAX_EMPLOYMENT_PER_AGENCY = 4;
        // employments: Peter ma 2STU/4Eset, Jozef 3STU
        // non overlapping projects, both projects will pass the test

        ProjectInterface P1 = projects.get(0);
        P1.addParticipant(persons.get(0));
        P1.addParticipant(persons.get(1));
        P1.setStartingYear(2022);
        GrantInterface grant1 = grants.get(0);
        grant1.callForProjects();
        grant1.registerProject(P1);
        grant1.evaluateProjects();
        grant1.closeGrant();
        assertEquals(100000, grant1.getBudgetForProject(P1));

        ProjectInterface P3 =  projects.get(2);
        P3.addParticipant(persons.get(0));
        P3.setStartingYear(2024);
        GrantInterface grant2 = grants.get(2);
        grant2.callForProjects();
        grant2.registerProject(P3);
        grant2.evaluateProjects();
        grant2.closeGrant();

        assertEquals(100000, grant2.getBudgetForProject(P3));
//        Constants.PROJECT_DURATION_IN_YEARS = 4;
//        Constants.MAX_EMPLOYMENT_PER_AGENCY = 5;
    }

    @Test
    void testSimpleProjectBudgetHalfPassed_2_() {
        Constants.PROJECT_DURATION_IN_YEARS = 3;
        Constants.MAX_EMPLOYMENT_PER_AGENCY = 4;
        // employments: Peter ma 2STU/4Eset, Jozef 3STU
        // the second project fail the test (overlapping projects and employment limit fo Peter)

        ProjectInterface P1 = projects.get(0);
        P1.addParticipant(persons.get(0));
        P1.addParticipant(persons.get(1));
        P1.setStartingYear(2022);
        GrantInterface grant1 = grants.get(0);
        grant1.callForProjects();
        grant1.registerProject(P1);
        grant1.evaluateProjects();
        grant1.closeGrant();
        assertEquals(100000, grant1.getBudgetForProject(P1));

        ProjectInterface P3 =  projects.get(2);
        P3.addParticipant(persons.get(0));
        P3.setStartingYear(2024);
        GrantInterface grant2 = grants.get(2);
        grant2.callForProjects();
        grant2.registerProject(P3);
        grant2.evaluateProjects();
        grant2.closeGrant();

        assertEquals(0, grant2.getBudgetForProject(P3));
//        Constants.PROJECT_DURATION_IN_YEARS = 4;
//        Constants.MAX_EMPLOYMENT_PER_AGENCY = 5;
    }

    @Test
    void testComplexProjectBudget_4_() {
        // 2022
        // complex scenario
        ProjectInterface P1 =  projects.get(0);
        P1.addParticipant(persons.get(0));
        P1.addParticipant(persons.get(1));
        P1.setStartingYear(2022);

        GrantInterface grant = grants.get(0);
        grant.callForProjects();
        grant.registerProject(P1);
        grant.evaluateProjects();
        grant.closeGrant();

        assertEquals(100000, P1.getTotalBudget());
        assertEquals(0, P1.getBudgetForYear(2021));
        assertEquals(100000/4, P1.getBudgetForYear(2022));
        assertEquals(100000/4, P1.getBudgetForYear(2023));
        assertEquals(100000/4, P1.getBudgetForYear(2024));
        assertEquals(100000/4, P1.getBudgetForYear(2025));
        assertEquals(0, P1.getBudgetForYear(2026));

        // 2023
        ProjectInterface P2 =  projects.get(1);
        P2.addParticipant(persons.get(2));
        P2.addParticipant(persons.get(3));
        P2.setStartingYear(2023);

        grant = grants.get(1);
        grant.callForProjects();
        grant.registerProject(P2);
        grant.evaluateProjects();
        grant.closeGrant();

        assertEquals(8000, P2.getTotalBudget());
        assertEquals(0, P2.getBudgetForYear(2022));
        assertEquals(8000/4, P2.getBudgetForYear(2023));
        assertEquals(8000/4, P2.getBudgetForYear(2024));
        assertEquals(8000/4, P2.getBudgetForYear(2025));
        assertEquals(8000/4, P2.getBudgetForYear(2026));
        assertEquals(0, P2.getBudgetForYear(2027));

        // 2024
        // APVV
        ProjectInterface P3 =  projects.get(2);
        P3.addParticipant(persons.get(0));
        P3.setStartingYear(2024);

        grant = grants.get(2);
        grant.callForProjects();
        grant.registerProject(P3);
        grant.evaluateProjects();
        grant.closeGrant();

        assertEquals(0, P3.getTotalBudget());
        assertEquals(0, P3.getBudgetForYear(2023));
        assertEquals(0, P3.getBudgetForYear(2024));
        assertEquals(0, P3.getBudgetForYear(2025));
        assertEquals(0, P3.getBudgetForYear(2026));
        assertEquals(0, P3.getBudgetForYear(2027));
        assertEquals(0, P3.getBudgetForYear(2028));

        // VEGA
        ProjectInterface P4 =  projects.get(3);
        P4.addParticipant(persons.get(2));
        P4.addParticipant(persons.get(3));
        P4.setStartingYear(2024);

        ProjectInterface P5 =  projects.get(4);
        P5.addParticipant(persons.get(0));
        P5.addParticipant(persons.get(1));
        P5.setStartingYear(2024);

        ProjectInterface P6 =  projects.get(5);
        P6.addParticipant(persons.get(2));
        P6.setStartingYear(2024);

        ProjectInterface P7 =  projects.get(6);
        P7.addParticipant(persons.get(3));
        P7.setStartingYear(2024);

        grant = grants.get(3);
        grant.callForProjects();
        grant.registerProject(P4);
        grant.registerProject(P5);
        grant.registerProject(P6);
        grant.registerProject(P7);
        grant.evaluateProjects();
        grant.closeGrant();

        assertEquals(8000, P4.getTotalBudget());
        assertEquals(0, P4.getBudgetForYear(2023));
        assertEquals(8000/4, P4.getBudgetForYear(2024));
        assertEquals(8000/4, P4.getBudgetForYear(2025));
        assertEquals(8000/4, P4.getBudgetForYear(2026));
        assertEquals(8000/4, P4.getBudgetForYear(2027));
        assertEquals(0, P4.getBudgetForYear(2028));
        assertEquals(0, P5.getTotalBudget());
        assertEquals(0, P6.getTotalBudget());
        assertEquals(0, P7.getTotalBudget());
    }


    @Test
    void testComplexProjectBudgetInOrganization_4_() {
        // 2022
        ProjectInterface P1 =  projects.get(0);
        P1.addParticipant(persons.get(0));
        P1.addParticipant(persons.get(1));
        P1.setStartingYear(2022);

        GrantInterface grant = grants.get(0);
        grant.callForProjects();
        grant.registerProject(P1);
        grant.evaluateProjects();
        grant.closeGrant();

        assertEquals(100000, P1.getApplicant().getProjectBudget(P1));

        // 2023
        ProjectInterface P2 =  projects.get(1);
        P2.addParticipant(persons.get(2));
        P2.addParticipant(persons.get(3));
        P2.setStartingYear(2023);

        grant = grants.get(1);
        grant.callForProjects();
        grant.registerProject(P2);
        grant.evaluateProjects();
        grant.closeGrant();

        assertEquals(8000, P2.getApplicant().getProjectBudget(P2));

        // 2024
        // APVV
        ProjectInterface P3 =  projects.get(2);
        P3.addParticipant(persons.get(0));
        P3.setStartingYear(2024);

        grant = grants.get(2);
        grant.callForProjects();
        grant.registerProject(P3);
        grant.evaluateProjects();
        grant.closeGrant();

        assertEquals(0, P3.getApplicant().getProjectBudget(P3));

        // VEGA
        ProjectInterface P4 =  projects.get(3);
        P4.addParticipant(persons.get(2));
        P4.addParticipant(persons.get(3));
        P4.setStartingYear(2024);

        ProjectInterface P5 =  projects.get(4);
        P5.addParticipant(persons.get(0));
        P5.addParticipant(persons.get(1));
        P5.setStartingYear(2024);

        ProjectInterface P6 =  projects.get(5);
        P6.addParticipant(persons.get(2));
        P6.setStartingYear(2024);

        ProjectInterface P7 =  projects.get(6);
        P7.addParticipant(persons.get(3));
        P7.setStartingYear(2024);

        grant = grants.get(3);
        grant.callForProjects();
        grant.registerProject(P4);
        grant.registerProject(P5);
        grant.registerProject(P6);
        grant.registerProject(P7);
        grant.evaluateProjects();
        grant.closeGrant();

        assertEquals(8000*2, P4.getApplicant().getProjectBudget(P4));
        assertEquals(0, P5.getApplicant().getProjectBudget(P5));
        assertEquals(0, P6.getApplicant().getProjectBudget(P6));
        assertEquals(0, P7.getApplicant().getProjectBudget(P7));
    }

    @Test
    void testRemainingBudget_2_(){
        // remaining budget test
        // VEGA 2024
        GrantInterface grant = grants.get(3);
        grant.setYear(2030);
        grant.callForProjects();
        LinkedList<ProjectInterface> projs = DataFactory.getProjects(14);
        int counter = 1;
        for(ProjectInterface p : projs) {
            p.setProjectName("PP"+(counter++));
            p.setApplicant(organizations.get(2));
            organizations.get(2).registerProjectInOrganization(p);
            p.addParticipant(persons.get(2));
            p.addParticipant(persons.get(3));
            p.setStartingYear(2030);
            grant.registerProject(p);
        }
        grant.evaluateProjects();
        grant.closeGrant();
        int projBudget = grant.getBudget()/(projs.size()/2);
        int remainingGrantBudget = grant.getBudget()%(projs.size()/2);
        assertEquals(remainingGrantBudget,grant.getRemainingBudget());
        for(int i=0; i < projs.size(); i++){
            ProjectInterface p = projs.get(i);
            if(i < projs.size()/2){
                assertEquals(Constants.PROJECT_DURATION_IN_YEARS*(projBudget/Constants.PROJECT_DURATION_IN_YEARS), p.getTotalBudget());
                assertEquals(0, p.getBudgetForYear(2029));
                assertEquals(projBudget/Constants.PROJECT_DURATION_IN_YEARS, p.getBudgetForYear(2030));
                assertEquals(projBudget/Constants.PROJECT_DURATION_IN_YEARS, p.getBudgetForYear(2031));
                assertEquals(projBudget/Constants.PROJECT_DURATION_IN_YEARS, p.getBudgetForYear(2032));
                assertEquals(projBudget/Constants.PROJECT_DURATION_IN_YEARS, p.getBudgetForYear(2033));
                assertEquals(0, p.getBudgetForYear(2034));
            } else {
                assertEquals(0, p.getTotalBudget());

            }
        }
    }

}
