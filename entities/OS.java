package entities;

import java.time.LocalDateTime;

public class OS {

    private int Id;
    private String name;
    private String description;
    private LocalDateTime solicitationTime;

    public OS(int Id, String name, String description, LocalDateTime solicitationTime) {
        this.setId(Id);
        this.setName(name);
        this.setDescription(description);
        this.setSolicitationTime(solicitationTime);
    }

    private void setId(int Id) {
        this.Id = Id;
    }

    public int getId() {
        return Id;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    private void setSolicitationTime(LocalDateTime solicitationTime) {
        this.solicitationTime = solicitationTime;
    }

    public LocalDateTime getSolicitationTime() {
        return solicitationTime;
    }

    @Override
    public String toString() {
        return "OS{" +
                "Id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", solicitationTime=" + getSolicitationTime() +
                '}';
    }

}
