package Tasks.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Document("Task")
public class Task {

    @Id
    private String id;

    @NotNull
    @Pattern(regexp = "\\b[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ ]*?\\b") //only letters and space
    private String title;

    @NotNull
    @Min(0)
    private Integer numberOfSubtasks;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer priority;

    @NotNull
    @PastOrPresent
    private Date creationDate;

    @NotNull
    private Boolean allConditionsMustBeSatisfied;

    @Pattern(regexp = "organization_A|organization_B|organization_C") //one of this three
    private String organizationUnit;

    @Pattern(regexp = "\\b[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ ]*?\\b") //only letters and space
    private String team;

    @Min(1)
    @Max(3)
    private Integer minimumExperienceLevel;

    @Min(0)
    private Integer maximumAgeInYears;

    @JsonIgnore
    private List<String> modificationHistory = new ArrayList<>();

    @JsonIgnore
    private Boolean deleted = false;

    public Task() {

        this.modificationHistory = new ArrayList<>();
    }

    public Task(String title, Integer numberOfSubtasks, Integer priority, Date creationDate, Boolean allConditionsMustBeSatisfied, String organizationUnit, String team, Integer minimumExperienceLevel, Integer maximumAgeInYears) {
        this.title = title;
        this.numberOfSubtasks = numberOfSubtasks;
        this.priority = priority;
        this.creationDate = creationDate;
        this.allConditionsMustBeSatisfied = allConditionsMustBeSatisfied;
        this.organizationUnit = organizationUnit;
        this.team = team;
        this.minimumExperienceLevel = minimumExperienceLevel;
        this.maximumAgeInYears = maximumAgeInYears;
    }

    public boolean fieldsAreNullValidation(){ //check if all nullable fields are null

        return (this.organizationUnit == null && this.team == null && this.minimumExperienceLevel == null && this.maximumAgeInYears == null);
    }

    public void addToModificationList(String modification){

        this.modificationHistory.add(modification);
    }
}
