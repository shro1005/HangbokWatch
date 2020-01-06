package com.hangbokwatch.backend.domain.player;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class PlayerDetailId implements Serializable {
    @EqualsAndHashCode.Include
    @Id
    private Long id;

    @EqualsAndHashCode.Include
    @Id
    private Long season;

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "hero_order")
    private Integer heroOrder;
}
