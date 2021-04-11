package com.hanss.assetup.webservices.restservices.asset;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ASSET")
public class Asset {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String ticker;
    private String description;
    private int price;
    private int quantity;
    private Date purchaseDate;
    private String currency = "RUB";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return id == asset.id
                && username.equals(asset.username)
                && ticker.equals(asset.ticker)
                && description.equals(asset.description)
                && price == asset.price
                && quantity == asset.quantity
                && purchaseDate.equals(asset.purchaseDate)
                && currency.equals(asset.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, ticker, description, price, quantity, purchaseDate, currency);
    }

    protected Asset() {
        super();
        this.id = -1L;
    }

    public Asset(Long id, String username, String ticker, String description, int price, int quantity, Date purchaseDate, String currency) {
        super();
        this.id = id;
        this.username = username;
        this.ticker = ticker;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int calories) {
        this.price = calories;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getTicker() { return ticker; }

    public void setTicker(String ticker) { this.ticker = ticker; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getCurrency() { return currency; }

    public void setCurrency(String currency) { this.currency = currency; }
}
