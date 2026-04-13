# HTTP İsteğinin Anatomisi ve Metot Analizi

Bu doküman, bir istemci (client) ile sunucu (server) arasındaki iletişimin temel birimi olan HTTP Request (HTTP İsteği) yapısını ve metodolojisini incelemektedir.



## 1. Bir HTTP İsteğinin Bileşenleri

Bir HTTP isteği, sunucuya gönderilen ham bir metin bloğudur ve dört temel bölümden oluşur:

### I. İstek Satırı (Request Line)
Sunucuya ne yapılacağını söyleyen ilk satırdır.
* **Metot:** İşlem türü (Örn: `GET`, `POST`, `PUT`).
* **Path (URL):** Kaynağın adresi (Örn: `/api/v1/products`).
* **Versiyon:** Kullanılan protokol sürümü (Örn: `HTTP/1.1`).

### II. İstek Başlıkları (Request Headers)
İsteğe dair ek bilgiler (metadata) içerir.
* `Host`: Hedef domain adresi.
* `User-Agent`: İsteği yapan tarayıcı veya platform bilgisi.
* `Content-Type`: Gönderilen verinin tipi (Örn: `application/json`).

### III. Boş Satır (Empty Line)
Başlıklar ile gövdeyi (Body) birbirinden ayıran teknik sınır çizgisidir. Protokol gereği zorunludur.

### IV. İstek Gövdesi (Request Body)
Sunucuya iletilen asıl veridir. Genellikle `POST` ve `PUT` gibi "yazma" amaçlı metotlarda kullanılır.

---

## 2. Metot Karşılaştırması: GET vs POST

| Kategori | GET | POST / PUT |
|----------|-----|------------|
| **Amaç** | Veri Okuma (Fetch).<br>Sunucudan bilgi çekmek veya bir kaynağı görüntülemek için kullanılır. | Veri Gönderme / İşlem Başlatma.<br>Sunucuya yeni veri iletmek, kayıt oluşturmak veya mevcut bir kaynağı güncellemek için kullanılır. |
| **Veri Konumu** | Veriler URL (Query Parameters) kısmında, yani adres çubuğunda taşınır. | Veriler Request Body (Gövde) kısmında, yani isteğin içinde paketlenmiş olarak taşınır. |
| **Güvenlik** | Düşük.<br>Veriler URL üzerinde açıkça göründüğü için tarayıcı geçmişinde, sunucu loglarında ve network izleme araçlarında kolayca okunabilir. | Daha Yüksek.<br>Veriler gövde içinde saklandığı için adres çubuğunda görünmez. Hassas veriler (şifre, kredi kartı vb.) için bu yöntem zorunludur. |
| **Kapasite** | Kısıtlı.<br>URL'lerin belirli bir karakter sınırı (genellikle 2048 karakter civarı) vardır. Bu yüzden büyük veri transferi yapılamaz. | Geniş.<br>Teorik olarak sınırsızdır. Dosyalar, yüksek çözünürlüklü resimler ve çok büyük JSON yapıları bu metotlarla gönderilir. |
| **Adreslenebilirlik** | Yer imi (Bookmark) eklenebilir.<br>Belirli bir arama sonucu veya filtreleme link olarak kaydedilebilir ve başkalarıyla paylaşılabilir. | Paylaşılabilir bir URL oluşturmaz.<br>İşlem verisi gövdede olduğu için linke tıklandığında aynı sonuca (örneğin doldurulmuş bir forma) ulaşılamaz. |

---

## 3. Kritik Sorular ve Teknik Cevaplar

### Neden GET İsteğinde Body Kullanılmaz?
Teknik olarak HTTP standartları buna izin verse de, GET isteğinin doğası **"Safe" (Güvenli)** ve **"Idempotent" (Eşgüçlü)** olmasıdır. Bir GET isteği sunucunun durumunu değiştirmez; sadece veri getirir. Bu yüzden verinin URL üzerinde taşınması, o aramanın veya sayfanın "paylaşılabilir/kaydedilebilir" olmasını sağlar. Ayrıca birçok sunucu yapısı, GET isteği içindeki Body'yi görmezden gelecek şekilde konfigüre edilmiştir.

### POST ve Body Kavramının Avantajları Nelerdir?
1. **Gizlilik:** Şifre, kredi kartı bilgisi veya kişisel veriler URL'de (tarayıcı geçmişinde ve loglarda) görünmeden "zarf" (Body) içinde taşınır.
2. **Yapısal Veri:** Karmaşık ve iç içe geçmiş JSON objeleri URL parametrelerine sığmaz; Body bu verileri temiz bir yapıda iletir.
3. **Büyük Veri:** Bir dosya yüklerken veya uzun bir metin gönderirken URL limitlerine takılmadan güvenli iletim sağlar.

---

## 4. Örnek Bir Ham (Raw) HTTP POST İsteği

Aşağıda, sunucuya yeni bir kullanıcı kaydeden sembolik bir istek yapısı görülmektedir:

```http
POST /api/register HTTP/1.1
Host: example.com
Content-Type: application/json
Content-Length: 60

{
  "username": "muhendis_ogrenci",
  "password": "guclu-sifre-123"
}