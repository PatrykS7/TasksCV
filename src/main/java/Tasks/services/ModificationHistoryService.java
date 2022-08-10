package Tasks.services;

import Tasks.JSON.IdAndDate;
import Tasks.entities.Task;
import Tasks.entities.TaskModificationHistory;
import Tasks.entities.User;
import Tasks.entities.UserModificationHistory;
import Tasks.repo.TaskModificationHistoryRepo;
import Tasks.repo.TaskRepo;
import Tasks.repo.UserModificationHistoryRepo;
import Tasks.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ModificationHistoryService {

    private final UserModificationHistoryRepo userModificationHistoryRepo;
    private final TaskModificationHistoryRepo taskModificationHistoryRepo;
    private final UserRepo userRepo;
    private final TaskRepo taskRepo;

    @Autowired
    public ModificationHistoryService(UserModificationHistoryRepo userModificationHistoryRepo, TaskModificationHistoryRepo taskModificationHistoryRepo, UserRepo userRepo, TaskRepo taskRepo) {
        this.userModificationHistoryRepo = userModificationHistoryRepo;
        this.taskModificationHistoryRepo = taskModificationHistoryRepo;
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
    }

    public ResponseEntity<List<UserModificationHistory>> getHistoryByUserId(String id) {

        Optional<User> user = userRepo.findById(id);

        if (user.isPresent()){

            var ite = userModificationHistoryRepo.findAllById(user.get().getModificationHistory()); //TODO optimize
            List<UserModificationHistory> modificationList = StreamSupport // convert to list
                    .stream(ite.spliterator(), false)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(modificationList ,HttpStatus.OK);
        }
            else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<TaskModificationHistory>> getHistoryByTaskId(String id) {

        Optional<Task> task = taskRepo.findById(id);

        if (task.isPresent()){

            var ite = taskModificationHistoryRepo.findAllById(task.get().getModificationHistory());
            List<TaskModificationHistory> modificationList = StreamSupport // convert to list
                    .stream(ite.spliterator(), false)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(modificationList ,HttpStatus.OK);
        }
        else return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<User> goBackToDate(IdAndDate idAndDate) {

        Optional<User> user = userRepo.findById(idAndDate.getId()); //grab user

        if (user.isPresent()){

            List<UserModificationHistory> modificationHistories = new ArrayList<>();
            user.get().getModificationHistory().forEach( mh -> modificationHistories.add(userModificationHistoryRepo.findTaskBeforeDate(mh,idAndDate.getDate()))); //get all modifications before certain date
            List<String> deletionList = new ArrayList<>(); //list of modifications to be deleted

            if(modificationHistories.size() == 0) // if date was wrong
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            if(modificationHistories.get(0) == null) // if we would delete everything
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);

            for(int i=0 ; i<modificationHistories.size() ; i++){

                if (modificationHistories.get(i) != null){  //loop through history and set fields

                    if (modificationHistories.get(i).getFirstName() != null) //patch user
                        user.get().setFirstName(modificationHistories.get(i).getFirstName());
                    if (modificationHistories.get(i).getLastName() != null)
                        user.get().setLastName(modificationHistories.get(i).getLastName());
                    if (modificationHistories.get(i).getDateOfBirth() != null)
                        user.get().setDateOfBirth(modificationHistories.get(i).getDateOfBirth());
                    if (modificationHistories.get(i).getOrganizationUnit() != null)
                        user.get().setOrganizationUnit(modificationHistories.get(i).getOrganizationUnit());
                    if (modificationHistories.get(i).getTeam() != null)
                        user.get().setTeam(modificationHistories.get(i).getTeam());
                    if (modificationHistories.get(i).getExperienceLevel() != null)
                        user.get().setExperienceLevel(modificationHistories.get(i).getExperienceLevel());
                }
                else { //delete history newer than date

                    userModificationHistoryRepo.deleteTasksAfterDate(user.get().getModificationHistory().get(i), idAndDate.getDate());
                    deletionList.add(user.get().getModificationHistory().get(i));
                }
            }

            user.get().deleteManyModifications(deletionList); //delete deleted modifications from user list

            return new ResponseEntity<>(userRepo.save(user.get()), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}