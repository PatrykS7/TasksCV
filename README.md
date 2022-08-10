# TasksCV

This is a simple backend application i made for my last job interview.

Application allows to create users and tasks that will be automaticlly assigned to user if conditions are met.
Conditions are:
 - user.organizationUnit == task.organizationUnit
 - user.team == task.team
 - user.experienceLevel >= task.minimumExperienceLevel
 - user.ageInYears() <= task.maximumAgeInYears
 
 If the user variable allConditionsMustBeSatisfied if equal to true all conditions must be satisfied oherwise only one of them.
 Most of the fields are validated on input so you can't assign random values to them.
 
 There is also a system that records all the changes to task and users and allows to reverse reverse them back to a specific date.
 
 I added a Dockerfile and a docker-compose.yaml file that creates a MongoDB database and set's up this application in containers. Spring app is exposed on port 8090 and mongodb on port 27018.
