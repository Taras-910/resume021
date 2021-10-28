package ua.training.top.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static ua.training.top.util.xss.XssUtil.xssClear;

@MappedSuperclass
public abstract class AbstractNamedEntity extends AbstractBaseEntity {

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    protected String name;

    protected AbstractNamedEntity() {
    }

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = xssClear(name);
    }

    public void setName(String name) {
        this.name = xssClear(name);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return super.toString() + '(' + name + ')';
    }
}
