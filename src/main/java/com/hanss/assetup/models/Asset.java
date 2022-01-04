package com.hanss.assetup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ASSET",
    uniqueConstraints = {
        @UniqueConstraint(name = "UniqueUserAndTicker", columnNames = {"USER_ID", "TICKER"}),
        @UniqueConstraint(name = "UniqueUserAndName", columnNames = {"USER_ID", "NAME"})
    }
)
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonIgnore
    @Column(name="USER_ID")
    private Long userId;

    @NotBlank
    @Size(max = 16)
    private String ticker;

    @NotBlank
    @Size(max = 256)
    private String name;

    @Lob
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return id == asset.id
                && userId.equals(asset.userId)
                && ticker.equals(asset.ticker)
                && name.equals(asset.name)
                && description.equals(asset.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, ticker, description);
    }

    protected Asset() {
        super();
        this.id = -1L;
    }

    public Asset(Long id, Long userId, String ticker, String name, String description
            , float price, int quantity, Date purchaseDate, Long currencyId) {
        super();
        this.id = id;
        this.userId = userId;
        this.ticker = ticker;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long username) {
        this.userId = username;
    }

    public String getTicker() { return this.ticker; }

    public void setTicker(String ticker) { this.ticker = ticker; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) {
        this.description = description;
    }
}
