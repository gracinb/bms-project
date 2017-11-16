create or replace package refcursor_employees as
        type ref_cursor is ref cursor;
          function showEmployees
          return ref_cursor;
end;
/


create or replace package body refcursor_employees as
        function showEmployees
        return ref_cursor is rc ref_cursor;
          begin
            open rc for
              select * from employees;
            return rc;
          end;
 end;
 /

