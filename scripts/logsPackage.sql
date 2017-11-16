create or replace package refcursor_logs as
        type ref_cursor is ref cursor;
        function showLogs
        return ref_cursor;
end;
/


create or replace package body refcursor_logs as
        function showLogs
        return ref_cursor is
        rc ref_cursor;
        begin
          open rc for
            select * from logs;
          return rc;
        end;
end;
/
