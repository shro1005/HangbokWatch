
-- ALTER TABLE player ADD COLUMN play_time bigint;
-- ALTER TABLE player ADD COLUMN spent_on_fire bigint;
-- ALTER TABLE player ADD COLUMN env_Kill integer;

create table if not EXISTS forranking
 (id bigint not null, player_level integer,
 tank_rating_point integer, deal_rating_point integer, heal_rating_point integer,
 tank_win_game integer, tank_lose_game integer, deal_win_game integer, deal_lose_game integer,
 heal_win_game integer, heal_lose_game integer, win_game integer, draw_game integer, lose_game integer,
 play_time bigint, spent_on_fire bigint, env_kill integer, is_base varchar(1) not null,
 rgt_dtm timestamp without time zone, udt_dtm timestamp without time zone, primary key (id, is_base));

