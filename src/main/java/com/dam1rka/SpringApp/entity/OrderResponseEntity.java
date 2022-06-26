package com.dam1rka.SpringApp.entity;

import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.util.Date;

@Entity
public class OrderResponseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    private Date created;

    private Integer iid;

    @Column(nullable = false, columnDefinition = "varchar(512)")
    private String chd;

    private String error_code;

    private String error_message;


    public Long getId() {
        return id;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public Date getCreated() {
        return created;
    }

    public Integer getIid() {
        return iid;
    }

    public String getError_code() {
        return error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getChd() {
        return chd;
    }

    public void setChd(String chd) {
        this.chd = chd;
    }
}
