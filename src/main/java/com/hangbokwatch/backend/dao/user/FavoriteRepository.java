package com.hangbokwatch.backend.dao.user;

import com.hangbokwatch.backend.domain.user.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Favorite findFavoriteByIdAndClickedId(Long id, Long clickedId);
}
