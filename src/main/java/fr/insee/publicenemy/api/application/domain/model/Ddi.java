package fr.insee.publicenemy.api.application.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ddi {    
    private String poguesId;
    private String label;
    private List<Mode> modes;
    private byte[] content;
}
