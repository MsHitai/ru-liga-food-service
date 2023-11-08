INSERT INTO users (username, password)
VALUES ('customer', '$2a$10$djeBB1KTGoYdqJZN2h3PGOrqvPcnUWdOoS4OurC4DHpSRsprgeoia'),
       ('courier', '$2a$10$TSnrVa4A2WNEsWnr4jRfm.G6kfa50JzT.hKCmBC1jfFQlxl9GRVmS'),
       ('kitchen', '$2a$10$M62Lo1fUGwYKfJulcf7kY.pbE7tzGtexUpPHMSt0QLWLY7il54KsW');

INSERT INTO authorities (username, authority)
VALUES ('customer', 'ROLE_CUSTOMER'),
       ('courier', 'ROLE_COURIER'),
       ('kitchen', 'ROLE_KITCHEN');

/*
{
    "username":"customer",
    "password":"customer"
}

{
    "username":"courier",
    "password":"courier"
}

{
    "username":"kitchen",
    "password":"kitchen"
}
*/