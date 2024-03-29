package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.*;

import static ru.practicum.shareit.utils.Consts.TIME_PATTERN_WITH_MILLISECONDS;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String text;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    @JsonFormat(pattern = TIME_PATTERN_WITH_MILLISECONDS)
    private LocalDateTime created;
}