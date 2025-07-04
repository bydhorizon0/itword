package org.news.itword.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieImageDTO {
    private Long id;
    private String imgName;
    private String path;

    public String getImageURL() {
        return path != null ? "/" + path + "/" + imgName : "/images/default_image.jpg";
    }
}
