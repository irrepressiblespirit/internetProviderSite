CONNECT 'jdbc:derby://localhost:1527/st4db;create=true;user=vik;password=vik';

SELECT us.login, rat.price FORM users us,rates rat WHERE us.rates_id=rat.id ORDER BY rat.price DESC;

DISCONNECT;