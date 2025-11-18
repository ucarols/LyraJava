package com.example.lyra.dto;

import com.example.lyra.model.EHumor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HumorMessage {
    private Long userId;
    private String userName;
    private EHumor humor;
    private String descricao;
    private LocalDateTime dataHora;
}
