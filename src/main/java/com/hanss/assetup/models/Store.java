package com.hanss.assetup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "STORE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueUserAndName", columnNames = {"USER_ID", "NAME"})
        }
)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonIgnore
    @Column(name="USER_ID")
    private Long userId;

    @NotBlank
    @Size(max = 128)
    private String name;

    public Store() {
        this.id = -1L;
    }

    public Store(Long id, Long userId, String name) {
        this.id = id;
        this.userId = userId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
