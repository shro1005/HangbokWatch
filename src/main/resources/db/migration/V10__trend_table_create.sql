create table if not EXISTS trendline
(id bigint not null, udt_dtm varchar(14) not null, tank_rating_point integer
, deal_rating_point integer, heal_rating_point integer, tank_win_game integer
, tank_lose_game integer, deal_win_game integer, deal_lose_game integer
, heal_win_game integer, heal_lose_game integer);
