package by.bsuir.ilya.Entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tbl_users", schema = "distcomp")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "distcomp_tbl_users_seq")
    @SequenceGenerator(name = "distcomp_tbl_users_seq", sequenceName = "distcomp_tbl_users_seq", allocationSize = 1)
    private long id;
    @Column(nullable = false,name="login")
    private String login;
    @Column(nullable = false,name="password")
    private String password;
    @Column(nullable = false,name="firstname")
    private String firstname;
    @Column(nullable = false,name="lastname")
    private String lastname;
    @OneToMany(mappedBy = "user")
    private Set<Issue> issues;


    public User() {

    }

    public Set<Issue> getIssues()
    {
        return  issues;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                '}';
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
