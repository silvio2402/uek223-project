package com.ourspace.backend.domain.mylistentry;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ourspace.backend.domain.mylistentry.dto.MyListEntryDTO;
import com.ourspace.backend.domain.mylistentry.dto.MyListEntryMapper;
import com.ourspace.backend.domain.mylistentry.dto.PostMyListEntryDTO;
import com.ourspace.backend.domain.user.UserService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/mylistentry")
public class MyListEntryController {

  private final MyListEntryService mylistentryService;
  private final UserService userService;
  private final MyListEntryMapper myListEntryMapper;

  public MyListEntryController(MyListEntryService mylistentryService, MyListEntryMapper myListEntryMapper,
      UserService userService) {
    this.mylistentryService = mylistentryService;
    this.myListEntryMapper = myListEntryMapper;
    this.userService = userService;
  }

  @GetMapping({ "", "/" })
  @PreAuthorize("hasAuthority('MYLISTENTRY_READ_ALL')")
  public ResponseEntity<List<MyListEntryDTO>> retrieveAll(@Filter Specification<MyListEntry> spec, Pageable pageable) {
    Page<MyListEntry> mylistentries = mylistentryService.findAll(spec, pageable);
    return ResponseEntity.ok(myListEntryMapper.toDtoList(mylistentries));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('MYLISTENTRY_READ_ALL')")
  public ResponseEntity<MyListEntryDTO> retrieveById(@PathVariable UUID id) {
    MyListEntry mylistentry = mylistentryService.findById(id);
    return ResponseEntity.ok(myListEntryMapper.toDto(mylistentry));
  }

  @PostMapping({ "", "/" })
  @PreAuthorize("hasAuthority('MYLISTENTRY_MODIFY_ALL') || hasAuthority('MYLISTENTRY_MODIFY_OWN')")
  public ResponseEntity<MyListEntryDTO> create(@Valid @RequestBody PostMyListEntryDTO myListEntryDTO) {
    MyListEntry myListEntry = myListEntryMapper.toEntity(myListEntryDTO);
    MyListEntry createdMyListEntry = mylistentryService.create(myListEntry);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(createdMyListEntry.getId()).toUri();
    return ResponseEntity.created(location).body(myListEntryMapper.toDto(createdMyListEntry));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('MYLISTENTRY_MODIFY_ALL') || (hasAuthority('MYLISTENTRY_MODIFY_OWN') && @myListEntryPermissionEvaluator.isOwnEntry(#id))")
  public ResponseEntity<MyListEntryDTO> update(@PathVariable UUID id,
      @Valid @RequestBody MyListEntryDTO myListEntryDTO) {
    MyListEntry myListEntry = myListEntryMapper.toEntity(myListEntryDTO);
    myListEntry.setUser(userService.findById(myListEntryDTO.getUser_id()));

    MyListEntry updatedMyListEntry = mylistentryService.updateById(id, myListEntry);
    return ResponseEntity.ok(myListEntryMapper.toDto(updatedMyListEntry));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('MYLISTENTRY_DELETE_ALL') || (hasAuthority('MYLISTENTRY_DELETE_OWN') && @myListEntryPermissionEvaluator.isOwnEntry(#id))")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    mylistentryService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
