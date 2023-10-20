package org.bot0ff.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    private Long id;

    @CreationTimestamp
    private LocalDateTime registerDate;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    private UserState state;

    @Transient
    private Long gameId;
}
