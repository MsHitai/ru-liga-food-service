delete
from order_items
where id in (select id from orders order by id desc limit 3);