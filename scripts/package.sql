create or replace package refcursor_package as
        type ref_cursor is ref cursor;

        function showEmployees
        return ref_cursor;

        function showCustomers
        return ref_cursor;

        function showProducts
        return ref_cursor;

        function showSuppliers
        return ref_cursor;

        function showSupplies
        return ref_cursor;

        function showDiscounts
        return ref_cursor;

        function showPurchases
        return ref_cursor;

        function showLogs
        return ref_cursor;
        
        procedure add_customer(c_id CHAR, c_name VARCHAR2, c_telephone# VARCHAR2);

        procedure add_purchase(e_id in Purchases.eid%type, 
	p_id in Purchases.pid%type, 
	c_id in Purchases.cid%type, 
	pur_qty in Purchases.qty%type);

end;
/


create or replace package body refcursor_package as

        function showEmployees
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from employees;
                return rc;
        end;

        function showCustomers
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from customers;
                return rc;

        end;

        function showProducts
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from products;
                return rc;

        end;

        function showDiscounts
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from discounts;
                return rc;

        end;

        function showSuppliers
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from suppliers;
                return rc;

        end;

        function showSupplies
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from supplies;
                return rc;

        end;

        function showPurchases
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from purchases;
                return rc;

        end;

        function showLogs
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from logs;
                return rc;

        end;
        
        procedure add_customer(c_id CHAR, c_name VARCHAR2, c_telephone# VARCHAR2) is
        begin
                insert into customers values (c_id, c_name, c_telephone#, 1, SYSDATE);
        end add_customer;

	procedure add_purchase(e_id in Purchases.eid%type, 
	p_id in Purchases.pid%type, 
	c_id in Purchases.cid%type, 
	pur_qty in Purchases.qty%type) is
		—-cursor for products
		cursor productCursor is
			select *
			from Products p;
		cursor discountCursor is
			select * from Discounts d;
		—-variable declarations
		productRecord productCursor%rowtype;
		declarationRecord discountCursor%rowtype;
		discountRate Discounts.discnt_rate%type;
		itemPrice Products.original_price%type;
		totalPrice Purchases.total_price%type;
		—-still need to declare exceptions
		not_enough_qty exception;
		
	begin
		—-need to check errors for wrong insertion values

		open productCursor;
		loop
			fetch productCursor into productRecord;
			exit when productCursor%notfound;
			if (productRecord.pid = p_id) then
				—-found the product to buy
				exit;
			end if;
		end loop;
		close productCursor;
		
		open discountCursor;
		loop
			fetch discountCursor into discountRecord;
			exit when discountCursor%notfound;
			if (discountRecord.discnt_category = productRecord.discnt_category) then
				exit;
			end if;
		end loop;
		close discountCursor;
		
		if (productRecord.qoh < pur_qty) then
			raise not_enough_qty;
		end if;

		itemPrice := productRecord.original_price * (1 - discountRecord.discnt_rate);
		totalPrice := itemPrice * pur_qty;

		insert into Purchases
		VALUES (pur_seq.NEXTVAL, e_id, p_id, c_id, pur_qty, SYSDATE, totalPrice);
	
	exception

		when not_enough_qty then
			dbms_output.put_line(‘Not enough quantity on hand to make purchase.’);

	end add_purchase;


end;
/
