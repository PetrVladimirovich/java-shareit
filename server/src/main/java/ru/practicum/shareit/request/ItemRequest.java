package ru.practicum.shareit.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static ru.practicum.shareit.utils.Consts.TIME_PATTERN;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
@Data
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String description;
    @Column(name = "requestor_id")
    private Long requestor;
    @JsonFormat(pattern = TIME_PATTERN)
    @Column
    private LocalDateTime created;
}