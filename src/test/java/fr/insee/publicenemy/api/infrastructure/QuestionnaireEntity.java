package fr.insee.publicenemy.api.infrastructure;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "questionnaire", schema = "public", catalog = "public-enemy-db")
public class QuestionnaireEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "label")
    private String label;
    @Basic
    @Column(name = "creation_date")
    private Date creationDate;
    @Basic
    @Column(name = "updated_date")
    private Date updatedDate;
    @Basic
    @Column(name = "questionnaire_pogues_id")
    private String questionnairePoguesId;
    @Basic
    @Column(name = "survey_unit_data")
    private String surveyUnitData;
    @Basic
    @Column(name = "context")
    private String context;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getQuestionnairePoguesId() {
        return questionnairePoguesId;
    }

    public void setQuestionnairePoguesId(String questionnairePoguesId) {
        this.questionnairePoguesId = questionnairePoguesId;
    }

    public String getSurveyUnitData() {
        return surveyUnitData;
    }

    public void setSurveyUnitData(String surveyUnitData) {
        this.surveyUnitData = surveyUnitData;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionnaireEntity that = (QuestionnaireEntity) o;
        return id == that.id && Objects.equals(label, that.label) && Objects.equals(creationDate, that.creationDate) && Objects.equals(updatedDate, that.updatedDate) && Objects.equals(questionnairePoguesId, that.questionnairePoguesId) && Objects.equals(surveyUnitData, that.surveyUnitData) && Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, creationDate, updatedDate, questionnairePoguesId, surveyUnitData, context);
    }
}
