SELECT reservation_id, site_id, name, start_date, num_days, create_date FROM reservation 
WHERE site_id IN (SELECT site_id FROM site WHERE campground_id = 1) 
AND (start_date, start_date + num_days) OVERLAPS (DATE '2020-02-19', DATE '2020-02-19' + 2);

SELECT site_id FROM reservation 
WHERE site_id IN (SELECT site_id FROM site WHERE campground_id = 1) 
AND (start_date, start_date + num_days) OVERLAPS (DATE '2020-02-19', DATE '2020-02-19' + 2);

SELECT site_id FROM reservation 
WHERE site_id IN (SELECT site_id FROM site WHERE campground_id = 1) 
AND NOT (start_date, start_date + num_days) OVERLAPS (DATE '2020-02-19', DATE '2020-02-19' + 2);


SELECT site_id FROM site WHERE campground_id = 1 AND site_id NOT IN (SELECT site_id FROM reservation 
WHERE (start_date, start_date + num_days) OVERLAPS (DATE '2020-02-19', DATE '2020-02-19' + 2));


SELECT * FROM campground;

SELECT * FROM site;


SELECT * FROM park;