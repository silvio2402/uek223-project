package com.ourspace.backend.domain.mylistentry;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourspace.backend.domain.user.User;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/mylistentry")
public class MyListEntryController {

  private final MyListEntryService mylistentryService;

  public MyListEntryController(MyListEntryService mylistentryService) {
    this.mylistentryService = mylistentryService;
  }

  @GetMapping({ "", "/" })
  public ResponseEntity<List<MyListEntry>> retrieveAll() {
    List<MyListEntry> mylistentries = mylistentryService.findAll();
    return ResponseEntity.ok(mylistentries);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MyListEntry> retrieveById(@PathVariable UUID id) {
    MyListEntry mylistentry = mylistentryService.findById(id);
    return ResponseEntity.ok(mylistentry);
  }

  @PostMapping({ "", "/" })
  public ResponseEntity<MyListEntry> create(@Valid @RequestBody MyListEntry myListEntry, User user) {
    MyListEntry createdMyListEntry = mylistentryService.create(myListEntry, user);
    return ResponseEntity.ok(createdMyListEntry);
  }

}
