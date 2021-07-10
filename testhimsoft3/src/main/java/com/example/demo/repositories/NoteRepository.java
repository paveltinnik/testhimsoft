package com.example.demo.repositories;

import com.example.demo.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findByPersonId(int id);
    Optional<Note> findByPersonIdAndId(int personId, int id);
}
