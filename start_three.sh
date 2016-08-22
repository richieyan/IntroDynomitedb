bin/dynomite -c conf/node1.yml -s 22221 -d --output=node1.log
bin/dynomite -c conf/node2.yml -s 22222 -d --output=node2.log
bin/dynomite -c conf/node3.yml -s 22223 -d --output=node3.log

redis-cli -h 10.130.138.47 -p 8102
redis-cli -h 10.130.138.47 -p 8202
redis-cli -h 10.130.138.47 -p 8302


