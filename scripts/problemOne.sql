drop sequence pur_seq;
drop sequence sup_seq;
drop sequence log_seq;
drop trigger supplies_insert;
drop trigger purchase_insert;
drop trigger log_insert;

create sequence pur_seq
        increment by 1
        start with 1;

create or replace trigger purchase_insert
        after insert on purchases
declare
        pNum NUMBER(3,0);
        CURSOR pur_cursor is select pur# from purchases;
begin
        FOR pur# in pur_cursor
        LOOP
                select pur_seq.NEXTVAL into pNum from dual;
                DBMS_OUTPUT.PUT_LINE(LPAD(to_char(pNum),3,'0'));
        END LOOP;
end;
/


create sequence sup_seq
        increment by 1
        start with 1;


create or replace trigger supplies_insert
        after insert on supplies
declare
        sNum NUMBER(4,0);
        CURSOR sup_cursor is select sup# from supplies;
begin
        FOR sup# in sup_cursor
        LOOP
                select sup_seq.NEXTVAL into sNum from dual;
                DBMS_OUTPUT.PUT_LINE(LPAD(to_char(sNum),4,'0'));
        END LOOP;
end;
/
create sequence log_seq
        increment by 1
        start with 1;

create or replace trigger log_insert
        after insert on logs
declare
        lNum NUMBER(5,0);
        CURSOR log_cursor is select log# from logs;
begin
        FOR log# in log_cursor
        LOOP
                select log_seq.NEXTVAL into lNum from dual;
                DBMS_OUTPUT.PUT_LINE(LPAD(to_char(lNum),5,'0'));
        END LOOP;
end;
/
