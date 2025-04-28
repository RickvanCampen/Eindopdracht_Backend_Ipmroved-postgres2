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
    private Long id;  // Unieke ID voor het bestand

    @Column(name = "file_name", nullable = false)
    private String fileName;  // Naam van het bestand

    @Column(name = "file_type", nullable = false)
    private String fileType;  // Type van het bestand (bijv. 'image/jpeg')

    @Column(name = "size", nullable = false)
    private Long size;  // Grootte van het bestand in bytes

    @Lob
    @Column(name = "data")
    private byte[] data;  // De binaire data van het bestand

    @Column(name = "upload_time", nullable = false, updatable = false)
    private LocalDateTime uploadTime;  // Het tijdstip van uploaden

    @PrePersist
    public void prePersist() {
        // Stel de upload_time in op het moment van aanmaken
        this.uploadTime = LocalDateTime.now();
    }
}
