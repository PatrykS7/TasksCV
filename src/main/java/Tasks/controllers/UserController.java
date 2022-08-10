package Tasks.controllers;

import Tasks.entities.User;
import Tasks.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){

        return userService.getAllUsers();
    }

    @GetMapping("/allDeleted")
    public List<User> getAllDeletedUsers(){

        return userService.getAllDeletedUsers();
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id){

        return userService.getUerById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user, Errors errors){

        return userService.saveUser(user, errors);
    }

    @PutMapping("/put")
    public ResponseEntity<User> putUser(@Valid @RequestBody User user, Errors errors){

        return userService.putUser(user, errors);
    }

    @PatchMapping("/patch")
    public ResponseEntity<User> patchUser(@RequestBody User user, Errors errors){

        return userService.patchUser(user, errors);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String id){

        return userService.deleteUserById(id);
    }
}
