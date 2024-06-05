package sk.stuba.fei.uim.oop.entity.people;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;

import java.util.HashSet;
import java.util.Set;

public class Person implements PersonInterface{
    private String name;
    private String address;
    private Set<OrganizationInterface> employers;

    public Person() {
        this.employers = new HashSet<>();
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
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void addEmployer(OrganizationInterface organization) {
        for (OrganizationInterface employer : this.employers) {
            if (employer.getName().equals(organization.getName())) {
                return;
            }
        }
        this.employers.add(organization);
    }

    @Override
    public Set<OrganizationInterface> getEmployers() {
        return new HashSet<>(this.employers);
    }
}
