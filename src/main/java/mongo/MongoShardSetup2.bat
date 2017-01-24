set replSet=essex
set replSetConfig={_id: '%replSet%', members: [{_id: 1, host: '%COMPUTERNAME%:27018'}]}
mongo %COMPUTERNAME%:27018 --eval "JSON.stringify(db.adminCommand({'replSetInitiate' : %replSetConfig%}))"