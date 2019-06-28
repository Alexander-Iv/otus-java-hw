package alexander.ivanov.orm.object;

import alexander.ivanov.orm.data.source.h2.annotations.Column;
import alexander.ivanov.orm.data.source.h2.annotations.Id;
import alexander.ivanov.orm.data.source.h2.annotations.Size;
import alexander.ivanov.orm.data.source.h2.annotations.Table;

@Table(name = "ACCOUNT")
public class Account {
    @Id @Size(20)
    private Long no;
    @Column @Size(255)
    private String type;
    @Column
    private Integer rest;

    public Account() {
    }

    public Account(Long no, String type, Integer rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    @Override
    public String toString() {
        return "Account{" +
                "no=" + no +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}
