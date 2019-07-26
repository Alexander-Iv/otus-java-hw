package alexander.ivanov.cache.database.hibernate.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PHONE")
public class Phone {
    @Id
    @SequenceGenerator(name="PHONE_SEQ", initialValue=1, allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHONE_SEQ")
    @Column(name = "PHONE_ID")
    private Long id;
    @Column(name = "NUMBER")
    private String number;
    @ManyToOne @JoinColumn(name = "USER_USER_ID")
    private User user;

    public Phone() {
    }

    public Phone(String number) {
        this.number = number;
    }

    public Phone(Long id, String number) {
        this(number);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Phone phone = (Phone) object;
        return Objects.equals(id, phone.id) &&
                Objects.equals(number, phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
