package org.example.springbootdeveloper.repository;

import org.example.springbootdeveloper.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
