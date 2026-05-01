# ORM ve Hibernate Rehberi

> **Hedef:** Java uygulamalarında veritabanı işlemlerini nesne yönelimli bir şekilde yönetmek.

---

## İçindekiler

1. [ORM Nedir?](#1-orm-nedir)
2. [ORM Olmadan Hayat: JDBC](#2-orm-olmadan-hayat-jdbc)
3. [JPA ve Hibernate İlişkisi](#3-jpa-ve-hibernate-ilişkisi)
4. [Hibernate Kurulumu](#4-hibernate-kurulumu)
5. [Temel Anotasyonlar](#5-temel-anotasyonlar)
6. [İlişki Türleri](#6-ilişki-türleri)
7. [CRUD İşlemleri](#7-crud-i̇şlemleri)
8. [HQL ve JPQL](#8-hql-ve-jpql)
9. [Lazy vs Eager Loading](#9-lazy-vs-eager-loading)
10. [Cascade Türleri](#10-cascade-türleri)
11. [Spring Boot ile Hibernate](#11-spring-boot-ile-hibernate)
12. [En İyi Pratikler](#12-en-i̇yi-pratikler)

---

## 1. ORM Nedir?

**ORM (Object-Relational Mapping)**, nesne yönelimli programlama dilleriyle ilişkisel veritabanları arasındaki **empedans uyumsuzluğunu** çözen bir programlama tekniğidir.

### Temel Fikir

```
Java Nesnesi  <---->  Veritabanı Tablosu
    Alan      <---->  Sütun
    Nesne     <---->  Satır
```

### ORM'nin Sağladıkları

| Özellik | Açıklama |
|---|---|
| **Veritabanı bağımsızlığı** | MySQL'den PostgreSQL'e geçiş için kod değişmez |
| **SQL yazmak gerekmez** | İşlemler Java metodlarıyla yapılır |
| **Nesne yönetimi** | Cache, lazy loading otomatik yönetilir |
| **Güvenlik** | SQL injection'a karşı doğal koruma |

---

## 2. ORM Olmadan Hayat: JDBC

ORM'nin değerini anlamak için önce JDBC ile nasıl çalışıldığına bakalım:

```java
// JDBC ile kullanıcı kaydetme - uzun, hata prone, veritabanına bağımlı
public void saveUser(User user) throws SQLException {
    String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getEmail());
        stmt.setInt(3, user.getAge());
        stmt.executeUpdate();
        
    } // Her işlem için bu kadar kod...
}
```

**Sorunlar:**
- Her tablo için yüzlerce satır tekrar eden kod
- Veritabanı değiştiğinde tüm SQL sorguları güncellenir
- `ResultSet` → nesne dönüşümü manuel yapılır
- Transaction yönetimi karmaşık

---

## 3. JPA ve Hibernate İlişkisi

```
┌─────────────────────────────────────────────┐
│                Uygulama Kodu                │
└─────────────────┬───────────────────────────┘
                  │ kullanır
┌─────────────────▼───────────────────────────┐
│         JPA (Java Persistence API)          │
│              (Standart / Arayüz)            │
└─────────────────┬───────────────────────────┘
                  │ implemente eder
┌─────────────────▼───────────────────────────┐
│              Hibernate                      │
│           (Gerçek Uygulama)                 │
└─────────────────┬───────────────────────────┘
                  │ konuşur
┌─────────────────▼───────────────────────────┐
│           Veritabanı (MySQL, PostgreSQL...)  │
└─────────────────────────────────────────────┘
```

- **JPA** → Jakarta EE standardı; sadece anotasyon ve interface tanımlar
- **Hibernate** → JPA'yı uygulayan en popüler framework
- Diğer JPA implementasyonları: EclipseLink, OpenJPA

> **Özet:** JPA kuralları koyar, Hibernate bu kuralları hayata geçirir.

---

## 4. Hibernate Kurulumu

### Maven Bağımlılıkları

```xml
<dependencies>
    <!-- Hibernate Core -->
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>6.4.0.Final</version>
    </dependency>

    <!-- PostgreSQL Sürücüsü -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.1</version>
    </dependency>
</dependencies>
```

### `hibernate.cfg.xml` Yapılandırması

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <!-- Veritabanı bağlantısı -->
        <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
        <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/mydb</property>
        <property name="hibernate.connection.username">postgres</property>
        <property name="hibernate.connection.password">password</property>

        <!-- Dialect: Hibernate'e hangi SQL "lehçesini" kullanacağını söyler -->
        <property name="hibernate.dialect">org.hibernate.dialect.PostgreSQLDialect</property>

        <!-- DDL: Uygulama başlarken tabloları otomatik oluştur/güncelle -->
        <!-- Değerler: none | validate | update | create | create-drop -->
        <property name="hibernate.hbm2ddl.auto">update</property>

        <!-- SQL sorgularını konsola yazdır (geliştirme için) -->
        <property name="hibernate.show_sql">true</property>
        <property name="hibernate.format_sql">true</property>

        <!-- Entity sınıflarını kaydet -->
        <mapping class="com.example.entity.User"/>
        <mapping class="com.example.entity.Product"/>
    </session-factory>
</hibernate-configuration>
```

### SessionFactory Oluşturma

```java
public class HibernateUtil {
    
    private static final SessionFactory sessionFactory;
    
    static {
        try {
            Configuration config = new Configuration().configure(); // hibernate.cfg.xml okur
            sessionFactory = config.buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        getSessionFactory().close();
    }
}
```

---

## 5. Temel Anotasyonlar

### Entity Tanımlama

```java
import jakarta.persistence.*;

@Entity                          // Bu sınıf bir veritabanı tablosunu temsil eder
@Table(name = "users",           // Tablo adını özelleştir (opsiyonel)
       schema = "public",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"email"})
       })
public class User {

    @Id                                           // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT / SERIAL
    private Long id;

    @Column(name = "full_name",   // Sütun adını özelleştir
            nullable = false,     // NOT NULL kısıtlaması
            length = 100)         // VARCHAR(100)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)  // Sadece tarih (saat bilgisi olmadan)
    private Date birthDate;

    @Transient  // Bu alan veritabanına kaydedilmez
    private String tempToken;

    @Enumerated(EnumType.STRING)  // Enum'u string olarak sakla (ORDINAL yerine STRING tercih edilir)
    private UserRole role;

    @CreationTimestamp   // Hibernate: ilk kayıtta otomatik doldurur
    private LocalDateTime createdAt;

    @UpdateTimestamp     // Hibernate: her güncellemede otomatik doldurur
    private LocalDateTime updatedAt;

    // Getter ve Setter'lar (Hibernate için zorunlu: no-arg constructor)
    public User() {}

    // ... getter/setter
}
```

### ID Üretme Stratejileri

```java
// 1. IDENTITY — veritabanı otomatik arttırır (MySQL: AUTO_INCREMENT, PostgreSQL: SERIAL)
@GeneratedValue(strategy = GenerationType.IDENTITY)

// 2. SEQUENCE — veritabanı sequence nesnesi kullanır (PostgreSQL için önerilir)
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
@SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)

// 3. UUID — evrensel benzersiz ID
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;

// 4. AUTO — Hibernate veritabanına göre seçer
@GeneratedValue(strategy = GenerationType.AUTO)
```

---

## 6. İlişki Türleri

### 6.1 One-to-One (@OneToOne)

Bir kullanıcının bir profili vardır.

```java
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private UserProfile profile;
}

@Entity
public class UserProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String bio;
    private String avatarUrl;
    
    @OneToOne(mappedBy = "profile")  // "mappedBy" = ilişkiyi karşı taraf yönetir
    private User user;
}
```

### 6.2 One-to-Many / Many-to-One

Bir kategori birçok ürün içerir.

```java
@Entity
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    // "mappedBy": ilişkiyi Product tarafı yönetir (FK Product tablosunda)
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
}

@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Double price;
    
    // Foreign key bu tabloda: product.category_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
```

### 6.3 Many-to-Many (@ManyToMany)

Bir öğrenci birçok kursa, bir kurs birçok öğrenciye sahip olabilir.

```java
@Entity
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "student_course",          // Ara tablonun adı
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}

@Entity
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}
```

---

## 7. CRUD İşlemleri

Hibernate'de tüm işlemler bir **Session** (oturum) üzerinden yapılır.

```java
public class UserRepository {

    // CREATE
    public User save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);   // INSERT INTO users ...
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // READ — ID ile
    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);  // bulamazsa null döner
            return Optional.ofNullable(user);
        }
    }

    // READ — Tümü
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    // UPDATE
    public User update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User updated = (User) session.merge(user);  // UPDATE users SET ...
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // DELETE
    public void delete(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);  // DELETE FROM users WHERE id = ?
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}
```

### `persist` vs `merge` vs `save` Farkı

| Metod | Açıklama | Nesne Durumu |
|---|---|---|
| `persist()` | Yeni nesneyi ekle | Detached → Managed |
| `merge()` | Güncelle veya ekle | Detached → Managed (kopyasını döner) |
| `get()` | ID ile getir, yoksa `null` | — |
| `load()` | ID ile getir, yoksa exception | — |
| `remove()` | Sil | Managed → Removed |

---

## 8. HQL ve JPQL

**HQL (Hibernate Query Language)** / **JPQL (Jakarta Persistence Query Language)** — tablo adı değil, **sınıf ve alan adı** kullanılır.

```java
// Temel sorgu
String hql = "FROM User u WHERE u.email = :email";
User user = session.createQuery(hql, User.class)
                   .setParameter("email", "osman@example.com")
                   .uniqueResult();

// Birden fazla parametre
String hql2 = "FROM User u WHERE u.age > :minAge AND u.role = :role";
List<User> users = session.createQuery(hql2, User.class)
                          .setParameter("minAge", 18)
                          .setParameter("role", UserRole.STUDENT)
                          .list();

// Sıralama ve limit
List<User> topUsers = session.createQuery("FROM User u ORDER BY u.createdAt DESC", User.class)
                             .setMaxResults(10)
                             .list();

// Aggregate fonksiyonlar
Long count = session.createQuery("SELECT COUNT(u) FROM User u", Long.class)
                    .uniqueResult();

// JOIN ile ilişkili veri çekme
String joinHql = "SELECT p FROM Product p JOIN p.category c WHERE c.name = :catName";
List<Product> products = session.createQuery(joinHql, Product.class)
                                .setParameter("catName", "Elektronik")
                                .list();

// UPDATE sorgusu
int updated = session.createMutationQuery("UPDATE User u SET u.role = :role WHERE u.id = :id")
                     .setParameter("role", UserRole.ADMIN)
                     .setParameter("id", 1L)
                     .executeUpdate();

// Native SQL (gerektiğinde)
List<Object[]> result = session.createNativeQuery(
    "SELECT name, email FROM users WHERE created_at > NOW() - INTERVAL '7 days'",
    Object[].class
).list();
```

---

## 9. Lazy vs Eager Loading

Hibernate'de ilişkili verinin **ne zaman** yükleneceğini kontrol ederiz.

```java
// LAZY (Varsayılan OneToMany, ManyToMany için)
// İlişkili veri sadece erişildiğinde yüklenir
@OneToMany(fetch = FetchType.LAZY)
private List<Order> orders;

// EAGER (Varsayılan OneToOne, ManyToOne için)
// Ana nesne yüklendiğinde ilişkili veri de hemen yüklenir
@ManyToOne(fetch = FetchType.EAGER)
private Category category;
```

### N+1 Problemi ve Çözümü

```java
// ❌ Problem: Her kullanıcı için ayrı sorgu gider (N+1 sorgu)
List<User> users = session.createQuery("FROM User", User.class).list();
for (User u : users) {
    System.out.println(u.getOrders().size()); // Her biri için SELECT!
}

// ✅ Çözüm: JOIN FETCH ile tek sorguda çek
List<User> users = session.createQuery(
    "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders", User.class
).list();
```

---

## 10. Cascade Türleri

Parent nesne üzerinde yapılan işlemlerin child nesnelere nasıl yayılacağını belirler.

```java
@OneToMany(cascade = CascadeType.ALL)   // Tüm işlemleri yay
@OneToMany(cascade = CascadeType.PERSIST)  // Sadece kaydetme
@OneToMany(cascade = CascadeType.MERGE)    // Sadece güncelleme
@OneToMany(cascade = CascadeType.REMOVE)   // Sadece silme
@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})  // Birden fazla
```

| CascadeType | Açıklama |
|---|---|
| `ALL` | Tüm operasyonları yay |
| `PERSIST` | `save()` yapılınca child'lar da kaydedilir |
| `MERGE` | `update()` yapılınca child'lar da güncellenir |
| `REMOVE` | Parent silinince child'lar da silinir |
| `REFRESH` | Parent refresh edilince child'lar da refresh edilir |
| `DETACH` | Parent detach edilince child'lar da detach edilir |

```java
// Örnek: Category silinince ürünleri de silinir
@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Product> products;

// orphanRemoval = true: listeden çıkarılan Product otomatik silinir
category.getProducts().remove(product); // Bu satır DB'den de siler!
```

---

## 11. Spring Boot ile Hibernate

Spring Boot'ta Hibernate otomatik yapılandırılır; ayrıca `hibernate.cfg.xml` gerekmez.

### `application.properties`

```properties
# Veritabanı bağlantısı
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### Spring Data JPA ile Repository

```java
// Sadece interface tanımla, implementasyonu Spring yazar!
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Metod adına göre otomatik sorgu üretilir
    Optional<User> findByEmail(String email);
    
    List<User> findByRoleAndAgeGreaterThan(UserRole role, int age);
    
    boolean existsByEmail(String email);
    
    // @Query ile özel JPQL
    @Query("SELECT u FROM User u WHERE u.createdAt > :date ORDER BY u.name")
    List<User> findRecentUsers(@Param("date") LocalDateTime date);
    
    // Native SQL
    @Query(value = "SELECT * FROM users WHERE name ILIKE %:keyword%", nativeQuery = true)
    List<User> searchByName(@Param("keyword") String keyword);
    
    // Güncelleme işlemi
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :id")
    int updateRole(@Param("id") Long id, @Param("role") UserRole role);
}
```

### Service Katmanında Kullanım

```java
@Service
@Transactional  // Tüm metodlar transaction içinde çalışır
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User createUser(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email zaten kullanımda: " + dto.getEmail());
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }
    
    @Transactional(readOnly = true)  // Sadece okuma: performans optimizasyonu
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
```

---

## 12. En İyi Pratikler

### ✅ Yapılması Gerekenler

```java
// 1. Her zaman @Transactional kullan — veri tutarlılığı için
@Transactional
public void transferMoney(Long fromId, Long toId, Double amount) { ... }

// 2. Fetch type'ı bilinçli seç — varsayılanı gözlemle
@ManyToOne(fetch = FetchType.LAZY)  // ManyToOne varsayılanı EAGER'dır, değiştir

// 3. equals() ve hashCode()'u ID'ye göre implement et
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;
    User user = (User) o;
    return id != null && id.equals(user.id);
}

// 4. Büyük veri setleri için Pagination kullan
Pageable pageable = PageRequest.of(0, 20, Sort.by("name").ascending());
Page<User> page = userRepository.findAll(pageable);

// 5. DTO kullan — Entity'yi doğrudan API'ye açma
public UserDto toDto(User user) {
    return new UserDto(user.getId(), user.getName(), user.getEmail());
}
```

### ❌ Yapılmaması Gerekenler

```java
// 1. Lazy ilişkiye Session kapandıktan sonra erişme
User user = userRepository.findById(1L).get();
// session kapandı
user.getOrders().size(); // ❌ LazyInitializationException!

// Çözüm: @Transactional içinde yap veya JOIN FETCH kullan

// 2. CascadeType.ALL'u düşünmeden kullanma
@ManyToMany(cascade = CascadeType.ALL)  // ❌ REMOVE yayılır, kazara silme olur!
// Çözüm:
@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})  // ✅

// 3. hbm2ddl.auto=create-drop production'da kullanma
# ❌ Her yeniden başlatmada tüm tabloları siler!
spring.jpa.hibernate.ddl-auto=create-drop

# ✅ Production için:
spring.jpa.hibernate.ddl-auto=validate  # Sadece şemayı doğrula
```

### `hbm2ddl.auto` Değer Özeti

| Değer | Ne Yapar | Kullanım |
|---|---|---|
| `none` | Hiçbir şey yapmaz | Production |
| `validate` | Şemayı doğrular, değiştirmez | Production |
| `update` | Fark varsa günceller | Development |
| `create` | Her başlangıçta yeniden oluşturur | Test |
| `create-drop` | Başlarken oluşturur, kapanırken siler | Test |

---

## Özet

```
Uygulama
    │
    ▼
Entity (@Entity, @Table, @Column, @Id)
    │
    ▼
Repository (JpaRepository — Spring Data JPA)
    │
    ▼
Session / EntityManager (Hibernate)
    │
    ▼
Veritabanı (PostgreSQL)
```

ORM, veritabanı katmanını soyutlayarak geliştirici odağını SQL yazmak yerine iş mantığına yönlendirir. Hibernate bu alanda en olgun ve yaygın kullanılan çözümdür; Spring Boot ile birleştiğinde minimum konfigürasyonla maksimum verimlilik sağlar.

---

*Hazırlayan: Osman Bulbul | Turkcell GYGY 5.0 Bootcamp*
