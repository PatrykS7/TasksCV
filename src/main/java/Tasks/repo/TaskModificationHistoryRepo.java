package Tasks.repo;

import Tasks.entities.TaskModificationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskModificationHistoryRepo extends MongoRepository<TaskModificationHistory,String> {

}
