# JPQL — Java Persistence Query Language

> **JPQL**, JPA (Java Persistence API) tarafından tanımlanan, nesne yönelimli bir sorgu dilidir. SQL'e sözdizimsel olarak benzer; ancak veritabanı tablolarını değil, **entity sınıflarını ve alanlarını** hedef alır.

---

## İçindekiler

1. [Giriş](#1-giriş)
2. [JPQL vs SQL](#2-jpql-vs-sql)
3. [Temel Sorgu Yapısı](#3-temel-sorgu-yapısı)
4. [SELECT Sorguları](#4-select-sorguları)
5. [WHERE Koşulları](#5-where-koşulları)
6. [Parametre Bağlama](#6-parametre-bağlama)
7. [JOIN İfadeleri](#7-join-i̇fadeleri)
8. [Aggregate Fonksiyonlar](#8-aggregate-fonksiyonlar)
9. [GROUP BY ve HAVING](#9-group-by-ve-having)
10. [ORDER BY](#10-order-by)
11. [Named Query](#11-named-query)
12. [UPDATE ve DELETE](#12-update-ve-delete)
13. [DTO Projeksiyonu (Constructor Expression)](#13-dto-projeksiyonu-constructor-expression)
14. [JPQL Fonksiyonları](#14-jpql-fonksiyonları)
15. [En İyi Pratikler](#15-en-i̇yi-pratikler)

---

## 1. Giriş

JPQL, veritabanından bağımsız sorgular yazmanızı sağlar. Hibernate, EclipseLink gibi JPA sağlayıcıları bu sorguları çalışma zamanında hedef veritabanının SQL lehçesine dönüştürür.

**Temel özellikler:**

- Veritabanından bağımsızdır (DB-agnostic)
- Nesne grafiği üzerinde gezinmeye olanak tanır (`user.address.city`)
- `EntityManager` aracılığıyla çalıştırılır
- Derleme zamanında doğrulanmaz (bunun için Criteria API veya Spring Data kullanılabilir)

---

## 2. JPQL vs SQL

| Özellik | SQL | JPQL |
|---|---|---|
| Hedef | Tablo / Sütun | Entity sınıfı / Alan |
| Büyük/küçük harf | Genellikle duyarsız | Entity/alan adları duyarlı |
| Veritabanı bağımsızlığı | ❌ | ✅ |
| İlişki gezinme | JOIN gerektirir | Nokta notasyonu ile doğrudan |
| Kalıtım desteği | ❌ | ✅ |

```sql
-- SQL
SELECT * FROM users WHERE age > 18;

-- JPQL
SELECT u FROM User u WHERE u.age > 18
```

> ⚠️ JPQL'de `*` kullanılmaz; entity alias doğrudan yazılır.

---

## 3. Temel Sorgu Yapısı

```
SELECT  <projeksiyon>
FROM    <Entity> <alias>
[JOIN   ...]
[WHERE  <koşul>]
[GROUP BY ...]
[HAVING ...]
[ORDER BY ...]
```

**Entity Tanımı (Örnek):**

```java
@Entity
public class Employee {

    @Id
    private Long id;

    private String name;
    private String department;
    private Double salary;

    @ManyToOne
    private Department dept;

    // getter / setter
}
```

---

## 4. SELECT Sorguları

### 4.1 Tüm Entity'leri Getirme

```java
TypedQuery<Employee> query = em.createQuery(
    "SELECT e FROM Employee e", Employee.class
);
List<Employee> employees = query.getResultList();
```

### 4.2 Tek Alan Seçme

```java
TypedQuery<String> query = em.createQuery(
    "SELECT e.name FROM Employee e", String.class
);
List<String> names = query.getResultList();
```

### 4.3 Birden Fazla Alan — Object Array

```java
Query query = em.createQuery(
    "SELECT e.name, e.salary FROM Employee e"
);
List<Object[]> results = query.getResultList();

for (Object[] row : results) {
    String name   = (String) row[0];
    Double salary = (Double) row[1];
}
```

### 4.4 DISTINCT

```java
"SELECT DISTINCT e.department FROM Employee e"
```

---

## 5. WHERE Koşulları

### 5.1 Karşılaştırma Operatörleri

```java
// Eşitlik
"SELECT e FROM Employee e WHERE e.department = 'IT'"

// Büyük / küçük
"SELECT e FROM Employee e WHERE e.salary > 5000"

// Aralık
"SELECT e FROM Employee e WHERE e.salary BETWEEN 3000 AND 7000"

// NULL kontrolü
"SELECT e FROM Employee e WHERE e.manager IS NULL"
"SELECT e FROM Employee e WHERE e.manager IS NOT NULL"
```

### 5.2 LIKE

```java
// 'A' ile başlayanlar
"SELECT e FROM Employee e WHERE e.name LIKE 'A%'"

// 'son' ile bitenler
"SELECT e FROM Employee e WHERE e.name LIKE '%son'"

// İçinde 'ali' geçenler
"SELECT e FROM Employee e WHERE e.name LIKE '%ali%'"
```

### 5.3 IN

```java
"SELECT e FROM Employee e WHERE e.department IN ('IT', 'HR', 'Finance')"
"SELECT e FROM Employee e WHERE e.id NOT IN (1, 2, 3)"
```

### 5.4 Mantıksal Operatörler

```java
"SELECT e FROM Employee e WHERE e.salary > 5000 AND e.department = 'IT'"
"SELECT e FROM Employee e WHERE e.department = 'HR' OR e.department = 'Finance'"
"SELECT e FROM Employee e WHERE NOT e.department = 'IT'"
```

---

## 6. Parametre Bağlama

> Güvenli sorgular için her zaman parametre bağlama kullanın; asla string birleştirme yapmayın (SQL Injection riski).

### 6.1 Konumsal Parametre (`?n`)

```java
TypedQuery<Employee> query = em.createQuery(
    "SELECT e FROM Employee e WHERE e.department = ?1 AND e.salary > ?2",
    Employee.class
);
query.setParameter(1, "IT");
query.setParameter(2, 4000.0);
```

### 6.2 İsimli Parametre (`:name`) — Önerilen

```java
TypedQuery<Employee> query = em.createQuery(
    "SELECT e FROM Employee e WHERE e.department = :dept AND e.salary > :minSalary",
    Employee.class
);
query.setParameter("dept", "IT");
query.setParameter("minSalary", 4000.0);

List<Employee> result = query.getResultList();
```

### 6.3 Tek Sonuç

```java
TypedQuery<Employee> query = em.createQuery(
    "SELECT e FROM Employee e WHERE e.id = :id", Employee.class
);
query.setParameter("id", 1L);

Employee emp = query.getSingleResult(); // Sonuç yoksa NoResultException fırlatır
```

---

## 7. JOIN İfadeleri

### 7.1 INNER JOIN

```java
"SELECT e FROM Employee e JOIN e.dept d WHERE d.name = 'Engineering'"
```

### 7.2 LEFT JOIN

```java
// Departmanı olmayan çalışanlar da gelir
"SELECT e FROM Employee e LEFT JOIN e.dept d"
```

### 7.3 JOIN FETCH — N+1 Problemini Önler

```java
// dept ilişkisini tek sorguda yükler (EAGER benzeri davranış)
"SELECT e FROM Employee e JOIN FETCH e.dept d WHERE d.name = :deptName"
```

> ℹ️ `JOIN FETCH`, ilişkili entity'yi ayrı bir sorgu atmadan tek seferinde yükler. Performans açısından kritiktir.

### 7.4 Koleksiyon JOIN

```java
@Entity
public class Department {
    @OneToMany(mappedBy = "dept")
    private List<Employee> employees;
}

// Koleksiyon üzerinden JOIN
"SELECT d FROM Department d JOIN d.employees e WHERE e.salary > 5000"
```

---

## 8. Aggregate Fonksiyonlar

| Fonksiyon | Açıklama |
|---|---|
| `COUNT(e)` | Kayıt sayısı |
| `SUM(e.salary)` | Toplam |
| `AVG(e.salary)` | Ortalama |
| `MAX(e.salary)` | En büyük değer |
| `MIN(e.salary)` | En küçük değer |

```java
// Toplam çalışan sayısı
TypedQuery<Long> countQuery = em.createQuery(
    "SELECT COUNT(e) FROM Employee e", Long.class
);
Long count = countQuery.getSingleResult();

// Ortalama maaş
TypedQuery<Double> avgQuery = em.createQuery(
    "SELECT AVG(e.salary) FROM Employee e WHERE e.department = :dept",
    Double.class
);
avgQuery.setParameter("dept", "IT");
Double avgSalary = avgQuery.getSingleResult();
```

---

## 9. GROUP BY ve HAVING

### 9.1 GROUP BY

```java
// Her departmandaki çalışan sayısı
Query query = em.createQuery(
    "SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department"
);
List<Object[]> results = query.getResultList();

for (Object[] row : results) {
    System.out.println(row[0] + " → " + row[1]);
}
```

### 9.2 HAVING

```java
// 5'ten fazla çalışanı olan departmanlar
"SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department HAVING COUNT(e) > 5"

// Ortalama maaşı 6000'den yüksek departmanlar
"SELECT e.department, AVG(e.salary) FROM Employee e GROUP BY e.department HAVING AVG(e.salary) > 6000"
```

---

## 10. ORDER BY

```java
// Maaşa göre azalan sıralama
"SELECT e FROM Employee e ORDER BY e.salary DESC"

// Önce departman, sonra isim
"SELECT e FROM Employee e ORDER BY e.department ASC, e.name ASC"
```

### Sayfalama (Pagination)

```java
TypedQuery<Employee> query = em.createQuery(
    "SELECT e FROM Employee e ORDER BY e.id", Employee.class
);
query.setFirstResult(0);   // offset
query.setMaxResults(10);   // limit

List<Employee> page = query.getResultList();
```

---

## 11. Named Query

Named Query'ler, entity üzerinde tanımlanır ve uygulama başlangıcında doğrulanır.

```java
@Entity
@NamedQueries({
    @NamedQuery(
        name = "Employee.findByDepartment",
        query = "SELECT e FROM Employee e WHERE e.department = :dept"
    ),
    @NamedQuery(
        name = "Employee.findHighEarners",
        query = "SELECT e FROM Employee e WHERE e.salary > :minSalary ORDER BY e.salary DESC"
    )
})
public class Employee { ... }
```

**Kullanım:**

```java
TypedQuery<Employee> query = em.createNamedQuery(
    "Employee.findByDepartment", Employee.class
);
query.setParameter("dept", "IT");
List<Employee> itEmployees = query.getResultList();
```

> ✅ Named Query'ler persistence context başladığında derlenir; hata erken yakalanır.

---

## 12. UPDATE ve DELETE

> `UPDATE` ve `DELETE` işlemleri **Bulk Operation** olarak adlandırılır. Persistence context'i bypass ederler; bu nedenle sonrasında `em.clear()` çağrılması önerilir.

### 12.1 UPDATE

```java
Query updateQuery = em.createQuery(
    "UPDATE Employee e SET e.salary = e.salary * 1.10 WHERE e.department = :dept"
);
updateQuery.setParameter("dept", "IT");
int updatedCount = updateQuery.executeUpdate();
System.out.println(updatedCount + " kayıt güncellendi.");
```

### 12.2 DELETE

```java
Query deleteQuery = em.createQuery(
    "DELETE FROM Employee e WHERE e.department = :dept"
);
deleteQuery.setParameter("dept", "Intern");
int deletedCount = deleteQuery.executeUpdate();
```

> ⚠️ Bulk operation'lardan sonra `em.clear()` çağırın; aksi hâlde birinci seviye önbellek tutarsız kalabilir.

---

## 13. DTO Projeksiyonu (Constructor Expression)

Tüm entity yerine yalnızca ihtiyaç duyulan alanları bir DTO'ya aktarmak için kullanılır.

**DTO Sınıfı:**

```java
public class EmployeeSummaryDTO {

    private String name;
    private Double salary;

    public EmployeeSummaryDTO(String name, Double salary) {
        this.name   = name;
        this.salary = salary;
    }

    // getter / setter
}
```

**JPQL:**

```java
TypedQuery<EmployeeSummaryDTO> query = em.createQuery(
    "SELECT new com.example.dto.EmployeeSummaryDTO(e.name, e.salary) " +
    "FROM Employee e WHERE e.department = :dept",
    EmployeeSummaryDTO.class
);
query.setParameter("dept", "IT");
List<EmployeeSummaryDTO> summaries = query.getResultList();
```

> ℹ️ `new` anahtar kelimesinden sonra tam nitelikli sınıf adı (fully qualified name) yazılmalıdır.

---

## 14. JPQL Fonksiyonları

### String Fonksiyonları

```java
"SELECT UPPER(e.name) FROM Employee e"
"SELECT LOWER(e.name) FROM Employee e"
"SELECT CONCAT(e.name, ' - ', e.department) FROM Employee e"
"SELECT LENGTH(e.name) FROM Employee e"
"SELECT SUBSTRING(e.name, 1, 3) FROM Employee e"
"SELECT TRIM(e.name) FROM Employee e"
```

### Aritmetik Fonksiyonlar

```java
"SELECT ABS(e.salary) FROM Employee e"
"SELECT SQRT(e.salary) FROM Employee e"
"SELECT MOD(e.id, 2) FROM Employee e"      -- çift/tek id
```

### Tarih Fonksiyonları

```java
"SELECT CURRENT_DATE FROM Employee e"       -- Bugünün tarihi
"SELECT CURRENT_TIME FROM Employee e"       -- Şu anki saat
"SELECT CURRENT_TIMESTAMP FROM Employee e"  -- Tarih + Saat
```

### CASE İfadesi

```java
"SELECT e.name, " +
"  CASE " +
"    WHEN e.salary < 3000 THEN 'Junior' " +
"    WHEN e.salary < 6000 THEN 'Mid-level' " +
"    ELSE 'Senior' " +
"  END " +
"FROM Employee e"
```

---

## 15. En İyi Pratikler

| # | Pratik | Neden? |
|---|---|---|
| 1 | Her zaman **isimli parametre** kullanın | SQL Injection önlenir, okunabilirlik artar |
| 2 | `JOIN FETCH` ile ilişkileri yükleyin | N+1 sorgu problemi engellenir |
| 3 | Sayfalama için `setFirstResult` / `setMaxResults` kullanın | Bellek taşması önlenir |
| 4 | **Named Query** tercih edin | Başlangıçta doğrulanır, yeniden kullanılabilir |
| 5 | Büyük veri setlerinde **DTO projeksiyonu** kullanın | Gereksiz alan yüklenmez, performans artar |
| 6 | Bulk operation sonrası `em.clear()` çağırın | Cache tutarsızlığı önlenir |
| 7 | `getSingleResult()` yerine ihtiyaç yoksa `getResultList()` kullanın | `NoResultException` / `NonUniqueResultException` riskini azaltır |
| 8 | Karmaşık sorgular için **Criteria API** veya **Spring Data Specifications** değerlendirin | Tip güvenliği sağlar |

---

## Özet

```
JPQL
├── SELECT  → Entity, alan, DTO projeksiyonu
├── FROM    → Entity adı (tablo değil!)
├── JOIN    → İlişki gezinme; FETCH ile N+1 önlemi
├── WHERE   → Koşullar, LIKE, IN, BETWEEN, IS NULL
├── Parametre → :isimli (önerilir) veya ?konumsal
├── GROUP BY / HAVING → Gruplama ve filtre
├── ORDER BY → Sıralama + Sayfalama
├── UPDATE / DELETE → Bulk operation (em.clear() sonrası)
└── Named Query → Entity üzerinde tanımlanmış, önceden doğrulanmış sorgular
```

---

*Bu belge JPA 2.x / Jakarta Persistence 3.x kapsamında hazırlanmıştır.*
