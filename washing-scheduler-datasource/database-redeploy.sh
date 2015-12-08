#!/bin/sh
source database.conf
echo 'NOTE: Make sure that you in washing-scheduler-datasource project root directory, before running this script. SQL scripts lookup relies on that.'
echo 'Clear up.'
sudo -u postgres dropdb $WASHING_SCHEDULER_DATABASE
sudo -u postgres dropuser $WASHING_SCHEDULER_DATABASE_USERNAME
echo 'Provide password for database owner.'
sudo -u postgres createuser -I -P $WASHING_SCHEDULER_DATABASE_USERNAME
echo 'Create database.'
sudo -u postgres createdb --owner=$WASHING_SCHEDULER_DATABASE_USERNAME $WASHING_SCHEDULER_DATABASE
echo 'Load database schema.'
psql -U $WASHING_SCHEDULER_DATABASE_USERNAME -d $WASHING_SCHEDULER_DATABASE -f src/main/sql/baseline.sql
source database-populate.sh
