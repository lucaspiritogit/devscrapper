package com.lpirito.devscrapper.entity;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ScrapperEntity{

    private int id;
    private String jobTitle;
    private String url;
    private String company;
    private String description;
    private String location;
    private DocumentEntity origin;

}
