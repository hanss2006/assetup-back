package com.hanss.assetup.webservices.restservices.asset;
import com.hanss.assetup.webservices.restservices.currency.Currency;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ASSET")
public class Asset {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String ticker;
    @Lob
    private String description;
    private float price;
    private int quantity;
    private Date purchaseDate;
    private int currency_id;
    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID", insertable = false, updatable = false)
    private Currency currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return id == asset.id
                && userId.equals(asset.userId)
                && ticker.equals(asset.ticker)
                && description.equals(asset.description)
                && price == asset.price
                && quantity == asset.quantity
                && purchaseDate.equals(asset.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, ticker, description, price, quantity, purchaseDate);
    }

    protected Asset() {
        super();
        this.id = -1L;
    }

    public Asset(Long id, Long userId, String ticker, String description, float price, int quantity, Date purchaseDate, Integer currency_id) {
        super();
        this.id = id;
        this.userId = userId;
        this.ticker = ticker;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
        this.currency_id = currency_id;
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

    public float getPrice() {
        return price;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long username) {
        this.userId = username;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getTicker() { return this.ticker; }

    public void setTicker(String ticker) { this.ticker = ticker; }

    public int getQuantity() { return this.quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Currency getCurrency() { return this.currency; }

    public int getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(int currency_id) {
        this.currency_id = currency_id;
    }
}
