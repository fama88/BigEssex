set replSet=essex
set DBName=essexDB
set collectionName=essexCollection
mongo %COMPUTERNAME%:27017 --eval "JSON.stringify(sh._adminCommand({ addShard : '%replSet%/%COMPUTERNAME%:27018' }))"
mongo %COMPUTERNAME%:27017 --eval "JSON.stringify(sh._adminCommand( { enableSharding : '%DBName%' } ))"
mongo %COMPUTERNAME%:27017 --eval "JSON.stringify(sh._adminCommand( { shardCollection : '%DBName%.%collectionName%', key : {_id : 1 } } ))"