package com.ourspace.backend.domain.mylistentry;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @Autowired
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
  @PreAuthorize("hasAuthority('MYLISTENTRY_MODIFY_ALL') || hasAuthority('MYLISTENTRY_MODIFY_OWN') && @myListEntryPermissionEvaluator.isOwnEntry(principal, #myListEntry.id, this)")
  public ResponseEntity<MyListEntry> create(@Valid @RequestBody MyListEntry myListEntry, User user) {
    MyListEntry createdMyListEntry = mylistentryService.create(myListEntry, user);
    return ResponseEntity.ok(createdMyListEntry);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('MYLISTENTRY_MODIFY_ALL') || hasAuthority('MYLISTENTRY_MODIFY_OWN') && @myListEntryPermissionEvaluator.isOwnEntry(principal, #myListEntry.id, this)")
  public ResponseEntity<MyListEntry> update(@PathVariable UUID id, @Valid @RequestBody MyListEntry myListEntry) {
    MyListEntry updatedMyListEntry = mylistentryService.updateById(id, myListEntry);
    return ResponseEntity.ok(updatedMyListEntry);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('MYLISTENTRY_DELETE_ALL') || hasAuthority('MYLISTENTRY_DELETE_OWN') && @myListEntryPermissionEvaluator.isOwnEntry(principal, #myListEntry.id, this)")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    mylistentryService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
