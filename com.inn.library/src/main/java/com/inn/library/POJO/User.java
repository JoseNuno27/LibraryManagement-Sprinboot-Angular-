package com.inn.library.POJO;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;


@NamedQuery(name ="User.findByEmailId" , query = "select u from User u where u.Email=:email ")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Integer id;

    @Getter
    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Getter
    @Column(name = "password")
    private String password;

    @Getter
    @Column(name = "email")
    private String Email;

    @Column(name = "status")
    private String status;

    @Getter
    @Column(name = "role")
    private String role;


    public User() {
    }
}
