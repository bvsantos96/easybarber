INSERT INTO user_type (user_type)
SELECT 'LOCKED'
WHERE NOT EXISTS
    (SELECT 1
     FROM user_type
     WHERE user_type = 'LOCKED' );


INSERT INTO user_type (user_type)
SELECT 'SYSTEM_ADMIN'
WHERE NOT EXISTS
    (SELECT 1
     FROM user_type
     WHERE user_type = 'SYSTEM_ADMIN' );

INSERT INTO user_type (user_type)
SELECT 'CLIENT'
WHERE NOT EXISTS
    (SELECT 1
     FROM user_type
     WHERE user_type = 'CLIENT' );


INSERT INTO user_type (user_type)
SELECT 'EMPLOYEE'
WHERE NOT EXISTS
    (SELECT 1
     FROM user_type
     WHERE user_type = 'EMPLOYEE' );
