create or replace package refcursor_products as
        type ref_cursor is ref cursor;
        function showProducts
        return ref_cursor;
end;
/


create or replace package body refcursor_products as
        function showProducts
        return ref_cursor is
        rc ref_cursor;
          begin
            open rc for
              select * from products;
            return rc;
          end;
end;
/
