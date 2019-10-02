package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, String> {
}
