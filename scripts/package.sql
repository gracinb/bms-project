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
        
	function getUserName
        return VARCHAR2;

        procedure setUserName(userName VARCHAR2);

        procedure add_customer(c_id CHAR, c_name VARCHAR2, c_telephone# VARCHAR2);

        procedure add_purchase(e_id in Purchases.eid%type, 
	p_id in Purchases.pid%type, 
	c_id in Purchases.cid%type, 
	pur_qty in Purchases.qty%type);
 
end;
/


create or replace package body refcursor_package as
        userName VARCHAR2(12);

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
        
	function getUserName
        return VARCHAR2 is v_userName VARCHAR2(12);
        begin
                v_userName:= userName;
                return v_userName;
        end;

        procedure setUserName(userName VARCHAR2) is
        begin
                refcursor_package.userName := userName;
        end setUserName;

        procedure add_customer(c_id CHAR, c_name VARCHAR2, c_telephone# VARCHAR2) is
	declare
		—-exceptions for null values
		null_cid exception;
		null_cname exception;
		null_telephone exception;
        begin
		—-checking for null values first
		if c_id is NULL then
			raise null_cid;
		elsif c_name is NULL then
			raise null_cname;
		elsif c_telephone# is NULL then
			raise null_telephone;


                insert into customers values (c_id, c_name, c_telephone#, 1, SYSDATE);
	exception
		when null_cid then
			dbms_output.put_line(‘c_id is null’);
		when null_cname then
			dbms_output.put_line(‘c_name is null’);
		when c_telephone# then
			dbms_output.put_line(‘telephone# is null’);
		when others then
			dbms_output.put_line(‘SQL Exception caught.’);
			
        end add_customer;

	procedure add_purchase(e_id in Purchases.eid%type, 
	p_id in Purchases.pid%type, 
	c_id in Purchases.cid%type, 
	pur_qty in Purchases.qty%type) is
	declare
		—-cursor for products
		cursor productCursor is
			select *
			from Products p;
		—-cursor for discount to choose right discount rate
		cursor discountCursor is
			select * from Discounts d;
		—-variable declarations
		productRecord productCursor%rowtype;
		declarationRecord discountCursor%rowtype;
		discountRate Discounts.discnt_rate%type;
		itemPrice Products.original_price%type;
		totalPrice Purchases.total_price%type;
		—- exceptions
		not_enough_qty exception;
		null_eid exception;
		null_pid exception;
		null_cid exception;
		null_qty exception;
		
	begin
		—-checking for null values first
		if e_id is NULL then
			raise null_eid;
		elsif p_id is NULL then
			raise null_pid;
		elsif c_id is NULL then
			raise null_cid;
		elsif pur_qty is NULL then
			raise null_qty;

		—-finding the product we need to buy with the p_id
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
		
		—-finding the discount rate for the product being purchased
		open discountCursor;
		loop
			fetch discountCursor into discountRecord;
			exit when discountCursor%notfound;
			if (discountRecord.discnt_category = productRecord.discnt_category) then
				exit;
			end if;
		end loop;
		close discountCursor;
		
		—-makign sure we got enoigh product on hand to complete purchase
		if (productRecord.qoh < pur_qty) then
			raise not_enough_qty;
		end if;

		—-calculating item price = original price * (1 - discount)
		itemPrice := productRecord.original_price * (1 - discountRecord.discnt_rate);
		—-total price is just item price * qty
		totalPrice := itemPrice * pur_qty;

		—-insert all values into purchases
		insert into Purchases
		VALUES (pur_seq.NEXTVAL, e_id, p_id, c_id, pur_qty, SYSDATE, totalPrice);
	
	exception
		—-catching exceptions of what can go wrong
		when not_enough_qty then
			dbms_output.put_line(‘Not enough quantity on hand to make purchase.’);
		when null_eid then
			dbms_output.put_line(‘e_id is null’);
		when null_pid then
			dbms_output.put_line(‘p_id is null’);
		when null_cid then
			dbms_output.put_line(‘c_id is null’);
		when null_qty then
			dbms_output.put_line(‘pur_qty is null’);
		when DUP_VAL_ON_INDEX then
			dbms_output.put_line(‘duplicate value on index’);	
		when INVALID_CURSOR then
			dbms_output.put_line(‘Invalid cursor’);
		when NO_DATA_FOUND then
			dbms_output.put_line(‘No data found’);
		when TOO_MANY_ROWS then
			dmbs_output.put_line(‘Too many rows’);
		when ROWTYPE_MISMATCH then
			dmbs_output.put_line(‘Rowtype is wrong’);
		when others then
			dbms_out.put_line(‘SQL exception caught’);
		
	end add_purchase;


end;
/
