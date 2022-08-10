package Tasks.controllers;

import Tasks.JSON.IdAndDate;
import Tasks.entities.TaskModificationHistory;
import Tasks.entities.User;
import Tasks.entities.UserModificationHistory;
import Tasks.services.ModificationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class ModificationHistoryController {

    private final ModificationHistoryService modificationService;

    @Autowired
    public ModificationHistoryController(ModificationHistoryService modificationService) {
        this.modificationService = modificationService;
    }

    @GetMapping("/findByUserId/{id}")
    public ResponseEntity<List<UserModificationHistory>> getHistoryByUserId(@PathVariable String id){

        return modificationService.getHistoryByUserId(id);
    }

    @GetMapping("/findByTaskId/{id}")
    public ResponseEntity<List<TaskModificationHistory>> getHistoryByTaskId(@PathVariable String id){

        return modificationService.getHistoryByTaskId(id);
    }

    @PatchMapping("/goBackToDate")
    public ResponseEntity<User> goBackToDate(@RequestBody IdAndDate idAndDate){

        return modificationService.goBackToDate(idAndDate);
    }
}
