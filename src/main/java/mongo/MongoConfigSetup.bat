set replSet=configEssex
START /B mongod --configsvr --replSet %replSet%
timeout 3
set replSetConfig={_id: '%replSet%', members: [{_id: 1, host: '%COMPUTERNAME%:27019'}]}
mongo %COMPUTERNAME%:27019 --eval "JSON.stringify(db.adminCommand({'replSetInitiate' : %replSetConfig%}))"