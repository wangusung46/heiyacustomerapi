/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiya.mobileapi.customer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author User
 */
@Setter
@Getter
@Entity
@Table(name="token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_customer")
    private Long idCustomer;

    @Column(name = "mobile_no")
    private String mobileNo;
    
    @Column(name = "id_token")
    private String idToken;
    
    @Column(name = "is_loggedin")
    private Boolean isLoggedin;

}
