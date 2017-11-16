create or replace package refcursor_customers as
        type ref_cursor is ref cursor;
        function showCustomers
        return ref_cursor;
end;
/


create or replace package body refcursor_customers as
        function showCustomers
        return ref_cursor is
        rc ref_cursor;
          begin
            open rc for
              select * from customers;
            return rc;
          end;
end;
/

