create or replace package refcursor_purchases as
        type ref_cursor is ref cursor;
        function showPurchases
        return ref_cursor;
end;
/


create or replace package body refcursor_purchases as
        function showPurchases
        return ref_cursor is
        rc ref_cursor;
        begin
          open rc for
            select * from purchases;
          return rc;
        end;
end;
/


