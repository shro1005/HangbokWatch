package com.hangbokwatch.backend.dao.user;

import com.hangbokwatch.backend.domain.user.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Favorite findFavoriteByIdAndClickedId(Long id, Long clickedId);

    //id로 검색
    List<Favorite> findFavoritesByIdAndLikeornot(Long id, String likeOrNot);

    //clicked-id로 검색
    List<Favorite> findFavoritesByClickedIdAndLikeornot(Long id, String likeOrNot);
}
