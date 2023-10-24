package org.bot0ff.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "game_filed")
    private List<String> gameFiled;

    @Column(name = "change_target")
    private String changeTarget;

    @Column(name = "is_active")
    private boolean isActive;
}
