package entities;

public class OS {

    private static int idCounter = 0;
    private final int id;
    private String name;
    private String description;
    private String solicitationTime;

    public OS(String name, String description, String solicitationTime) {
        this.id = idCounter++;
        this.setName(name);
        this.setDescription(description);
        this.setSolicitationTime(solicitationTime);
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

    private void setSolicitationTime(String solicitationTime) {
        this.solicitationTime = solicitationTime;
    }

    public String getSolicitationTime() {
        return solicitationTime;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Id: " + this.id + " - " + "Name: " + this.name + " - " + "Description: " + this.description + " - " + "Solicitation Time: " + this.solicitationTime;
    }

}