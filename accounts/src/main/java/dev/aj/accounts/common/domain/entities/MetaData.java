package dev.aj.accounts.common.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;

@Embeddable
@Getter
@Setter
public class MetaData {

    @CreatedDate
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime created_date;

    @CreatedBy
    @Column(updatable = false, nullable = false, columnDefinition = "VARCHAR(100)")
    private String created_by;

    @LastModifiedDate
    @Column(insertable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime last_modified_date;

    @LastModifiedBy
    @Column(insertable = false, columnDefinition = "VARCHAR(100)")
    private String last_modified_by;
}
