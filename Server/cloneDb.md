# Create sql dump file 
```bash
mysqldump -h 172.233.245.119 -P 3306 -u teamsantos -p easy_barber_testing > easy_barber_testing_dump.sql
```

# Restore database from sql dump file
```bash
mysql -h 172.233.245.119 -P 3306 -u teamsantos -p easy_barber_stagging < easy_barber_testing_dump.sql
```
