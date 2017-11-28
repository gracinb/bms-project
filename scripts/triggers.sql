/* triggers.sql
File contains all triggers that will be used for project.
	seq.sql must be run first */


create or replace trigger purchase_insert
        after insert on purchases
	for each row
declare
	prodID Products.pid%type;
	suppID Suppliers.sid%type;
	custID Customers.cid%type;
	requestedQoh Products.qoh%type;
	productRecord Products%rowtype;
	numSuppliers number(6);
	cVisitsMade Customers.visits_made%type;
	lastVisit Customers.last_visit_date%type;
	no_suppliers exception;
        --pNum NUMBER(3,0);
        --CURSOR pur_cursor is select pur# from purchases;
begin
        --FOR pur# in pur_cursor
        --LOOP
        --        select pur_seq.NEXTVAL into pNum from dual;
        --        DBMS_OUTPUT.PUT_LINE(LPAD(to_char(pNum),3,'0'));
        --END LOOP;
	prodID := :NEW.pid;
	dbms_output.put_line('Product purchased: ' || prodID);
	--grabbing product purchased into productRecord
	select * into productRecord
	from Products p
	where p.pid = prodID;
	-- need to update products qoh
	update Products
	set qoh = (productRecord.qoh - :NEW.qty)
	where pid = prodID;
	--getting the updated product value
	select * into productRecord
	from Products p
	where p.pid = prodID;
	--checking if qoh<threshold cause then we have to request a new supply
	if (productRecord.qoh < productRecord.qoh_threshold) then
		requestedQoh := productRecord.qoh_threshold - productRecord.qoh + 11;
		dbms_output.put_line('the current qoh of ' || productRecord.name || ' is below the required threshold and new supply is required');
		--getting the number of suppliers who supply this product since multiple suppliers can supply the same product
		select count(*) into numSuppliers
		from Supplies s
		where s.pid = prodID;
		--case that no suppliers supply this product raise and error
		if (numSuppliers < 1) then
			raise no_suppliers;
		end if;
		--finding the first supplier out of all the suppliers
		select sid into suppID
		from Supplies s
		where s.pid = prodID and rownum = 1
		order by sid asc;
		--ordering supply
		insert into Supplies
		VALUES (sup_seq.NEXTVAL, prodID, suppID, SYSDATE, requestedQoh);
	end if;
	-- finding the customer who made this purchase
	select c.cid, visits_made, last_visit_date
	into custID, cVisitsMade, lastVisit
	from Customers c
	where :NEW.cid = cid;
	--update last visit of the customer and increment visits made by 1
	if (to_char(lastVisit, 'DD-MON-YY') != to_char(:NEW.ptime, 'DD-MON-YY')) then
		update Customers c
		set c.visits_made = cVisitsMade + 1
		where c.cid = custID;
		update Customers c
		set c.last_visit_date = :NEW.ptime
		where c.cid = custID;
	end if;
exception
	when no_suppliers then
		raise_application_error(-20001, 'no suppliers supply this product');
end;
/

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

--Handles updating products and customers tables when a purchase is returned
create or replace trigger purchase_return
	before delete on purchases
	for each row

declare
	v_date varchar2(20);
	v_current varchar2(20);

begin
	--Update products table with new qoh from returned purchase
	update products
	set qoh = qoh + :old.qty
	where pid = :old.pid;

	--Check when last the customer visited
	select to_char(c.last_visit_date, 'DD-MON-YYYY') into v_date from customers c
	where c.cid = :old.cid;

	--Get current date
	select to_char(current_date, 'DD-MON-YYYY') into v_current from dual;

	--If return is after last visit for customer update field
	if (v_date != v_current) then
		update  customers
		set last_visit_date = v_current
		where cid = :old.cid;
	end if;
end;
/
