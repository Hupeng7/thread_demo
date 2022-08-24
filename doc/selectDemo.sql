--1
SELECT count(id)
from thread_demo.push_process
where push_flag is NULL;

--2
update thread_demo.push_process set push_flag=null,push_state=0;

--3
SELECT *
from thread_demo.push_process
where push_flag is NULL limit 10;

--4
select * from thread_demo.push_process where push_state=0 and id>160000 LIMIT 10;