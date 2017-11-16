create or replace package refcursor_supplies as
        type ref_cursor is ref cursor;
        function showSupplies
        return ref_cursor;
end;
/


create or replace package body refcursor_supplies as
        function showSupplies
        return ref_cursor is
        rc ref_cursor;
        begin
          open rc for
            select * from supplies;
          return rc;
        end;
end;
/

