package alexander.ivanov.messageSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

public class Address {
    private static final Logger logger = LoggerFactory.getLogger(Address.class);
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
        boolean isEquals = Objects.equals(name, address.name);
        return isEquals;
    }

    @Override
    public int hashCode() {
        logger.info("Address.name = {}", name);
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Address{" +
                "name='" + name + '\'' +
                '}';
    }
}
