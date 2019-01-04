#!/bin/sh
curl --header "Content-Type: application/json" --request POST --data '{"firstName": "John","lastName": "Scott","age": 30}' http://localhost:8090/customer
curl --header "Content-Type: application/json" --request POST --data '{"number": "1234567890","amount": 5000,"customerId": "5aec1debfa656c0b38b952b4"}' http://localhost:8090/account
curl --header "Content-Type: application/json" --request POST --data '{"number": "1234567891","amount": 12000,"customerId": "5aec1debfa656c0b38b952b4"}' http://localhost:8090/account
curl --header "Content-Type: application/json" --request POST --data '{"number": "1234567892","amount": 2000,"customerId": "5aec1debfa656c0b38b952b4"}' http://localhost:8090/account
