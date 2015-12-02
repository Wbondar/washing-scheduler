#!/bin/sh
source database.conf
psql -U $USER -d $DATABASE
