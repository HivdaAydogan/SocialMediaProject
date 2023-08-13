package com.socialmedia.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;


@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@MappedSuperclass
public class BaseEntity {
    private Long createDate;
    private Long updateDate;
}
