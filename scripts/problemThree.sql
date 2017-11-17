create or replace function purchase_saving
	(purNum IN Purchases.pur#%type)
	return Purchases.total_price%type IS savings Purchases.total_price%type;
begin
	select original_price * qty - total_price saving into savings
	from Purchases pu, Products pr
	where purNum = pu.pur# and pu.pid = pr.pid;
	return savings;
end;
/