package alexander.ivanov.orm.object;

import alexander.ivanov.orm.data.source.h2.annotations.Column;
import alexander.ivanov.orm.data.source.h2.annotations.Size;
import alexander.ivanov.orm.data.source.h2.annotations.Table;
import alexander.ivanov.orm.data.source.h2.annotations.Id;

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
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", someField='" + someField + '\'' +
                '}';
    }
}