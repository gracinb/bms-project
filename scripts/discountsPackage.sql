create or replace package refcursor_discounts as
        type ref_cursor is ref cursor;
        function showDiscounts
        return ref_cursor;
end;
/


create or replace package body refcursor_discounts as
        function showDiscounts
        return ref_cursor is
        rc ref_cursor;
        begin
          open rc for
            select * from discounts;
          return rc;
        end;
 end;
 /

