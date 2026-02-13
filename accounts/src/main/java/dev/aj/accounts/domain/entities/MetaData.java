package dev.aj.accounts.domain.entities;

import jakarta.persistence.Embeddable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;

@Embeddable
public class MetaData {

    @CreatedDate
    private ZonedDateTime created_date;

    @CreatedBy
    private String created_by;

    @LastModifiedDate
    private ZonedDateTime last_modified_date;

    @LastModifiedBy
    private String last_modified_by;
}
