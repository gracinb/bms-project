create or replace package refcursor_suppliers as
        type ref_cursor is ref cursor;
        function showSuppliers
        return ref_cursor;
end;
/


create or replace package body refcursor_suppliers as
        function showSuppliers
        return ref_cursor is
        rc ref_cursor;
        begin
          open rc for
            select * from suppliers;
          return rc;
        end;
end;
/



