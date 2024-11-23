#!/bin/bash

# Update these to your actual values
DB_HOST="172.233.245.119"
DB_PORT="3306"
DB_USER="teamsantos"
DB_PASSWORD="PMnLZYu4jVfT-U2" 
DB_NAME_TEST="easy_barber_testing"
DUMP_FILE="easy_barber_test_schema.sql"

# Create SQL dump file
mysqldump -h $DB_HOST -P $DB_PORT -u $DB_USER -p$DB_PASSWORD $DB_NAME_TEST > $DUMP_FILE --no-data
