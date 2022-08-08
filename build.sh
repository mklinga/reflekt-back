#!/bin/bash

set -e

mvn clean package
scp -i ~/reflekt-back-pair.pem target/reflekt-0.0.1-SNAPSHOT.jar ec2-user@ec2-16-171-60-177.eu-north-1.compute.amazonaws.com:~
