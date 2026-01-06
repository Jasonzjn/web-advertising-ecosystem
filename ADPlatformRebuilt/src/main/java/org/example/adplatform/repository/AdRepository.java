package org.example.adplatform.repository;

import org.example.adplatform.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByUser(User user);
    List<Advertisement> findByActiveTrue();
    List<Advertisement> findByCategory(Advertisement.Category category);

}