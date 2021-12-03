create table currency
(
    code varchar(255) not null
        primary key,
    name varchar(255) null
);

create table currency_transactions
(
    id bigint auto_increment primary key,
    bought_amount decimal(20, 2) null,
    date datetime null,
    sold_amount   decimal(20, 2) null,
    buy_currency  varchar(255) not null,
    sell_currency varchar(255) not null,
    foreign key (buy_currency) references currency (code),
    foreign key (sell_currency) references currency (code)
);

create table nbp_exchange_rates
(
    id bigint auto_increment primary key,
    effective_date datetime null,
    mid_rate_inpln decimal(10, 6) null,
    currency_code  varchar(255) not null,
    foreign key (currency_code) references currency (code)
);