# NBP Exchange Api

A simple REST application to retrieve exchange rate data directly from the [NBP](http://api.nbp.pl/) API.

- [Description](#description)
- [Setup](#setup)
- [Docs](#docs)
  - [Initialization](#initialization)
  - [Endpoints](#endpoints)
    - [Getting rates](#getting-all-rates)
    - [Making trade](#making-trade)
    - [Trade history](#trade-history)
    - [Error responses](#error-responses)

**The app is available on [Heroku](https://nbp-exchange-api.herokuapp.com/api/rates/recent?currency=PLN)! (free tier, so it may not be available a few hours a day)**
# Description
The app is based on the NBP API, specifically Tables A and B, so *de facto* uses averaged rates.
It allows conversion of currency conversions and the addition and review of transactions.
The updates occur Monday to Friday at 12:31 p.m. This is because the NBP updates its data at around this time.
# Setup

Make sure you have [git](https://git-scm.com/) installed.
The application uses Postgres as the database by default.
It is required to provide database connection URL and database user conditionals for an application to run.  
**If you do not want to play around with the database configuration you can use the 'H2' profile. Explained below**.

### Maven

**Java 17 is required for this step**  
Clone repository and enter its folder:

```
git clone https://github.com/doomedcat17/nbp-exchange-api.git
cd nbp-exchange-api
```

And you can run the app using spring-boot maven plugin providing your postgres url and conditionals:

```
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=databaseURLHere --spring.datasource.username=databaseUserHere,--spring.datasource.password=databaseUserPasswordHere"
```

Or you can use H2 profile instead:

```
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2
```
It will use H2 database in-memory mode, so none of data will be persisted.

### Docker

Clone repository and enter its folder:

```
git clone https://github.com/doomedcat17/nbp-exchange-api.git
cd nbp-exchange-api
```

And build Docker Image and run the container providing database conditionals via environment variables:

```
docker build -t gpu-price-api:1.0 . 
docker run -d -p 8080:8080 \
-e DB_USER='db_user' \
-e DB_PASSWORD='superStrongPassword' \
-e DB_HOST='db_host' \
-e DB_PORT='3306' \
-e DB_NAME='db_name' \
--name gpu-price-api \
gpu-price-api:1.0
```

Alternatively you can use docker compose to easily setup application container and MySQL database:

```
docker compose -f docker-compose.yaml up -d
```

It will create MySQL database container and the app itself.
It will run on port 80, so make sure it is available.


## Docs

### Initialization

When we start the application, the initialisation process will begin. The application will create a database structure and
will download the rates data from **the last seven working days. Including today**.
If the process fails (no connection), the application will repeat the initialisation attempt every 15 minutes.

### Properties

The application uses several custom properties:

Property                                            | Default value | Description                                                                                                                                          |
|-----------------------------------------------------|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| `doomedcat17.nbp-exchange-api.updater.sleep-time-on-failure`    | 900           | number of seconds the application must wait, to attempt to update the currency rates again.                                                          |
| `doomedcat17.nbp-exchange-api.rates-ttl-in-workdays:0` | 0             | number of working days after which the currency rate will be deleted from the database. If it is zero, the exchange rates will never be removed.     |
| `doomedcat17.nbp-exchange-api.transactions-ttl-in-workdays`               | 0             | number of working days after which the currency transaction will be deleted from the database. If it is zero, the transaction will never be removed. |
| `doomedcat17.nbp-exchange-api.initialization.enabled`  | true          | if disabled, the app will skip intialization proces.                                                                                                 |
| `doomedcat17.nbp-exchange-api.scheduling.enabled`      | true          | if disabled, the app wont't perform any update.                                                                                                      |
| `doomedcat17.nbp-exchange-api.page-size`       | 50            | max number of elements per response.                                                                                                                 |

### Endpoints

#### Getting all rates

Return rates based on parameters.

```
/api/rates
```


| Paramater | Default value | Details                                             |
|-----------|---------------|-----------------------------------------------------|
| *currency | None          | Returns all exchange rates for given currency.      |
| targetCurrency    | None          | Returns all `currency` rates in the `targetCurrency`. |
| effectiveDate  | None          | Returnes all exchange rates from given date in ISO format |
| page      | *1*           | Pagination parameter. Max 50 results per page.      |

*Required parameter

<details><summary><b>Sample Response</b></summary>
<p>

```json
{
  "page": 1,
  "totalPages": 8,
  "results": [
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "NOK",
      "rate": 10.225112
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "MYR",
      "rate": 4.449123
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "BRL",
      "rate": 5.423564
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "HRK",
      "rate": 7.494688
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "PHP",
      "rate": 56.364277
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "BGN",
      "rate": 1.949758
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "CNY",
      "rate": 6.760536
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "THB",
      "rate": 36.615267
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "HKD",
      "rate": 7.850409
    }
  ]
}
```

</p>
</details>

#### Getting most recent rates

Return most recent rates based on parameters.

```
/api/rates/recent
```


| Paramater | Default value | Details                                             |
|-----------|---------------|-----------------------------------------------------|
| *currency | None          | Returns all exchange rates for given currency.      |
| targetCurrency    | None          | Returns all `currency` rates in the `targetCurrency`. |

*Required parameter

<details><summary><b>Sample Response</b></summary>
<p>

```json
{
  "page": 1,
  "totalPages": 8,
  "results": [
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "NOK",
      "rate": 10.225112
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "MYR",
      "rate": 4.449123
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "BRL",
      "rate": 5.423564
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "HRK",
      "rate": 7.494688
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "PHP",
      "rate": 56.364277
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "BGN",
      "rate": 1.949758
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "CNY",
      "rate": 6.760536
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "THB",
      "rate": 36.615267
    },
    {
      "effectiveDate": "2022-07-15",
      "code": "USD",
      "targetCode": "HKD",
      "rate": 7.850409
    }
  ]
}
```

</p>
</details>

#### Making trade

Performs a transaction and returns the result.

##### GET method
```
/api/trade/{buyCurrencyCode}/{sellCurrencyCode}/{buyAmount}
```


| Paramater | Default value | Details                                             |
|-----------|---------------|-----------------------------------------------------|
| *currency | None          | Returns all exchange rates for given currency.      |
| targetCurrency    | None          | Returns all `currency` rates in the `targetCurrency`. |


<details><summary><b>Sample Response</b></summary>
<p>

```json
{
  "date": "2022-07-15T21:35:38.405323768",
  "buyCode": "USD",
  "buyAmount": 100.00,
  "sellCode": "PLN",
  "sellAmount": 479.66
}
```

</p>
</details>

##### POST method

```
/api/trade
```

Request body:
```json
{
  "sellCode": "PLN",
  "buyCode": "USD",
  "buyAmount": 100.00
}
```


<details><summary><b>Sample Response</b></summary>
<p>

```json
{
  "date": "2022-07-15T21:35:38.405323768",
  "buyCode": "USD",
  "buyAmount": 100.00,
  "sellCode": "PLN",
  "sellAmount": 479.66
}
```
</p>
</details>

#### Trade history

Return history of transactions based on parameters.

```
/api/trade
```


| Paramater | Default value | Details                                        |
|-----------|---------------|------------------------------------------------|
| startDate | None          | Returns all transaction since this date.       |
| endDate   | None          | Returns all transaction since to this date.    |
| page      | *1*           | Pagination parameter. Max 50 results per page. |


<details><summary><b>Sample Response</b></summary>
<p>

```json
{
  "date": "2022-07-15T21:35:38.405323768",
  "buyCode": "USD",
  "buyAmount": 100.00,
  "sellCode": "PLN",
  "sellAmount": 479.66
}
```

</p>
</details>

#### Error responses

Table of possible error responses:

| Cause                     | Code | Response body |
|---------------------------| ------------- | ------------- |
| Invalid resource path     | 404  | `{"title": "Not Found", "status": 404,"detail": "No handler found for GET /"}` |
| Missing currecy parameter | 400  | `{"title": "Bad Request", "status": 400,"detail": "Required request parameter 'currency' for method parameter type String is not present"}`|





