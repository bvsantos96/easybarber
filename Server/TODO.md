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

## Create requests postman (DONE)
Create request in postman for every available endpoint

## Create tests
Create a group of tests that for a clean database run all the functionality (same as the requests available) as checks if the result is what we expected

## Define public requests
Make requests that should be public available with no authentication

## Make list requests pageable
- [ ] Make all list requests pageable
- [ ] Update all list requests in postman to use the pageable methodology

### Questions:
- Do we really need a table for user_type and a field for user_type_id if we already have a Employee table?
