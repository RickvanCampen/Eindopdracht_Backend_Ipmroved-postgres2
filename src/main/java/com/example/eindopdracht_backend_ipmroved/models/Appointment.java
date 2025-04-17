package com.example.eindopdracht_backend_ipmroved.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "appointments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "bicycle_name")
    private String bicycle_name;

    @Column(name = "description")
    private String description;

    @Column(name = "date_time")
    private LocalDateTime date_time;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Lob
    @Column(name = "attachment")
    private byte[] attachment;
}

