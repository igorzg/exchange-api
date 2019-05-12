# exchange api
Spring boot 
# exchange ui
Angular - not done fully I need to connect to REST apis

# Requirements

* [Docker](https://docs.docker.com/install/)

# Running
```
docker-compose up
```
Command will compile java code and run in docker, it can take a while.


## REST API
Currency converter - GET 
* http://localhost:5010/currency/eur/chf/20
* http://localhost:5010/currency/usd/chf/20
* http://localhost:5010/currency/chf/usd/20
* http://localhost:5010/currency/hrk/chf/20

List all available dates - GET
* http://localhost:5010/currency/dates

List all Cubs - GET
* http://localhost:5010/currency
