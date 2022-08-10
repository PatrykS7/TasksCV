package Tasks.repo;

import Tasks.entities.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends MongoRepository<Task,String> {

    List<Task> findAllByDeletedFalseOrDeletedNull();
    Optional<Task> findByIdAndDeletedFalseOrDeletedNull(String id);

    List<Task> findAllByDeletedTrue();

    @Query( value = "{ $or: [ {'organizationUnit': ?0} , {'team': ?1}, {'minimumExperienceLevel': {$gte: ?2}}, {'maximumAgeInYears': {$lte: ?3}} ] , $or: [ {'deleted': false} , {'deleted': {$exists: false}} ] }", sort = "{ 'priority': -1}")
    List<Task> findAllByOneCriteriaOrderByPriority(String organization, String team, Integer experienceLevel, Integer ageInYears);
}
