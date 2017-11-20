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

end;
/
