package org.example.usermanage.repository;

import jakarta.transaction.Transactional;
import org.example.usermanage.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserInfo, Long> {

    Page<UserInfo> findByUsername(String username, Pageable pageable);

    @Transactional
    @Modifying
    @Query("delete from UserInfo u where u.id in (:ids)")
    void deleteByIds(@Param("ids") List<Long> ids);
}
