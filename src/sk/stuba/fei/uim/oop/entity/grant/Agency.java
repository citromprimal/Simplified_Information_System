package sk.stuba.fei.uim.oop.entity.grant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Agency implements AgencyInterface{
    private String name;
    private Map<Integer, Set<GrantInterface>> grantsByYear;
    private Set<GrantInterface> allGrants;
    private Set<String> grantIdentifiers;

    public Agency() {
        this.grantsByYear = new HashMap<>();
        this.allGrants = new HashSet<>();
        this.grantIdentifiers = new HashSet<>();
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
    public void addGrant(GrantInterface gi, int year) {
        if (grantIdentifiers.contains(gi.getIdentifier())) {
            return;
        }
        grantIdentifiers.add(gi.getIdentifier());
        this.grantsByYear.computeIfAbsent(year, k -> new HashSet<>()).add(gi);
        this.allGrants.add(gi);
    }

    @Override
    public Set<GrantInterface> getAllGrants() {
        return allGrants;
    }

    @Override
    public Set<GrantInterface> getGrantsIssuedInYear(int year) {
        return grantsByYear.getOrDefault(year, new HashSet<>());
    }
}
