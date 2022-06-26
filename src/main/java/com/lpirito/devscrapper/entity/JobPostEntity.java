package com.lpirito.devscrapper.entity;

import lombok.*;
import org.springframework.stereotype.Component;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Component
public class JobPostEntity {

    private int id;
    private String jobTitle;
    private String url;
    private String company;
    private String location;
    private String wasPosted;
    private DocumentEntity origin;

}
