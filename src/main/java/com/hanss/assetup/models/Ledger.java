package com.hanss.assetup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "LEDGER", indexes = {@Index(name="NAME_INDEX",columnList = "USER_ID,NAME")})
public class Ledger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonIgnore
    @Column(name="USER_ID")
    private Long userId;

    @Size(max = 256)
    private String name;

    private float price;

    private int quantity;

    private Date purchaseDate;

    @NotNull
    @Column(name="ASSET_ID")
    private long assetId;
    @ManyToOne
    @JoinColumn(name = "ASSET_ID", insertable = false, updatable = false)
    private Asset asset;

    @NotNull
    @Column(name="STORE_ID")
    private long storeId;
    @ManyToOne
    @JoinColumn(name = "STORE_ID", insertable = false, updatable = false)
    private Store store;

    @NotNull
    @Column(name="CURRENCY_ID")
    private long currencyId;

    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID", insertable = false, updatable = false)
    private Currency currency;

    public Ledger() {
        this.id = -1L;
    }

    public Ledger(Long id, Long userId, String name, float price, int quantity, Date purchaseDate,
                  long assetId, long storeId, long currencyId) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
        this.assetId = assetId;
        this.storeId = storeId;
        this.currencyId = currencyId;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public long getAssetId() {
        return assetId;
    }

    public void setAssetId(long assetId) {
        this.assetId = assetId;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
