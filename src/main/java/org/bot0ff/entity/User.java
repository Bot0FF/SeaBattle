package org.bot0ff.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

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

    @Column(name = "opponent_id")
    private Long opponentId;

    @Column(name = "user_game_filed")
    private List<String> userGameFiled;

    @Column(name = "opponent_game_filed")
    private List<String> opponentGameFiled;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "messageId")
    private Integer messageId;

    @Column(name = "countVictory")
    private Integer countVictory;

    @Column(name = "countLoss")
    private Integer countLoss;

    @Transient
    private SendMessage sendMessage;

    @Transient
    private EditMessageText editMessageText;

    @Transient
    private AnswerCallbackQuery answerCallbackQuery;
}
