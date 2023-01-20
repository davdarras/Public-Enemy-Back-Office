package fr.insee.publicenemy.api.infrastructure;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "questionnaire_mode", schema = "public", catalog = "public-enemy-db")
public class QuestionnaireModeEntity {
    @Basic
    @Column(name = "questionnaire_id")
    private int questionnaireId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "mode")
    private String mode;

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionnaireModeEntity that = (QuestionnaireModeEntity) o;
        return questionnaireId == that.questionnaireId && Objects.equals(mode, that.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionnaireId, mode);
    }
}
