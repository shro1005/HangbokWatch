create table if not EXISTS ana
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), heal_per_life varchar(10), damage_to_hero_per_life varchar(10),
 nano_booster_avg varchar(10), sleep_dart_avg varchar(10), biotic_grenade_kill_per_life varchar(10), gold_medal varchar(10),
 silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS baptiste
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), heal_per_life varchar(10), damage_to_hero_per_life varchar(10),
 damage_to_shield_per_life varchar(10), immortality_field_save_avg varchar(10), amplification_matrix_avg varchar(10), gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

 create table if not EXISTS brigitte
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), heal_per_life varchar(10), damage_to_hero_per_life varchar(10),
 armor_per_life varchar(10), inspire_active_rate varchar(10), gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

 create table if not EXISTS lucio
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), heal_per_life varchar(10), damage_to_hero_per_life varchar(10),
 soundwave_avg varchar(10), gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

 create table if not EXISTS mercy
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), heal_per_life varchar(10), damage_to_hero_per_life varchar(10),
 resurrect_avg varchar(10), damage_amp_avg varchar(10), gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

 create table if not EXISTS moira
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), heal_per_life varchar(10), damage_to_hero_per_life varchar(10),
 coalescence_kill_avg varchar(10), selfheal_per_life varchar(10), gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

 create table if not EXISTS zenyatta
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), heal_per_life varchar(10), damage_to_hero_per_life varchar(10),
 transcendence_heal_avg varchar(10), gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));