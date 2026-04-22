 
## PostgreSQL Notları

### Sequence Mantığı
- Sequence tablodan bağımsızdır
- ID'ler her zaman artar, geri düşmez

### ID Atlaması
- Başarısız insert işlemlerinde bile sequence artar
- Bu yüzden ID'ler atlayabilir

### NOT NULL Kısıtı
- Tabloda veri varsa direkt eklenemez
- Önce NULL değerler doldurulmalıdır

### DELETE
- bagli oldugum bir FK varsa o PKLI veri silinmez!
