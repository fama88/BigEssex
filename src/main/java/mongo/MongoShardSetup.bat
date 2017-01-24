
set replSet=essex
START /B mongod --shardsvr --replSet %replSet%
timeout 3
set replSetConfig={_id: '%replSet%', members: [{_id: 1, host: '%COMPUTERNAME%:27018'}]}
mongo %COMPUTERNAME%:27018 --eval "JSON.stringify(db.adminCommand({'replSetInitiate' : %replSetConfig%}))"

START /B mongos --config %~dp0\mongos.conf
timeout 3
set DBName=essexDB
set collectionName=essexCollection
mongo %COMPUTERNAME%:27017 --eval "JSON.stringify(sh._adminCommand({ addShard : '%replSet%/%COMPUTERNAME%:27018' }))"
mongo %COMPUTERNAME%:27017 --eval "JSON.stringify(sh._adminCommand( { enableSharding : '%DBName%' } ))"
mongo %COMPUTERNAME%:27017 --eval "JSON.stringify(sh._adminCommand( { shardCollection : '%DBName%.%collectionName%', key : {_id : 1 } } ))"