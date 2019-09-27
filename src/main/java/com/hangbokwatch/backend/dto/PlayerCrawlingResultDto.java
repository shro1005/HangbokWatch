package com.hangbokwatch.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonDeserialize(builder = PlayerCrawlingResultDto.PlayerCrawlingResultDtoBuilder.class)
public class PlayerCrawlingResultDto {
    private String name;
    private String urlName;
    private Long id;
    private Integer level;
    private Integer playerLevel;
    private Boolean isPublic;
    private String platform;
    private String portrait;

    private PlayerCrawlingResultDto(String name, String urlName, Long id, Integer level, Integer playerLevel, Boolean isPublic, String platform, String portrait) {
        this.name = name;
        this.urlName = urlName;
        this.id = id;
        this.level = level;
        this.playerLevel = playerLevel;
        this.isPublic = isPublic;
        this.platform = platform;
        this.portrait = portrait;
    }

    public static PlayerCrawlingResultDtoBuilder newBuilder() {
        return new PlayerCrawlingResultDtoBuilder();
    }

    @JsonPOJOBuilder(buildMethodName = "createPlayerCrawlingResultDto", withPrefix = "set")
    public static class PlayerCrawlingResultDtoBuilder{
        String name;
        String urlName;
        Long id;
        Integer level;
        Integer playerLevel;
        Boolean isPublic;
        String platform;
        String portrait;

        public PlayerCrawlingResultDtoBuilder setId(Long id) {
            this.id = id;
            return this;
        }
        public PlayerCrawlingResultDtoBuilder setName(String name) {
            this.name = name;
            return this;
        }
        public PlayerCrawlingResultDtoBuilder setUrlName(String urlName) {
            this.urlName = urlName;
            return this;
        }
        public PlayerCrawlingResultDtoBuilder setLevel(Integer level) {
            this.level = level;
            return this;
        }
        public PlayerCrawlingResultDtoBuilder setPlayerLevel(Integer playerLevel) {
            this.playerLevel = playerLevel;
            return this;
        }
        public PlayerCrawlingResultDtoBuilder setIsPublic(Boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }
        public PlayerCrawlingResultDtoBuilder setPlatform(String platform) {
            this.platform = platform;
            return this;
        }
        public PlayerCrawlingResultDtoBuilder setPortrait(String portrait) {
            this.portrait = portrait;
            return this;
        }

        public PlayerCrawlingResultDto createPlayerCrawlingResultDto()
        {
            return new PlayerCrawlingResultDto(name, urlName, id, level, playerLevel, isPublic, platform, portrait);
        }
    }
}
