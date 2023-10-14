delete
from orders
where id in (select id from orders order by id desc limit 3);