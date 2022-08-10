package Tasks.services;

import Tasks.entities.Task;
import Tasks.entities.TaskModificationHistory;
import Tasks.repo.TaskModificationHistoryRepo;
import Tasks.repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private final MongoOperations mongoOperations;
    private final TaskModificationHistoryRepo taskModificationHistoryRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo, MongoOperations mongoOperations, TaskModificationHistoryRepo taskModificationHistoryRepo) {
        this.taskRepo = taskRepo;
        this.mongoOperations = mongoOperations;
        this.taskModificationHistoryRepo = taskModificationHistoryRepo;
    }

    @Autowired
    Validator validator;

    public List<Task> getAllTasks() {

        return taskRepo.findAllByDeletedFalseOrDeletedNull();
    }

    public List<Task> getAllDeletedTasks() {

        return taskRepo.findAllByDeletedTrue();
    }

    public ResponseEntity<Task> getTaskById(String id) {

        Optional<Task> task = taskRepo.findByIdAndDeletedFalseOrDeletedNull(id);

        if(task.isPresent())
            return new ResponseEntity<>(task.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Task> saveTask(Task task, Errors errors) {

        if(errors.hasErrors() || task.fieldsAreNullValidation()) //check validation errors
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        else {

            var taskModHis = taskModificationHistoryRepo.save(new TaskModificationHistory(task)); // save modification
            task.addToModificationList(taskModHis.getId());  // add modification to list

            return new ResponseEntity<>(taskRepo.save(task), HttpStatus.CREATED);
        }
    }

    public ResponseEntity<Task> putTask(Task task, Errors errors) {

        if(errors.hasErrors() || task.fieldsAreNullValidation()) //check validation errors
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        else {

            var taskModHis = taskModificationHistoryRepo.save(new TaskModificationHistory(task)); // save modification
            task.addToModificationList(taskModHis.getId());  // add modification to list

            return new ResponseEntity<>(taskRepo.save(task), HttpStatus.OK);
        }
    }

    public ResponseEntity<Task> patchTask(Task task, Errors errors) {

        Optional<Task> DBTask = taskRepo.findById(task.getId());

        if(DBTask.isPresent()){

            if(task.getTitle() != null)
                DBTask.get().setTitle(task.getTitle());
            if(task.getNumberOfSubtasks() != null)
                DBTask.get().setNumberOfSubtasks(task.getNumberOfSubtasks());
            if(task.getPriority() != null)
                DBTask.get().setPriority(task.getPriority());
            if(task.getCreationDate() != null)
                DBTask.get().setCreationDate(task.getCreationDate());
            if(task.getAllConditionsMustBeSatisfied() != null)
                DBTask.get().setAllConditionsMustBeSatisfied(task.getAllConditionsMustBeSatisfied());
            if(task.getOrganizationUnit() != null)
                DBTask.get().setOrganizationUnit(task.getOrganizationUnit());
            if(task.getTeam() != null)
                DBTask.get().setTeam(task.getTeam());
            if(task.getMinimumExperienceLevel() != null)
                DBTask.get().setMinimumExperienceLevel(task.getMinimumExperienceLevel());
            if(task.getMaximumAgeInYears() != null)
                DBTask.get().setMaximumAgeInYears(task.getMaximumAgeInYears());

            validator.validate(DBTask.get(), errors); //validate updated object
            if(errors.hasErrors() || DBTask.get().fieldsAreNullValidation())
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            else{

                task.setId(DBTask.get().getId());
                task.setDeleted(null);
                var taskModHis = taskModificationHistoryRepo.save(new TaskModificationHistory(task)); // save modification
                DBTask.get().addToModificationList(taskModHis.getId());  // add modification to list

                return new ResponseEntity<>(taskRepo.save(DBTask.get()),HttpStatus.OK);
            }

        }
        else
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> deleteTaskById(String id) {

        Update update = new Update();
        update.set("deleted", true);
        update.push("modificationHistory", taskModificationHistoryRepo.save(new TaskModificationHistory(true)).getId()); //add to modification history

        mongoOperations.findAndModify(
                Query.query(Criteria.where(("id")).is(id)), update, Task.class);

        return ResponseEntity.noContent().build();
    }
}