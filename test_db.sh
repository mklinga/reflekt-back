#!/bin/bash

podman run --rm -e POSTGRES_USER=reflekt -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=reflekt -p 5433:5432 --name test-db postgres

