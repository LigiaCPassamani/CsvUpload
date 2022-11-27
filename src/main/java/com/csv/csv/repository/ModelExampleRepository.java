package com.csv.csv.repository;


import com.csv.csv.model.ModelExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelExampleRepository extends JpaRepository<ModelExample, Long> {
}
