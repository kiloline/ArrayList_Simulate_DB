--release
--(1)create
create tablespace temp;
create Table first (id int,name StrINg,time double); --这里附带测试大小写不敏感的功能
show dbtree;
--(2)insert
insert into FIRST(id) values(1); --因为first表是在默认表空间public保存的
insert 
into first(id,name) --这里测试多段SQL的合成
values(10,'abcd');
insert into first values(2,'abcd',1.0);
insert into first values(3,'efgh',2.1);
insert into first values(4,'klmpfdsgfdsgvtsvrwtrgvrsgrvs grs ',3.1);
insert into first values(5,'kppom',3.1);
--(3)select
select * from public.first;
Select name,id from first;
select * from first where id>=3;
select * from first where id>3;
select * from first where id<3;
select * from first where id<=2;
select * from first where id=3;
select * from first where id!=3;
select * from first where name is null;
select * from first where time isnot null;
select * from first where time isnot null or id=3 and name!='abcd';
select id pid,name pname from first where id=3 or name='kppom';

select * from first
union
select id pid,name pname,time from first
where id=3
or name='kppom'; --测试union语法解析
commit;
--(4)update
update first set name='kppom';
select * from first;
update first set name='zhongziming' where id=1;
update first set id=121 where time is null;
--(5)alter
alter table FIRST
add column mda string;
update first
set mda='oui'
where id<10;
select mda nname
from FIRST
where mda is NULL
or id=3;
alter table FIRST
del column name;
select * from first;
--(6)delete
Delete from first where time is null;
Delete from first;
--(7)drop
Drop table public.first;
Drop tablespace temp;

--(8)child select