set replSet=essex
START /B mongod --shardsvr --replSet %replSet%
ping 127.0.0.1 -n1 -w 3000 >NUL 
START /B mongos --config %~dp0\mongos.conf
