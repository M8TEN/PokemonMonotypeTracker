package Client;

import Client.LocationEncounter;

import java.util.Arrays;
import java.util.Objects;

public class Location {
    private String name;
    private LocationEncounter[] foundEncounters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationEncounter[] getFoundEncounters() {
        return foundEncounters;
    }

    public void setFoundEncounters(LocationEncounter[] foundEncounters) {
        this.foundEncounters = foundEncounters;
    }

    public Location(String name, LocationEncounter[] foundEncounters) {
        this.name = name;
        this.foundEncounters = foundEncounters;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Location location)) return false;
        if (!Objects.equals(getName(), location.getName())) return false;
        Location other = (Location) o;
        if (!(this.name.equals((other.getName())))) return false;
        if (!(this.foundEncounters.length == (other.getFoundEncounters().length))) return false;
        for (LocationEncounter encounter : this.foundEncounters) {
            if (!hasLocationEncounter(other, encounter)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), Arrays.hashCode(getFoundEncounters()));
    }

    private boolean hasLocationEncounter(Location other, LocationEncounter encounter) {
        for (LocationEncounter otherEncounter : other.getFoundEncounters()) {
            if (!encounter.equals(otherEncounter)) return false;
        }
        return true;
    }

}
