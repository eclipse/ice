@echo off
FOR /f "tokens=*" %%i IN ('docker-machine env --shell cmd default') DO %%i
SET DOCKER