package ua.training.top.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "rate")
public class Rate  extends AbstractBaseEntity implements Serializable {

    @NotNull
    @Size(min = 6, max = 6)
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "value")
    private Double value;

    @Column(name = "local_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate localDate;

    public Rate(@NotNull @Size(min = 6, max = 6) String name, @NotNull Double value, LocalDate localDate) {
        this.name = name;
        this.value = value;
        this.localDate = localDate;
    }

    public Rate(Integer id, @NotNull @Size(min = 6, max = 6) String name, @NotNull Double value, LocalDate localDate) {
        this(name, value, localDate);
        this.id = id;
    }

    public Rate() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDate getLocalDate() { return localDate; }

    public void setLocalDate(LocalDate localDate) { this.localDate = localDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Objects.equals(name, rate.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", localDate=" + localDate +
                '}';
    }
}
