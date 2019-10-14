create table if not EXISTS dva
(id bigint not null, win_game int, lose_game int, win_rate varchar(7), play_time varchar(4), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), block_damage_per_life varchar(10), damage_to_hero_per_life varchar(10),
 damage_to_shield_per_life varchar(10), mecha_suicide_kill_avg varchar(10), mecah_call_avg varchar(10), gold_medal varchar(10),
 silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS orisa
(id bigint not null, win_game int, lose_game int, win_rate varchar(7), play_time varchar(4), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), block_damage_per_life varchar(10), damage_to_hero_per_life varchar(10),
 damage_to_shield_per_life varchar(10), gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));