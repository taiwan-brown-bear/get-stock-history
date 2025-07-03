package com.taiwan_brown_bear.get_stock_history.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "stock_history")
public class StockHistoryDAO {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String  stockTicker;
    private String  date;
    private Double  close;
    private Double  volume;
    private Double  open;
    private Double  high;
    private Double  low;
    @CreationTimestamp
    @Column(name = "created", nullable = false, updatable = false)
    private Instant created;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastModified;
}
