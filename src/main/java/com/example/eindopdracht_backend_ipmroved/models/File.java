package com.example.eindopdracht_backend_ipmroved.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "size", nullable = false)
    private Long size;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "upload_time", nullable = false, updatable = false)
    private LocalDateTime uploadTime;

    @PrePersist
    public void prePersist() {
        this.uploadTime = LocalDateTime.now();
    }
}
