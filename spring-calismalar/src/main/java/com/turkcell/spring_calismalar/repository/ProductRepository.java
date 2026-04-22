package com.turkcell.spring_calismalar.repository;

import com.turkcell.spring_calismalar.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // springe bu sinifin veritabani islemlerinden sorumlu oldugunu soyluyoruz.
public interface ProductRepository extends JpaRepository<Product,Long> {
    //  JpaRepository kisminda Product  ahngi tabloya bakacagimi anlatiyor, Long ise o tablosnun idsinin hangi tipte oldgunu soyluyor.
 
    // saka gibi ama bitti
    // JpaRepository sayesinde save(), findall(),delete() gibi tum metodlar hazir geldi.

}
