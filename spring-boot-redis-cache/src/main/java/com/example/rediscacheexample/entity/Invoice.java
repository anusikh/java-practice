package com.example.rediscacheexample.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invoice implements Serializable {
    @Id
    @GeneratedValue
    private Integer invId;
    private String invName;
    private Double invAmt;
}
