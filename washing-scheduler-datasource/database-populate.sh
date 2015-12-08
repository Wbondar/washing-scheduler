#!/bin/sh
source database.conf
psql -U postgres -d $WASHING_SCHEDULER_DATABASE -f 'src/test/sql/populate.sql'
