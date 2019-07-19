#!/bin/bash

#expect -c "
#spawn scp target/jewelry-support-server-0.0.1.jar root@47.100.28.58:/opt/jewelry-sup-server
#expect {
#\"*assword\" {set timeout 300; send \"password\r\";}
#\"yes/no\" {send \"yes\r\"; exp_continue;}
#}
#expect eof"

scp target/jewelry-support-server-0.0.1.jar root@47.100.28.58:/opt/jewelry-sup-server

