package org.bot0ff.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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

    @Type(type = "jsonb")
    @Column(name = "game_filed", columnDefinition = "jsonb")
    private GameFiled gameFiled;
}
