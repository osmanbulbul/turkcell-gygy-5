# Spring Framework ve Temel Mimari Kavramlar

Bu doküman, Spring ekosistemindeki temel yapı taşlarını ve modern web uygulamalarının iletişim prensiplerini açıklamaktadır.

---

## 1. Spring IoC (Inversion of Control) Nedir?

Geleneksel programlamada bir nesneye ihtiyaç duyulduğunda, o nesne geliştirici tarafından `new` anahtar kelimesiyle oluşturulur. **IoC (Kontrolün Tersine Çevrilmesi)** prensibinde ise bu kontrol geliştiriciden alınarak **Spring Container**'a devredilir.



**Avantajları:**
* **Gevşek Bağlılık (Loose Coupling):** Sınıflar birbirine doğrudan bağımlı olmaz.
* **Yönetilebilirlik:** Nesnelerin yaşam döngüsü (oluşturulma, yok edilme) merkezden yönetilir.
* **Test Edilebilirlik:** Bağımlılıklar kolayca simüle edilebilir (Mocking).

---

## 2. Spring Bean ve Service Kavramları

Spring dünyasında her yapısal birim birer nesnedir, ancak yönetim biçimlerine göre farklı isimlendirilirler.

### Bean
Spring IoC konteyneri tarafından başlatılan, yapılandırılan ve yönetilen nesnelere **Bean** denir. Bir sınıfın Spring tarafından yönetilmesini istiyorsanız, onu bir Bean olarak işaretlersiniz.

### Service (@Service)
Uygulamanın **İş Mantığının (Business Logic)** bulunduğu katmandır. Veri tabanı işlemleri ile kullanıcı arayüzü arasındaki köprüdür.
* Bir sınıfı `@Service` notasyonu ile işaretlediğinizde, Spring bu sınıfı bir **Bean** olarak tanır ve ihtiyaç duyulan yerlere otomatik olarak enjekte eder.

---

## 3. Request-Response (İstek-Yanıt) Pattern

Modern web iletişiminin temelidir. İstemci (Client) ve Sunucu (Server) arasındaki karşılıklı alışverişi tanımlar.



1. **Request (İstek):** Kullanıcının tarayıcı üzerinden veya bir uygulama aracılığıyla sunucuya gönderdiği mesajdır. İçerisinde URL, Metot (GET/POST), Headerlar ve bazen bir Body bulunur.
2. **Processing (İşleme):** Sunucu isteği alır, ilgili servisleri çalıştırır ve bir sonuç üretir.
3. **Response (Yanıt):** Sunucunun işlemin sonucunu istemciye geri göndermesidir. Genellikle bir durum kodu (Örn: 200 OK, 404 Not Found) ve veriden (JSON/HTML) oluşur.

---

## 4. Spring Uygulama Akışı

Bir web isteği geldiğinde Spring içerisindeki döngü şu şekilde ilerler:

* **Controller:** İsteği (Request) karşılar ve dış dünya ile iletişim kurar.
* **Service:** Controller'dan gelen veriyi işler, kuralları uygular (İş Mantığı).
* **Repository:** Veri tabanı işlemlerini gerçekleştirir.
* **Response:** İşlenen veri Controller üzerinden kullanıcıya geri döner.

---

[HTTP Anatomisi Detayları İçin Tıklayın](./HTTP-ISTEGININ-ANATOMISI.md)