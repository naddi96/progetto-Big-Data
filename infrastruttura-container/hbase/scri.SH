#!/bin/bash
echo "create '<table_name>', '<column_family_name>'" | hbase shell -n
status=$?
if [$status -ne 0]; then
  echo "The command may have failed."
fi
