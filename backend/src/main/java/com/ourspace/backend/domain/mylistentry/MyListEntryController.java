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

import com.ourspace.backend.domain.mylistentry.dto.MyListEntryDTO;
import com.ourspace.backend.domain.mylistentry.dto.MyListEntryMapper;
import com.ourspace.backend.domain.mylistentry.dto.PostMyListEntryDTO;
import com.ourspace.backend.domain.user.User;
import com.ourspace.backend.domain.user.UserUtil;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/mylistentry")
public class MyListEntryController {

  private final MyListEntryService mylistentryService;
  private final MyListEntryMapper myListEntryMapper;

  @Autowired
  public MyListEntryController(MyListEntryService mylistentryService, MyListEntryMapper myListEntryMapper) {
    this.mylistentryService = mylistentryService;
    this.myListEntryMapper = myListEntryMapper;
  }

  @GetMapping({ "", "/" })
  public ResponseEntity<List<MyListEntryDTO>> retrieveAll() {
    List<MyListEntry> mylistentries = mylistentryService.findAll();
    return ResponseEntity.ok(myListEntryMapper.toDtoList(mylistentries));
  }

  @GetMapping("/{id}")
  public ResponseEntity<MyListEntryDTO> retrieveById(@PathVariable UUID id) {
    MyListEntry mylistentry = mylistentryService.findById(id);
    return ResponseEntity.ok(myListEntryMapper.toDto(mylistentry));
  }

  @PostMapping({ "", "/" })
  @PreAuthorize("hasAuthority('MYLISTENTRY_MODIFY_ALL') || hasAuthority('MYLISTENTRY_MODIFY_OWN') && @myListEntryPermissionEvaluator.isOwnEntry(principal, #myListEntry.id, this)")
  public ResponseEntity<MyListEntryDTO> create(@Valid @RequestBody PostMyListEntryDTO myListEntryDTO) {
    User user = UserUtil.getCurrentUser();
    MyListEntry createdMyListEntry = mylistentryService.create(myListEntryDTO,
        user);
    return ResponseEntity.ok(myListEntryMapper.toDto(createdMyListEntry));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('MYLISTENTRY_MODIFY_ALL') || hasAuthority('MYLISTENTRY_MODIFY_OWN') && @myListEntryPermissionEvaluator.isOwnEntry(principal, #myListEntry.id, this)")
  public ResponseEntity<MyListEntryDTO> update(@PathVariable UUID id,
      @Valid @RequestBody MyListEntryDTO myListEntryDTO) {
    MyListEntry myListEntry = myListEntryMapper.toEntity(myListEntryDTO);
    MyListEntry updatedMyListEntry = mylistentryService.updateById(id, myListEntry);
    return ResponseEntity.ok(myListEntryMapper.toDto(updatedMyListEntry));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('MYLISTENTRY_DELETE_ALL') || hasAuthority('MYLISTENTRY_DELETE_OWN') && @myListEntryPermissionEvaluator.isOwnEntry(principal, #myListEntry.id, this)")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    mylistentryService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
