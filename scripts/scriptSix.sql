drop trigger customerInsert;
drop trigger customerUpdate;
drop trigger purchasesInsert;
drop trigger productsUpdate;
drop trigger suppliesInsert;

--Trigger to insert a new log tuple when a customer is added.
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

--Trigger to occur when an update occurs on the customers table
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
--trigger to occur when a new purchases has been added to the table and inserts a new log
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
--Trigger to fire when products' qoh has been updated and inserts a new tuple into the lag table
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
--create or replace trigger and inserts a new tuple into the log table when an insertion onto supplies has occurred
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
