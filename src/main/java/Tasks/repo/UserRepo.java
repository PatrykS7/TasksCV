package Tasks.repo;

import Tasks.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends MongoRepository<User,String> {

    List<User> findAllByDeletedFalseOrDeletedNull();
    Optional<User> findByIdAndDeletedFalseOrDeletedNull(String id);

    List<User> findAllByDeletedTrue();

}
