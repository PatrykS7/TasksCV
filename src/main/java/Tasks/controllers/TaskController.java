package Tasks.controllers;

import Tasks.entities.Task;
import Tasks.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public List<Task> getAllTasks(){

        return taskService.getAllTasks();
    }

    @GetMapping("/allDeleted")
    public List<Task> getAllDeletedTasks(){

        return taskService.getAllDeletedTasks();
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id){

        return taskService.getTaskById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<Task> saveTask(@Valid @RequestBody Task task, Errors errors){

        return taskService.saveTask(task, errors);
    }

    @PutMapping("/put")
    public ResponseEntity<Task> putTask(@Valid @RequestBody Task task, Errors errors){

        return taskService.putTask(task, errors);
    }

    @PatchMapping("/patch")
    public ResponseEntity<Task> patchTask(@RequestBody Task task, Errors errors){

        return taskService.patchTask(task, errors);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteTaskById(@PathVariable String id){

        return taskService.deleteTaskById(id);
    }
}
