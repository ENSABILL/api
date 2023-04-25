
# How to run project (if you want use docker follow these steps) :

## Prerequisites
1. install docker
2. linux or use wsl2 on windows  (on windows without wsl2 it might not work well so prefer run project and configure it yourself)

## Run backend + phpmyadmin + mysql containers
```bash
./start_all.sh
```
#### springboot app is accessible on : http://localhost:8080
#### mysql is accessible on : port 3307  user = root password=root (in case you want to use it)
#### phpmyadmin is accessible on : http://localhost:81 user=root password=root

## stop all containers
```bash
./stop_all.sh
```



