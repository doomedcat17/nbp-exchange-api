create table IF NOT EXISTS currencies
(
    code varchar(255) not null primary key,
    name varchar(255) not null
);

create table IF NOT EXISTS  currency_transactions
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

create table IF NOT EXISTS  nbp_exchange_rates
(
    id bigint auto_increment primary key,
    effective_date datetime not null,
    mid_rate_inpln decimal(10, 6) not null,
    currency_code  varchar(255) not null,
    foreign key (currency_code) references currencies (code)
);