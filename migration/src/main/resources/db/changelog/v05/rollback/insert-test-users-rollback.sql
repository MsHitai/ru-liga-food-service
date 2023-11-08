DELETE
FROM authorities
WHERE username IN ('customer', 'courier', 'kitchen');
DELETE
FROM users
WHERE username IN ('customer', 'courier', 'kitchen');