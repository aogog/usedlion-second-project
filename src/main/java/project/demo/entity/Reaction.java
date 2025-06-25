package project.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reactionId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;
    private int targetId;
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
    private LocalDateTime createdAt;

}
