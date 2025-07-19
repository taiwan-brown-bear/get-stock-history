package com.taiwan_brown_bear.get_stock_history.dao;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@Builder(toBuilder = true)
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "stock_history", uniqueConstraints = { @UniqueConstraint(name = "StockTickerAndDate", columnNames = { "stockTicker", "date" }) })
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



