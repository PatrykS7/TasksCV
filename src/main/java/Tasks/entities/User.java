package Tasks.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@Document("User")
public class User {

    @Id
    private String id;

    @NotNull
    @Pattern(regexp = "\\b[A-Z][a-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*?\\b") //first letter capital then only letters
    private String firstName;

    @NotNull
    @Pattern(regexp = "\\b[A-Z][a-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*?\\b") //first letter capital then only letters
    private String lastName;

    @NotNull
    @Past
    private Date dateOfBirth;

    @NotNull
    @PastOrPresent
    private Date creationDate = new Date();

    @NotNull
    @Pattern(regexp = "organization_A|organization_B|organization_C") //one of this three
    private String organizationUnit;

    @NotNull
    @Pattern(regexp = "\\b[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ ]*?\\b") //only letters and space
    private String team;

    @NotNull
    @Min(1)
    @Max(3)
    private Integer experienceLevel;

    @JsonIgnore
    List<String> modificationHistory = new ArrayList<>();

    @JsonIgnore
    private Boolean deleted = false;

    List<Task> tasks;

    public User() {
        modificationHistory = new ArrayList<>();
    }

    public User(String firstName, String lastName, Date dateOfBirth, String organizationUnit, String team, Integer experienceLevel) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.organizationUnit = organizationUnit;
        this.team = team;
        this.experienceLevel = experienceLevel;
    }

    public boolean validateDateOfBirth(){ //check if date is after 1900-01-01

        if (this.dateOfBirth == null)
            return false;
        else
            return !(this.dateOfBirth.after(Date.from((LocalDate.of(1900,1,1).atStartOfDay( ZoneId.of("Europe/Paris")).toInstant()))));
    }

    public void addToModificationList(String modification){

        this.modificationHistory.add(modification);
    }

    public void deleteManyModifications(List<String> deletionList){

        this.modificationHistory.removeAll(deletionList);
    }

    public Integer calculateAgeInYears(){

        LocalDate birthDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
