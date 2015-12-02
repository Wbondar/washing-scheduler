#!/bin/sh
source database.conf
echo 'NOTE: Make sure that you in washing-scheduler-datasource project root directory, before running this script. SQL scripts lookup relies on that.'
echo 'Clear up.'
sudo -u postgres dropdb $DATABASE
sudo -u postgres dropuser $USER
echo 'Provide password for database owner.'
sudo -u postgres createuser -I -P $USER
echo 'Create database.'
sudo -u postgres createdb --owner=$USER $DATABASE
echo 'Load database schema.'
sudo -u postgres psql -U $USER -d $DATABASE -f src/main/sql/baseline.sql
