package ua.training.top.model;

import org.hibernate.annotations.BatchSize;
import org.springframework.util.CollectionUtils;
import ua.training.top.HasIdAndEmail;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import static ua.training.top.util.xss.XssUtil.xssClear;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "users_unique_email_idx")})
public class User extends AbstractNamedEntity implements HasIdAndEmail {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(min = 5, max = 100)
    private String email;

//    @JsonIgnore
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    @NotNull
    @Size(min = 5, max = 100)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled = true;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private Date registered = new Date();

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_unique_idx")})
    @Column(name = "role")
//    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 200)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User() {
    }

    public User(User u) {
        this(u.getId(), u.getName(), u.getEmail(), u.getPassword(), u.isEnabled(), u.getRegistered(), u.getRoles());
    }
//
    public User(Integer id, String name, String email, String password, Role role, Role... roles) {
        this(id, name,email, password, true, new Date(),  EnumSet.of(role, roles));
    }

    public User(Integer id, String name, String email, String password, boolean enabled, Date registered, Collection<Role> roles) {
        super(id, name);
        this.email = xssClear(email);
        this.password = xssClear(password);
        this.enabled = enabled;
        this.registered = registered;
        setRoles(roles);
    }

    public String getEmail() {
        return email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }
    public void setEmail(String email) {
        this.email = xssClear(email);
    }

    public void setPassword(String password) {
        this.password = xssClear(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                ", registered=" + registered +
                ", roles=" + roles +
                '}';
    }
}
