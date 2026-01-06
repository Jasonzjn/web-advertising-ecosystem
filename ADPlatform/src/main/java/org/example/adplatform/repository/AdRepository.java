package org.example.adplatform.repository;

import org.example.adplatform.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByUser(User user);
    List<Advertisement> findByActiveTrue();
    List<Advertisement> findByAdTypeAndActiveTrue(Advertisement.AdType adType);

    // 统计用户广告数量
    long countByUser(User user);

    // 统计用户广告总文件大小
    @Query("SELECT SUM(a.fileSize) FROM Advertisement a WHERE a.user = :user")
    Long sumFileSizeByUser(@Param("user") User user);

}
