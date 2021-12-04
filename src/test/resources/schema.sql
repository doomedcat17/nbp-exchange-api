DROP TABLE IF EXISTS nbp_exchange_rates;

DROP TABLE IF EXISTS currency_transactions;

DROP TABLE IF EXISTS currencies;


create table currencies
(
    code varchar(3) not null primary key,
    name varchar(60) not null
);

create table currency_transactions
(
    id bigint auto_increment primary key,
    bought_amount decimal(20, 2) not null,
    date datetime not null,
    sold_amount   decimal(20, 2) not null,
    buy_currency  varchar(255) not null,
    sell_currency varchar(255) not null,
    foreign key (buy_currency) references currencies (code),
    foreign key (sell_currency) references currencies (code)
);

create table nbp_exchange_rates
(
    id bigint auto_increment primary key,
    effective_date datetime not null,
    mid_rate_inpln decimal(10, 6) not null,
    currency_code  varchar(255) not null,
    foreign key (currency_code) references currencies (code)
);