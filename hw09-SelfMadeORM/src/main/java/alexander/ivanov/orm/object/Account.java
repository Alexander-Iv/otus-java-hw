package alexander.ivanov.orm.object;

import alexander.ivanov.orm.data.source.h2.annotations.Column;
import alexander.ivanov.orm.data.source.h2.annotations.Id;
import alexander.ivanov.orm.data.source.h2.annotations.Size;
import alexander.ivanov.orm.data.source.h2.annotations.Table;

@Table(name = "ACCOUNT")
public class Account {
    @Id @Size(20)
    private Long id;
    @Column @Size(255)
    private String name;
    @Column
    private Integer age;

    public Account() {
    }

    public Account(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
