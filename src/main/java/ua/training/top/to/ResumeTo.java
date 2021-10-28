package ua.training.top.to;

import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ResumeTo extends BaseTo implements Serializable, Comparable<ResumeTo> {

    @NotNull
    @Size(min = 2, max = 250)
    private String title;

    @NotNull
    @Size(min = 2, max = 512)
    private String name;

    private String age;

    @NotNull
    @Size(min = 2, max = 512)
    private String address;

    @NotNull
    @Range(min = 1, max = 10000000)
    private Integer salary;

    @NotNull
    @Size(min = 2, max = 512)
    private String workBefore;

    @NotNull
    @Size(min = 4, max = 1000)
    String url;

    @NotNull
    @Size(min = 2, max = 512)
    private String skills;

    private LocalDate releaseDate;

    private String language;

    private String level;

    private String workplace;

    private boolean toVote = false;

    public ResumeTo(Integer id, @NotNull String title, @NotNull String name, String age, @NotNull String address,
                    @NotNull Integer salary, @NotNull String workBefore, @NotNull String url, @NotNull String skills,
                    @Nullable LocalDate releaseDate, @Nullable String language, @Nullable String level,
                    @Nullable String workplace, @Nullable boolean toVote) {
        super(id);
        this.title = title;
        this.name = name;
        this.age = age;
        this.address = address;
        this.salary = salary;
        this.workBefore = workBefore;
        this.url = url;
        this.skills = skills;
        this.releaseDate = releaseDate;
        this.language = language;
        this.level = level;
        this.workplace = workplace;
        this.toVote = toVote;
    }

    public ResumeTo(){}

    public ResumeTo(ResumeTo rTo){
        this(rTo.getId(), rTo.getTitle(), rTo.getName(), rTo.getAge(), rTo.getAddress(), rTo.getSalary(),
                rTo.getWorkBefore(), rTo.getUrl(), rTo.getSkills(), rTo.getReleaseDate(), rTo.getLanguage(),
                rTo.getLevel(), rTo.getWorkplace(), rTo.isToVote());
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAge() { return age; }

    public void setAge(String age) { this.age = age; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public Integer getSalary() { return salary; }

    public void setSalary(Integer salary) { this.salary = salary; }

    public String getWorkBefore() { return workBefore; }

    public void setWorkBefore(String workBefore) { this.workBefore = workBefore; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getSkills() { return skills; }

    public void setSkills(String skills) { this.skills = skills; }

    public LocalDate getReleaseDate() { return releaseDate; }

    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getLanguage() { return language; }

    public void setLanguage(String language) { this.language = language; }

    public String getLevel() { return level; }

    public void setLevel(String level) { this.level = level; }

    public String getWorkplace() { return workplace; }

    public void setWorkplace(String workplace) { this.workplace = workplace; }

    public boolean isToVote() { return toVote; }

    public void setToVote(boolean toVote) { this.toVote = toVote; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResumeTo resumeTo = (ResumeTo) o;
        return  Objects.equals(title, resumeTo.title) &&
                Objects.equals(name, resumeTo.name) &&
                Objects.equals(skills, resumeTo.skills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, name, skills);
    }

    @Override
    public String toString() {
        return "\n        ResumeTo{" +
                "\nid=" + id +
                ", \ntitle='" + title + '\'' +
                ", \nname='" + name + '\'' +
                ", \nage='" + age + '\'' +
                ", \naddress='" + address + '\'' +
                ", \nsalary=" + salary +
                ", \nworkBefore='" + workBefore + '\'' +
                ", \nurl='" + url + '\'' +
                ", \nskills=" + skills +
                ", \nreleaseDate=" + releaseDate +
                ", \nlanguage=" + language +
                ", \nlevel=" + level +
                ", \nworkplace=" + workplace +
                ", \ntoVote=" + toVote +
                '}';
    }

    @Override
    public int compareTo(ResumeTo rTo) {
        String language = null;
        try {
            language = rTo.getLanguage().equals("all") ? "java" : rTo.getLanguage();
        } catch (NullPointerException e) {
            return -1;
        }
        return rTo.getTitle().toLowerCase().matches(".*\\b" + language + "\\b.*")
                && rTo.getTitle().toLowerCase().contains("middle") ? 1 : -1;
    }
}
