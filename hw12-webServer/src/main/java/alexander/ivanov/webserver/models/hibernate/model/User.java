package alexander.ivanov.webserver.models.hibernate.model;

import javax.persistence.*;

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
    @Column(name = "PASSWORD")
    private String password;

    public User() {
    }

    public User(Long id, String name, String password) {
        this(name, password);
        this.id = id;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password.replaceAll(".", "*");
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}