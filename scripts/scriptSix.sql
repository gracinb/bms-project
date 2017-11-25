drop trigger customerInsert;
drop trigger customerUpdate;
drop trigger purchasesInsert;
drop trigger productsUpdate;
drop trigger suppliesInsert;

create or replace trigger customerInsert
        after insert on customers
        FOR EACH ROW

        declare
                logVal Number;
        begin
                select count(*) into logVal from logs;
                insert into logs VALUES (logVal+1,
                                        refcursor_package.getUserName(),
                                        'insert',
                                        SYSDATE(),
                                        'customers',
                                        :new.cid);
        end;
/

create or replace trigger customerUpdate
        after update of last_visit_date on customers
        FOR EACH ROW

        declare
                logVal Number;

        begin
                select count(*) into logVal from logs;
                insert into logs VALUES(logVal+1,
                                        refcursor_package.getUserName(),
                                        'update',
                                        SYSDATE(),
                                        'customers',
                                        :old.cid);
        end;
/

create or replace trigger purchasesInsert
        after insert on purchases
        FOR EACH ROW

        declare
                logVal Number;
        begin
                select count(*) into logVal from logs;
                insert into logs VALUES(logVal + 1,
                                        refcursor_package.getUserName(),
                                        'insert',
                                        SYSDATE(),
                                        'purchases',
                                        :new.pur#);
        end;
/

create or replace trigger productsUpdate
        after update of qoh on products
        FOR EACH ROW

        declare
                logVal Number;

        begin
                select count(*) into logVal from logs;
                insert into logs VALUES(logVal + 1,
                                        refcursor_package.getUserName(),
                                        'update',
                                        SYSDATE(),
                                        'products',
                                        :old.pid);
        end;
/

create or replace trigger suppliesInsert
        after insert on supplies
        FOR EACH ROW

        declare
                logVal Number;
        begin
                select count(*) into logVal from logs;
                insert into logs VALUES(logVal + 1,
                                        refcursor_package.getUserName(),
                                        'insert',
                                        SYSDATE(),
                                        'supplies',
                                        :new.sup#);
        end;
/
