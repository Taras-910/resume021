package ua.training.top.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "resume", uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "work_before"}, name = "resume_idx")})
public class Resume extends AbstractBaseEntity {

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age")
    private String age;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "salary")
    private Integer salary;

    @NotNull
    @Column(name = "work_before")
    private String workBefore;

    @Column(name = "url")
    private String url;

    @NotNull
    @Size(min = 1, max = 100000)
    @Column(name = "skills")
    private String skills;

    @Column(name = "release_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate releaseDate;

    @ApiModelProperty(hidden = true)
    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "freshen_id", nullable = false)
    @JsonBackReference(value = "freshen-movement") //https://stackoverflow.com/questions/20119142/jackson-multiple-back-reference-properties-with-name-defaultreference
    private Freshen freshen;

    public Resume() {
    }

    public Resume(Integer id, @NotNull String title, @NotNull String name, @Nullable String age, @NotNull String address,
                  @NotNull Integer salary, @NotNull String workBefore, String url,
                  @NotNull String skills, LocalDate releaseDate, Freshen freshen) {
        this(id, title, name, age, address, salary, workBefore, url, skills, releaseDate);
        this.freshen = freshen;
    }

    public Resume(Integer id, @NotNull String title, @NotNull String name, String age, @NotNull String address,
                  @NotNull Integer salary, @NotNull String workBefore, String url, @NotNull String skills, LocalDate releaseDate) {
        this(title, name, age, address, salary, workBefore, url, skills, releaseDate);
        this.id = id;
    }

    public Resume(@NotNull String title, @NotNull String name, String age, @NotNull String address, @NotNull Integer salary,
                  @NotNull String workBefore, String url, @NotNull String skills, LocalDate releaseDate) {
        this.title = title;
        this.name = name;
        this.age = age;
        this.address = address;
        this.salary = salary;
        this.workBefore = workBefore;
        this.url = url;
        this.skills = skills;
        this.releaseDate = releaseDate;
    }

    public Resume(Resume r) {
        this(r.getId(), r.getTitle(), r.getName(), r.getAge(), r.getAddress(), r.getSalary(),
                r.getWorkBefore(), r.getUrl(), r.getSkills(), r.getReleaseDate());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getWorkBefore() {
        return workBefore;
    }

    public void setWorkBefore(String lastWork) {
        this.workBefore = lastWork;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Freshen getFreshen() {
        return freshen;
    }

    public void setFreshen(Freshen freshen) {
        this.freshen = freshen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(title, resume.title) &&
                Objects.equals(workBefore, resume.workBefore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, workBefore);
    }

    @Override
    public String toString() {
        return "\n\nResume{" +
                "\ntitle='" + title + '\'' +
                ", \nname=" + name +
                ", \nage=" + age +
                ", \naddress=" + address +
                ", \nsalary='" + salary + '\'' +
                ", \nworkBefore='" + workBefore + '\'' +
                ", \nurl='" + url + '\'' +
                ", \nskills=" + skills +
                ", \nreleaseDate=" + releaseDate +
                ", \nid=" + id +
                ", \nfreshen=" + freshen +
                '}';
    }
}
