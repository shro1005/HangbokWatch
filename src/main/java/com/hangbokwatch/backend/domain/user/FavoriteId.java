package com.hangbokwatch.backend.domain.user;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class FavoriteId implements Serializable {
    @EqualsAndHashCode.Include
    @Id
    private Long id;

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "clicked_id")
    private Long clickedId;
}
