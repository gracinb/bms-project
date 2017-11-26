set serveroutput on size 10000
declare
	emp_cursor SYS_REFCURSOR;
	emp_id Employees.eid%type;
	emp_name Employees.name%type;
	emp_date varchar2(10);
	emp_sales number;
	emp_quantity number;
	emp_dollar_amt number;
begin
	monthly_sale_activities (empID => 'e01',
							sales_record => emp_cursor);

	LOOP
		FETCH emp_cursor
		INTO emp_id, emp_name, emp_date, emp_sales, emp_quantity, emp_dollar_amt;
		exit when emp_cursor%NOTFOUND;
		DBMS_OUTPUT.PUT_LINE(emp_id || ' | ' || emp_name || ' | ' || emp_date || ' | ' || emp_sales || ' | ' || emp_quantity || ' | ' || emp_dollar_amt);
	END LOOP;
	CLOSE emp_cursor;
END;
/