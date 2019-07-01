package alexander.ivanov.orm.object;

import alexander.ivanov.orm.data.source.h2.annotations.Column;
import alexander.ivanov.orm.data.source.h2.annotations.Size;
import alexander.ivanov.orm.data.source.h2.annotations.Table;
import alexander.ivanov.orm.data.source.h2.annotations.Id;

import java.util.Objects;

@Table(name = "USER")
public class User {
    @Id @Size(20)
    private Long id;
    @Column @Size(255)
    private String name;
    @Column @Size(3)
    private Integer age;

    private String someField; //не добавляется, т.к. нет @Column

    public User() {
    }

    public User(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return id.equals(user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(age, user.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", someField='" + someField + '\'' +
                '}';
    }
}