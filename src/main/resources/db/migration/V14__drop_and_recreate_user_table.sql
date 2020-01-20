drop table if EXISTS favorite;

create table if not EXISTS favorite
 (id bigint not null, clicked_id bigint not null, like_or_not varchar(1),
 rgt_dtm timestamp without time zone, udt_dtm timestamp without time zone, primary key (id, clicked_id));