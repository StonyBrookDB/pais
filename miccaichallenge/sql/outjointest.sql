drop table test.test1;
drop table test.test2;
create table test.test1(x int, y int);
create table test.test2(x int, y int);

insert into test.test1 values (1,1);
insert into test.test1 values (1,2);
insert into test.test1 values (1,1);
insert into test.test2 values (2,1);

select count(*) from 
test.test1 t1 full outer join test.test2 t2 on t1.x = t2.x and t1.y = t2.y ;
