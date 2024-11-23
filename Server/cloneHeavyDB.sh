#!/bin/bash

# Update these to your actual values
DB_HOST="172.233.245.119"
DB_PORT="3306"
DB_USER="teamsantos"
DB_PASSWORD="PMnLZYu4jVfT-U2" 
DB_NAME_TEST="easy_barber_stagging_heavy"
DUMP_FILE="heavy_db.sql"

# Create SQL dump file with all data, triggers, routines, indexes, keys, etc.
mysqldump -h $DB_HOST -P $DB_PORT -u $DB_USER -p$DB_PASSWORD $DB_NAME_TEST \
  --routines \
  --triggers \
  --events \
  --single-transaction \
  --quick \
  --add-drop-database \
  --add-drop-table \
  --add-locks \
  --disable-keys \
  > $DUMP_FILE
