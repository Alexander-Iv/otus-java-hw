package alexander.ivanov.messageSystem;

import java.util.Objects;
import java.util.UUID;

public class Address {
    private final String name;

    public Address() {
        this.name = UUID.randomUUID().toString();
    }

    public Address(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(name, address.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Address{" +
                "name='" + name + '\'' +
                '}';
    }
}
