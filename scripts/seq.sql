/* seq.sql
File contains all sequences that will be used for project */

drop sequence pur_seq;
drop sequence sup_seq;
drop sequence log_seq; 

create sequence pur_seq
        increment by 1
        start with 100015;

create sequence sup_seq
        increment by 1
        start with 1010;

create sequence log_seq
        increment by 1
        start with 1;
