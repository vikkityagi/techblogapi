package com.techyatra.blog_api.dto;



import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
}
