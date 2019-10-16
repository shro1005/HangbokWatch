create table if not EXISTS dva
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(4), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), block_damage_per_life varchar(10), damage_to_hero_per_life varchar(10),
 damage_to_shield_per_life varchar(10), mecha_suicide_kill_avg varchar(10), mecah_call_avg varchar(10), gold_medal varchar(10),
 silver_medal varchar(10), bronze_medal varchar(10));

create table if not EXISTS orisa
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(4), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), block_damage_per_life varchar(10), damage_to_hero_per_life varchar(10),
 damage_to_shield_per_life varchar(10), damage_amp_avg varchar(10), gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

 create table if not EXISTS reinhardt
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(4), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), block_damage_per_life varchar(10), damage_to_hero_per_life varchar(10),
 damage_to_shield_per_life varchar(10), earthshatter_kill_avg varchar(10), charge_kill_avg varchar(10), fire_strike_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

 create table if not EXISTS winston
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(4), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), block_damage_per_life varchar(10), damage_to_hero_per_life varchar(10),
 damage_to_shield_per_life varchar(10), jump_pack_kill_avg varchar(10), primal_rage_kill_avg varchar(10), push_emeny_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

 create table if not EXISTS zarya
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(4), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), block_damage_per_life varchar(10), damage_to_hero_per_life varchar(10),
 damage_to_shield_per_life varchar(10), energy_avg varchar(10), high_energy_kill_avg varchar(10), graviton_surge_kill_avg varchar(10),
 projected_barrier_avg varchar(10), gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

 create table if not EXISTS roadhog
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(4), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), solo_kill_avg varchar(10), damage_to_hero_per_life varchar(10),
 damage_to_shield_per_life varchar(10), whole_hog_kill_avg varchar(10), chain_hook_accuracy varchar(10), hooking_enemy_avg varchar(10),
 self_heal_per_life varchar(10), gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

 create table if not EXISTS wreckingball
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(4), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), block_damage_per_life varchar(10), damage_to_hero_per_life varchar(10),
 damage_to_shield_per_life varchar(10), grappling_claw_kill_avg varchar(10), piledriver_kill_avg varchar(10), minefield_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));

 create table if not EXISTS sigma
(id bigint not null, win_game int, lose_game int, entire_game int, win_rate varchar(7), play_time varchar(4), kill_per_death varchar(10),
 spent_on_fire_avg varchar(10), death_avg varchar(10), block_damage_per_life varchar(10), damage_to_hero_per_life varchar(10),
 damage_to_shield_per_life varchar(10), absorption_damage_per_life varchar(10), gravitic_flux_kill_avg varchar(10), accretion_kill_avg varchar(10),
 gold_medal varchar(10), silver_medal varchar(10), bronze_medal varchar(10));