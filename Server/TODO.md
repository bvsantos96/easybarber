# TODO:
Tasks to do before moving to the rest of the tasks defined

## Refactor employee (DONE)
Restructure employee related code (from user type to employee class)

### List (DONE)
- [x] Create employee
    - [x] Check if this is working, existsByUserId might need to be implemented
- [x] Create establishment
- [x] Add employee to establishment
- [x] Validations for isEmployee and can employee change establishment
- [x] Register services to employee 
- [x] List employee services
- [x] List employee establishments
- [x] Update user info
- [x] Delete user

## Create requests postman (DONE)
Create request in postman for every available endpoint

## Transactional methods (DONE)
Make it so every method that does more than one write from DB has a @Transaction flag

## Make list requests pageable (DONE)
- [x] Make all list requests pageable
- [x] Update all list requests in postman to use the pageable methodology

## Define public requests (DONE)
Make requests that should be public available with no authentication

## User deletion
- Deletion of users and employee should not really delete the entities, simple disable them.
    - We need to have information regarding the appointments they had. Users can still see with who they had their appointment even if the Employee is no longer present in the application
- We need to alert the users that their appointments will be deleted since the employee that they are booked with will be deleted

## Create tests
Create a group of tests that for a clean database run all the functionality (same as the requests available) as checks if the result is what we expected

## Test functionality
Test all available constroller requests

## Questions:
- Do we really need a table for user_type and a field for user_type_id if we already have a Employee table?

## TODO's:
See TODO's present in the code and fix them

