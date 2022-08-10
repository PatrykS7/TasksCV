package Tasks.services;

import Tasks.entities.Task;
import Tasks.entities.User;
import Tasks.entities.UserModificationHistory;
import Tasks.repo.TaskRepo;
import Tasks.repo.UserModificationHistoryRepo;
import Tasks.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final TaskRepo taskRepo;
    private final UserModificationHistoryRepo userModificationHistoryRepo;

    @Autowired
    public UserService(UserRepo userRepo, TaskRepo taskRepo, UserModificationHistoryRepo userModificationHistoryRepo) {
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.userModificationHistoryRepo = userModificationHistoryRepo;
    }

    @Autowired
    Validator validator;

    public List<User> getAllUsers() {

        List<User> users = userRepo.findAllByDeletedFalseOrDeletedNull();
        users.forEach( u -> u.setTasks(fetchTasksPerUser(u))); //get tasks per user

        return users;
    }

    public List<User> getAllDeletedUsers() {

        return userRepo.findAllByDeletedTrue();
    }

    public ResponseEntity<User> getUerById(String id) {

        Optional<User> user = userRepo.findByIdAndDeletedFalseOrDeletedNull(id);

        if(user.isPresent()) {

            user.get().setTasks(fetchTasksPerUser(user.get()));
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<User> saveUser(User user, Errors errors) {

        if(errors.hasErrors() || user.validateDateOfBirth()) //check validation errors
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        else {

            var userModHis = userModificationHistoryRepo.save(new UserModificationHistory(user)); // save modification
            user.addToModificationList(userModHis.getId()); // add modification to list

            return new ResponseEntity<>(userRepo.save(user), HttpStatus.CREATED);
        }
    }

    public ResponseEntity<User> putUser(User user, Errors errors) {

        if(errors.hasErrors() || user.validateDateOfBirth()) //check validation errors
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        else {

            var userModHis = userModificationHistoryRepo.save(new UserModificationHistory(user)); // save modification
            user.addToModificationList(userModHis.getId()); // add modification to list

            return new ResponseEntity<>(userRepo.save(user), HttpStatus.OK);
        }
    }

    public ResponseEntity<User> patchUser(User patchUser, Errors errors) {

            Optional<User> DBUser = userRepo.findById(patchUser.getId());

            if (DBUser.isPresent()) {

                if (patchUser.getFirstName() != null)
                    DBUser.get().setFirstName(patchUser.getFirstName());
                if (patchUser.getLastName() != null)
                    DBUser.get().setLastName(patchUser.getLastName());
                if (patchUser.getDateOfBirth() != null)
                    DBUser.get().setDateOfBirth(patchUser.getDateOfBirth());
                if (patchUser.getOrganizationUnit() != null)
                    DBUser.get().setOrganizationUnit(patchUser.getOrganizationUnit());
                if (patchUser.getTeam() != null)
                    DBUser.get().setTeam(patchUser.getTeam());
                if (patchUser.getExperienceLevel() != null)
                    DBUser.get().setExperienceLevel(patchUser.getExperienceLevel());

                validator.validate(DBUser.get(), errors); //validate updated object
                if(errors.hasErrors() || DBUser.get().validateDateOfBirth())
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                else {

                    patchUser.setId(DBUser.get().getId()); //setup modification data
                    patchUser.setDeleted(null);
                    var userModHis = userModificationHistoryRepo.save(new UserModificationHistory(patchUser));
                    DBUser.get().addToModificationList(userModHis.getId()); // add modification to list

                    return new ResponseEntity<>(userRepo.save(DBUser.get()), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> deleteUserById(String id) {

        Optional<User> user = userRepo.findById(id);

        if (user.isPresent()){

            user.get().addToModificationList(userModificationHistoryRepo.save(new UserModificationHistory(true)).getId()); //add modification history

            user.get().setDeleted(true);
            userRepo.save(user.get());
        }
        return ResponseEntity.noContent().build();
    }

    private List<Task> fetchTasksPerUser(User user){

        Integer age = user.calculateAgeInYears();

        List<Task> tasks = taskRepo.findAllByOneCriteriaOrderByPriority(user.getOrganizationUnit(),user.getTeam(),user.getExperienceLevel(),age);

        for(int i=tasks.size()-1 ; i>=0 ; i--){ //check if all conditions must be satisfied and remove wrong tasks

            if(tasks.get(i).getAllConditionsMustBeSatisfied() == null)
                tasks.get(i).setAllConditionsMustBeSatisfied(false);

            if(tasks.get(i).getAllConditionsMustBeSatisfied())
                if(!( (tasks.get(i).getOrganizationUnit().equals(user.getOrganizationUnit())) && (tasks.get(i).getTeam().equals(user.getTeam())) && (tasks.get(i).getMinimumExperienceLevel() <= user.getExperienceLevel()) && (tasks.get(i).getMaximumAgeInYears() >= age) )){
                    tasks.remove(i);
                }
        }
        return tasks;
    }
}
