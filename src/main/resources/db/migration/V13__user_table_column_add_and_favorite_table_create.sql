ALTER TABLE myuser ADD COLUMN last_login_dtm varchar(14);
ALTER TABLE myuser ADD COLUMN rgt_dtm timestamp without time zone;
ALTER TABLE myuser ADD COLUMN udt_dtm timestamp without time zone;

create table if not EXISTS favorite
 (id bigint not null, clicked_id bigint not null, like_or_not varchar(1),
 rgt_dtm timestamp without time zone, udt_dtm timestamp without time zone, primary key (id));