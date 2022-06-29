package com.lpirito.devscrapper.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class DocumentEntity {

    private Integer id;
    private String source;
    private String url;
}
