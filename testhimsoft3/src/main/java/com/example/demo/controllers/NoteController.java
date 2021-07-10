package com.example.demo.controllers;

import com.example.demo.entities.Note;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.NoteRepository;
import com.example.demo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NoteController {
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    PersonRepository personRepository;

    @GetMapping("/persons/{personId}/notes")
    public List<Note> getAllNotesByPersonId(@PathVariable(value = "personId") int personId) {
        return noteRepository.findByPersonId(personId);
    }

    @GetMapping("/persons/{personId}/notes/{noteId}")
    public Note getNoteByPersonIdAndNoteId(@PathVariable(value = "personId") int personId,
                                                 @PathVariable(value = "noteId") int noteId) {
        return noteRepository.findByPersonIdAndId(personId, noteId).orElseThrow(() ->
                new ResourceNotFoundException("NoteId " + noteId + " not found"));
    }

    @PostMapping("/persons/{personId}/notes")
    public Note createNote(@PathVariable (value = "personId") int personId,
                           @Valid @RequestBody Note note) {
        return personRepository.findById(personId).map(person -> {
            note.setPerson(person);
            return noteRepository.save(note);
        }).orElseThrow(() -> new ResourceNotFoundException("PersonId " + personId + " not found"));
    }

    @PutMapping("/persons/{personId}/notes/{noteId}")
    public Note updateNote(@PathVariable (value = "personId") int personId,
                           @PathVariable (value = "noteId") int noteId,
                           @Valid @RequestBody Note noteRequest) {
        if(!personRepository.existsById(personId)) {
            throw new ResourceNotFoundException("PersonId " + personId + " not found");
        }

        return noteRepository.findById(noteId).map(note -> {
            note.setTitle(noteRequest.getTitle());
            note.setText(noteRequest.getText());
            note.setNotifyAt(noteRequest.getNotifyAt());
            return noteRepository.save(note);
        }).orElseThrow(() -> new ResourceNotFoundException("NoteId " + noteId + "not found"));
    }

    @DeleteMapping("/persons/{personId}/notes/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable (value = "personId") int personId,
                                        @PathVariable (value = "noteId") int noteId) {
        return noteRepository.findByPersonIdAndId(personId, noteId).map(note -> {
            noteRepository.delete(note);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Note not found with id " +
                noteId + " and personId " + personId));
    }
}