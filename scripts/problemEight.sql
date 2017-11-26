create or replace procedure delete_purchase
	(purNum IN Purchases.pur#%type) IS

declare
	--Variables for exception handling
	null_purNum exception;

begin
	if (purNum is NULL) then
		raise null_purNum;
	delete from purchases
	where pur# = purNum;

exception
	when null_purNum then
		dbms_output.put_line('purNum is null')
end;
/

create or replace trigger purchase_return
	before delete on purchases
	for each row

declare
	v_date varchar2(20);
	v_current varchar2(20);

begin
	update products
	set qoh = qoh + :old.qty
	where pid = :old.pid;

	select to_char(c.last_visit_date, 'DD-MON-YYYY') into v_date from customers c
	where c.cid = :old.cid;

	select to_char(current_date, 'DD-MON-YYYY') into v_current from dual;

	if (v_date != v_current) then
		update  customers
		set last_visit_date = v_current
		where cid = :old.cid;
	end if;

exception

end;
/