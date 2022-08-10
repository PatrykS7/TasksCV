package Tasks.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document("TaskModificationHistory")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskModificationHistory{

    @Id
    private String id;

    private Date modificationDate;
    private String title;
    private Integer numberOfSubtasks;
    private Integer priority;
    private Date creationDate;
    private Boolean allConditionsMustBeSatisfied;
    private String organizationUnit;
    private String team;
    private Integer minimumExperienceLevel;
    private Integer maximumAgeInYears;
    private Boolean deleted;

    public TaskModificationHistory(Task task) {

        this.modificationDate = new Date();
        this.title = task.getTitle();
        this.numberOfSubtasks = task.getNumberOfSubtasks();
        this.priority = task.getPriority();
        this.creationDate = task.getCreationDate();
        this.allConditionsMustBeSatisfied = task.getAllConditionsMustBeSatisfied();
        this.organizationUnit = task.getOrganizationUnit();
        this.team = task.getTeam();
        this.minimumExperienceLevel = task.getMinimumExperienceLevel();
        this.maximumAgeInYears = task.getMaximumAgeInYears();
        this.deleted = task.getDeleted();
    }

    public TaskModificationHistory(Boolean deleted) {

        this.deleted = deleted;
        this.modificationDate = new Date();
    }
}