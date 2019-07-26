package alexander.ivanov.cache.database.hibernate.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "USER")
public class User {
    @Id
    @SequenceGenerator(name="USER_SEQ", initialValue=1, allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @Column(name = "USER_ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "AGE")
    private Integer age;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDR_ADDR_ID")
    private Address address;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Collection<Phone> phones;

    public User() {
    }

    public User(String name, Integer age, Address address, Collection<Phone> phones) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = phones;
        phones.forEach(phone -> phone.setUser(this));
    }

    public User(Long id, String name, Integer age, Address address, Collection<Phone> phones) {
        this(name, age, address, phones);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Collection<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Collection<Phone> phones) {
        this.phones = phones;
        phones.forEach(phone -> phone.setUser(this));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(age, user.age) &&
                Objects.equals(address, user.address) &&
                Objects.equals(phones, user.phones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, address, phones);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", phone=" + phones +
                '}';
    }
}