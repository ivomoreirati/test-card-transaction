This is a Java Spring implementation for receiving a flow of card transactions, for creating an account and receiving purchase transactions.
Where each operation is submitted to a set of validations, it is possible to add future validations with low compliance and low impact.

To run it locally with docker:

- Create Package: mvn clean install

- Create image Docker:** docker build --tag=authorize-1.0-snapshot .

- Create and execute Container:** docker run -p 80:8080 authorize-1.0-snapshot


The application was developed in the Stdin and Stdout mode, so just copy the stream in json format on the console and the application will process the input. For example:

{"account": {"active-card": true, "available-limit": 200}}
{"transaction": {"merchant": "MacDonalds", "amount": 20, "time": "2019-02-13T10:01:00.000Z"}}
{"transaction": {"merchant": "Habibs", "amount": 20, "time": "2019-02-13T10:02:00.000Z"}}
{"transaction": {"merchant": "Americanas", "amount": 20, "time": "2019-02-13T10:03:00.000Z"}}
{"transaction": {"merchant": "Americanas", "amount": 15, "time": "2019-02-13T10:04:00.000Z"}}

Result example, possible validations returned:

{"account":{"active-card":true,"available-limit":140},"violations":["high-frequency-small-interval"]}
{"account":{"active-card":true,"available-limit":140},"violations":["doubled-transaction","high-frequency-small-interval"]}
{"account":{"active-card":true,"available-limit":140},"violations":["doubled-transaction","high-frequency-small-interval"]}
{"account":{"active-card":true,"available-limit":140},"violations":["doubled-transaction","high-frequency-small-interval"]}

Tools: 
- Java 11
- SpringBoot
- H2 - in-memory structure
