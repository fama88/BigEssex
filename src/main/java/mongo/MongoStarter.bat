set replSet=configEssex
START /B mongod --configsvr --replSet %replSet%
timeout 3
set replSet=essex
START /B mongod --shardsvr --replSet %replSet%
timeout 3
START /B mongos --config %~dp0\mongos.conf