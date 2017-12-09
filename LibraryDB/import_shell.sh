json_list=`ls *.json`
for json_file in $json_list
do
    table_name=${json_file::-5}
    rethinkdb import -f $json_file --table LibraryDB.$table_name
done
echo import complete!
