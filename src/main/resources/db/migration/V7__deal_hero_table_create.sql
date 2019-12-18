create table if not EXISTS ashe
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 coach_gun_kill_avg varchar(10), dynamite_kill_avg varchar(10), bob_kill_avg varchar(10), scope_critical_hit_rate varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS bastion
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 sentry_mode_kill_avg varchar(10), recon_mode_kill_avg varchar(10), tank_mode_kill_avg varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS doomfist
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 skill_damage_avg varchar(10), create_shield_avg varchar(10), meteor_strike_kill_avg varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS genji
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 dragonblade_kill_avg varchar(10), deflect_damage_avg varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS hanzo
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 dragon_strike_kill_avg varchar(10), storm_arrow_kill_avg varchar(10), sight_support_avg varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS junkrat
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 steel_trap_enemy_avg varchar(10), concussion_mine_avg varchar(10), rip_tire_kill_avg varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS mccree
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 peacekeeper_kill_avg varchar(10), deadeye_kill_avg varchar(10), critical_hit_rate varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS mei
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), block_damage_per_life varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 blizzard_kill_avg varchar(10), freezing_enemy_avg varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS pharah
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 rocket_hit_rate_avg varchar(10), strait_hit_rate varchar(10), barrage_kill_avg varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS reaper
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 death_blossom_kill_avg varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS soldier76
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), heal_per_life varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 helix_rocket_kill_avg varchar(10), tactical_visor_kill_avg varchar(10), critical_hit_rate varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS sombra
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 hacking_enemy_avg varchar(10), emp_enemy_avg varchar(10), critical_hit_rate varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS symmetra
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), block_damage_per_life varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 turret_kill_avg varchar(10), teleport_using_avg varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS torbjorn
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 molten_core_kill_avg varchar(10), torbjorn_direct_kill_avg varchar(10), turret_kill_avg varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS tracer
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 pulse_bomb_stick_avg varchar(10), pulse_bomb_kill_avg varchar(10), critical_hit_rate varchar(10), self_heal_per_life varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS widowmaker
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(10), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), last_hit_per_life varchar(10), damage_to_hero_per_life varchar(10), damage_to_shield_per_life varchar(10),
 scope_hit_rate varchar(10), scope_critical_hit_rate varchar(10), solo_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));