# NBP Exchange Api

Prosta RESTowa aplikacja do pobierania danych o kursie walut bezpośrednio z [NBP](http://api.nbp.pl/).

- [Opis](#opis)
- [Uruchamianie](#uruchamianie)
- [Dokumentacja](#dokumentacja)
  - [Inicjalizacja](#inicjalizacja)
  - [Endpointy](#endpointy)
    - [Pobieranie kursów](#pobieranie-kursów)
    - [Wymiana walut](#wymiana-walut)
    - [Historia wymiany walut](#historia-wymiany-walut)
    - [Komunikaty błędów](#komunikaty-błędów)
- [Stack](#stack)

# Opis
Aplikacja bazuje na NBP API, a konkretnie na tablach A oraz B, więc *de facto* korzysta z kursów uśrednionych.
Zależało, aby były dostępne kursy wszystkich walut. Kursy są przechowywane do siedmiu dni roboczych i są aktualizowane w każdy dzień roboczy o godzinie 12:31.
Wynika to z tego, że mniej więcej o tej godzinie NBP aktualizuje swoją bazę danych.

# Uruchamianie

**Wymagania:** Java 17, Maven, MySQL

Skorzystaj z gita, aby sklonować repozytorium na dysk:

```
git clone https://github.com/doomedcat17/nbp-exchange-api.git
```

A następnie przejdź do folderu z aplikacją:

```
cd nbp-exchange-api
```

Do uruchomienia wymagana jest baza MySQL. 

### Docker
Jeśli masz zainstalowanego Dockera, możesz wykorzystać `docker-compose.yaml`
do szybkiego postawienia kontenera z MySQL. 

W tym celu wykonaj komendę w terminalu:

```
docker compose-up -d
```
Teraz wystarczy, że wykonasz poniższą komendę:
```
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.datasource.url=jdbc:mysql://root:rootpass@localhost:3306/exchangeDb
```
### Co jeśli mam już MySQL?

Jeżeli posiadasz już jakąś instancję bazy, możesz ją wykorzystać podając podmieniając URL w poniższej komendzie:
```
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.datasource.url=jdbc:mysql:{TU URL POŁĄCZENIA}
```

Aplikacja powinna już być gotowa do użycia.

# Dokumentacja

## Inicjalizacja

Kiedy uruchomimy aplikację rozpocznie się proces inicjalizacji. Aplikacja utworzy strukturę bazy danych oraz 
pobierze dane o kursach z **ostatnich siedmiu dni roboczych. Z dzisiaj włącznie.** 
Jeśli proces się nie powiedzie (brak połączenia), aplikacja będzie ponawiać próbę inicjalizacji co 15 minut.

Po tym procesie, będzie można zacząć zabawę z API :D

## Endpointy

### Pobieranie kursów

**Endpoint:** `/api/rates/{currencyCode}/recent`  
**Metoda:** `GET`  
**Opis:** zwraca aktualne kursy sprzedaży podanej waluty  
**Parametry:**
- `{currencyCode}`: kod waluty
<details><summary><b>Przykład</b></summary>
<p>

**Zapytanie:**
```
/api/rates/USD/recent
```

Zwraca aktualne kursy sprzedaży dolara:

```json
{
    "code": "USD",
    "rates": [
        {
            "code": "IQD",
            "effectiveDate": "2021-12-01",
            "rate": 1456.742370
        },
        {
            "code": "TOP",
            "effectiveDate": "2021-12-01",
            "rate": 2.282386
        },
        {
            "code": "DZD",
            "effectiveDate": "2021-12-01",
            "rate": 138.878176
        },
        {
            "code": "SOS",
            "effectiveDate": "2021-12-01",
            "rate": 577.613620
        },
        {
            "code": "VUV",
            "effectiveDate": "2021-12-01",
            "rate": 112.827067
        },
        {
            "code": "AWG",
            "effectiveDate": "2021-12-01",
            "rate": 1.806902
        },
        {
            "code": "THB",
            "effectiveDate": "2021-12-02",
            "rate": 33.854167
        },
        {
            "code": "UZS",
            "effectiveDate": "2021-12-01",
            "rate": 10746.335079
        },
        {
            "code": "XPF",
            "effectiveDate": "2021-12-01",
            "rate": 105.358930
        }
    ]
}
```
</p>
</details>

**Endpoint:** `/api/rates/{currencyCode}/all`  
**Metoda:** `GET`  
**Opis:** zwraca **wszyskie** kursy sprzedaży podanej waluty  
**Parametry:**
- `{currencyCode}`: kod waluty
- `effectiveDate`: opcjonalny parametr zapytania, zwraca wszystkie kursy z danego dnia

<details><summary><b>Przykład 1</b></summary>
<p>

**Zapytanie:**
```
/api/rates/USD/all
```

Zwraca wszystkie kursy sprzedaży dolara:

```json
{
    "code": "USD",
    "rates": [
        {
            "code": "IQD",
            "effectiveDate": "2021-12-01",
            "rate": 1456.742370
        },
        {
            "code": "TOP",
            "effectiveDate": "2021-12-01",
            "rate": 2.282386
        },
        {
            "code": "DZD",
            "effectiveDate": "2021-12-01",
            "rate": 138.878176
        },
        {
            "code": "SOS",
            "effectiveDate": "2021-12-01",
            "rate": 577.613620
        },
        {
            "code": "VUV",
            "effectiveDate": "2021-12-01",
            "rate": 112.827067
        },
        {
            "code": "AWG",
            "effectiveDate": "2021-12-01",
            "rate": 1.806902
        },
        {
            "code": "THB",
            "effectiveDate": "2021-12-02",
            "rate": 33.854167
        },
        {
            "code": "UZS",
            "effectiveDate": "2021-12-01",
            "rate": 10746.335079
        },
        {
            "code": "XPF",
            "effectiveDate": "2021-12-01",
            "rate": 105.358930
        }
    ]
}
```
</p>
</details>
<details><summary><b>Przykład 2</b></summary>
<p>

**Zapytanie:**
```
/api/rates/USD/all?effectiveDate=2021-12-02
```

Zwraca wszystkie kursy sprzedaży dolara z dnia 2021-12-02:

```json
{
    "code": "USD",
    "rates": [
        {
            "code": "THB",
            "effectiveDate": "2021-12-02",
            "rate": 33.854167
        },
        {
            "code": "AUD",
            "effectiveDate": "2021-12-02",
            "rate": 1.406829
        },
        {
            "code": "ZAR",
            "effectiveDate": "2021-12-02",
            "rate": 15.807393
        },
        {
            "code": "CAD",
            "effectiveDate": "2021-12-02",
            "rate": 1.278521
        },
        {
            "code": "NZD",
            "effectiveDate": "2021-12-02",
            "rate": 1.466183
        },
        {
            "code": "DKK",
            "effectiveDate": "2021-12-02",
            "rate": 6.567249
        },
        {
            "code": "CLP",
            "effectiveDate": "2021-12-02",
            "rate": 839.186119
        },
        {
            "code": "CZK",
            "effectiveDate": "2021-12-02",
            "rate": 22.457159
        },
        {
            "code": "ISK",
            "effectiveDate": "2021-12-02",
            "rate": 129.465566
        }
    ]
}
```
</p>
</details>

**Endpoint:** `/api/rates/{sourceCurrencyCode}/{targetCurrencyCode}/recent`  
**Metoda:** `GET`  
**Opis:** zwraca aktualny kurs dla podanych walut  
**Parametry:**
- `{sourceCurrencyCode}`: kod waluty, którą wymieniamy
- `{targetCurrencyCode}`: kod waluty, na którą chcemy wymienić

<details><summary><b>Przykład</b></summary>
<p>

**Zapytanie:**
```
/api/rates/USD/PLN/recent
```

Zwraca aktualny kurs wymiany dolara na złotówki:

```json
{
    "code": "USD",
    "rates": [
        {
            "code": "PLN",
            "effectiveDate": "2021-12-02",
            "rate": 4.062500
        }
    ]
}
```
</p>
</details>

**Endpoint:** `/api/rates/{currencyCode}/{targetCurrencyCode}/{effectiveDate}`  
**Metoda:** `GET`  
**Opis:** zwraca kurs dla podanych walut z podanej daty  
**Parametry:**
- `{sourceCurrencyCode}`: kod waluty, którą wymieniamy
- `{targetCurrencyCode}`: kod waluty, na którą chcemy wymienić
- `{effectiveDate}`: data kursu

<details><summary><b>Przykład</b></summary>
<p>

**Zapytanie:**
```
/api/rates/USD/PLN/2021-11-29
```

Zwraca aktualny kurs wymiany dolara na złotówki z dnia 2021-11-29:

```json
{
    "code": "USD",
    "rates": [
        {
            "code": "PLN",
            "effectiveDate": "2021-11-29",
            "rate": 4.162700
        }
    ]
}
```
</p>
</details>

**Endpoint:** `/api/rates/{currencyCode}/{targetCurrencyCode}/all`  
**Metoda:** `GET`  
**Opis:** zwraca wszystkie kursy dla podanych walut  
**Parametry:**
- `{sourceCurrencyCode}`: kod waluty, którą wymieniamy
- `{targetCurrencyCode}`: kod waluty, na którą chcemy wymienić

<details><summary><b>Przykład</b></summary>
<p>

**Zapytanie:**
```
/api/rates/USD/PLN/all
```

Zwraca wszystkie kursy wymiany dolara na złotówki:

```json
{
    "code": "USD",
    "rates": [
        {
            "code": "PLN",
            "effectiveDate": "2021-12-02",
            "rate": 4.062500
        },
        {
            "code": "PLN",
            "effectiveDate": "2021-12-01",
            "rate": 4.105100
        },
        {
            "code": "PLN",
            "effectiveDate": "2021-11-30",
            "rate": 4.121400
        },
        {
            "code": "PLN",
            "effectiveDate": "2021-11-29",
            "rate": 4.162700
        },
        {
            "code": "PLN",
            "effectiveDate": "2021-11-26",
            "rate": 4.175400
        },
        {
            "code": "PLN",
            "effectiveDate": "2021-11-25",
            "rate": 4.160000
        }
    ]
}
```
</p>
</details>

### Wymiana walut

**Endpoint:** `/api/trade/{buyCurrencyCode}/{sellCurrencyCode}/{buyAmount}`  
**Metoda:** `GET`  
**Opis:** zwraca ilość zakupionej waluty dla podanych walut  
**Parametry:**
- `{buyCurrencyCode}`: kod waluty, na którą kupujemy
- `{sellCurrencyCode}`: kod waluty, na którą sprzedajemy
- `{buyAmount}`: ilość, jaką chcemy kupić

<details><summary><b>Przykład</b></summary>
<p>

**Zapytanie:**
```
/api/trade/PLN/USD/20
```

Zwraca ilość zakupionych dolarów za złotówki:

```json
{
    "date": "2021-12-03T03:21:51.997+00:00",
    "buyCode": "PLN",
    "buyAmount": 20.00,
    "sellCode": "USD",
    "sellAmount": 4.92
}
```
</p>
</details>

**Endpoint:** `/api/trade`  
**Metoda:** `POST`  
**Opis:** jak wyżej, ale przyjmuje JSONa  
**Ciało zapytania:**
- `sellCode`: kod waluty, którą sprzedajemy
- `buyCode`: kod waluty, którą kupujemy
- `buyAmount`: ilość, jaką chcemy kupić

<details><summary><b>Przykład</b></summary>
<p>

**Zapytanie:**
```
/api/trade
```

**Ciało zapytania:**
```json
{
    "sellCode": "USD",
    "buyCode": "PLN",
    "buyAmount": "1000"
}
```

Zwraca ilość zakupionych złotówek za dolary

```json
{
    "date": "2021-12-03T03:22:18.014+00:00",
    "buyCode": "PLN",
    "buyAmount": 1000.00,
    "sellCode": "USD",
    "sellAmount": 246.15
}
```
</p>
</details>

### Historia wymiany walut

**Endpoint:** `/api/trade/history/{date}`  
**Metoda:** `GET`  
**Opis:** zwraca wszystkie transakcje z danego dnia
**Parametry:**
- `{date}`: data, z której chcemy uzyskać historię transakcji

<details><summary><b>Przykład</b></summary>
<p>

**Zapytanie:**
```
/api/trade/history/2021-12-03
```

Zwraca wszystkie transakcje z dnia 2021-12-03:

```json
[
  {
    "date": "2021-12-03T01:45:41.000+00:00",
    "buyCode": "PLN",
    "buyAmount": 1000.00,
    "sellCode": "USD",
    "sellAmount": 245.98
  },
  {
    "date": "2021-12-03T01:46:19.000+00:00",
    "buyCode": "PLN",
    "buyAmount": 250.00,
    "sellCode": "USD",
    "sellAmount": 61.50
  },
  {
    "date": "2021-12-03T01:46:23.000+00:00",
    "buyCode": "PLN",
    "buyAmount": 212.00,
    "sellCode": "USD",
    "sellAmount": 52.15
  }
]
```
</p>
</details>

**Endpoint:** `/api/trade/history/{startDate}/{endDate}`  
**Metoda:** `GET`  
**Opis:** zwraca wszystkie transakcje z danego zakresu dat
**Parametry:**
- `{startDate}`: początkowa data zakresu
- `{endDate}`: końcowa data zakresu

<details><summary><b>Przykład</b></summary>
<p>

**Zapytanie:**
```
/api/trade/history/2021-12-01/2021-12-03
```

Zwraca wszystkie transakcje z zakresu od 2021-12-01 do 2021-12-03:

```json
[
  {
    "date": "2021-12-01T01:48:59.000+00:00",
    "buyCode": "AUD",
    "buyAmount": 300.00,
    "sellCode": "USD",
    "sellAmount": 211.62
  },
  {
    "date": "2021-12-02T01:49:04.000+00:00",
    "buyCode": "AUD",
    "buyAmount": 300.00,
    "sellCode": "JPY",
    "sellAmount": 23977.26
  },
  {
    "date": "2021-12-03T01:49:15.000+00:00",
    "buyCode": "AFN",
    "buyAmount": 300.00,
    "sellCode": "RUB",
    "sellAmount": 231.00
  }
]
```
</p>
</details>

### Komunikaty błędów

W przypadku braku danych, zwracane jest pusta odpowiedź o kodzie `404 Not Found`.  
W przypadku nieprawidłowego formatu daty, zwracany jest stosowny komunikat o kodzie `400 Bad Request`.


# Stack
Java 17, 
Spring Boot, JUnit, Jackson, Mockito, MySQL, H2





