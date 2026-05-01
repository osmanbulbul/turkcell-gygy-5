-- Alias-> Takma Ad

select *from customers c where c.contact_name LIKE '%a%'

--joinler
select *from orders o
inner join customers c-- gercekten ikisninde de vasa calisir
on o.customer_id=c.customer_id  -- nasil katilmasi gerektigini soyluitoruz.
---
select *from orders o
right join customers c-- sag tabloyu kesin ghetir sol ile doldur.
on o.customer_id=c.customer_id

INSERT INTO customers(customer_id,company_name,contact_name,contact_title,address,city,postal_code,country,phone,fax)
VALUES('HALIT','Deneme','Halit Kalayci','Abc','Abc','Istanbul','34788','Türkiye','+90','abc')

-- full outer joine cok rastlamayiz order bilmek istiyorsan customer id bilmek zorundasin, e aydi de atamadigimiz icin 

--

select *from orders o
inner join employees e
on o.employee_id=e.employee_id

--

select *from orders o 
inner join customers c
on o.customer_id=c.customer_id
inner join order_details od
on o.order_id=od.order_id
inner join products p
on od.product_id=p.product_id
where od.quantity>10
order by c.contact_name

--Group By kullanimi ile-> *kullanamazsin :)
select c.country,c.city,COUNT(*) from  customers c --aggregate funciton olarka count(*) kullandik.
GROUP By c.country,c.city

select c.country,COUNT(*) from  customers c --aggregate funciton olarka count(*) kullandik.
GROUP By c.country
order by count(*) DESC
---

select s.company_name,COUNT(*) from shippers s
inner join orders o
on s.shipper_id=o.ship_via
group by s.shipper_id,s.company_name
order by count(o.order_id) DESC

select s.company_name,COUNT(o.order_id) from shippers s-- burada siparis sayisi degil satis sayisini sayiyorum. COUNTkullanimina dikkat
left join orders o
on s.shipper_id=o.ship_via
group by s.shipper_id,s.company_name
HAVING COUNT(o.order_id)>250 -- group by kullaniminda where yerine having kullanilir
order by count(o.order_id) DESC
---
-- Hnagi musteriler 10'dan fazla siparis vermis?
select c.contact_name,COUNT(*) AS total_orders from customers c
join orders o on c.customer_id=o.customer_id
group by c.customer_id,c.contact_name
having COUNT(*)>10
order by count(*) DESC
---
 
-- Topla Cirosu 60K'dan buyuk musteriler
SELECT  c.customer_id, c.company_name,
SUM(od.unit_price * od.quantity * (1 - od.discount)) AS total_revenue
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
JOIN order_details od ON o.order_id = od.order_id
GROUP BY c.customer_id, c.company_name
HAVING SUM(od.unit_price * od.quantity * (1 - od.discount)) > 50000
ORDER BY total_revenue DESC;


-- Her kategoriu icin en az 5 farkli urun satan kategoriler
SELECT cat.category_id,cat.category_name,
COUNT(DISTINCT p.product_id) AS product_count -- ayni urun tekrardan sayilmasin
FROM categories cat
JOIN products p ON cat.category_id = p.category_id
GROUP BY cat.category_id, cat.category_name
HAVING COUNT(DISTINCT p.product_id) >= 5
ORDER BY product_count DESC;

-- Calisan bazli toplam satis tutari ( Birim Fiyat)
SELECT  e.employee_id,e.first_name,e.last_name,
SUM(od.unit_price * od.quantity * (1 - od.discount)) AS total_sales
FROM employees e
JOIN orders o ON e.employee_id = o.employee_id -- orders.employee_id-> siparisi alan calisan
JOIN order_details od ON o.order_id = od.order_id
GROUP BY e.employee_id, e.first_name, e.last_name
ORDER BY total_sales DESC;

--Sayfalama? 
select *from products p
limit 10 -- id 1'den 10'a kadar gidiyor.
---
select *from products p
offset 5 -- id (5,x] seklinde gidiyor. 6,7,8,9 ...
---

select*from products p
limit 10 offset 4 --> burada ise id (4,14] olarak ilerliyor.
---

-- 1-> Sayfa boyutu?
-- 2-> Aktif Sayfa? 
  
-- 1 sayfa, sayfa basi 10 element.
--LIMIT  {sayfa_BASI_ELEMENT} OFFSET {(aktif_sayfa-1)*sayfa_basi_element} -> matematigi bu sekilde.
select * from products p
Limit 10 offset 0

select *from products p
LIMIT 10 OFFSET 10

