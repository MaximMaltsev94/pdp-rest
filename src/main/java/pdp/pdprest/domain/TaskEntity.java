package pdp.pdprest.domain;

public class TaskEntity {
    private Long id;
    private String firstName;
    private String lastName;

    public TaskEntity() {
    }

    public TaskEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TaskEntity)) {
            return false;
        }

        TaskEntity other = (TaskEntity) obj;

        if(this == obj) {
            return true;
        }
        return this.id.equals(other.id);
    }
}
