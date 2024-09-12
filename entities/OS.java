package entities;

import java.time.LocalDateTime;

public class OS {

    public static int nextId = 0;
    private int id;
    private String name;
    private String description;
    private LocalDateTime solicitationTime;

    public OS(String name, String description, LocalDateTime solicitationTime) {
        this.id = nextId++;
        this.setName(name);
        this.setDescription(description);
        this.setSolicitationTime(solicitationTime);
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
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
        return "ID: " + this.id + " - " +
                "Name: " + this.name + " - " +
                "Description: " + this.description + " - " +
                "Solicitation Time: " + this.solicitationTime;
    }

}