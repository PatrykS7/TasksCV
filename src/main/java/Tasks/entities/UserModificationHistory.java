package Tasks.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document("UserModificationHistory")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModificationHistory{

    @Id
    private String id;

    private Date modificationDate;

    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Date creationDate;
    private String organizationUnit;
    private String team;
    private Integer experienceLevel;
    private Boolean deleted;

    public UserModificationHistory(User user) {

        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dateOfBirth = user.getDateOfBirth();
        this.creationDate = user.getCreationDate();
        this.organizationUnit = user.getOrganizationUnit();
        this.team = user.getTeam();
        this.experienceLevel = user.getExperienceLevel();
        this.deleted = user.getDeleted();

        this.modificationDate = new Date();
    }

    public UserModificationHistory(Boolean deleted) {

        this.deleted = deleted;
        this.modificationDate = new Date();
    }

}
