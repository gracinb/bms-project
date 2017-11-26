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

        procedure setUserName(userName in VARCHAR2);

        function purchase_saving
        (purNum IN Purchases.pur#%type)
        return Purchases.total_price%type;

        procedure monthly_sale_activities
        (empId IN Employees.eid%type,
        sales_record OUT SYS_REFCURSOR);

        procedure add_customer(c_id in Customers.cid%type, 
        c_name in Customers.name%type, 
        c_telephone# in Customers.telephone#%type);

	--Objective: to add a purchase to the purchases table
	--Usage: takes a employee id, product id, customer id and purchase quantity
	-- Cursors are used to grab the referenced product, customer and to find
	-- the right discount rate. Exceptions are taken care of as well
        procedure add_purchase(e_id in Purchases.eid%type, 
        p_id in Purchases.pid%type, 
        c_id in Purchases.cid%type, 
        pur_qty in Purchases.qty%type);

        procedure delete_purchase
        (purNum IN Purchases.pur#%type); 
end;
/

create or replace package body refcursor_package as
        userName VARCHAR2(12);

        --@problemOne.sql

        function showEmployees
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from employees;
                return rc;
        end showEmployees;

        function showCustomers
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from customers;
                return rc;

        end showCustomers;

        function showProducts
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from products;
                return rc;

        end showProducts;

        function showDiscounts
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from discounts;
                return rc;

        end showDiscounts;

        function showSuppliers
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from suppliers;
                return rc;

        end showSuppliers;

        function showSupplies
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from supplies;
                return rc;

        end showSupplies;

        function showPurchases
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from purchases;
                return rc;

        end showPurchases;

        function showLogs
        return ref_cursor is rc ref_cursor;
        begin
                open rc for
                        select * from logs;
                return rc;

        end showLogs;
        
        function getUserName
        return VARCHAR2 is v_userName VARCHAR2(12);
        begin
                v_userName:= userName;
                return v_userName;
        end getUserName;

        procedure setUserName(userName in VARCHAR2) is
        begin
                refcursor_package.userName := userName;
        end setUserName;

        --Return amount saved customer on transaction
        function purchase_saving
        (purNum IN Purchases.pur#%type)
        return Purchases.total_price%type IS savings Purchases.total_price%type;
        begin
                select original_price * qty - total_price saving into savings
                from Purchases pu, Products pr
                where purNum = pu.pur# and pu.pid = pr.pid;
                return savings;
        end;

        --Return information on employee sales per month
        procedure monthly_sale_activities
        (empId IN Employees.eid%type,
        sales_record OUT SYS_REFCURSOR) AS
        begin
                OPEN sales_record FOR
                        select eid, 
                                eName.name, 
                                to_char(ptime, 'MON YYYY') pDate, 
                                count(eid) num_sales, 
                                sum(qty) quantity_sale, 
                                sum(total_price) total_sale
                        from purchases, (select name from employees where eid = empId) eName
                        where eid = empId
                        group by to_char(ptime, 'MON YYYY'), eid, eName.name;
        end;

        procedure add_customer(c_id in Customers.cid%type, 
        c_name in Customers.name%type, 
        c_telephone# in Customers.telephone#%type) IS
                null_cid exception;
                null_cname exception;
                null_telephone exception;
        begin
                --checking for null values first
                if c_id is NULL then
                        raise null_cid;
                elsif c_name is NULL then
                        raise null_cname;
                elsif c_telephone# is NULL then
                        raise null_telephone;
                end if;

                insert into customers values (c_id, c_name, c_telephone#, 1, SYSDATE);

        exception
                when null_cid then
                        dbms_output.put_line('c_id is null');
                when null_cname then
                        dbms_output.put_line('c_name is null');
                when null_telephone then
                        dbms_output.put_line('telephone# is null');
                when others then
                        dbms_output.put_line('SQL Exception caught.');
                        
        end add_customer;

        procedure add_purchase(e_id in Purchases.eid%type, 
        p_id in Purchases.pid%type, 
        c_id in Purchases.cid%type, 
        pur_qty in Purchases.qty%type) is
                --cursor for products
                cursor productCursor is
                        select *
                        from Products p;
                --cursor for discount to choose right discount rate
                cursor discountCursor is
                        select * from Discounts d;
                --variable declarations
                productRecord productCursor%rowtype;
                discountRecord discountCursor%rowtype;
                discountRate Discounts.discnt_rate%type;
                itemPrice Products.original_price%type;
                totalPrice Purchases.total_price%type;
                -- exceptions
                not_enough_qty exception;
                null_eid exception;
                null_pid exception;
                null_cid exception;
                null_qty exception;
                
        begin
                --checking for null values first
                if e_id is NULL then
                        raise null_eid;
                elsif p_id is NULL then
                        raise null_pid;
                elsif c_id is NULL then
                        raise null_cid;
                elsif pur_qty is NULL then
                        raise null_qty;
                end if;

                --finding the product we need to buy with the p_id
                open productCursor;
                loop
                        fetch productCursor into productRecord;
                        exit when productCursor%notfound;
                        if (productRecord.pid = p_id) then
                                --found the product to buy
                                exit;
                        end if;
                end loop;
                close productCursor;
                
                --finding the discount rate for the product being purchased
                open discountCursor;
                loop
                        fetch discountCursor into discountRecord;
                        exit when discountCursor%notfound;
                        if (discountRecord.discnt_category = productRecord.discnt_category) then
                                exit;
                        end if;
                end loop;
                close discountCursor;
                
                --makign sure we got enoigh product on hand to complete purchase
                if (productRecord.qoh < pur_qty) then
                        raise not_enough_qty;
                end if;

                --calculating item price = original price * (1 - discount)
                itemPrice := productRecord.original_price * (1 - discountRecord.discnt_rate);
                --total price is just item price * qty
                totalPrice := itemPrice * pur_qty;

                --insert all values into purchases
                insert into Purchases
                VALUES (pur_seq.NEXTVAL, e_id, p_id, c_id, pur_qty, SYSDATE, totalPrice);
        
        exception
                --catching exceptions of what can go wrong
                when not_enough_qty then
                        dbms_output.put_line('Not enough quantity on hand to make purchase.');
                when null_eid then
                        dbms_output.put_line('e_id is null');
                when null_pid then
                        dbms_output.put_line('p_id is null');
                when null_cid then
                        dbms_output.put_line('c_id is null');
                when null_qty then
                        dbms_output.put_line('pur_qty is null');
                when DUP_VAL_ON_INDEX then
                        dbms_output.put_line('duplicate value on index');       
                when INVALID_CURSOR then
                        dbms_output.put_line('Invalid cursor');
                when NO_DATA_FOUND then
                        dbms_output.put_line('No data found');
                when TOO_MANY_ROWS then
                        dbms_output.put_line('Too many rows');
                when ROWTYPE_MISMATCH then
                        dbms_output.put_line('Rowtype is wrong');
                when others then
                        dbms_output.put_line('SQL exception caught');
                
        end add_purchase;

        procedure delete_purchase
        (purNum IN Purchases.pur#%type) IS
                --Variables for exception handling
                null_purNum exception;

        begin
                if (purNum is NULL) then
                        raise null_purNum;
                end if;
                
                delete from purchases
                where pur# = purNum;

        exception
                when null_purNum then
                        dbms_output.put_line('purNum is null');
        end;
end;
/
