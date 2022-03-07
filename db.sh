#!/bin/bash

podman run --rm -e POSTGRES_USER=reflekt -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=reflekt -v /home/m/reflekt-postgres-data:/var/lib/postgresql/data -p 5432:5432 --name db postgres

