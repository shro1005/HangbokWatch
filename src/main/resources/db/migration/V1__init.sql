create table if not EXISTS player
 (id bigint not null, battle_tag varchar(255) not null, player_name varchar(255) not null, player_level integer not null, for_url varchar(255) not null, is_public varchar(255) not null, platform varchar(255) not null,
 portrait varchar(255) not null, tank_rating_point integer, deal_rating_point integer, heal_rating_point integer, win_game integer, draw_game integer, lose_game integer,
 most_hero1 varchar(255), most_hero2 varchar(255), most_hero3 varchar(255), rgt_dtm timestamp without time zone, udt_dtm timestamp without time zone, primary key (id));

