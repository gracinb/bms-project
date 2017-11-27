
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

exception

end;
/
