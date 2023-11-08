delete
from order_items
where id in (select id from order_items order by id desc limit 3);