create table if not EXISTS myuser
 (id bigint not null, battle_tag varchar(255) not null, email varchar(255), role varchar(255) ,
 troll_index integer, bad_speaker_index integer, primary key (id));

