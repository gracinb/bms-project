create or replace procedure monthly_sale_activities
	(empId IN Employees.eid%type,
	sales_record OUT SYS_REFCURSOR) AS
begin
	OPEN sales_record FOR
		select eid, 
			eName.name, 
			to_char(ptime, 'MON YYYY') pDate, 
			count(eid) num_sales, 
			sum(qty) quantity_sale, 
			sum(total_price) total_sale
		from purchases, (select name from employees where eid = empId) eName
		where eid = empId
		group by to_char(ptime, 'MON YYYY'), eid, eName.name;
end;
/
