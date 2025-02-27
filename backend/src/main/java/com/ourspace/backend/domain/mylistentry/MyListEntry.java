package com.ourspace.backend.domain.mylistentry;

import java.sql.Date;

import com.ourspace.backend.core.generic.AbstractEntity;
import com.ourspace.backend.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a MyListEntry.
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class MyListEntry extends AbstractEntity {

  @Column(name = "title")
  private String title;

  @Column(name = "text")
  private String text;

  @Column(name = "importance")
  private Integer importance;

  @Column(name = "creation_date")
  private Date creation_date;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
