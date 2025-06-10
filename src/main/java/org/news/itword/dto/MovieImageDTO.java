package org.news.itword.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieImageDTO {
    private Long id;
    private String imgName;
    private String path;

    public String getImageURL() {
        return URLEncoder.encode(path + "/" + imgName, StandardCharsets.UTF_8);
    }
}
