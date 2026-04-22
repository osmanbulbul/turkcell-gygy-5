
--kutuphane sistemi verittabani

-- 1-> Kitap tablosu olusturma
CREATE TABLE kitap(
	kitap_id SERIAL PRIMARY KEY,
	isbn VARCHAR(20) NOT NULL UNIQUE,
	baslik VARCHAR(200) NOT NULL,
	yazar VARCHAR(150) NOT NULL,
	yayinevi VARCHAR(100),
	yayin_yili SMALLINT CHECK(yayin_yili between 1000 and 2100),
	kategori VARCHAR(50),
	stok_adet SMALLINT NOT NULL DEFAULT 1 CHECK (stok_adet>=0),
	raf_no varchar(10),
	eklenme_tarih DATE NOT NULL DEFAULT CURRENT_DATE
);

select *from kitap	


-- 2-> oGRENCI TABLOSU OLSUTURUYORUZ
create table ogrenci(
	ogrenci_id serial primary key,
	ogrenci_no VARCHAR(20) NOT NULL UNIQUE,
	ad VARCHAR(80) NOT NULL,
	soyad VARCHAR(80) NOT NULL,
	email VARCHAR(120) NOT NULL UNIQUE,
	telefon VARCHAR(20),
	bolum VARCHAR(100),
	kayit_tarihi DATE NOT NULL DEFAULT CURRENT_DATE,
	aktif_ogrenci_mi BOOLEAN NOT NULL DEFAULT TRUE
);

select *from ogrenci

-- 3-> Gorevli taboosu olsuturutyoruz
CREATE TABLE gorevli (
    gorevli_id   SERIAL        PRIMARY KEY,
    ad           VARCHAR(80)   NOT NULL,
    soyad        VARCHAR(80)   NOT NULL,
    email        VARCHAR(120)  NOT NULL UNIQUE,
    telefon      VARCHAR(20),
    gorev        VARCHAR(50)   NOT NULL
                               CHECK (gorev IN ('Kütüphaneci','Asistan','Yönetici')),
    vardiya      VARCHAR(20)   CHECK (vardiya IN ('Sabah','Öğle','Akşam')),
    ise_baslama  DATE          NOT NULL DEFAULT CURRENT_DATE,
    aktif_mi     BOOLEAN       NOT NULL DEFAULT TRUE
);

select *from gorevli

-- 4-> odunc alma tablosu olusturuytoruz
CREATE TABLE odunc_alma (
    odunc_id        SERIAL    PRIMARY KEY,
    ogrenci_id      INT       NOT NULL REFERENCES ogrenci(ogrenci_id),
    kitap_id        INT       NOT NULL REFERENCES kitap(kitap_id),
    gorevli_id      INT       NOT NULL REFERENCES gorevli(gorevli_id),
    odunc_tarihi    DATE      NOT NULL DEFAULT CURRENT_DATE,
    iade_planlanan  DATE      NOT NULL,
    durum           VARCHAR(20) NOT NULL DEFAULT 'Devam Ediyor'
                               CHECK (durum IN ('Devam Ediyor','İade Edildi','Kayıp')),
    CONSTRAINT iade_sonra_odunc CHECK (iade_planlanan > odunc_tarihi)
);

select *from odunc_alma	

-- 5-> IADE tablosu olusturuyoruz
CREATE TABLE iade (
    iade_id      SERIAL     PRIMARY KEY,
    odunc_id     INT        NOT NULL UNIQUE REFERENCES odunc_alma(odunc_id),
    gorevli_id   INT        NOT NULL REFERENCES gorevli(gorevli_id),
    iade_tarihi  DATE       NOT NULL DEFAULT CURRENT_DATE,
    kitap_durumu VARCHAR(30) NOT NULL DEFAULT 'İyi'
                             CHECK (kitap_durumu IN ('İyi','Yıpranmış','Hasarlı','Kayıp'))
);

select *from iade

-- 6-> Ceza tablosu olusturuyoruz
CREATE TABLE ceza (
    ceza_id       SERIAL          PRIMARY KEY,
    odunc_id      INT             NOT NULL REFERENCES odunc_alma(odunc_id),
    ogrenci_id    INT             NOT NULL REFERENCES ogrenci(ogrenci_id),
    gecikme_gun   SMALLINT        NOT NULL CHECK (gecikme_gun > 0),
    ceza_tutari   NUMERIC(8,2)    NOT NULL CHECK (ceza_tutari > 0),
    olusturma_tar DATE            NOT NULL DEFAULT CURRENT_DATE,
    odendi_mi     BOOLEAN         NOT NULL DEFAULT FALSE,
    odeme_tarihi  DATE
);

select *from ceza


-- bu kisim icin ise dml ve ddl komuylarii kullanacagim.

--dml- kitap 
-- insert metodu ile kitap ekedlik
INSERT INTO kitap (isbn, baslik, yazar, yayinevi, yayin_yili, kategori, stok_adet, raf_no) VALUES
('978-975-08-0694-7', 'Suç ve Ceza',              'Fyodor Dostoyevski', 'İş Bankası Kültür',  1866, 'Roman',   3, 'A-01'),
('978-975-31-0001-1', 'Sefiller',                 'Victor Hugo',        'Türkiye İş Bankası', 1862, 'Roman',   2, 'A-02'),
('978-975-10-2490-0', 'Dune',                     'Frank Herbert',      'İthaki',             1965, 'Bilim Kurgu', 4, 'B-01'),
('978-605-09-0123-4', 'Yapay Zeka: Bir Giriş',    'Stuart Russell',     'Nobel Akademik',     2020, 'Teknoloji',  2, 'C-01'),
('978-975-07-0312-6', 'Küçük Prens',              'Antoine de S.E.',    'Can Yayınları',      1943, 'Çocuk',   5, 'D-01'),
('978-605-18-0456-8', 'Olasılık Kuramı',          'Sheldon Ross',       'Palme Yayıncılık',   2018, 'Matematik',  3, 'C-02'),
('978-975-08-1122-4', 'Sineklerin Tanrısı',       'William Golding',    'İş Bankası Kültür',  1954, 'Roman',   2, 'A-03');

select *from kitap

--dml iade
INSERT INTO iade (odunc_id, gorevli_id, iade_tarihi, kitap_durumu) VALUES
(1, 2, '2025-03-14', 'İyi'),        -- Ahmet zamaninda iade etti
(2, 3, '2025-03-25', 'Yıpranmış'), -- Ayse gec + yipranmis
(3, 3, '2025-03-24', 'İyi'),        -- Mehmet zamaninda
(5, 4, '2025-03-28', 'İyi'),        -- Ali zamaninda
(6, 4, '2025-04-10', 'Hasarlı');   -- Zeynep gec + hasarli


select COUNT(*) from odunc_alma
TRUNCATE ceza, iade, odunc_alma, gorevli, ogrenci, kitap RESTART IDENTITY CASCADE; -- hata aldimtablonun icerigindekileri sifirlamak istiyorum

-- idlerin esitlenmemesinden dolayi hata aldim. Bunlarin cozumunu yaptim asagkidaki kisimda .
select *from kitap

INSERT INTO kitap (isbn, baslik, yazar, yayinevi, yayin_yili, kategori, stok_adet, raf_no) VALUES
('978-975-08-0694-7', 'Suç ve Ceza',           'Fyodor Dostoyevski', 'İş Bankası Kültür',  1866, 'Roman',      3, 'A-01'),
('978-975-31-0001-1', 'Sefiller',              'Victor Hugo',        'Türkiye İş Bankası', 1862, 'Roman',      2, 'A-02'),
('978-975-10-2490-0', 'Dune',                  'Frank Herbert',      'İthaki',             1965, 'Bilim Kurgu',4, 'B-01'),
('978-605-09-0123-4', 'Yapay Zeka: Bir Giriş', 'Stuart Russell',     'Nobel Akademik',     2020, 'Teknoloji',  2, 'C-01'),
('978-975-07-0312-6', 'Küçük Prens',           'Antoine de S.E.',    'Can Yayınları',      1943, 'Çocuk',      5, 'D-01'),
('978-605-18-0456-8', 'Olasılık Kuramı',       'Sheldon Ross',       'Palme Yayıncılık',   2018, 'Matematik',  3, 'C-02'),
('978-975-08-1122-4', 'Sineklerin Tanrısı',    'William Golding',    'İş Bankası Kültür',  1954, 'Roman',      2, 'A-03');


INSERT INTO ogrenci (ogrenci_no, ad, soyad, email, telefon, bolum) VALUES
('2021001', 'Ahmet',  'Yılmaz', 'ahmet.yilmaz@uni.edu.tr',  '5301112233', 'Bilgisayar Mühendisliği'),
('2021002', 'Ayşe',   'Kaya',   'ayse.kaya@uni.edu.tr',     '5312223344', 'Elektrik Elektronik'),
('2022001', 'Mehmet', 'Demir',  'mehmet.demir@uni.edu.tr',  '5323334455', 'Makine Mühendisliği'),
('2022002', 'Fatma',  'Çelik',  'fatma.celik@uni.edu.tr',   '5334445566', 'Matematik'),
('2023001', 'Ali',    'Şahin',  'ali.sahin@uni.edu.tr',     '5345556677', 'Fizik'),
('2023002', 'Zeynep', 'Arslan', 'zeynep.arslan@uni.edu.tr', '5356667788', 'Bilgisayar Mühendisliği'),
('2020001', 'Emre',   'Koç',    'emre.koc@uni.edu.tr',      '5367778899', 'Endüstri Mühendisliği');

INSERT INTO gorevli (ad, soyad, email, telefon, gorev, vardiya) VALUES
('Selma',  'Aydın',   'selma.aydin@kutuphane.edu.tr',   '5381234567', 'Yönetici',    'Sabah'),
('Tarık',  'Güneş',   'tarik.gunes@kutuphane.edu.tr',   '5382345678', 'Kütüphaneci', 'Sabah'),
('Nesrin', 'Polat',   'nesrin.polat@kutuphane.edu.tr',  '5383456789', 'Kütüphaneci', 'Öğle'),
('Burak',  'Erdoğan', 'burak.erdogan@kutuphane.edu.tr', '5384567890', 'Asistan',     'Akşam'),
('Gülcan', 'Yıldız',  'gulcan.yildiz@kutuphane.edu.tr', '5385678901', 'Asistan',     'Öğle');


INSERT INTO odunc_alma (ogrenci_id, kitap_id, gorevli_id, odunc_tarihi, iade_planlanan) VALUES
(1, 1, 2, '2025-03-01', '2025-03-15'),
(2, 3, 2, '2025-03-05', '2025-03-19'),
(3, 4, 3, '2025-03-10', '2025-03-24'),
(4, 2, 3, '2025-03-12', '2025-03-26'),
(5, 5, 4, '2025-03-14', '2025-03-28'),
(6, 6, 4, '2025-03-20', '2025-04-03'),
(7, 7, 5, '2025-03-22', '2025-04-05');

INSERT INTO iade (odunc_id, gorevli_id, iade_tarihi, kitap_durumu) VALUES
(1, 2, '2025-03-14', 'İyi'),
(2, 3, '2025-03-25', 'Yıpranmış'),
(3, 3, '2025-03-24', 'İyi'),
(5, 4, '2025-03-28', 'İyi'),
(6, 4, '2025-04-10', 'Hasarlı');



SELECT 'kitap'      AS tablo, COUNT(*) FROM kitap
UNION ALL
SELECT 'ogrenci',              COUNT(*) FROM ogrenci
UNION ALL
SELECT 'gorevli',              COUNT(*) FROM gorevli
UNION ALL
SELECT 'odunc_alma',           COUNT(*) FROM odunc_alma;

SELECT odunc_id FROM odunc_alma ORDER BY odunc_id; 


INSERT INTO iade (odunc_id, gorevli_id, iade_tarihi, kitap_durumu) VALUES
(8,  2, '2025-03-14', 'İyi'),
(9,  3, '2025-03-25', 'Yıpranmış'),
(10, 3, '2025-03-24', 'İyi'),
(12, 4, '2025-03-28', 'İyi'),
(13, 4, '2025-04-10', 'Hasarlı');


INSERT INTO ceza (odunc_id, ogrenci_id, gecikme_gun, ceza_tutari, olusturma_tar, odendi_mi) VALUES
(9,  2, 6,  18.00, '2025-03-25', TRUE),
(13, 6, 7,  21.00, '2025-04-10', FALSE),
(11, 4, 10, 30.00, '2025-04-05', FALSE),
(14, 7, 5,  15.00, '2025-04-10', FALSE),
(9,  2, 1,   8.00, '2025-03-25', TRUE);

SELECT kitap_id, baslik, yazar, stok_adet FROM kitap ORDER BY kitap_id;
DELETE FROM ceza WHERE gecikme_gun = 0;

select *from ceza


