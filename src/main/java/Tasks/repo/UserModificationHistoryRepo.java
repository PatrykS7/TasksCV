package Tasks.repo;

import Tasks.entities.UserModificationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;

public interface UserModificationHistoryRepo extends MongoRepository<UserModificationHistory, String> {

    @Query( value = "{ $and: [ {'id': ?0}, {'modificationDate': { $lte: ?1}} ] }")
    UserModificationHistory findTaskBeforeDate(String id, Date date);

    @Query( value = "{ $and: [ {'id': ?0}, {'modificationDate': { $gt: ?1}} ] }", delete = true)
    UserModificationHistory deleteTasksAfterDate(String id, Date date);

}
